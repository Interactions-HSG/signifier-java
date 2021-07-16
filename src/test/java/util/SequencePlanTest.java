package util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.SequencePlan;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SequencePlanTest {

    ValueFactory rdf;
    Affordance a1;
    Affordance a2;
    Resource spId;
    SequencePlan sequencePlan;

    @Before
    public void init(){
        rdf = SimpleValueFactory.getInstance();
        Resource a1id = rdf.createBNode("a1");
        a1 = new Affordance.Builder(a1id)
                .build();
        Resource a2id = rdf.createBNode("a2");
        a2 = new Affordance.Builder(a2id)
                .build();
        ArrayList<Affordance> list = new ArrayList<>();
        list.add(a1);
        list.add(a2);
        spId = rdf.createBNode("sequencePlan");
        sequencePlan = new SequencePlan.Builder(spId)
                .addSequence(list)
                .build();

    }

    @Test
    public void checkAffordances(){
        List<Affordance> affordances = sequencePlan.getSequence();
        assertEquals(a1, affordances.get(0));
        assertEquals(a2, affordances.get(1));
    }
}
