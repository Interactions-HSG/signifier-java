package org.hyperagents.plan;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.signifier.SignifierModelBuilder;
import org.hyperagents.util.RDFS;
import org.hyperagents.util.State;

import java.util.Optional;
import java.util.Set;

public class AffordancePlan extends Plan{

    private State objective;

    public AffordancePlan(Resource planId, State objective){
        super(planId);
        this.objective = objective;
        SignifierModelBuilder builder = new SignifierModelBuilder();
        builder.addType(planId, RDFS.rdf.createIRI(SignifierOntology.AffordancePlan));
        builder.addObjective(planId, objective);
        this.model = builder.build();
    }

    public State getObjective(){
        return this.objective;
    }

    public boolean satisfyPlan(Affordance affordance){
        boolean b = false;
        Set<State> affordanceObjectives = affordance.getObjectives();
        int size = affordanceObjectives.size();
        for (State affordanceObjective : affordanceObjectives){
            if (affordanceObjective.equals(objective)){
                b = true;
            }
        }
        return b;

    }


    public static AffordancePlan getAsAffordancePlan(Plan p){
        Resource planId = p.getId();
        Model model = p.getModel();
        Optional<Resource> opPlanObjectiveId = Models.objectResource(model.filter(planId, RDFS.rdf.createIRI(SignifierOntology.hasObjective), null));
        if (opPlanObjectiveId.isPresent()){
            Resource planObjectiveId = opPlanObjectiveId.get();
            State planObjective = State.retrieveState(planObjectiveId, model);
            AffordancePlan affordancePlan = new AffordancePlan(planId, planObjective);
            return affordancePlan;
        }
        return null;
    }


}
