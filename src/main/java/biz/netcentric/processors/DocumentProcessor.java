package biz.netcentric.processors;


import biz.netcentric.script.ScriptScope;
import biz.netcentric.transformations.*;
import org.jsoup.nodes.Document;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentProcessor {

    private final ScriptScope scriptScope;
    private List<Transformation> transformations = new ArrayList<>();

    public DocumentProcessor(ScriptScope scriptScope) {
        this.scriptScope = scriptScope;
        addTransformations();
    }

    public void process(Document document) throws ScriptException, IOException {
        for (Transformation transformation : transformations) {
            transformation.apply(document);
        }
    }

    private void addTransformations() {
        transformations = new ArrayList<>();
        transformations.add(new DataInclusionTransformation(scriptScope));
        transformations.add(new DataIfTransformation(scriptScope));
        transformations.add(new DataForTransformation(scriptScope));
        transformations.add(new RenderingTransformation(scriptScope));
    }
}
