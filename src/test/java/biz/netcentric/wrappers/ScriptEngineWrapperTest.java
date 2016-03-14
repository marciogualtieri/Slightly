package biz.netcentric.wrappers;

import biz.netcentric.helpers.TestHelper;
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

public class ScriptEngineWrapperTest {

    private ScriptEngineWrapper scriptEngineWrapper;

    @Before
    public void before() throws Exception {
        scriptEngineWrapper = new ScriptEngineWrapper();
        scriptEngineWrapper.evaluateOnly(TestHelper.TEST_SCRIPT);
    }

    @Test
    public void whenExpressionEvaluatesToTrue_thenOk() throws Exception {
        Boolean result = scriptEngineWrapper.evaluatesToFalse("marriedWithChildren.married");
        assertThat(result, equalTo(false));
    }

    @Test
    public void whenExpressionEvaluatesToFalse_thenOk() throws Exception {
        Boolean result = scriptEngineWrapper.evaluatesToFalse("!marriedWithChildren.married");
        assertThat(result, equalTo(true));
    }

    @Test
    public void whenExpressionEvaluatesToList_thenOk() throws Exception {
        @SuppressWarnings("unchecked")
        List<String> result = scriptEngineWrapper.evaluateToList("marriedWithChildren.children");
        assertThat(result, containsInAnyOrder(TestHelper.TEST_CHILDREN_NAMES.toArray()));
    }

    @Test
    public void whenExpressionEvaluatesToString_thenOk() throws Exception {
        String result = scriptEngineWrapper.evaluateToString("\"uppercase\".toUpperCase()");
        assertThat(result, equalTo("UPPERCASE"));
    }

    @Test
    public void whenExpressionEvaluatesToInteger_thenOk() throws Exception {
        Integer result = scriptEngineWrapper.evaluateToInteger("parseInt(\"3\")");
        System.out.println("person: " + result);
        assertThat(result, equalTo(3));
    }

    @Test
    public void whenExpressionHasSyntaxError_thenException() throws Exception {
        when(scriptEngineWrapper).evaluateOnly("i_do_not_exist.do_something()");
        then(caughtException())
                .isInstanceOf(ScriptException.class)
                .hasMessageContaining("javax.script.ScriptException");
    }

    @Test
    public void whenExpressionIsFunction_thenOk() throws Exception {
        String localName = scriptEngineWrapper.evaluateToString("(function(){var notMarried = new Person(\"Lucy\", \"Rick\", false, 0); return notMarried.name;}())");
        assertThat(localName, equalTo("Lucy"));
        String globalName = scriptEngineWrapper.evaluateToString("notMarried.name");
        assertThat(globalName, equalTo("Name 1"));

    }

    @Test
    public void whenSaveAndRecoverBindings_thenOk() throws Exception {
        createTestPerson();
        scriptEngineWrapper.saveBindings();
        overrideTestPerson();
        scriptEngineWrapper.recoverBindings();
        String name = scriptEngineWrapper.evaluateToString("person.name");
        assertThat(name, equalTo("Lucy"));
    }

    private void createTestPerson() throws Exception {
        scriptEngineWrapper.evaluateOnly("var person = new Person(\"Lucy\", \"Rick\", true,1);");
        String name = scriptEngineWrapper.evaluateToString("person.name");
        assertThat(name, equalTo("Lucy"));
    }

    private void overrideTestPerson() throws Exception {
        scriptEngineWrapper.evaluateOnly("person = new Person(\"Bonny\", \"Clyde\", false, 0);");
        String name = scriptEngineWrapper.evaluateToString("person.name");
        assertThat(name, equalTo("Bonny"));
    }

} 
