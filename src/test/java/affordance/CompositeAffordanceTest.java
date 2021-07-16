package affordance;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.ValueFactory;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.affordance.CompositeAffordance;
import org.hyperagents.ontologies.RDFSOntology;
import org.hyperagents.util.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CompositeAffordanceTest {
    ValueFactory rdf;
    State precondition;
    State objective;
    Affordance affordance1;
    Affordance affordance2;
    Plan plan;
    CompositeAffordance affordance;

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
        Resource planId = rdf.createBNode("plan");
        plan = new ChoicePlan.Builder(planId).addOption(affordance1).addOption(affordance2).build();
        Resource affordanceId = rdf.createBNode("affordance");
        affordance = (CompositeAffordance) new CompositeAffordance.Builder(affordanceId)
                .addPlan(plan)
                .setPrecondition(precondition)
                .setObjective(objective)
                .add(rdf.createIRI("https://example.com/name"), rdf.createLiteral("affordance"))
                .build();
    }

    @Test
    public void checkPlan(){
        assertEquals(plan, affordance.getFirstPlan());
    }
}
