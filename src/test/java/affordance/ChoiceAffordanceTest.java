package affordance;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.hyperagents.util.ReifiedStatement;
import org.hyperagents.util.State;
import org.hyperagents.util.RDFS;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.affordance.ChoiceAffordance;
import org.hyperagents.ontologies.RDFSOntology;
import org.junit.Before;
import org.junit.Test;
import static  org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.Set;

public class ChoiceAffordanceTest {

    ValueFactory rdf;
    State precondition;
    State objective;
    Affordance affordance1;
    Affordance affordance2;
    ChoiceAffordance affordance;

    @Before
    public void init() {
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
        Resource affordance1Id = rdf.createBNode("affordance1");

        affordance1 = new Affordance.Builder(affordance1Id)
                .setPrecondition(precondition)
                .setObjective(objective)
                .add(rdf.createIRI("https://example.com/name"), rdf.createLiteral("affordance1"))
                .build();
        Resource affordance2Id = rdf.createBNode("affordance2");
        affordance2 = new Affordance.Builder(affordance2Id)
                .setPrecondition(precondition)
                .setObjective(objective)
                .add(rdf.createIRI("https://example.com/name"), rdf.createLiteral("affordance2"))
                .build();
        Resource affordanceId = rdf.createBNode("affordance");
        ChoiceAffordance.Builder builder = (ChoiceAffordance.Builder) new ChoiceAffordance.Builder(affordanceId)
                .addAffordance(affordance1)
                .addAffordance(affordance2)
                .setPrecondition(precondition)
                .setObjective(objective)
                .add(rdf.createIRI("https://example.com/name"), rdf.createLiteral("affordance"));
        affordance = builder.build();
    }

    @Test
    public void checkSubAffordances(){
        Set<Affordance> affordances = affordance.getSubAffordances();
        Iterator<Affordance> iterator = affordances.iterator();
        while (iterator.hasNext()){
            Affordance a = iterator.next();
            assertEquals(affordance.getPrecondition(),a.getPrecondition());
            assertEquals(affordance.getObjective(),a.getObjective());
        }
    }

}

