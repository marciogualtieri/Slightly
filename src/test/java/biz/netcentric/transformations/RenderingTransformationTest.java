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

public class RenderingTransformationTest {

    private Transformation transformation;
    private ScriptEngineWrapper scriptEngineWrapper;

    @Before
    public void before() throws Exception {
        scriptEngineWrapper = new ScriptEngineWrapper();
        transformation = new RenderingTransformation(scriptEngineWrapper);
    }

    @Test
    public void whenApplyRenderingTransformation_thenTitleIsRendered() throws Exception {
        Document document = TestHelper.TEST_RENDERING_DOCUMENT.clone();
        transformation.apply(document);
        Element title = document.select("title").first();
        assertThat(title, is(notNullValue()));
        assertThat(title.html(), equalTo("Name 2"));
    }

    @Test
    public void whenApplyRenderingTransformation_thenH1IsRendered() throws Exception {
        Document document = TestHelper.TEST_RENDERING_DOCUMENT.clone();
        transformation.apply(document);
        Element h1 = document.select("h1[title=Name 2]").first();
        assertThat(h1, is(notNullValue()));
    }

    @Test
    public void whenApplyRenderingTransformation_thenH2IsRendered() throws Exception {
        Document document = TestHelper.TEST_RENDERING_DOCUMENT.clone();
        transformation.apply(document);
        Element h2 = document.select("h2[title=Spouse 2]").first();
        assertThat(h2, is(notNullValue()));
        assertThat(h2.html(), equalTo("Spouse: Spouse 2"));
    }
}