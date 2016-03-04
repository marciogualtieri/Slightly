package biz.netcentric.transformations;

import biz.netcentric.script.ScriptScope;
import org.jsoup.nodes.Document;

import javax.script.ScriptException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Renders all Javascript expressions in the document where is applied.
 */
public class RenderingTransformation extends Transformation {

    private static final Pattern ENVELOPED_EXPRESSION_PATTERN = Pattern.compile("\\$\\{[^\\}]*\\}");
    private static final Pattern EXPRESSION_ENVELOPE_PATTERN = Pattern.compile("(\\$\\{)|(\\})");

    public RenderingTransformation(ScriptScope scriptScope) {
        super(scriptScope);
    }

    @Override
    public void apply(Document document) throws ScriptException {
        evaluateScript(document);
        String body = document.body().html();
        body = renderExpressionsInString(body);
        String head = document.head().html();
        head = renderExpressionsInString(head);
        document.body().html(body);
        document.head().html(head);
        removeScript(document);
    }

    private String renderExpressionsInString(String string) throws ScriptException {
        Matcher matcher = ENVELOPED_EXPRESSION_PATTERN.matcher(string);
        while (matcher.find()) {
            String expression = removeExpressionEnvelope(matcher.group(0));
            string = matcher.replaceFirst(scriptScope.evaluateToString(expression));
            matcher = ENVELOPED_EXPRESSION_PATTERN.matcher(string);
        }
        return string;
    }

    private String removeExpressionEnvelope(String expression) {
        return EXPRESSION_ENVELOPE_PATTERN.matcher(expression).replaceAll("");
    }
}
