package util;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.Creator;
import org.hyperagents.util.Location;
import org.hyperagents.util.RDFS;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class LocationTest {

    @Test
    public void getLocationLiteral(){
        ModelBuilder builder = new ModelBuilder();
        Resource signifierId = RDFS.rdf.createBNode("signifier");
        Value value = RDFS.rdf.createLiteral("Here");
        builder.add(signifierId, RDFS.rdf.createIRI(SignifierOntology.hasLocation), value);
        Model model = builder.build();
        Location location= Location.getLocation(signifierId, model);
        assertEquals("Here", location.getValue().stringValue());
        Literal literal = (Literal) value;
        assertEquals("Here", literal.getLabel());
        assertEquals(Optional.empty(), location.getModel());

    }

    public void getLocationDescription(){
        ModelBuilder builder = new ModelBuilder();
        Resource signifierId = RDFS.rdf.createBNode("signifier");
        Value value = RDFS.rdf.createBNode("creator");
        builder.add(signifierId, RDFS.rdf.createIRI(SignifierOntology.hasLocation), value);
        BNode node = (BNode) value;
        Value name = RDFS.rdf.createLiteral("Here");
        IRI namePredicate = RDFS.rdf.createIRI("http://www.example.com/St. Gallen");
        builder.add(node, namePredicate, name );
        Model model = builder.build();
        Location location = Location.getLocation(signifierId, model);
        assertEquals(value, location.getValue());
        Optional<Literal> optionalLiteral = Models.objectLiteral(location.getModel().get().filter(node, namePredicate, null));
        assertEquals("Here", optionalLiteral.get().getLabel());

    }
}
