package util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.Plan;
import org.hyperagents.util.RDFS;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class PlanTest {
    Resource planId;
    Plan plan;
    Model model;

    @Before
    public void init() {
        planId = RDFS.rdf.createBNode("plan");
        plan = new Plan.Builder(planId).build();
        ModelBuilder builder = new ModelBuilder();
        builder.add(planId, RDF.TYPE, RDFS.rdf.createIRI(SignifierOntology.Plan));
        model = builder.build();
    }

    @Test
    public void checkId(){
        assertEquals(planId, plan.getId());
        assertEquals(model, plan.getModel());
    }
}
