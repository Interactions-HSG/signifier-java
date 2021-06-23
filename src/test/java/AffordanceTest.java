import org.eclipse.rdf4j.model.*;
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
        Value object = rdf.createLiteral(30);
        ReifiedStatement preconditionStatement = new ReifiedStatement(preconditionStatementId, subject, predicate, object);
        Resource preconditionId = rdf.createBNode("precondition");
        precondition = new State.Builder(preconditionId).addStatement(preconditionStatement).build();
        Resource objectiveStatementId = rdf.createBNode("objectiveStatement");
        Value object2 = rdf.createLiteral(40);
        ReifiedStatement objectiveStatement = new ReifiedStatement(objectiveStatementId, subject, predicate, object2);
        Resource objectiveId = rdf.createBNode("objective");
        objective = new State.Builder(objectiveId).addStatement(objectiveStatement).build();
        Resource affordanceId = rdf.createBNode("affordance");
        affordance = new Affordance.Builder(affordanceId).setPrecondition(precondition).setObjective(objective).build();
    }

    @Test
    public void checkPrecondition(){
        SignifierModelBuilder graphBuilder = new SignifierModelBuilder();
        graphBuilder.addState(precondition);
        Model model = graphBuilder.build();
        assertEquals(model,affordance.getPrecondition().get().getModel());
    }

    @Test
    public void checkObjective(){
        SignifierModelBuilder graphBuilder = new SignifierModelBuilder();
        graphBuilder.addState(objective);
        Model model = graphBuilder.build();
        assertEquals(model,affordance.getObjective().get().getModel());
    }


}
