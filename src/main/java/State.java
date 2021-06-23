import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Models;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class State {

    private Resource stateId;

    private Set<ReifiedStatement> statements;


    protected State(Resource stateId, Set<ReifiedStatement> statements) {
        this.stateId = stateId;
        this.statements = statements;
    }

    public Resource getStateId() {
        return stateId;
    }

    public Set<ReifiedStatement> getStatements() {
        return statements;
    }

    public List<ReifiedStatement> getStatementList(){
       return new ArrayList(statements);

    }

   public Model getModel(){
        SignifierModelBuilder builder = new SignifierModelBuilder();
        for (ReifiedStatement statement : statements) {
            builder.addReifiedStatement(stateId, statement);
        }
        return builder.build();
   }

   @Override
   public String toString(){
        return SignifierWriter.writeModel(getModel());
   }


    public static State retrieveState(Resource stateId, Model m) {
        State.Builder builder = new State.Builder(stateId);
        Model model = SignifierReader.getBlock(stateId, m);
        Set<Resource> statementIds = Models.objectResources(model.filter(stateId, RDFS.rdf.createIRI(SignifierOntology.hasStatement), null));
        for (Resource statementId : statementIds) {
            ReifiedStatement s = ReifiedStatement.readReifiedStatement(statementId,model);

                builder.addStatement(s);
            }
        return builder.build();

        }

    public static class Builder{
        private Resource stateId;
        private Set<ReifiedStatement> statements;

        public Builder(Resource stateId){
            this.stateId=stateId;
            this.statements=new HashSet<>();
        }

        public Builder addStatement(ReifiedStatement s){
            this.statements.add(s);
            return this;
        }



        public State build(){
            return new State(stateId,statements);
        }
    }
}
