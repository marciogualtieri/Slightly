package biz.netcentric.script;

import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.List;

public class ScriptScope {
    private static final String BACKWARDS_COMPATIBILITY_JAVASCRIPT = "load(\"nashorn:mozilla_compat.js\");";

    private final ScriptEngineManager manager;
    private final javax.script.ScriptEngine engine;

    public ScriptScope() throws ScriptException {
        manager = new ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
        engine.eval(BACKWARDS_COMPATIBILITY_JAVASCRIPT);
    }

    public boolean evaluatesToTrue(String expression) throws ScriptException {
        try {
            return (Boolean) engine.eval(expression);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    public boolean evaluatesToFalse(String expression) throws ScriptException {
        return !evaluatesToTrue(expression);
    }

    public List evaluateToList(String expression) throws ScriptException {
        try {
            return (List) engine.eval(expression);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    public String evaluateToString(String expression) throws ScriptException {
        try {
            return (String) engine.eval(expression);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    public Integer evaluateToInteger(String expression) throws ScriptException {
        try {
            return (Integer) engine.eval(expression);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    public void evaluateOnly(String script) throws ScriptException {
        try {
            engine.eval(script);
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    public void exposeObject(Object object, String name) {
        engine.put(name, object);
    }
}

