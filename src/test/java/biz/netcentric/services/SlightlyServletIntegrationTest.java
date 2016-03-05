package biz.netcentric.services;

import biz.netcentric.helpers.HtmlHelper;
import biz.netcentric.helpers.TestHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import test.utils.annotation.type.IntegrationTest;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

@Category(IntegrationTest.class)
public class SlightlyServletIntegrationTest {
    private static final int BROWSER_TIMEOUT_SECS = 30;
    private static final String BASE_URL = "http://localhost:8080/Slightly/";

    private static FirefoxDriver browser;
    private static final HtmlHelper htmlHelper = new HtmlHelper();

    @Before
    public void before() throws Exception {
        browser = new FirefoxDriver();
        browser.manage().timeouts().implicitlyWait(BROWSER_TIMEOUT_SECS, TimeUnit.SECONDS);
    }

    @After
    public void after() throws Exception {
        browser.quit();
    }

    @Test
    public void whenIGetTheRenderedPersonOnePageResource_thenOk() throws Exception {
        assertPersonPage(1);
    }

    @Test
    public void whenIGetTheRenderedPersonTwoPageResource_thenOk() throws Exception {
        assertPersonPage(2);
    }

    @Test
    public void whenIGetTheRenderedPersonThreePageResource_thenOk() throws Exception {
        assertPersonPage(3);
    }

    @Test
    public void whenIGetNonExistentResource_thenOk() throws Exception {
        Document document = getResourceInnerHtmlAsDocument("i/do/not/exist.html");
        Document expectedDocument = htmlHelper
                .getHtmlFileAsDocument(TestHelper.RENDERED_PAGES_FOLDER +
                        "/template_does_not_exist.html");
        assertThat(TestHelper.getNormalizedHtml(document),
                equalTo(TestHelper.getNormalizedHtml(expectedDocument)));
    }

    private Document getResourceInnerHtmlAsDocument(String resource) {
        browser.get(BASE_URL + resource);
        WebElement htmlElement = browser.findElement(By.tagName("html"));
        String htmlString = htmlElement.getAttribute("innerHTML");
        return Jsoup.parseBodyFragment(htmlString, "UTF-8");
    }

    private void assertPersonPage(int id) throws IOException {
        Document document = getResourceInnerHtmlAsDocument(String.format("person.html?id=%d", id));
        Document expectedDocument =
                htmlHelper.getHtmlFileAsDocument(
                        String.format("%s/person%d.html", TestHelper.RENDERED_PAGES_FOLDER, id));
        assertThat(TestHelper.getNormalizedHtml(document),
                equalTo(TestHelper.getNormalizedHtml(expectedDocument)));
    }

}