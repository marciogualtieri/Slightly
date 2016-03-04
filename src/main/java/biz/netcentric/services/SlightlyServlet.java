package biz.netcentric.services;

import biz.netcentric.helpers.DocumentHelper;
import biz.netcentric.processors.DocumentProcessor;
import biz.netcentric.script.ScriptScope;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SlightlyServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(SlightlyServlet.class);
    private static final String TEMPLATES_FOLDER = "templates";
    private static final String JAVA_SCRIPT_ERROR_MESSAGE = "Error evaluating template's javascript: ";
    private static final String TEMPLATE_NOT_FOUND_ERROR_MESSAGE = "Template file could not be found.";

    private final DocumentHelper documentHelper = new DocumentHelper();
    private ScriptScope scriptScope;

    public void init() throws ServletException {
        try {
            scriptScope = new ScriptScope();
        } catch (ScriptException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String template = buildTemplatePath(request.getRequestURI());
        try {
            scriptScope.exposeObject(request, "request");
            Document document = documentHelper.getHtmlFileAsDocument(template);
            DocumentProcessor documentProcessor = new DocumentProcessor(scriptScope);
            documentProcessor.process(document);
            writeDocumentToResponse(document, response);
        } catch (ScriptException e) {
            logger.error(JAVA_SCRIPT_ERROR_MESSAGE, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    JAVA_SCRIPT_ERROR_MESSAGE + e.getMessage());
        } catch (IOException e) {
            logger.error(TEMPLATE_NOT_FOUND_ERROR_MESSAGE, e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, TEMPLATE_NOT_FOUND_ERROR_MESSAGE);
        }
    }

    public void destroy() {
    }

    private void writeDocumentToResponse(Document document, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        out.println(document.toString());
    }

    private String buildTemplatePath(String templateName) {
        Path resourceName = Paths.get(templateName);
        Path template = Paths.get(TEMPLATES_FOLDER);
        template = template.resolve(resourceName.subpath(1, resourceName.getNameCount()));
        return template.toString();
    }
}
