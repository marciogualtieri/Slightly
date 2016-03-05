package biz.netcentric.transformations;

import biz.netcentric.script.ScriptScope;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * Removes data-if tags that evaluateOnly to false in the document where the transformation is applied.
 */
public class DataIfTransformation extends Transformation {
    private static final String DATA_IF_ATTRIBUTE_NAME = "data-if";

    public DataIfTransformation(ScriptScope scriptScope) {
        super(scriptScope);
    }

    @Override
    public void apply(Document document) throws ScriptException, IOException {
        evaluateScript(document);
        Elements dataIfs = document.select("[" + DATA_IF_ATTRIBUTE_NAME + "]");
        for (Element dataIf : dataIfs) {
            processDataIf(dataIf);
        }
    }

    private void processDataIf(Element dataIf) throws ScriptException {
        String expression = dataIf.attr(DATA_IF_ATTRIBUTE_NAME);
        if (scriptScope.evaluatesToFalse(expression)) {
            dataIf.empty();
            dataIf.remove();
        } else {
            dataIf.removeAttr(DATA_IF_ATTRIBUTE_NAME);
        }
    }
}
