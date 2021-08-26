package org.hyperagents.util;

import org.hyperagents.io.SignifierWriter;
import org.hyperagents.io.SignifierReader;
import org.hyperagents.signifier.SignifierModelBuilder;
import org.hyperagents.ontologies.SignifierOntology;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Models;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

public class State {

    private Resource stateId;

    private Set<ReifiedStatement> statements;

    private Set<ReifiedStatement> falseStatements;


    protected State(Resource stateId, Set<ReifiedStatement> statements, Set<ReifiedStatement> falseStatements) {
        this.stateId = stateId;
        this.statements = statements;
        this.falseStatements = falseStatements;
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

    public Set<ReifiedStatement> getFalseStatements(){
        return falseStatements;
    }

    public List<ReifiedStatement> getFalseStatementList(){
        return new ArrayList<>(falseStatements);
    }

   public Model getModel(){
        SignifierModelBuilder builder = new SignifierModelBuilder();
        for (ReifiedStatement statement : statements) {
            builder.addReifiedStatement(stateId, statement);
        }
        for (ReifiedStatement statement : falseStatements){
            builder.addFalseReifiedStatement(stateId, statement);
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
        Set<Resource> notStatementIds = Models.objectResources(model.filter(stateId, RDFS.rdf.createIRI(SignifierOntology.hasNotStatement), null));
        for (Resource notStatementId : notStatementIds){
            ReifiedStatement s = ReifiedStatement.readReifiedStatement(notStatementId,model);
            builder.addFalseStatement(s);

        }
        return builder.build();

        }

    public static class Builder{
        private Resource stateId;
        private Set<ReifiedStatement> statements;
        private Set<ReifiedStatement> falseStatements;

        public Builder(Resource stateId){
            this.stateId=stateId;
            this.statements=new HashSet<>();
            this.falseStatements = new HashSet<>();
        }

        public Builder addStatement(ReifiedStatement s){
            this.statements.add(s);
            return this;
        }

        public Builder addFalseStatement(ReifiedStatement s){
            this.falseStatements.add(s);
            return this;
        }



        public State build(){
            return new State(stateId,statements, falseStatements);
        }
    }
}
