package biz.netcentric.transformations;

import biz.netcentric.helpers.TestHelper;
import biz.netcentric.wrappers.ScriptEngineWrapper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

public class DataIfTransformationTest {

    private Transformation transformation;
    private ScriptEngineWrapper scriptEngineWrapper;

    @Before
    public void before() throws Exception {
        scriptEngineWrapper = new ScriptEngineWrapper();
        transformation = new DataIfTransformation(scriptEngineWrapper);
    }

    @Test
    public void whenApplyDataIfTransformationAndEvaluatesToTrue_thenKeepElement() throws Exception {
        Document document = TestHelper.TEST_DATA_IF_DOCUMENT.clone();
        transformation.apply(document);
        Element element = document.select("[title=${married.spouse}]").first();
        assertThat(element, is(notNullValue()));
        assertThat(element.hasAttr("data-if"), equalTo(false));
    }

    @Test
    public void whenApplyDataIfTransformationAndEvaluatesToFalse_thenRemoveElement() throws Exception {
        Document document = TestHelper.TEST_DATA_IF_DOCUMENT.clone();
        transformation.apply(document);
        Elements elements = document.select("h2[title=${notMarried.spouse}]");
        assertThat(elements, is(empty()));
    }

    @Test
    public void whenApplyDataIfTransformationAndNestedElementsEvaluatesToFalse_thenRemoveElementAndChildren()
            throws Exception {
        Document document = TestHelper.TEST_DATA_IF_DOCUMENT.clone();
        transformation.apply(document);
        Elements elements = document.select("li[title=${notMarried.spouse}]");
        assertThat(elements, is(empty()));
    }

    @Test
    public void whenApplyDataIfTransformationAndNestedElementsEvaluatesToTrue_thenKeepElementAndChildren()
            throws Exception {
        Document document = TestHelper.TEST_DATA_IF_DOCUMENT.clone();
        transformation.apply(document);
        Element element = document.select("li[title=${married.spouse}]").first();
        assertThat(element, is(notNullValue()));
    }
} 
