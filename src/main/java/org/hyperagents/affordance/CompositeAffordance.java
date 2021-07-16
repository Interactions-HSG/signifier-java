package org.hyperagents.affordance;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.Plan;
import org.hyperagents.util.RDFS;
import org.hyperagents.util.State;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;

public class CompositeAffordance extends Affordance{

    private Set<Plan> plans;
    protected CompositeAffordance(Resource affordanceId, Optional<State> precondition, Optional<State> objective, Model model, Set<Plan> plans) {
        super(affordanceId, precondition, objective, model);
        this.plans = plans;
    }

    public Set<Plan> getPlans(){
        return this.plans;
    }

    public Plan getFirstPlan(){
        return new Vector<>(plans).firstElement();
    }

    public static Set<Plan> retrievePlans(Affordance affordance){
        Set<Plan> plans = new HashSet<>();
        Model m = affordance.getModel();
        Set<Resource> planIds = Models.objectResources(m.filter(affordance.getAffordanceId(),
                RDFS.rdf.createIRI(SignifierOntology.hasPlan),null));
        for (Resource planId : planIds){
            Model planModel = RDFS.retrieveBlock(planId, m);
            Plan p = new Plan.Builder(planId).addModel(planModel).build();
            plans.add(p);
        }
        return plans;
    }

    public static CompositeAffordance getAsCompositeAffordance(Affordance affordance){
        CompositeAffordance.Builder builder = new CompositeAffordance.Builder(affordance.getAffordanceId());
        Optional<State> precondition = affordance.getPrecondition();
        if (precondition.isPresent()){
            builder.setPrecondition(precondition.get());
        }
        Optional<State> objective = affordance.getObjective();
        if (objective.isPresent()){
            builder.setObjective(objective.get());
        }
        builder.add(affordance.getModel());
        Set<Plan> plans = retrievePlans(affordance);
        builder.addPlans(plans);
        return builder.build();
    }

    public static class Builder extends Affordance.Builder {

        private Set<Plan> plans;

        public Builder(Resource affordanceId) {
            super(affordanceId);
            this.plans = new HashSet<>();
        }

        public Builder addPlan(Plan plan){
            this.plans.add(plan);
            graphBuilder.addPlan(plan);
            return this;
        }

        public Builder addPlans(Set<Plan> plans){
            this.plans.addAll(plans);
            graphBuilder.addPlans(plans);
            return this;
        }

        public CompositeAffordance build(){
            return new CompositeAffordance(affordanceId, precondition, objective, graphBuilder.build(), plans);
        }
    }
}
