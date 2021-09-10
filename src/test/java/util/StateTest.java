package util;


import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.hyperagents.util.ReifiedStatement;
import org.hyperagents.util.State;
import org.hyperagents.util.RDFS;
import org.hyperagents.ontologies.RDFSOntology;
import org.hyperagents.ontologies.SignifierOntology;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StateTest {
    private ValueFactory rdf;
    Resource statementId;
    Resource subject;
    IRI predicate;
    Value object;
    private ReifiedStatement statement;
    private State state;

    @Before
    public void init(){
        rdf = RDFS.rdf;
        statementId = rdf.createBNode("statement");
        subject = rdf.createBNode("subject");
        predicate = rdf.createIRI(RDFSOntology.TYPE);
        object = rdf.createLiteral(30);
        statement = new ReifiedStatement(statementId, subject, predicate, object);
        Resource stateId = rdf.createBNode("state");
        state = new State.Builder(stateId).addStatement(statement).build();
    }

    @Test
    public void checkModel(){
        ModelBuilder graphBuilder = new ModelBuilder();
        graphBuilder.add(state.getId(), RDF.TYPE, rdf.createIRI(SignifierOntology.State));
        graphBuilder.add(state.getId(),rdf.createIRI(SignifierOntology.hasStatement),statementId);
        graphBuilder.add(statementId,rdf.createIRI(RDFSOntology.subject),subject);
        graphBuilder.add(statementId,rdf.createIRI(RDFSOntology.predicate),predicate);
        graphBuilder.add(statementId,rdf.createIRI(RDFSOntology.object),object);
        Model model = graphBuilder.build();
        assertEquals(model,state.getModel());
    }

    @Test
    public void checkEquals(){
        Resource statement1Id = rdf.createBNode("statement1");
        ReifiedStatement statement1 = new ReifiedStatement(statement1Id,subject, predicate, object);
        State state1 = new State.Builder(rdf.createBNode("state1")).addStatement(statement1).build();
        Resource statement2Id = rdf.createBNode("statement2");
        ReifiedStatement statement2 = new ReifiedStatement(statement2Id,subject, predicate, object);
        State state2 = new State.Builder(rdf.createBNode("state2")).addStatement(statement2).build();
        boolean b = state1.equals(state2);
        assertTrue(b);
    }
}
