package org.hyperagents.plan;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.RDFS;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SequencePlan extends CompositePlan {

    protected List<Plan> plans;

    protected SequencePlan(Resource planId, Model model, List<Plan> plans) {
        super(planId, model);
        this.plans = plans;
    }

    public List<Plan> getSequence(){
        return plans;
    }


    public static boolean hasSequence(Plan p){
        Resource planId = p.getId();
        Model model = p.getModel();
        boolean b = false;
        Optional<Value> optional = Models.object(model.filter(planId,
                RDFS.rdf.createIRI(SignifierOntology.hasSequence),null));
        if (optional.isPresent()){
            b= true;
        }
        return b;
    }

    public static List<Plan> getSequence(Plan p){
        Resource planId = p.getId();
        Model model = p.getModel();
        List<Plan> plans = new ArrayList<>();
        Optional<Resource> optionalResource = Models.objectResource(model.filter(planId,
                RDFS.rdf.createIRI(SignifierOntology.hasSequence),null));
        if (optionalResource.isPresent()){
            Resource sequenceId = optionalResource.get();
            List<Resource> list = RDFS.readResourceSeq(sequenceId, model);
            for (Resource id : list){
                Plan plan = Plan.retrievePlan(id, model);
                plans.add(plan);
            }
            return plans;
        }
        return plans;
    }

    public static SequencePlan getAsSequencePlan(Plan p){
        SequencePlan sp = null;
        if (hasSequence(p)){
            List<Plan> sequence = getSequence(p);
            sp = new SequencePlan.Builder(p.getId())
                    .addSequence(sequence)
                    .build();
        }
        return sp;
    }

    public static class Builder extends Plan.Builder {

        List<Plan> plans;

        public Builder(Resource planId) {
            super(planId);
            plans = new ArrayList<>();
        }


        public Builder addSequence(List<Plan> sequence){
            this.plans = sequence;
            modelBuilder.addSequence(planId, sequence);
            return this;
        }

        public SequencePlan build(){
            return new SequencePlan(planId, modelBuilder.build(), plans);
        }
    }
}
