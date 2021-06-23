import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;

import java.util.*;

public class SequenceAffordance extends Affordance {

    private List<Affordance> affordances;

    protected SequenceAffordance(Resource affordanceId, Optional<State> precondition, Optional<State> objective, Model model, List<Affordance> affordances){
        super(affordanceId, precondition, objective, model);
        this.affordances=affordances;
    }

    public List<Affordance> getSequence(){
        return affordances;
    }

    public static SequenceAffordance retrieveSequenceAffordance(Resource affordanceId, Model m){
        Model model = SignifierReader.getBlock(affordanceId,m);
        SequenceAffordance.Builder builder = new SequenceAffordance.Builder(affordanceId);
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
        Optional<Resource> listId = Models.objectResource(model.filter(affordanceId,
                RDFS.rdf.createIRI(SignifierOntology.hasSequence),null));
        if (listId.isPresent()){
            List<Resource> affordances = RDFS.readResourceList(listId.get(),model);
            List<Affordance> affordanceList = new ArrayList<>();
            for (Resource newAffordanceId : affordances){
                Affordance affordance = Affordance.retrieveAffordance(newAffordanceId,model);
                affordanceList.add(affordance);
            }
            builder.addSequence(affordanceList);
        }


        return builder.build();


    }

    public static SequenceAffordance getAsSequenceAffordance(Affordance affordance){
       SequenceAffordance.Builder builder = new SequenceAffordance.Builder(affordance.getAffordanceId());
        if (affordance.getPrecondition().isPresent()){
            builder.setPrecondition(affordance.getPrecondition().get());
        }
        if (affordance.getObjective().isPresent()){
            builder.setObjective(affordance.getObjective().get());
        }
        Model model = affordance.getModel();
        Optional<Resource> listId = Models.objectResource(model.filter(affordance.getAffordanceId(),
                RDFS.rdf.createIRI(SignifierOntology.hasSequence),null));
        if (listId.isPresent()){
            List<Resource> list = RDFS.readResourceList(listId.get(),model);
            List<Affordance> affordanceList = new ArrayList<>();
            for (Resource aId : list){
                Affordance a = Affordance.retrieveAffordance(aId,model);
                affordanceList.add(a);
            }
            builder.addSequence(affordanceList);
        }
        builder.add(model);
        return builder.build();
    }


    public static class Builder extends Affordance.Builder {
        private Resource sequenceId;
        private List<Affordance> affordances;


        public Builder(Resource affordanceId) {
            super(affordanceId);
            this.sequenceId= RDFS.rdf.createBNode();
            this.graphBuilder.add(affordanceId,rdf.createIRI(SignifierOntology.hasSequence),sequenceId);
            this.affordances=new ArrayList<Affordance>();

        }

        //The affordance is added at the beginning of the list
        public Builder addSequence(List<Affordance> affordanceList) {
            this.affordances.addAll(affordanceList);
            this.createRDFList(this.sequenceId, affordanceList, this.graphBuilder);
            for (Affordance affordance : affordanceList) {
                Model m = affordance.getModel();
                add(m);
            }
            return this;
        }

        private void createRDFList(Resource sequenceId, List<Affordance> affordanceList, ModelBuilder graphBuilder){
            Resource currentSequenceId=sequenceId;
            Iterator<Affordance> iterator = affordanceList.iterator();
            while (iterator.hasNext()){
                Affordance affordance = iterator.next();
                if (iterator.hasNext()) {
                    Resource nextSequenceId = RDFS.rdf.createBNode();
                    graphBuilder.add(currentSequenceId, RDFS.rdf.createIRI(RDFSOntology.first), affordance.getAffordanceId());
                    graphBuilder.add(currentSequenceId, RDFS.rdf.createIRI(RDFSOntology.rest), nextSequenceId);
                    currentSequenceId = nextSequenceId;
                }
                else{
                    graphBuilder.add(currentSequenceId, RDFS.rdf.createIRI(RDFSOntology.first), affordance.getAffordanceId());
                    graphBuilder.add(currentSequenceId, RDFS.rdf.createIRI(RDFSOntology.rest), RDFS.rdf.createIRI(RDFSOntology.nil));

                }

            }

        }

        public Builder add(Model m){
            for (Statement s: m){
                graphBuilder.add(s.getSubject(),s.getPredicate(),s.getObject());

            }
            return this;
        }

        public SequenceAffordance build(){
            this.graphBuilder.add(affordanceId,RDFS.rdf.createIRI(SignifierOntology.hasSequence),sequenceId);
            return new SequenceAffordance(this.affordanceId,this.precondition, this.objective, this.graphBuilder.build(),this.affordances);
        }

    }

}
