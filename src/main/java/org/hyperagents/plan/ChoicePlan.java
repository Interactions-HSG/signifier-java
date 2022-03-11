package org.hyperagents.plan;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.RDFS;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;

public class ChoicePlan extends CompositePlan {

    Set<Plan> plans;

    protected ChoicePlan(Resource planId, Model model, Set<Plan> plans) {
        super(planId, model);
        this.plans = plans;
    }

    public Set<Plan> getPlans(){
        return plans;
    }

    public Plan getFirstPlan(){
        return new Vector<>(plans).firstElement();
    }

    public static boolean hasOption(Plan p){
        Resource planId = p.getId();
        Model model = p.getModel();
        boolean b = false;
        Optional<Value> optional = Models.object(model.filter(planId,
                RDFS.rdf.createIRI(SignifierOntology.hasOption),null));
        if (optional.isPresent()){
            b= true;
        }
        return b;

    }

    public static Set<Plan> getOptions(Plan p){
        Resource planId = p.getId();
        Model model = p.getModel();
        Set<Plan> plans = new HashSet<>();
        Set<Resource> options = Models.objectResources(model.filter(planId,
                RDFS.rdf.createIRI(SignifierOntology.hasOption),null));
        for (Resource option : options){
            Plan plan = Plan.retrievePlan(option, model);
            plans.add(plan);

        }
        return plans;
    }

    public static ChoicePlan getAsChoicePlan(Plan p){
        Resource planId = p.getId();
        Model model = p.getModel();
        Set<Plan> options = getOptions(p);
        return new ChoicePlan.Builder(planId).addOptions(options).build();
    }

    public static class Builder extends Plan.Builder {

        Set<Plan> plans;

        public Builder(Resource planId) {
            super(planId);
            this.plans = new HashSet<>();
        }

        public Builder addOption(Plan p){
            modelBuilder.addOption(planId, p);
            this.plans.add(p);
            return this;
        }

        public Builder addOptions(Set<Plan> planSet){
            modelBuilder.addOptions(planId, planSet);
            this.plans.addAll(planSet);
            return this;
        }

        public ChoicePlan build(){
            return new ChoicePlan(planId, modelBuilder.build(), plans);
        }
    }
}
