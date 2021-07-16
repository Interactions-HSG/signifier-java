package org.hyperagents.affordance;

import org.hyperagents.io.SignifierReader;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.State;
import org.hyperagents.util.RDFS;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.Models;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ChoiceAffordance extends Affordance {

    private Set<Affordance> affordances;

    public ChoiceAffordance(Resource affordanceId, Optional<State> precondition,
                            Optional<State> objective, Model model, Set<Affordance> affordances){
        super(affordanceId, precondition, objective, model);
        this.affordances = affordances;
    }

    public Set<Affordance> getSubAffordances(){ return affordances; }

    public static ChoiceAffordance retrieveChoiceAffordance(Resource affordanceId, Model m){
        Model model = SignifierReader.getBlock(affordanceId,m);
        ChoiceAffordance.Builder builder = new ChoiceAffordance.Builder(affordanceId);
        Optional<Resource> preconditionId = Models.objectResource(model.filter(affordanceId,RDFS.rdf.createIRI(SignifierOntology.hasPrecondition),null));
        if (preconditionId.isPresent()){
            State precondition = State.retrieveState(preconditionId.get(),model);
            builder.setPrecondition(precondition);
        }
        Optional<Resource> objectiveId = Models.objectResource(model.filter(affordanceId,RDFS.rdf.createIRI(SignifierOntology.hasObjective),null));
        if (objectiveId.isPresent()){
            State objective = State.retrieveState(objectiveId.get(),model);
            builder.setPrecondition(objective);
        }
        Set<Resource> affordanceIds = Models.objectResources(model.filter(affordanceId,RDFS.rdf.createIRI(SignifierOntology.hasOption),null));
        for (Resource newAffordanceId : affordanceIds){
            Affordance affordance = Affordance.retrieveAffordance(newAffordanceId,model);
            builder.addAffordance(affordance);
        }
        return builder.build();


    }

    public static ChoiceAffordance getAsChoiceAffordance(Affordance affordance){
        ChoiceAffordance.Builder builder = new ChoiceAffordance.Builder(affordance.getAffordanceId());
        if (affordance.getPrecondition().isPresent()){
            builder.setPrecondition(affordance.getPrecondition().get());
        }
        if (affordance.getObjective().isPresent()){
            builder.setObjective(affordance.getObjective().get());
        }
        Model model = affordance.getModel();
        Set<Resource> affordanceIds = Models.objectResources(model.filter(affordance.getAffordanceId(),
                RDFS.rdf.createIRI(SignifierOntology.hasOption),null));
        for (Resource affordanceId : affordanceIds){
            Affordance a = Affordance.retrieveAffordance(affordanceId,model);
            builder.addAffordance(a);
        }
        builder.add(model);
        return builder.build();
    }


    public static class Builder extends Affordance.Builder{
        private Set<Affordance> affordances;


        public Builder(Resource affordanceId){
            super(affordanceId);
            this.affordances=new HashSet<>();
        }

        public Builder addAffordance(Affordance affordance){
            this.affordances.add(affordance);
            this.graphBuilder.addOption(affordanceId,affordance);
            return this;
        }

        public Builder add(Model m){
            for (Statement s: m){
                this.graphBuilder.add(s.getSubject(),s.getPredicate(),s.getObject());
            }
            return this;
        }

        public ChoiceAffordance build(){
            return new ChoiceAffordance(this.affordanceId, this.precondition, this.objective, graphBuilder.build(),this.affordances);
        }
    }
}
