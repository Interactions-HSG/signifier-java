package org.hyperagents.util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SequencePlan extends Plan {

    protected List<Affordance> affordances;

    protected SequencePlan(Resource planId, Model model, List<Affordance> affordances) {
        super(planId, model);
        this.affordances = affordances;
    }

    public List<Affordance> getSequence(){
        return affordances;
    }


    public static boolean hasSequence(Plan p){
        Resource planId = p.getPlanId();
        Model model = p.getModel();
        boolean b = false;
        Optional<Value> optional = Models.object(model.filter(planId,
                RDFS.rdf.createIRI(SignifierOntology.hasSequence),null));
        if (optional.isPresent()){
            b= true;
        }
        return b;
    }

    public static List<Affordance> getSequence(Plan p){
        Resource planId = p.getPlanId();
        Model model = p.getModel();
        List<Affordance> affordances = new ArrayList<>();
        Optional<Resource> optionalResource = Models.objectResource(model.filter(planId,
                RDFS.rdf.createIRI(SignifierOntology.hasSequence),null));
        if (optionalResource.isPresent()){
            Resource sequenceId = optionalResource.get();
            List<Resource> list = RDFS.readResourceList(sequenceId, model);
            for (Resource id : list){
                Affordance affordance = Affordance.retrieveAffordance(id, model);
                affordances.add(affordance);
            }
            return affordances;
        }
        return affordances;
    }

    public static SequencePlan getAsSequencePlan(Plan p){
        SequencePlan sp = null;
        if (hasSequence(p)){
            List<Affordance> sequence = getSequence(p);
            sp = new SequencePlan.Builder(p.getPlanId())
                    .addSequence(sequence)
                    .build();
        }
        return sp;
    }

    public static class Builder extends Plan.Builder {

        List<Affordance> affordances;

        public Builder(Resource planId) {
            super(planId);
            affordances = new ArrayList<>();
        }


        public Builder addSequence(List<Affordance> sequence){
            this.affordances = sequence;
            modelBuilder.addSequence(planId, sequence);
            return this;
        }

        public SequencePlan build(){
            return new SequencePlan(planId, modelBuilder.build(), affordances);
        }
    }
}
