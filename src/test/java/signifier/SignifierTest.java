package signifier;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.hyperagents.util.Creator;
import org.hyperagents.util.RDFS;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.ontologies.RDFSOntology;
import org.hyperagents.signifier.Signifier;
import org.junit.Before;
import org.junit.Test;

import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SignifierTest {
    ValueFactory rdf;
    Resource signifierId;
    Model model;
    Signifier signifier;

    /*@Before
    public void init(){
        rdf = RDFS.rdf;
        signifierId = rdf.createBNode("signifier");
        ModelBuilder builder = new ModelBuilder();
        builder.add(signifierId,rdf.createIRI(RDFSOntology.TYPE),rdf.createIRI(SignifierOntology.Signifier));
        model = builder.build();
        signifier = new Signifier.Builder(signifierId).add(model).build();
    }*/

    @Before
    public void init(){
        rdf = RDFS.rdf;
        signifierId = rdf.createBNode("signifier");
        Signifier.Builder builder = new Signifier.Builder(signifierId);
        builder.setSalience(10);
        Creator c = new Creator.Builder(rdf.createLiteral("Jérémy Lemée")).build();
        builder.setCreator(c);
        signifier = builder.build();
        model = signifier.getModel();
        //System.out.println(signifier.getTextTriples(RDFFormat.TURTLE));
        System.out.println(model);
    }

    @Test
    public void checkId(){
        assertEquals(signifierId,signifier.getId());
    }

    @Test
    public void checkModel(){
        assertEquals(model,signifier.getModel());
    }

    /*@Test
    public void checkExpirationDate(){
        Optional<Literal> optionalLiteral = Models.objectLiteral(model.filter(signifierId,rdf.createIRI(SignifierOntology.hasExpirationDate),null));
        System.out.println("optionalLiteral: "+optionalLiteral);
        Instant instant = optionalLiteral.get().calendarValue().toGregorianCalendar().toInstant();
        System.out.println("instant: "+instant);
        assertEquals(instant,signifier.getExpirationDate().get());
    }*/

    @Test
    public void checkSalience(){
        Optional<Literal> optionalLiteral = Models.objectLiteral(model.filter(signifierId,
                rdf.createIRI(SignifierOntology.hasSalience),null));
        int salience = optionalLiteral.get().intValue();
        assertEquals(salience, signifier.getSalience().get().intValue());
    }

    @Test
    public void checkCreator(){
        Creator c1 = Creator.getCreator(signifier.getId(), signifier.getModel());
        Creator c2 = signifier.getCreator().get();
        assertEquals(c1.getValue(), c2.getValue());
        assertEquals(c1.getModel(), c2.getModel());
    }
}
