package org.hyperagents.plan;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;

public abstract class DirectPlan extends Plan {
    protected DirectPlan(Resource planId, Model model) {
        super(planId, model);
    }
}
