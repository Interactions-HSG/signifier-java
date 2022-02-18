package affordance;

import org.eclipse.rdf4j.model.*;
import org.hyperagents.action.Action;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.hypermedia.HypermediaAction;
import org.hyperagents.plan.AffordancePlan;
import org.hyperagents.plan.ChoicePlan;
import org.hyperagents.plan.DirectPlan;
import org.hyperagents.plan.Plan;
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
    State objective1;
    State objective2;
    Action action;
    Affordance affordance;

    @Before
    public void init(){
        rdf = RDFS.rdf;
        Resource preconditionStatementId = rdf.createBNode("preconditionStatement");
        Resource subject = rdf.createBNode("subject");
        IRI predicate = rdf.createIRI(RDFSOntology.TYPE);
        IRI predicate2 = rdf.createIRI("http://example.org/predicate");
        Value object = rdf.createLiteral("Type1");
        ReifiedStatement preconditionStatement = new ReifiedStatement(preconditionStatementId, subject, predicate, object);
        Resource preconditionId = rdf.createBNode("precondition");
        precondition = new State.Builder(preconditionId).addStatement(preconditionStatement).build();
        Resource objectiveStatementId = rdf.createBNode("objectiveStatement");
        Value object2 = rdf.createLiteral("Type2");
        ReifiedStatement objectiveStatement = new ReifiedStatement(objectiveStatementId, subject, predicate, object2);
        Resource objectiveId = rdf.createBNode("objective");
        objective = new State.Builder(objectiveId).addStatement(objectiveStatement).build();
        Resource objective1Id = rdf.createBNode("objective1");
        objective1 = new State.Builder(objective1Id).addStatement(objectiveStatement).build();
        ReifiedStatement objectiveStatement2 = new ReifiedStatement(objectiveStatementId, subject, predicate2, object2);
        Resource objective2Id = rdf.createBNode("objective2");
        objective2 = new State.Builder(objective2Id).addStatement(objectiveStatement2).build();
        Resource affordanceId = rdf.createBNode("affordance");
        IRI property = rdf.createIRI("https://example.com/number");
        Literal l = rdf.createLiteral(30);
        Resource affordance1Id = rdf.createBNode("affordance1");
        Affordance affordance1 = new Affordance.Builder(affordance1Id)
                .setPrecondition(precondition)
                .setPostcondition(objective1)
                .addObjective(objective1)
                .add(rdf.createIRI("https://example.com/name"), rdf.createLiteral("affordance1"))
                .build();
        Resource affordance2Id = rdf.createBNode("affordance2");
        Affordance affordance2 = new Affordance.Builder(affordance2Id)
                .setPrecondition(precondition)
                .setPostcondition(objective2)
                .addObjective(objective2)
                .add(rdf.createIRI("https://example.com/name"), rdf.createLiteral("affordance2"))
                .build();
        AffordancePlan plan1 = new AffordancePlan(rdf.createBNode(), objective1);
        AffordancePlan plan2 = new AffordancePlan(rdf.createBNode(), objective2);
        Resource planId = rdf.createBNode("plan");
        //plan = new ChoicePlan.Builder(planId).addOption(plan1).addOption(plan2).build();
        action = new HypermediaAction.Builder(rdf.createBNode(),"http://example.org", "GET").build();
        affordance = new Affordance.Builder(affordanceId)
                .setPrecondition(precondition)
                .setPostcondition(objective)
                .addObjective(objective)
                .addAction(action)
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
    public void checkPostconditionModel(){
        SignifierModelBuilder graphBuilder = new SignifierModelBuilder();
        graphBuilder.addState(objective);
        Model model = graphBuilder.build();
        assertEquals(model,affordance.getPostcondition().get().getModel());
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

    /*@Test
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
    }*/

    @Test
    public void checkAction(){
        assertEquals(action, affordance.getFirstAction());
    }




}
