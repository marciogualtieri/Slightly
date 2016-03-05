package biz.netcentric.transformations;

import biz.netcentric.script.ScriptScope;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * Process template includes.
 */
public class DataInclusionTransformation extends Transformation {
    private static final String DATA_INCLUSION_ATTRIBUTE_NAME = "data-inclusion";

    public DataInclusionTransformation(ScriptScope scriptScope) {
        super(scriptScope);
    }

    @Override
    public void apply(Document document) throws ScriptException, IOException {
        evaluateScript(document);
        Elements dataInclusions = document.select("[" + DATA_INCLUSION_ATTRIBUTE_NAME + "]");
        for (Element dataInclusion : dataInclusions) {
            processDataInclusion(dataInclusion);
        }
    }

    private void processDataInclusion(Element dataInclusion) throws ScriptException, IOException {
        String templateFile = dataInclusion.attr(DATA_INCLUSION_ATTRIBUTE_NAME);
        String inclusion = documentHelper.getHtmlFileAsString(templateFile);
        dataInclusion.html(inclusion);
        dataInclusion.removeAttr(DATA_INCLUSION_ATTRIBUTE_NAME);
    }
}
