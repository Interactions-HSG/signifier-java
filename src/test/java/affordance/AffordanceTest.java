package affordance;

import org.eclipse.rdf4j.model.*;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.util.ReifiedStatement;
import org.hyperagents.util.State;
import org.hyperagents.util.RDFS;
import org.hyperagents.ontologies.RDFSOntology;
import org.hyperagents.signifier.SignifierModelBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AffordanceTest {
    ValueFactory rdf;
    State precondition;
    State objective;
    Affordance affordance;

    @Before
    public void init(){
        rdf = RDFS.rdf;
        Resource preconditionStatementId = rdf.createBNode("preconditionStatement");
        Resource subject = rdf.createBNode("subject");
        IRI predicate = rdf.createIRI(RDFSOntology.TYPE);
        Value object = rdf.createLiteral("Type1");
        ReifiedStatement preconditionStatement = new ReifiedStatement(preconditionStatementId, subject, predicate, object);
        Resource preconditionId = rdf.createBNode("precondition");
        precondition = new State.Builder(preconditionId).addStatement(preconditionStatement).build();
        Resource objectiveStatementId = rdf.createBNode("objectiveStatement");
        Value object2 = rdf.createLiteral("Type2");
        ReifiedStatement objectiveStatement = new ReifiedStatement(objectiveStatementId, subject, predicate, object2);
        Resource objectiveId = rdf.createBNode("objective");
        objective = new State.Builder(objectiveId).addStatement(objectiveStatement).build();
        Resource affordanceId = rdf.createBNode("affordance");
        IRI property = rdf.createIRI("https://example.com/number");
        Literal l = rdf.createLiteral(30);
        affordance = new Affordance.Builder(affordanceId)
                .setPrecondition(precondition)
                .setObjective(objective)
                .add(property,l)
                .build();
    }

    @Test
    public void checkPrecondition(){
        assertEquals(precondition,affordance.getPrecondition().get());
    }

    @Test
    public void checkPreconditionModel(){
        SignifierModelBuilder graphBuilder = new SignifierModelBuilder();
        graphBuilder.addState(precondition);
        Model model = graphBuilder.build();
        assertEquals(model,affordance.getPrecondition().get().getModel());
    }

    /*@Test
    public void checkObjective(){
        assertEquals(objective, affordance.getObjective());
    }*/

    @Test
    public void checkObjectiveModel(){
        SignifierModelBuilder graphBuilder = new SignifierModelBuilder();
        graphBuilder.addState(objective);
        Model model = graphBuilder.build();
        assertEquals(model,affordance.getObjective().get().getModel());
    }

    @Test
    public void checkValue(){
        Value expected = rdf.createLiteral(30);
        Value actual = affordance.getValue("https://example.com/number").get();
        assertEquals(expected,actual);

    }

    @Test
    public void checkLiteral(){
        Literal expected = rdf.createLiteral(30);
        Literal actual = affordance.getLiteral("https://example.com/number").get();
        assertEquals(expected,actual);

    }


}
