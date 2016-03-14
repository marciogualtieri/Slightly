package biz.netcentric.wrappers;

import javax.script.Bindings;
import javax.script.ScriptException;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static javax.script.ScriptContext.ENGINE_SCOPE;

public class ScriptEngineWrapper {
    private static final Pattern ENVELOPED_EXPRESSION_PATTERN = Pattern.compile("\\$\\{[^\\$]*\\}");

    private final javax.script.ScriptEngineManager manager;
    private final javax.script.ScriptEngine engine;

    private Bindings bindings = null;

    public ScriptEngineWrapper() throws ScriptException {
        manager = new javax.script.ScriptEngineManager();
        engine = manager.getEngineByName("JavaScript");
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
            return ((Double) engine.eval(expression)).intValue();
        } catch (Exception e) {
            throw new ScriptException(e);
        }
    }

    public Object evaluateToObject(String expression) throws ScriptException {
        try {
            return engine.eval(expression);
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

    public String renderExpressionsInString(String string) throws ScriptException {
        Matcher matcher = ENVELOPED_EXPRESSION_PATTERN.matcher(string);
        while (matcher.find()) {
            String expression = removeExpressionEnvelope(matcher.group(0));
            string = matcher.replaceFirst(evaluateToString(expression));
            matcher = ENVELOPED_EXPRESSION_PATTERN.matcher(string);
        }
        return string;
    }

    public void saveBindings() {
        bindings = engine.createBindings();
        for (Map.Entry<String, Object> entry : engine.getBindings(ENGINE_SCOPE).entrySet()) {
            bindings.put(entry.getKey(), entry.getValue());
        }
    }

    public void recoverBindings() {
        engine.setBindings(bindings, ENGINE_SCOPE);
    }

    private String removeExpressionEnvelope(String expression) {
        return expression.substring(2, expression.length() - 1);
    }
}

