package biz.netcentric.transformations;

import biz.netcentric.helpers.TestHelper;
import biz.netcentric.wrappers.ScriptEngineWrapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class DataLocalVarTransformationTest {

    private Transformation transformation;
    private ScriptEngineWrapper scriptEngineWrapper;

    @Before
    public void before() throws Exception {
        scriptEngineWrapper = new ScriptEngineWrapper();
        transformation = new DataLocalVarTransformation(scriptEngineWrapper);
    }

    @Test
    public void whenApplyDataIfTransformationAndEvaluatesToTrue_thenKeepElement() throws Exception {
        Document document = TestHelper.TEST_DATA_LOCAL_VAR_DOCUMENT.clone();
        transformation.apply(document);
        Element element = document.select("h1").first();
        assertThat(element, is(notNullValue()));
        assertThat(element.html(),
                equalTo(TestHelper.TEST_DATA_LOCAL_VAR_H1_RENDERED_EXPRESSION));
    }

} 
