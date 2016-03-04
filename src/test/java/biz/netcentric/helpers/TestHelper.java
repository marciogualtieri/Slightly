package biz.netcentric.helpers;

import com.google.common.collect.ImmutableList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Tag;
import org.jsoup.safety.Whitelist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestHelper {
    private static final DocumentHelper documentHelper = new DocumentHelper();
    public static final Document TEST_DATA_IF_DOCUMENT;
    public static final Document TEST_DATA_FOR_DOCUMENT;
    public static final List<Element> TEST_CHILDREN;
    public static final Document TEST_RENDERING_DOCUMENT;
    public static final String TEST_SCRIPT =
            "importClass(Packages.biz.netcentric.Person);" +
                    "var notMarried=Person.lookup(\"1\");" +
                    "var marriedWithChildren=Person.lookup(\"2\");";
    public static final List<String> TEST_CHILDREN_NAMES =
            ImmutableList.of("Children 0", "Children 1", "Children 2");

    static {
        try {
            TEST_DATA_IF_DOCUMENT = documentHelper.getHtmlFileAsDocument("templates/data_if_transformer.html");
            TEST_DATA_FOR_DOCUMENT = documentHelper.getHtmlFileAsDocument("templates/data_for_transformer.html");
            TEST_RENDERING_DOCUMENT = documentHelper.getHtmlFileAsDocument("templates/rendering_transformer.html");
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        TEST_CHILDREN = createChildElements();
    }

    public static String getNormalizedHtml(Document document) {
        document.normalise();
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode(Entities.EscapeMode.xhtml);
        return Jsoup.clean(document.html(), "", Whitelist.relaxed(), settings);
    }

    private static Element createElement(String tag, String html, String title) {
        Element element = new Element(Tag.valueOf(tag), "");
        element.html(html);
        element.attr("title", title);
        return element;
    }

    private static List<Element> createChildElements() {
        List<Element> children = new ArrayList<>();
        for (String name : TEST_CHILDREN_NAMES) {
            Element child = createElement("div", String.format("Child: %s", name),
                    "${withChildren.name} child");
            children.add(child);
        }
        return children;
    }

    private TestHelper() {
    }
}
