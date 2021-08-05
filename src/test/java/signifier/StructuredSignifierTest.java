package signifier;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.Before;
import org.junit.Test;

import javax.xml.datatype.XMLGregorianCalendar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import org.hyperagents.util.RDFS;
import org.hyperagents.util.ReifiedStatement;
import org.hyperagents.util.State;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.signifier.Signifier;
import org.hyperagents.signifier.StructuredSignifier;
import org.hyperagents.signifier.SignifierModelBuilder;
import org.hyperagents.ontologies.RDFSOntology;
import org.hyperagents.ontologies.SignifierOntology;

public class StructuredSignifierTest {

    ValueFactory rdf;
    Resource signifierId;
    Model model;
    Signifier signifier;
    Affordance affordance;
    StructuredSignifier structuredSignifier;

    @Before
    public void init(){
        rdf = RDFS.rdf;
        signifierId = rdf.createBNode("signifier");
        SignifierModelBuilder builder = new SignifierModelBuilder();
        Instant expirationDate = Instant.now();
        builder.addExpirationDate(signifierId, expirationDate);
        builder.addSalience(signifierId,10);
        Resource preconditionStatementId = rdf.createBNode("preconditionStatement");
        Resource subject = rdf.createBNode("subject");
        IRI predicate = rdf.createIRI(RDFSOntology.TYPE);
        Value object = rdf.createLiteral(30);
        ReifiedStatement preconditionStatement = new ReifiedStatement(preconditionStatementId, subject, predicate, object);
        Resource preconditionId = rdf.createBNode("precondition");
        State precondition = new State.Builder(preconditionId).addStatement(preconditionStatement).build();
        Resource objectiveStatementId = rdf.createBNode("objectiveStatement");
        Value object2 = rdf.createLiteral(40);
        ReifiedStatement objectiveStatement = new ReifiedStatement(objectiveStatementId, subject, predicate, object2);
        Resource objectiveId = rdf.createBNode("objective");
        State objective = new State.Builder(objectiveId).addStatement(objectiveStatement).build();
        Resource affordanceId = rdf.createBNode("affordance");
        affordance = new Affordance.Builder(affordanceId).setPrecondition(precondition).setObjective(objective).build();
        builder.addAffordance(signifierId,affordance);
        model = builder.build();
        signifier = new Signifier.Builder(signifierId)
                .setExpirationDate(expirationDate)
                .setSalience(10)
                .addAffordance(affordance)
                .build();
        structuredSignifier = StructuredSignifier.getAsStructuredSignifier(signifier);
    }

    @Test
    public void checkSaliency(){
        Optional<Literal> optionalLiteral = Models.objectLiteral(model.filter(signifierId,
                rdf.createIRI(SignifierOntology.hasSalience),null));
        int salience = optionalLiteral.get().intValue();
        assertEquals(salience,structuredSignifier.getSalience().get().intValue());
    }

    @Test
    public void checkExpirationDate(){
        Optional<Literal> optionalLiteral = Models.objectLiteral(model.filter(signifierId,rdf.createIRI(SignifierOntology.hasExpirationDate),null));
        Instant instant = optionalLiteral.get().calendarValue().toGregorianCalendar().toInstant();
        assertEquals(instant,structuredSignifier.getExpirationDate().get());
    }

   /* @Test
    public void checkAffordance(){
        Set<Resource> affordanceIds = Models.objectResources(model.filter(signifierId,
                rdf.createIRI(SignifierOntology.hasAffordance),null));
        Resource affordanceId = affordanceIds.iterator().next();
        Affordance affordance = Affordance.retrieveAffordance(affordanceId,model);
        State precondition = affordance.getPrecondition().get();
        State objective = affordance.getObjective().get();
        State preconditionSignifier = structuredSignifier.getListAffordances().get(0).getPrecondition().get();
        State objectiveSignifier = structuredSignifier.getListAffordances().get(0).getObjective().get();
        assertEquals(precondition,preconditionSignifier);
        assertEquals(objective,objectiveSignifier);
    }*/




}
