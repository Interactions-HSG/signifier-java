package org.hyperagents.plan;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.signifier.SignifierModelBuilder;
import org.hyperagents.util.State;

import java.util.Set;

public class AffordancePlan extends Plan{

    private State objective;

    public AffordancePlan(Resource planId, State objective){
        super(planId);
        this.objective = objective;
        SignifierModelBuilder builder = new SignifierModelBuilder();
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
        System.out.println("size");
        System.out.println(size);
        for (State affordanceObjective : affordanceObjectives){
            if (affordanceObjective.equals(objective)){
                b = true;
            }
        }
        return b;

    }


}
