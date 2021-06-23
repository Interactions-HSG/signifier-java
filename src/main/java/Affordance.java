import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Models;

import java.util.Optional;

public class Affordance {

    protected Resource affordanceId;

    private Optional<State> precondition;
    private Optional<State> objective;

    private Model model;

    protected Affordance(Resource affordanceId, Optional<State> precondition, Optional<State> objective, Model model){
        this.affordanceId = affordanceId;
        this.precondition = precondition;
        this.objective = objective;
        this.model = model;
    }

    public Resource getAffordanceId(){
        return affordanceId;
    }

    public Optional<State> getPrecondition() { return precondition; }

    public Optional<State> getObjective(){ return objective; }

    public Model getModel(){
        return model;
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
        return builder.build();
    }



    public static class Builder{
        protected Resource affordanceId;
        protected Optional<State> precondition;
        protected Optional<State> objective;
        protected SignifierModelBuilder graphBuilder;
        protected ValueFactory rdf;

        public Builder(Resource affordanceId){
            this.affordanceId = affordanceId;
            this.precondition = Optional.empty();
            this.objective = Optional.empty();
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

        public Builder add(Model m){
            this.graphBuilder.addModel(m);
            return this;
        }

        public Builder add(IRI predicate, Value object){
            graphBuilder.add(affordanceId,predicate,object);
            return this;
        }

        public Builder add(Resource subject, IRI predicate, Value object){
            graphBuilder.add(subject, predicate, object);
            return this;
        }

        public Affordance build(){
            return new Affordance(affordanceId, precondition, objective, graphBuilder.build());
        }
    }
}
