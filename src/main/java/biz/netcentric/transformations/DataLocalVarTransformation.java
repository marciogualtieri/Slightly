package biz.netcentric.transformations;

import biz.netcentric.wrappers.ScriptEngineWrapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * Renders expressions for data-local-var elements only.
 */
public class DataLocalVarTransformation extends Transformation {
    private static final String DATA_LOCAL_VAR_ATTRIBUTE_NAME = "data-local-var";

    public DataLocalVarTransformation(ScriptEngineWrapper scriptEngineWrapper) {
        super(scriptEngineWrapper);
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
        scriptEngineWrapper.saveBindings();
        String overrideExpression = dataLocalVar.attr(DATA_LOCAL_VAR_ATTRIBUTE_NAME);
        scriptEngineWrapper.evaluateOnly(overrideExpression);
        String html = dataLocalVar.html();
        html = scriptEngineWrapper.renderExpressionsInString(html);
        dataLocalVar.html(html);
        scriptEngineWrapper.recoverBindings();
    }

}




