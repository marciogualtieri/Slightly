package biz.netcentric.transformations;

import biz.netcentric.script.ScriptScope;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.script.ScriptException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Expands data-for tags in the document where the transformation is applied.
 */
public class DataForTransformation extends Transformation {
    private static final Pattern DATA_FOR_ATTRIBUTE_NAME_PATTERN = Pattern.compile("(data-for-)(.+)");
    private static final int DATA_FOR_ATTRIBUTE_NAME_GROUP_INDEX = 0;
    private static final int DATA_FOR_VARIABLE_GROUP_INDEX = 2;
    private static final String EXPRESSION_ENVELOPE_FORMAT = "\\$\\{%s\\}";

    public DataForTransformation(ScriptScope scriptScope) {
        super(scriptScope);
    }

    @Override
    public void apply(Document document) throws ScriptException {
        evaluateScript(document);
        Elements dataFors = document.select("[^data-for]");
        for (Element dataFor : dataFors) {
            processDataFor(dataFor);
        }
    }

    private void processDataFor(Element dataFor) throws ScriptException {
        String dataForAttribute = getDataForAttributeName(dataFor);
        String dataForVariable = getDataForVariableName(dataFor);
        String expression = dataFor.attr(dataForAttribute);
        List dataList = scriptScope.evaluateToList(expression);
        for (Object data : dataList) {
            Element dataElement =
                    buildDataElement(dataFor, dataForAttribute, dataForVariable, String.valueOf(data));
            dataFor.before(dataElement);
        }
        dataFor.remove();
    }

    private String getDataForAttributeName(Element dataFor) {
        return documentHelper.getAttributeByPattern(
                dataFor,
                DATA_FOR_ATTRIBUTE_NAME_PATTERN,
                DATA_FOR_ATTRIBUTE_NAME_GROUP_INDEX);
    }

    private String getDataForVariableName(Element dataFor) {
        return documentHelper.getAttributeByPattern(
                dataFor,
                DATA_FOR_ATTRIBUTE_NAME_PATTERN,
                DATA_FOR_VARIABLE_GROUP_INDEX);
    }

    private Element buildDataElement(Element dataForElement,
                                     String dataForAttribute,
                                     String dataForVariable,
                                     String dataForVariableValue) {
        Element dataElement = dataForElement.clone();
        String html = dataForElement.html();
        dataElement.html(html.replaceAll(envelopVariable(dataForVariable), dataForVariableValue));
        dataElement.removeAttr(dataForAttribute);
        return dataElement;
    }

    private String envelopVariable(String variable) {
        return String.format(EXPRESSION_ENVELOPE_FORMAT, variable);
    }
}
