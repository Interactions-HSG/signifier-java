package org.hyperagents.affordance;
import org.hyperagents.io.SignifierReader;
import org.hyperagents.io.SignifierWriter;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.signifier.SignifierModelBuilder;
import org.hyperagents.util.Creator;
import org.hyperagents.util.Plan;
import org.hyperagents.util.State;
import org.hyperagents.util.RDFS;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Models;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.Vector;

public class Affordance {

    protected Resource affordanceId;

    private Optional<State> precondition;
    private Optional<State> objective;
    private Optional<Creator> creator;
    private Set<Plan> plans;

    private Model model;

    protected Affordance(Resource affordanceId, Optional<State> precondition, Optional<State> objective, Optional<Creator> creator, Set<Plan> plans, Model model){
        this.affordanceId = affordanceId;
        this.precondition = precondition;
        this.objective = objective;
        this.creator = creator;
        this.plans = plans;
        this.model = model;
    }

    public Resource getAffordanceId(){
        return affordanceId;
    }

    public Optional<State> getPrecondition() { return precondition; }

    public Optional<State> getObjective(){ return objective; }

    public Optional<Creator> getCreator(){ return creator; };

    public Set<Plan> getPlans(){ return plans; }

    public Plan getFirstPlan(){ return new Vector<Plan>(plans).firstElement(); }

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

    @Override
    public String toString(){
        return SignifierWriter.writeModel(model);

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
        Optional<Resource> objectiveId = Models.objectResource(m.filter(newAffordanceId,
                RDFS.rdf.createIRI(SignifierOntology.hasObjective),null));
        if (objectiveId.isPresent()){
            State objective = State.retrieveState(objectiveId.get(),m);
            builder.setObjective(objective);
        }
        Creator creator = Creator.getCreator(newAffordanceId, model);
        if (creator != null){
            builder.setCreator(creator);
        }
        Set<Resource> planIds = Models.objectResources(model.filter(newAffordanceId,
                RDFS.rdf.createIRI(SignifierOntology.hasPlan), null));
        for (Resource planId : planIds){
            Plan.Builder planBuilder = new Plan.Builder(planId);
            planBuilder.addModel(RDFS.retrieveBlock(planId, model));
        }
        return builder.build();
    }



    public static class Builder{
        protected Resource affordanceId;
        protected Optional<State> precondition;
        protected Optional<State> objective;
        protected Optional<Creator> creator;
        protected Set<Plan> plans;
        protected SignifierModelBuilder graphBuilder;
        protected ValueFactory rdf;

        public Builder(Resource affordanceId){
            this.affordanceId = affordanceId;
            this.precondition = Optional.empty();
            this.objective = Optional.empty();
            this.creator = Optional.empty();
            this.plans = new HashSet<>();
            this.graphBuilder = new SignifierModelBuilder();
            this.rdf=RDFS.rdf;
        }

        public Builder setPrecondition(State precondition){
            this.precondition = Optional.of(precondition);
            this.graphBuilder.addPrecondition(affordanceId,precondition);
            return this;
        }

        public Builder setObjective(State objective){
            this.objective = Optional.of(objective);
            this.graphBuilder.addObjective(affordanceId,objective);
            return this;
        }

        public Builder setCreator(Creator creator){
            this.creator = Optional.of(creator);
            this.graphBuilder.addCreator(affordanceId, creator);
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

        public Affordance build(){
            graphBuilder.addType(affordanceId, rdf.createIRI(SignifierOntology.Affordance));
            return new Affordance(affordanceId, precondition, objective, creator, plans, graphBuilder.build());
        }
    }
}
