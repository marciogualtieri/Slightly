package biz.netcentric.transformations;

import biz.netcentric.wrappers.ScriptEngineWrapper;
import biz.netcentric.helpers.TestHelper;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.core.Is.is;

public class DataForTransformationTest {

    private Transformation transformation;
    private ScriptEngineWrapper scriptEngineWrapper;

    @Before
    public void before() throws Exception {
        scriptEngineWrapper = new ScriptEngineWrapper();
        transformation = new DataForTransformation(scriptEngineWrapper);
    }

    @Test
    public void whenApplyDataForTransformationAndNotEmpty_thenTagIsExpandedIntoMultipleTags() throws Exception {
        Document document = TestHelper.TEST_DATA_FOR_DOCUMENT.clone();
        transformation.apply(document);
        Elements elements = document.select("[title=${withChildren.name} child]");
        assertThat(elements, containsInAnyOrder(TestHelper.TEST_CHILDREN.toArray()));
    }

    @Test
    public void whenApplyDataForTransformationAndEmpty_thenTagIsRemoved() throws Exception {
        Document document = TestHelper.TEST_DATA_FOR_DOCUMENT.clone();
        transformation.apply(document);
        Elements elements = document.select("[title=${withoutChildren.name} child]");
        assertThat(elements, is(empty()));
    }

}
