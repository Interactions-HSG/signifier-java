package plan;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.plan.AffordancePlan;
import org.hyperagents.plan.ParallelPlan;
import org.hyperagents.util.ReifiedStatement;
import org.hyperagents.util.State;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParallelPlanTest {
    ValueFactory rdf;
    Resource cpId;
    ParallelPlan parallelPlan;
    Model model;

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
        AffordancePlan plan1 = new AffordancePlan(rdf.createBNode(), objective1);
        AffordancePlan plan2 = new AffordancePlan(rdf.createBNode(), objective2);
        cpId = rdf.createBNode("choicePlan");
        parallelPlan = new ParallelPlan.Builder(cpId)
                .addParallelPlan(plan1)
                .addParallelPlan(plan2)
                .build();
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.add(cpId, rdf.createIRI(SignifierOntology.hasParallelPlan), plan1.getId());
        for (Statement s : plan1.getModel()){
            modelBuilder.add(s.getSubject(), s.getPredicate(), s.getObject());
        }
        modelBuilder.add(cpId, rdf.createIRI(SignifierOntology.hasParallelPlan), plan2.getId());
        for (Statement s : plan2.getModel()){
            modelBuilder.add(s.getSubject(), s.getPredicate(), s.getObject());
        }
        model = modelBuilder.build();

    }

    @Test
    public void checkModel(){
        assertEquals(model, parallelPlan.getModel());
    }
}
