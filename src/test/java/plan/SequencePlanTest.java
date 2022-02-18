package plan;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.plan.AffordancePlan;
import org.hyperagents.plan.SequencePlan;
import org.hyperagents.plan.Plan;
import org.hyperagents.util.ReifiedStatement;
import org.hyperagents.util.State;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SequencePlanTest {

    ValueFactory rdf;
    AffordancePlan a1;
    AffordancePlan a2;
    Resource spId;
    SequencePlan sequencePlan;

    @Before
    public void init(){
        rdf = SimpleValueFactory.getInstance();
        Resource objective1Id = rdf.createBNode("objective1");
        ReifiedStatement statement1 = new ReifiedStatement(rdf.createBNode(), rdf.createBNode(), rdf.createIRI("http://example.org/property"), rdf.createBNode());
        State objective1 = new State.Builder(objective1Id)
                .addStatement(statement1)
                .build();
        Resource objective2Id = rdf.createBNode("objective2");
        ReifiedStatement statement2 = new ReifiedStatement(rdf.createBNode(), rdf.createBNode(), rdf.createIRI("http://example.org/property2"), rdf.createBNode());
        State objective2 = new State.Builder(objective1Id)
                .addStatement(statement2)
                .build();
        Resource a1id = rdf.createBNode("a1");
        a1 = new AffordancePlan(a1id, objective1);
        Resource a2id = rdf.createBNode("a2");
        a2 = new AffordancePlan(a2id, objective2);
        ArrayList<Plan> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);
        spId = rdf.createBNode("sequencePlan");
        sequencePlan = new SequencePlan.Builder(spId)
                .addSequence(list)
                .build();

    }

    @Test
    public void checkPlans(){
        //List<Plan> plans = sequencePlan.getSequence();
        //assertEquals(a1, plans.get(0));
        //assertEquals(a2, plans.get(1));
    }
}
