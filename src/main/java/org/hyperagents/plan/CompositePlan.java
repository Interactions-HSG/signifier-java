package org.hyperagents.plan;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;

public abstract class CompositePlan extends DirectPlan {
    protected CompositePlan(Resource planId, Model model) {
        super(planId, model);
    }
}
