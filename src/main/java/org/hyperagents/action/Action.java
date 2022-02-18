package org.hyperagents.action;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.hyperagents.util.RDFComponent;

public abstract class Action extends RDFComponent {


    protected Action(Resource id, Model model) {
        super(id);
    }

    protected static abstract class Builder extends RDFComponent.Builder {

        protected Builder(Resource id) {
            super(id);
        }
    }

}
