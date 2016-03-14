package biz.netcentric.transformations;

import biz.netcentric.helpers.HtmlHelper;
import biz.netcentric.wrappers.ScriptEngineWrapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * Specialize this to implement some document transformation.
 */
public abstract class Transformation {
    private final static String SERVER_JAVASCRIPT_SELECT_QUERY = "script[type=server/javascript]";

    protected final ScriptEngineWrapper scriptEngineWrapper;
    protected final HtmlHelper htmlHelper = new HtmlHelper();

    public Transformation(ScriptEngineWrapper scriptEngineWrapper) {
        this.scriptEngineWrapper = scriptEngineWrapper;
    }

    protected void evaluateScript(Document document) throws ScriptException {
        Elements scripts = document.select(SERVER_JAVASCRIPT_SELECT_QUERY);
        for (Element script : scripts) {
            scriptEngineWrapper.evaluateOnly(script.html());
        }
    }

    protected void removeScript(Document document) throws ScriptException {
        Elements scripts = document.select(SERVER_JAVASCRIPT_SELECT_QUERY);
        for (Element script : scripts) {
            script.remove();
        }
    }

    abstract public void apply(Document document) throws ScriptException, IOException;
}
