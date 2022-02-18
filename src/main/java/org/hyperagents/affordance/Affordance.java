package org.hyperagents.affordance;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.hyperagents.action.Action;
import org.hyperagents.io.SignifierReader;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.plan.DirectPlan;
import org.hyperagents.signifier.SignifierModelBuilder;
import org.hyperagents.plan.Plan;
import org.hyperagents.util.RDFComponent;
import org.hyperagents.util.State;
import org.hyperagents.util.RDFS;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Models;

import java.util.*;

public class Affordance extends RDFComponent {

    protected Resource affordanceId;

    private Optional<State> precondition;
    private Optional<State> postcondition;
    private Set<State> objectives;
    private Set<Action> actions;

    private Model model;

    protected Affordance(Resource affordanceId, Optional<State> precondition, Optional<State> postcondition, Set<State> objectives, Set<Action> actions, Model model){
        super(affordanceId);
        this.affordanceId = affordanceId;
        this.precondition = precondition;
        this.postcondition = postcondition;
        this.objectives = objectives;
        this.actions = actions;
        this.model = model;
    }

    public Resource getId(){ return affordanceId; }

    public Optional<State> getPrecondition() { return precondition; }

    public Optional<State> getPostcondition(){ return postcondition; }

    public Set<State> getObjectives(){ return objectives; }

    public List<State> getObjectiveList(){ return new ArrayList<>(objectives); }

    public Set<Action> getActions(){ return actions; }

    public Action getFirstAction(){ return new Vector<Action>(actions).firstElement(); }

    public Model getModel(){
        return model;
    }

    public Optional<Value> getValue(IRI predicate){
      return Models.object(model.filter(affordanceId,predicate,null));

    }

    public Optional<Value> getValue(String predicate){
        return getValue(RDFS.rdf.createIRI(predicate));
    }

    public Optional<Literal> getLiteral(IRI predicate){
        return Models.objectLiteral(model.filter(affordanceId,predicate,null));

    }

    public Optional<Literal> getLiteral(String predicate){
        return getLiteral(RDFS.rdf.createIRI(predicate));
    }



    public static Affordance retrieveAffordance(Resource newAffordanceId, Model model) {
        Affordance.Builder builder = new Affordance.Builder(newAffordanceId);
        Model m = SignifierReader.getBlock(newAffordanceId,model);
        builder.add(m);
        Optional<Resource> preconditionId = Models.objectResource(m.filter(newAffordanceId,
                RDFS.rdf.createIRI(SignifierOntology.hasPrecondition),null));
        if (preconditionId.isPresent()){
            State precondition = State.retrieveState(preconditionId.get(),m);
            builder.setPrecondition(precondition);
        }
        Optional<Resource> postconditionId = Models.objectResource(m.filter(newAffordanceId,
                RDFS.rdf.createIRI(SignifierOntology.hasPostcondition),null));
        if (postconditionId.isPresent()){
            State postcondition = State.retrieveState(postconditionId.get(),m);
            builder.setPostcondition(postcondition);
        }
        Set<Resource> objectiveIds = Models.objectResources(model.filter(newAffordanceId,
                RDFS.rdf.createIRI(SignifierOntology.hasObjective), null));
        for (Resource objectiveId: objectiveIds){
            State objective = State.retrieveState(objectiveId, model);
            builder.addObjective(objective);
        }
        Set<Resource> actionIds = Models.objectResources(model.filter(newAffordanceId,
                RDFS.rdf.createIRI(SignifierOntology.hasAction), null));
        for (Resource actionId : actionIds){
            Plan.Builder planBuilder = new Plan.Builder(actionId);
            planBuilder.addModel(RDFS.retrieveBlock(actionId, model));
            Action action = null;//planBuilder.build();
            builder.addAction(action);
        }
        return builder.build();
    }





    public static class Builder{
        protected Resource affordanceId;
        protected Optional<State> precondition;
        protected Optional<State> postcondition;
        protected Set<State> objectives;
        protected Set<Action> actions;
        protected SignifierModelBuilder graphBuilder;
        protected ValueFactory rdf;

        public Builder(Resource affordanceId){
            this.affordanceId = affordanceId;
            this.precondition = Optional.empty();
            this.objectives = new HashSet<>();
            this.actions = new HashSet<>();
            this.graphBuilder = new SignifierModelBuilder();
            this.rdf=RDFS.rdf;
        }

        public Builder setPrecondition(State precondition){
            this.precondition = Optional.of(precondition);
            this.graphBuilder.addPrecondition(affordanceId,precondition);
            return this;
        }

        public Builder setPostcondition(State postcondition){
            this.postcondition = Optional.of(postcondition);
            this.graphBuilder.addPostcondition(affordanceId,postcondition);
            return this;
        }

        public Builder addObjective(State objective){
            this.objectives.add(objective);
            this.graphBuilder.addObjective(affordanceId, objective);
            return this;
        }


        public Builder add(Model m){
            this.graphBuilder.addModel(m);
            return this;
        }

        public Builder add(IRI predicate, Value object){
            graphBuilder.add(affordanceId,predicate,object);
            return this;
        }

        public Builder add(String predicate, Value object){
            return add(RDFS.rdf.createIRI(predicate),object);
        }

        public Builder add(Resource subject, IRI predicate, Value object){
            graphBuilder.add(subject, predicate, object);
            return this;
        }

        public Builder addAction(Action action){
            this.actions.add(action);
            graphBuilder.addAction(affordanceId, action);
            return this;
        }



        public Affordance build(){
            graphBuilder.addType(affordanceId, rdf.createIRI(SignifierOntology.Affordance));
            return new Affordance(affordanceId, precondition, postcondition, objectives, actions, graphBuilder.build());
        }
    }
}
