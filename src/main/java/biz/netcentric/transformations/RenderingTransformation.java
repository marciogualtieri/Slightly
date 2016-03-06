package biz.netcentric.transformations;

import biz.netcentric.wrappers.ScriptEngineWrapper;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * Renders all Javascript expressions in the document where is applied.
 */
public class RenderingTransformation extends Transformation {

    public RenderingTransformation(ScriptEngineWrapper scriptEngineWrapper) {
        super(scriptEngineWrapper);
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
            attribute.setValue(scriptEngineWrapper.renderExpressionsInString(expression));
        }
    }

    private void renderElementInnerHtml(Element element) throws ScriptException {
        String expression = element.html();
        element.html(scriptEngineWrapper.renderExpressionsInString(expression));
    }

    private boolean isLeafElement(Element element) {
        return CollectionUtils.isEmpty(element.children());
    }

}
