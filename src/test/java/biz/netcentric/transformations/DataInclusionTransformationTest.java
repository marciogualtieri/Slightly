package test.biz.netcentric.transformations;

import biz.netcentric.helpers.TestHelper;
import biz.netcentric.script.ScriptScope;
import biz.netcentric.transformations.DataInclusionTransformation;
import biz.netcentric.transformations.Transformation;
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
    private ScriptScope scriptScope;

    @Before
    public void before() throws Exception {
        scriptScope = new ScriptScope();
        transformation = new DataInclusionTransformation(scriptScope);
    }

    @Test
    public void whenApplyDataInclusionTransformation_thenInclusionTemplateIsAppended() throws Exception {
        Document document = TestHelper.TEST_DATA_INCLUSION_DOCUMENT.clone();
        transformation.apply(document);
        Element brand = document.select("[id=brand]").first();
        assertThat(brand, is(notNullValue()));
        assertThat(brand.html(), equalTo("Netcentric"));
        Element slogan = document.select("[id=slogan]").first();
        assertThat(slogan, is(notNullValue()));
        assertThat(slogan.html(), equalTo("Code. Analyze. Build. Repair. Improve. Innovate."));
    }

}
