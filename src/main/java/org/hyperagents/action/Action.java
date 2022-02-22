package org.hyperagents.action;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.hyperagents.io.SignifierReader;
import org.hyperagents.util.RDFComponent;

public class Action extends RDFComponent {

    Model model;


    protected Action(Resource id, Model model) {
        super(id);
        this.model = model;
    }

    public static Action readAction(Resource id, Model m){
        Model model = SignifierReader.retrieveBlock(id, m);
        return new Action(id, model);

    }

    @Override
    public Model getModel() {
        return model;
    }

    protected static abstract class Builder extends RDFComponent.Builder {

        protected Builder(Resource id) {
            super(id);
        }
    }

}
