package biz.netcentric.helpers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DocumentHelper {

    public Document getHtmlFileAsDocument(String fileNameAndPath) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL url = classLoader.getResource(fileNameAndPath);
        if (url == null) {
            throw new FileNotFoundException(fileNameAndPath);
        }
        File file = new File(url.getFile());
        return Jsoup.parse(file, "UTF-8");
    }

    public String getAttributeByPattern(Element element, Pattern pattern, int group) {
        List<Attribute> attributes = element.attributes().asList();
        for (Attribute attribute : attributes) {
            Matcher matcher = pattern.matcher(attribute.getKey());
            if (matcher.find()) {
                return matcher.group(group);
            }
        }
        return null;
    }

}
