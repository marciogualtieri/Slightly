package biz.netcentric.processors;

import biz.netcentric.helpers.HtmlHelper;
import biz.netcentric.helpers.TestHelper;
import biz.netcentric.wrappers.ScriptEngineWrapper;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class DocumentProcessorTest {

    private DocumentProcessor documentProcessor;
    private HtmlHelper htmlHelper = new HtmlHelper();

    @Before
    public void before() throws Exception {
        documentProcessor = new DocumentProcessor(new ScriptEngineWrapper());
    }

    @Test
    public void whenIRenderATemplateWithDataLocalVar_thenPageIsRendered() throws Exception {
        Document document = TestHelper.TEST_DATA_LOCAL_VAR_DOCUMENT.clone();
        documentProcessor.process(document);
        Document expectedDocument = htmlHelper
                .getHtmlFileAsDocument(TestHelper.TEST_RENDERED_PAGES_FOLDER +
                        "/data_local_var_rendered.html");
        assertThat(TestHelper.getNormalizedHtml(document),
                equalTo(TestHelper.getNormalizedHtml(expectedDocument)));
    }
}