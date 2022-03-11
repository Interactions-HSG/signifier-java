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
import org.hyperagents.plan.ChoicePlan;
import org.hyperagents.util.ReifiedStatement;
import org.hyperagents.util.State;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ChoicePlanTest {
    ValueFactory rdf;
    Resource cpId;
    ChoicePlan choicePlan;
    Model model;

    @Before
    public void init(){
        rdf = SimpleValueFactory.getInstance();
        ReifiedStatement statement1 = new ReifiedStatement(rdf.createBNode(), rdf.createBNode(), rdf.createIRI("http://example.org/property"), rdf.createBNode());
        State objective1 = new State.Builder(rdf.createBNode())
                .addStatement(statement1)
                .build();
        ReifiedStatement statement2 = new ReifiedStatement(rdf.createBNode(), rdf.createBNode(), rdf.createIRI("http://example.org/property"), rdf.createBNode());
        State objective2 = new State.Builder(rdf.createBNode())
                .addStatement(statement2)
                .build();
        Resource plan1Id = rdf.createBNode("plan1");
        AffordancePlan plan1 = new AffordancePlan(plan1Id, objective1);
        Resource plan2Id = rdf.createBNode("plan2");
        AffordancePlan plan2 = new AffordancePlan(plan2Id, objective2);
        cpId = rdf.createBNode("choicePlan");
        choicePlan = new ChoicePlan.Builder(cpId)
                .addOption(plan1)
                .addOption(plan2)
                .build();
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.add(cpId, rdf.createIRI(SignifierOntology.hasOption), plan1.getId());
        for (Statement s : plan1.getModel()){
            modelBuilder.add(s.getSubject(), s.getPredicate(), s.getObject());
        }
        modelBuilder.add(cpId, rdf.createIRI(SignifierOntology.hasOption), plan2.getId());
        for (Statement s : plan2.getModel()){
            modelBuilder.add(s.getSubject(), s.getPredicate(), s.getObject());
        }
        model = modelBuilder.build();

    }

    @Test
    public void checkModel(){
        assertEquals(model, choicePlan.getModel());
    }
}
