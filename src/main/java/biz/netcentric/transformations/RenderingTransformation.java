package biz.netcentric.transformations;

import biz.netcentric.script.ScriptScope;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Renders all Javascript expressions in the document where is applied.
 */
public class RenderingTransformation extends Transformation {

    private static final Pattern ENVELOPED_EXPRESSION_PATTERN = Pattern.compile("\\$\\{[^\\$]*\\}");

    public RenderingTransformation(ScriptScope scriptScope) {
        super(scriptScope);
    }

    @Override
    public void apply(Document document) throws ScriptException, IOException {
        evaluateScript(document);
        Elements elements = document.select("*");
        for (Element element : elements) {
            renderElementAttributes(element);
            if (isLeafElement(element)) {
                renderElementInnerHtml(element);
            }
        }
        removeScript(document);
    }

    private void renderElementAttributes(Element element) throws ScriptException {
        Attributes attributes = element.attributes();
        for (Attribute attribute : attributes) {
            String expression = attribute.getValue();
            attribute.setValue(renderExpressionsInString(expression));
        }
    }

    private void renderElementInnerHtml(Element element) throws ScriptException {
        String expression = element.html();
        element.html(renderExpressionsInString(expression));
    }

    private boolean isLeafElement(Element element) {
        return CollectionUtils.isEmpty(element.children());
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
        return expression.substring(2, expression.length() - 1);
    }
}
