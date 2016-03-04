package biz.netcentric.processors;


import biz.netcentric.script.ScriptScope;
import biz.netcentric.transformations.DataForTransformation;
import biz.netcentric.transformations.DataIfTransformation;
import biz.netcentric.transformations.RenderingTransformation;
import biz.netcentric.transformations.Transformation;
import org.jsoup.nodes.Document;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class DocumentProcessor {

    private final ScriptScope scriptScope;
    private List<Transformation> transformations = new ArrayList<>();

    public DocumentProcessor(ScriptScope scriptScope) {
        this.scriptScope = scriptScope;
        addTransformations();
    }

    public void process(Document document) throws ScriptException {
        for (Transformation transformation : transformations) {
            transformation.apply(document);
        }
    }

    private void addTransformations() {
        transformations = new ArrayList<>();
        transformations.add(new DataIfTransformation(scriptScope));
        transformations.add(new DataForTransformation(scriptScope));
        transformations.add(new RenderingTransformation(scriptScope));
    }
}
