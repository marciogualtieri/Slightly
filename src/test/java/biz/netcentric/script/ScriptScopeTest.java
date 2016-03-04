package test.biz.netcentric.script;

import biz.netcentric.helpers.TestHelper;
import biz.netcentric.script.ScriptScope;
import org.junit.Before;
import org.junit.Test;

import javax.script.ScriptException;
import java.util.List;

import static com.googlecode.catchexception.CatchException.caughtException;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.then;
import static com.googlecode.catchexception.apis.CatchExceptionBdd.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsEqual.equalTo;

public class ScriptScopeTest {

    private ScriptScope scriptScope;

    @Before
    public void before() throws Exception {
        scriptScope = new ScriptScope();
        scriptScope.evaluateOnly(TestHelper.TEST_SCRIPT);
    }

    @Test
    public void whenExpressionEvaluatesToTrue_thenOk() throws Exception {
        Boolean result = scriptScope.evaluatesToFalse("marriedWithChildren.married");
        assertThat(result, equalTo(false));
    }

    @Test
    public void whenExpressionEvaluatesToFalse_thenOk() throws Exception {
        Boolean result = scriptScope.evaluatesToFalse("!marriedWithChildren.married");
        assertThat(result, equalTo(true));
    }

    @Test
    public void whenExpressionEvaluatesToList_thenOk() throws Exception {
        @SuppressWarnings("unchecked")
        List<String> result = scriptScope.evaluateToList("marriedWithChildren.children");
        assertThat(result, containsInAnyOrder(TestHelper.TEST_CHILDREN_NAMES.toArray()));
    }

    @Test
    public void whenExpressionEvaluatesToString_thenOk() throws Exception {
        String result = scriptScope.evaluateToString("\"uppercase\".toUpperCase()");
        assertThat(result, equalTo("UPPERCASE"));
    }

    @Test
    public void whenExpressionHasSyntaxError_thenException() throws Exception {
        when(scriptScope).evaluateOnly("i_do_not_exist.do_something()");
        then(caughtException())
                .isInstanceOf(ScriptException.class)
                .hasMessageContaining("javax.script.ScriptException: ReferenceError");
    }
} 
