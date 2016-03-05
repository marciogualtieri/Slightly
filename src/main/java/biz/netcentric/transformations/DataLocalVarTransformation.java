package biz.netcentric.transformations;

import biz.netcentric.script.ScriptScope;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Wraps expression in a function to create a local scope.
 */
public class DataLocalVarTransformation extends Transformation {
    private static final String DATA_LOCAL_VAR_ATTRIBUTE_NAME = "data-local-var";
    private static final String FUNCTION_ENVELOPE_FORMAT = "${(function(){var %s; return %s;}())}";
    private static final String FUNCTION_ENVELOPE_REGEX_FORMAT = "\\$\\{\\(function\\(\\)\\{var %s; return %s;\\}\\(\\)\\)\\}";
    private static final Pattern ENVELOPED_EXPRESSION_PATTERN = Pattern.compile("\\$\\{([^\\}]*)\\}");

    public DataLocalVarTransformation(ScriptScope scriptScope) {
        super(scriptScope);
    }

    @Override
    public void apply(Document document) throws ScriptException, IOException {
        evaluateScript(document);
        Elements dataLocalVars = document.select("[" + DATA_LOCAL_VAR_ATTRIBUTE_NAME + "]");
        for (Element dataLocalVar : dataLocalVars) {
            processDataLocalVar(dataLocalVar);
        }
    }

    private void processDataLocalVar(Element dataLocalVar) throws ScriptException {
        String overrideExpression = dataLocalVar.attr(DATA_LOCAL_VAR_ATTRIBUTE_NAME);
        String html = dataLocalVar.html();
        html = envelopeExpressionsInString(html, overrideExpression);
        dataLocalVar.html(html);
    }

    private String envelopeExpressionsInString(String string, String overrideExpression) throws ScriptException {
        Matcher matcher = ENVELOPED_EXPRESSION_PATTERN.matcher(string);

        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            String expression = matcher.group(1);
            String envelopedExpression = String.format(FUNCTION_ENVELOPE_FORMAT, overrideExpression, expression);
            String envelopedRegexExpression = String.format(FUNCTION_ENVELOPE_REGEX_FORMAT, overrideExpression, expression);
            string = string.substring(matcher.end());
            String newString = matcher.replaceFirst(envelopedRegexExpression);
            buffer.append(newString.substring(0, matcher.start() + envelopedExpression.length()));
            matcher = ENVELOPED_EXPRESSION_PATTERN.matcher(string);
        }
        buffer.append(string);
        return buffer.toString();
    }

}




