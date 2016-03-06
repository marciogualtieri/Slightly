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

public class DataInclusionTransformationTest {

    private Transformation transformation;
    private ScriptEngineWrapper scriptEngineWrapper;

    @Before
    public void before() throws Exception {
        scriptEngineWrapper = new ScriptEngineWrapper();
        transformation = new DataInclusionTransformation(scriptEngineWrapper);
    }

    @Test
    public void whenApplyDataInclusionTransformation_thenFooterTemplateIsAppended() throws Exception {
        Document document = TestHelper.TEST_DATA_INCLUSION_DOCUMENT.clone();
        transformation.apply(document);
        assertThatTemplateIsPresent(document,
                "footer", "Netcentric", "Code. Analyze. Build. Repair. Improve. Innovate.");
    }

    @Test
    public void whenApplyDataInclusionTransformation_thenHeaderTemplateIsAppended() throws Exception {
        Document document = TestHelper.TEST_DATA_INCLUSION_DOCUMENT.clone();
        transformation.apply(document);
        assertThatTemplateIsPresent(document, "header", "Netcentric", "Expect Excellence.");
    }

    private void assertThatTemplateIsPresent(Document document, String id, String brandValue, String sloganValue) {
        Element brand = document.select("[id=" + id + "_brand]").first();
        assertThat(brand, is(notNullValue()));
        assertThat(brand.html(), equalTo(brandValue));
        Element slogan = document.select("[id=" + id + "_slogan]").first();
        assertThat(slogan, is(notNullValue()));
        assertThat(slogan.html(), equalTo(sloganValue));
    }

}
