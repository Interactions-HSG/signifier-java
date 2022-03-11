package plan;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.plan.AffordancePlan;
import org.hyperagents.plan.Plan;
import org.hyperagents.util.RDFS;
import org.hyperagents.util.ReifiedStatement;
import org.hyperagents.util.State;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AffordancePlanTest {

    ValueFactory rdf;

    State objective;

    Affordance affordance;

    AffordancePlan affordancePlan;

    @Before
    public void init(){
        rdf = SimpleValueFactory.getInstance();
        Resource objectiveId = rdf.createBNode("objective");
        ReifiedStatement statement = new ReifiedStatement(rdf.createBNode(), rdf.createBNode(), rdf.createIRI("http://example.org/property"), rdf.createBNode());
        objective = new State.Builder(objectiveId)
                .addStatement(statement)
                .build();
        Resource affordanceId = rdf.createBNode("affordance");
        affordance = new Affordance.Builder(affordanceId)
                .addObjective(objective)
                .build();
        affordancePlan = new AffordancePlan(rdf.createBNode(), objective);
    }

    @Test
    public void checkGetObjective(){
        State retrievedObjective = affordancePlan.getObjective();
        assertEquals(objective, retrievedObjective);
    }

    @Test
    public void checkSatisfyPlan(){
        boolean b = affordancePlan.satisfyPlan(affordance);
        assertTrue(b);
    }

}
