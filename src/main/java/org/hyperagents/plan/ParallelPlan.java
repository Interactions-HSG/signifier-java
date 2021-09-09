package org.hyperagents.plan;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.RDFS;

import java.util.HashSet;
import java.util.Set;

public class ParallelPlan extends CompositePlan {

    Set<Plan> plans;

    protected ParallelPlan(Resource planId, Model model, Set<Plan> plans) {
        super(planId, model);
        this.plans = plans;
    }

    public Set<Plan> getAffordances(){
        return plans;
    }

    public static Set<Plan> getPlans(Plan p){
        Resource planId = p.getId();
        Model model = p.getModel();
        Set<Plan> plans = new HashSet<>();
        Set<Resource> options = Models.objectResources(model.filter(planId,
                RDFS.rdf.createIRI(SignifierOntology.hasParallelPlan),null));
        for (Resource option : options){
            Plan plan = Plan.retrievePlan(option, model);
            plans.add(plan);
        }
        return plans;
    }

    public static ParallelPlan getAsParallelPlan(Plan p){
        Resource planId = p.getId();
        Model model = p.getModel();
        Set<Plan> plans = getPlans(p);
        return new Builder(planId).addParallelPlans(plans).build();
    }

    public static class Builder extends Plan.Builder {

        Set<Plan> plans;

        public Builder(Resource planId) {
            super(planId);
            this.plans = new HashSet<>();
        }

        public Builder addParallelPlan(Plan p){
            modelBuilder.addParallelPlan(planId, p);
            this.plans.add(p);
            return this;
        }

        public Builder addParallelPlans(Set<Plan> planSet){
            modelBuilder.addParallelPlans(planId, planSet);
            this.plans.addAll(planSet);
            return this;
        }

        public ParallelPlan build(){
            return new ParallelPlan(planId, modelBuilder.build(), plans);
        }
    }
}
