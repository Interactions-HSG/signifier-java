package org.hyperagents.signifier;

import org.hyperagents.action.Action;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.plan.Plan;
import org.hyperagents.util.*;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Set;

public class SignifierModelBuilder extends ModelBuilder {

    ValueFactory rdf = RDFS.rdf;

    public void addModel(Model model){
        for (Statement s : model){
            add(s.getSubject(),s.getPredicate(),s.getObject());
        }
    }

    public void addList(Resource listId, List<Affordance> list){
        Model m = RDFS.createAffordanceList(listId, list);
        addModel(m);
    }

    public void addSeq(Resource seqId, List<Plan> list){
        Model m = RDFS.createPlanSeq(seqId, list);
        addModel(m);
    }

    public void addSequence(Resource planId, List<Plan> sequence){
        Resource sequenceId =rdf.createBNode();
        add(planId, rdf.createIRI(SignifierOntology.hasSequence), sequenceId);
        addSeq(sequenceId, sequence);
    }


    public void addReifiedStatement(ReifiedStatement statement){
        addModel(statement.getModel());

    }

    public void addReifiedStatement(Resource stateId, ReifiedStatement statement){
        this.add(stateId, rdf.createIRI(SignifierOntology.hasStatement),statement.getStatementId());
        addReifiedStatement(statement);
    }

    public void addFalseReifiedStatement(Resource stateId, ReifiedStatement statement){
        this.add(stateId, rdf.createIRI(SignifierOntology.hasNotStatement),statement.getStatementId());
        addReifiedStatement(statement);

    }

    public void defineState(Resource stateId){
        add(stateId, RDF.TYPE, rdf.createIRI(SignifierOntology.State));
    }

    public void addState(State state){
        addModel(state.getModel());
    }

    public void addPrecondition(Resource affordanceId,State state){
        add(affordanceId,rdf.createIRI(SignifierOntology.hasPrecondition),state.getId());
        addState(state);
    }

    public void addPostcondition(Resource affordanceId, State state){
        add(affordanceId, rdf.createIRI(SignifierOntology.hasPostcondition), state.getId());
        addState(state);
    }

    public void addObjective(Resource id,State state){
        add(id,rdf.createIRI(SignifierOntology.hasObjective),state.getId());
        addState(state);
    }

    public void addCreator(Resource affordanceId, Creator creator){
        add(affordanceId, rdf.createIRI(SignifierOntology.hasCreator), creator.getValue());
        if (creator.getModel().isPresent()){
            addModel(creator.getModel().get());
        }
    }

    public void addOption(Resource resource, Plan plan){
        add(resource,rdf.createIRI(SignifierOntology.hasOption),plan.getId());
        addPlan(plan);
    }

    public void addOptions(Resource resource, Set<Plan> plans){
        for (Plan plan: plans){
            addOption(resource, plan);
        }
    }

    public void addParallelPlan(Resource planId, Plan plan){
        add(planId, rdf.createIRI(SignifierOntology.hasParallelPlan), plan.getId());
        addPlan(plan);
    }

    public void addParallelPlans(Resource resource, Set<Plan> plans){
        for (Plan plan : plans){
            addParallelPlan(resource, plan);
        }
    }



    public void addAction(Resource id, Action action){
        add(id, rdf.createIRI(SignifierOntology.hasAction), action.getId());
        addModel(action.getModel());

    }


    public void addPlan(Plan plan){
        addModel(plan.getModel());
    }

    public void addPlan(Resource affordanceId, Plan plan){
        add(affordanceId, rdf.createIRI(SignifierOntology.hasPlan), plan.getId());
        addPlan(plan);

    }

    public void addPlans(Set<Plan> plans){
        for (Plan plan : plans){
            addPlan(plan);
        }
    }

    public void addPlans(Resource affordanceId, Set<Plan> plans){
        for (Plan plan : plans){
            add(affordanceId, rdf.createIRI(SignifierOntology.hasPlan), plan.getId());
            addPlan(plan);
        }
    }

    public void addAffordance(Affordance affordance){
        addModel(affordance.getModel());
    }

    public void addAffordance(Resource signifierId, Affordance affordance){
        add(signifierId, rdf.createIRI(SignifierOntology.hasAffordance),affordance.getId());
        addAffordance(affordance);
    }

    public void addType(Resource id, Value type){
        add(id, RDF.TYPE, type);
    }

    public void addExpirationDate(Resource signifierId, Date date){
        Value v= rdf.createLiteral(date);
        add(signifierId,RDFS.rdf.createIRI(SignifierOntology.hasExpirationDate),v);

    }

    public void addExpirationDate(Resource signifierId, Instant instant){
        Date date = Date.from(instant);
        Value v= rdf.createLiteral(date);
        add(signifierId,RDFS.rdf.createIRI(SignifierOntology.hasExpirationDate),v);

    }

    public void addSalience(Resource signifierId, int salience){
        Value v = rdf.createLiteral(salience);
        add(signifierId,RDFS.rdf.createIRI(SignifierOntology.hasSalience),v);
    }
}
