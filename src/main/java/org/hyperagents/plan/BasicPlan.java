package org.hyperagents.plan;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;

public abstract class BasicPlan extends DirectPlan {
    protected BasicPlan(Resource planId, Model model) {
        super(planId, model);
    }
}
