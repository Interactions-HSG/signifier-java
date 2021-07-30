package util;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.Creator;
import org.hyperagents.util.RDFS;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
public class CreatorTest {

    @Test
    public void getCreatorLiteral(){
        ModelBuilder builder = new ModelBuilder();
        Resource affordanceId = RDFS.rdf.createBNode("affordance");
        Value value = RDFS.rdf.createLiteral("Jérémy Lemée");
        builder.add(affordanceId, RDFS.rdf.createIRI(SignifierOntology.hasCreator), value);
        Model model = builder.build();
        Creator creator = Creator.getCreator(affordanceId, model);
        assertEquals("Jérémy Lemée", creator.getValue().stringValue());
        Literal literal = (Literal) value;
        assertEquals("Jérémy Lemée", literal.getLabel());
        assertEquals(Optional.empty(), creator.getModel());

    }

    public void getCreatorDescription(){
        ModelBuilder builder = new ModelBuilder();
        Resource affordanceId = RDFS.rdf.createBNode("affordance");
        Value value = RDFS.rdf.createBNode("creator");
        builder.add(affordanceId, RDFS.rdf.createIRI(SignifierOntology.hasCreator), value);
        BNode node = (BNode) value;
        Value name = RDFS.rdf.createLiteral("Jérémy Lemée");
        IRI namePredicate = RDFS.rdf.createIRI("http://www.example.com/name");
        builder.add(node, namePredicate, name );
        Model model = builder.build();
        Creator creator = Creator.getCreator(affordanceId, model);
        assertEquals(value, creator.getValue());
        Optional<Literal> optionalLiteral = Models.objectLiteral(creator.getModel().get().filter(node, namePredicate, null));
        assertEquals("Jérémy Lemée", optionalLiteral.get().getLabel());

    }
}
