package biz.netcentric.transformations;

import biz.netcentric.helpers.DocumentHelper;
import biz.netcentric.script.ScriptScope;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.io.IOException;

/**
 * Specialize this to implement some document transformation.
 */
public abstract class Transformation {

    protected final ScriptScope scriptScope;
    protected final DocumentHelper documentHelper = new DocumentHelper();

    public Transformation(ScriptScope scriptScope) {
        this.scriptScope = scriptScope;
    }

    protected void evaluateScript(Document document) throws ScriptException {
        Elements scripts = document.select("script[type=server/javascript]");
        for (Element script : scripts) {
            scriptScope.evaluateOnly(script.html());
        }
    }

    protected void removeScript(Document document) throws ScriptException {
        Elements scripts = document.select("script[type=server/javascript]");
        for (Element script : scripts) {
            script.remove();
        }
    }

    abstract public void apply(Document document) throws ScriptException, IOException;
}
