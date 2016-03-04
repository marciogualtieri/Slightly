package biz.netcentric.transformations;

import biz.netcentric.script.ScriptScope;
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
    private ScriptScope scriptScope;

    @Before
    public void before() throws Exception {
        scriptScope = new ScriptScope();
        transformation = new DataForTransformation(scriptScope);
    }

    @Test
    public void whenApplyDataForTransformationAndNotEmpty_thenExpandIntoMultipleTags() throws Exception {
        Document document = TestHelper.TEST_DATA_FOR_DOCUMENT.clone();
        transformation.apply(document);
        Elements elements = document.select("[title=${withChildren.name} child]");
        assertThat(elements, containsInAnyOrder(TestHelper.TEST_CHILDREN.toArray()));
    }

    @Test
    public void whenApplyDataForTransformationAndEmpty_thenRemoveTag() throws Exception {
        Document document = TestHelper.TEST_DATA_FOR_DOCUMENT.clone();
        transformation.apply(document);
        Elements elements = document.select("[title=${withoutChildren.name} child]");
        assertThat(elements, is(empty()));
    }

}
