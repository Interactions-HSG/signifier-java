package org.hyperagents.util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;

public class ChoicePlan extends Plan {

    Set<Affordance> affordances;

    protected ChoicePlan(Resource planId, Model model, Set<Affordance> affordances) {
        super(planId, model);
        this.affordances = affordances;
    }

    public Set<Affordance> getAffordances(){
        return affordances;
    }

    public Affordance getFirstAffordance(){
        return new Vector<>(affordances).firstElement();
    }

    public static boolean hasOption(Plan p){
        Resource planId = p.getPlanId();
        Model model = p.getModel();
        boolean b = false;
        Optional<Value> optional = Models.object(model.filter(planId,
                RDFS.rdf.createIRI(SignifierOntology.hasOption),null));
        if (optional.isPresent()){
            b= true;
        }
        return b;

    }

    public static Set<Affordance> getOptions(Plan p){
        Resource planId = p.getPlanId();
        Model model = p.getModel();
        Set<Affordance> affordances = new HashSet<>();
        Set<Resource> options = Models.objectResources(model.filter(planId,
                RDFS.rdf.createIRI(SignifierOntology.hasOption),null));
        for (Resource option : options){
            Affordance affordance = Affordance.retrieveAffordance(option,model);
            affordances.add(affordance);
        }
        return affordances;
    }

    public static ChoicePlan getAsChoicePlan(Plan p){
        Resource planId = p.getPlanId();
        Model model = p.getModel();
        Set<Affordance> options = getOptions(p);
        return new ChoicePlan.Builder(planId).addOptions(options).build();
    }

    public static class Builder extends Plan.Builder {

        Set<Affordance> affordances;

        public Builder(Resource planId) {
            super(planId);
            this.affordances = new HashSet<>();
        }

        public Builder addOption(Affordance a){
            modelBuilder.addOption(planId, a);
            this.affordances.add(a);
            return this;
        }

        public Builder addOptions(Set<Affordance> affordanceSet){
            modelBuilder.addOptions(planId, affordanceSet);
            this.affordances.addAll(affordanceSet);
            return this;
        }

        public ChoicePlan build(){
            return new ChoicePlan(planId, modelBuilder.build(), affordances);
        }
    }
}
