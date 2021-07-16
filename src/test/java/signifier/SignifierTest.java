package signifier;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.hyperagents.util.RDFS;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.ontologies.RDFSOntology;
import org.hyperagents.signifier.Signifier;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class SignifierTest {
    ValueFactory rdf;
    Resource signifierId;
    Model model;
    Signifier signifier;

    @Before
    public void init(){
        rdf = RDFS.rdf;
        signifierId = rdf.createBNode("signifier");
        ModelBuilder builder = new ModelBuilder();
        builder.add(signifierId,rdf.createIRI(RDFSOntology.TYPE),rdf.createIRI(SignifierOntology.Signifier));
        model = builder.build();
        signifier = new Signifier.Builder(signifierId).add(model).build();
    }

    @Test
    public void checkId(){
        assertEquals(signifierId,signifier.getSignifierId());
    }

    @Test
    public void checkModel(){
        assertEquals(model,signifier.getModel());
    }
}
