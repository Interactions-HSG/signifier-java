package org.hyperagents.util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;

import java.util.HashSet;
import java.util.Set;

public class ParallelPlan extends Plan {

    Set<Affordance> affordances;

    protected ParallelPlan(Resource planId, Model model, Set<Affordance> affordances) {
        super(planId, model);
        this.affordances = affordances;
    }

    public Set<Affordance> getAffordances(){
        return affordances;
    }

    public static Set<Affordance> getAffordances(Plan p){
        Resource planId = p.getPlanId();
        Model model = p.getModel();
        Set<Affordance> affordances = new HashSet<>();
        Set<Resource> options = Models.objectResources(model.filter(planId,
                RDFS.rdf.createIRI(SignifierOntology.hasParallelAffordance),null));
        for (Resource option : options){
            Affordance affordance = Affordance.retrieveAffordance(option,model);
            affordances.add(affordance);
        }
        return affordances;
    }

    public static ParallelPlan getAsParallelPlan(Plan p){
        Resource planId = p.getPlanId();
        Model model = p.getModel();
        Set<Affordance> affordances= getAffordances(p);
        return new Builder(planId).addParallelAffordances(affordances).build();
    }

    public static class Builder extends Plan.Builder {

        Set<Affordance> affordances;

        public Builder(Resource planId) {
            super(planId);
            this.affordances = new HashSet<>();
        }

        public Builder addParallelAffordance(Affordance a){
            modelBuilder.addParallelAffordance(planId, a);
            this.affordances.add(a);
            return this;
        }

        public Builder addParallelAffordances(Set<Affordance> affordanceSet){
            modelBuilder.addParallelAffordances(planId, affordanceSet);
            this.affordances.addAll(affordanceSet);
            return this;
        }

        public ParallelPlan build(){
            return new ParallelPlan(planId, modelBuilder.build(), affordances);
        }
    }
}
