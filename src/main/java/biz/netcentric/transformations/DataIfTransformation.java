package biz.netcentric.transformations;

import biz.netcentric.script.ScriptScope;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;

/**
 * Removes data-if tags that evaluateOnly to false in the document where the transformation is applied.
 */
public class DataIfTransformation extends Transformation {

    public DataIfTransformation(ScriptScope scriptScope) {
        super(scriptScope);
    }

    @Override
    public void apply(Document document) throws ScriptException {
        evaluateScript(document);
        Elements dataIfs = document.select("[data-if]");
        for (Element dataIf : dataIfs) {
            processDataIf(dataIf);
        }
    }

    private void processDataIf(Element dataIf) throws ScriptException {
        String expression = dataIf.attr("data-if");
        if (scriptScope.evaluatesToFalse(expression)) {
            dataIf.empty();
            dataIf.remove();
        } else {
            dataIf.removeAttr("data-if");
        }
    }
}
