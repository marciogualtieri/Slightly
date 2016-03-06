package biz.netcentric.processors;


import biz.netcentric.wrappers.ScriptEngineWrapper;
import biz.netcentric.transformations.*;
import org.jsoup.nodes.Document;

import javax.script.ScriptException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentProcessor {

    private final ScriptEngineWrapper scriptEngineWrapper;
    private List<Transformation> transformations = new ArrayList<>();

    public DocumentProcessor(ScriptEngineWrapper scriptEngineWrapper) {
        this.scriptEngineWrapper = scriptEngineWrapper;
        addTransformations();
    }

    public void process(Document document) throws ScriptException, IOException {
        for (Transformation transformation : transformations) {
            transformation.apply(document);
        }
    }

    private void addTransformations() {
        transformations = new ArrayList<>();
        transformations.add(new DataInclusionTransformation(scriptEngineWrapper));
        transformations.add(new DataIfTransformation(scriptEngineWrapper));
        transformations.add(new DataForTransformation(scriptEngineWrapper));
        transformations.add(new DataLocalVarTransformation(scriptEngineWrapper));
        transformations.add(new RenderingTransformation(scriptEngineWrapper));
    }
}
