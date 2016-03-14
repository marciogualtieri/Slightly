package biz.netcentric.transformations;

import biz.netcentric.wrappers.ScriptEngineWrapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * Removes data-if tags that evaluate to false in the document where the transformation is applied.
 */
public class DataIfTransformation extends Transformation {
    private static final String DATA_IF_ATTRIBUTE_NAME = "data-if";

    public DataIfTransformation(ScriptEngineWrapper scriptEngineWrapper) {
        super(scriptEngineWrapper);
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
        if (scriptEngineWrapper.evaluatesToFalse(expression)) {
            dataIf.empty();
            dataIf.remove();
        } else {
            dataIf.removeAttr(DATA_IF_ATTRIBUTE_NAME);
        }
    }
}
