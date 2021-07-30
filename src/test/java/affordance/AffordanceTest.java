package affordance;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.*;
import org.hyperagents.ontologies.RDFSOntology;
import org.hyperagents.signifier.SignifierModelBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class AffordanceTest {
    ValueFactory rdf;
    State precondition;
    State objective;
    Plan plan;
    Affordance affordance;

    @Before
    public void init(){
        rdf = RDFS.rdf;
        Resource preconditionStatementId = rdf.createBNode("preconditionStatement");
        Resource subject = rdf.createBNode("subject");
        IRI predicate = rdf.createIRI(RDFSOntology.TYPE);
        Value object = rdf.createLiteral("Type1");
        ReifiedStatement preconditionStatement = new ReifiedStatement(preconditionStatementId, subject, predicate, object);
        Resource preconditionId = rdf.createBNode("precondition");
        precondition = new State.Builder(preconditionId).addStatement(preconditionStatement).build();
        Resource objectiveStatementId = rdf.createBNode("objectiveStatement");
        Value object2 = rdf.createLiteral("Type2");
        ReifiedStatement objectiveStatement = new ReifiedStatement(objectiveStatementId, subject, predicate, object2);
        Resource objectiveId = rdf.createBNode("objective");
        objective = new State.Builder(objectiveId).addStatement(objectiveStatement).build();
        Resource affordanceId = rdf.createBNode("affordance");
        IRI property = rdf.createIRI("https://example.com/number");
        Literal l = rdf.createLiteral(30);
        Resource affordance1Id = rdf.createBNode("affordance1");
        Affordance affordance1 = new Affordance.Builder(affordance1Id)
                .setPrecondition(precondition)
                .setObjective(objective)
                .add(rdf.createIRI("https://example.com/name"), rdf.createLiteral("affordance1"))
                .build();
        Resource affordance2Id = rdf.createBNode("affordance2");
        Affordance affordance2 = new Affordance.Builder(affordance2Id)
                .setPrecondition(precondition)
                .setObjective(objective)
                .add(rdf.createIRI("https://example.com/name"), rdf.createLiteral("affordance2"))
                .build();
        Resource planId = rdf.createBNode("plan");
        plan = new ChoicePlan.Builder(planId).addOption(affordance1).addOption(affordance2).build();
        affordance = new Affordance.Builder(affordanceId)
                .setPrecondition(precondition)
                .setObjective(objective)
                .addPlan(plan)
                .add(property,l)
                .build();
    }

    @Test
    public void checkPrecondition(){
        assertEquals(precondition,affordance.getPrecondition().get());
    }

    @Test
    public void checkPreconditionModel(){
        SignifierModelBuilder graphBuilder = new SignifierModelBuilder();
        graphBuilder.addState(precondition);
        Model model = graphBuilder.build();
        assertEquals(model,affordance.getPrecondition().get().getModel());
    }

    /*@Test
    public void checkObjective(){
        assertEquals(objective, affordance.getObjective());
    }*/

    @Test
    public void checkObjectiveModel(){
        SignifierModelBuilder graphBuilder = new SignifierModelBuilder();
        graphBuilder.addState(objective);
        Model model = graphBuilder.build();
        assertEquals(model,affordance.getObjective().get().getModel());
    }

    @Test
    public void checkValue(){
        Value expected = rdf.createLiteral(30);
        Value actual = affordance.getValue("https://example.com/number").get();
        assertEquals(expected,actual);

    }

    @Test
    public void checkLiteral(){
        Literal expected = rdf.createLiteral(30);
        Literal actual = affordance.getLiteral("https://example.com/number").get();
        assertEquals(expected,actual);

    }

    @Test
    public void checkCreator(){
        Value value = rdf.createLiteral("Jeremy Lemee");
        Creator creator = new Creator.Builder(value).build();
        Resource creatorAffordanceId = rdf.createBNode("creatorAffordance");
        Affordance creatorAffordance = new Affordance.Builder(creatorAffordanceId)
                .setCreator(creator)
                .build();
        ModelBuilder builder = new ModelBuilder();
        builder.add(creatorAffordanceId, RDF.TYPE, rdf.createIRI(SignifierOntology.Affordance));
        builder.add(creatorAffordanceId, rdf.createIRI(SignifierOntology.hasCreator), "Jeremy Lemee");
        Model m = builder.build();
        Affordance actualAffordance = Affordance.retrieveAffordance(creatorAffordanceId, m);
        //System.out.println(RDFS.printModel(creatorAffordance.getModel()));
        assertEquals(creatorAffordance.getCreator().get().getValue(), actualAffordance.getCreator().get().getValue());
        assertEquals(creatorAffordance.getCreator().get().getModel(), actualAffordance.getCreator().get().getModel());
        //assertEquals(creatorAffordance.getCreator(), actualAffordance.getCreator());
        assertEquals(creatorAffordance.getModel(), actualAffordance.getModel());
    }

    @Test
    public void checkComplexCreator(){
        Value c = rdf.createBNode("creator");
        Value value = rdf.createLiteral("Jeremy Lemee");
        Model creatorModel = new ModelBuilder()
                .add((Resource) c, rdf.createIRI("http://www.example.com/name"), value)
                .build();
        Creator creator = new Creator.Builder(c)
                .addModel(creatorModel)
                .build();
        Resource creatorAffordanceId = rdf.createBNode("creatorAffordance");
        Affordance creatorAffordance = new Affordance.Builder(creatorAffordanceId)
                .setCreator(creator)
                .build();
        ModelBuilder builder = new ModelBuilder();
        builder.add(creatorAffordanceId, RDF.TYPE, rdf.createIRI(SignifierOntology.Affordance));
        builder.add(creatorAffordanceId, rdf.createIRI(SignifierOntology.hasCreator), c);
        builder.add((Resource)c, rdf.createIRI("http://www.example.com/name"), value);
        Model m = builder.build();
        Affordance actualAffordance = Affordance.retrieveAffordance(creatorAffordanceId, m);
        assertEquals(creatorAffordance.getCreator().get().getValue(), actualAffordance.getCreator().get().getValue());
        assertEquals(creatorAffordance.getCreator().get().getModel(), actualAffordance.getCreator().get().getModel());
        //assertEquals(creatorAffordance.getCreator(), actualAffordance.getCreator());
        assertEquals(creatorAffordance.getModel(), actualAffordance.getModel());
    }

    @Test
    public void checkPlan(){
        assertEquals(plan, affordance.getFirstPlan());
    }




}
