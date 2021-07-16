package util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.ParallelPlan;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParallelPlanTest {
    ValueFactory rdf;
    Affordance a1;
    Affordance a2;
    Resource cpId;
    ParallelPlan parallelPlan;
    Model model;

    @Before
    public void init(){
        rdf = SimpleValueFactory.getInstance();
        Resource a1id = rdf.createBNode("a1");
        a1 = new Affordance.Builder(a1id)
                .build();
        Resource a2id = rdf.createBNode("a2");
        a2 = new Affordance.Builder(a2id)
                .build();
        cpId = rdf.createBNode("choicePlan");
        parallelPlan = new ParallelPlan.Builder(cpId)
                .addParallelAffordance(a1)
                .addParallelAffordance(a2)
                .build();
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.add(cpId, rdf.createIRI(SignifierOntology.hasParallelAffordance), a1.getAffordanceId());
        for (Statement s : a1.getModel()){
            modelBuilder.add(s.getSubject(), s.getPredicate(), s.getObject());
        }
        modelBuilder.add(cpId, rdf.createIRI(SignifierOntology.hasParallelAffordance), a2.getAffordanceId());
        for (Statement s : a2.getModel()){
            modelBuilder.add(s.getSubject(), s.getPredicate(), s.getObject());
        }
        model = modelBuilder.build();

    }

    @Test
    public void checkModel(){
        assertEquals(model, parallelPlan.getModel());
    }
}
