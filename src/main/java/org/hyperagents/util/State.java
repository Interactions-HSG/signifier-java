package org.hyperagents.util;

import org.eclipse.rdf4j.model.util.ModelBuilder;
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

public class State extends RDFComponent {


    private Set<ReifiedStatement> statements;

    private Set<ReifiedStatement> falseStatements;


    protected State(Resource stateId, Set<ReifiedStatement> statements, Set<ReifiedStatement> falseStatements) {
        super(stateId);
        this.statements = statements;
        this.falseStatements = falseStatements;
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
        builder.defineState(id);
        for (ReifiedStatement statement : statements) {
            builder.addReifiedStatement(id, statement);
        }
        for (ReifiedStatement statement : falseStatements){
            builder.addFalseReifiedStatement(id, statement);
        }
        return builder.build();
   }

   public Set<Statement> getStatementSet(){
        Set<Statement> trueStatements = new HashSet<>();
        Set<ReifiedStatement> statements = getStatements();
        for (ReifiedStatement statement : statements){
            trueStatements.add(statement.getStatement());
        }
        return trueStatements;

    }

    public Set<Statement> getFalseStatementSet(){
        Set<Statement> falseStatements = new HashSet<>();
        Set<ReifiedStatement> statements = getFalseStatements();
        for (ReifiedStatement statement : statements){
            falseStatements.add(statement.getStatement());
        }
        return falseStatements;

    }

    @Override
    public boolean equals(Object obj) {
        boolean b = false;
        State state = (State) obj;
        Set<Statement> trueStatements = getStatementSet();
        Set<Statement> falseStatement = getFalseStatementSet();
        Set<Statement> trueStateStatements = state.getStatementSet();
        Set<Statement> falseStateStatements = state.getFalseStatementSet();
        if (trueStatements.equals(trueStateStatements) && falseStatements.equals(falseStateStatements)){
            b = true;
        }
        return b;
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
