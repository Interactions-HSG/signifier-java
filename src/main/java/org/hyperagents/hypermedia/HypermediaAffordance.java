package org.hyperagents.hypermedia;

import org.hyperagents.affordance.Affordance;
import org.hyperagents.util.Plan;
import org.hyperagents.util.State;
import ch.unisg.ics.interactions.wot.td.affordances.InteractionAffordance;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;

import java.util.Optional;
import java.util.Set;

public class HypermediaAffordance extends Affordance {

    private Optional<InteractionAffordance> interactionAffordance;


    protected HypermediaAffordance(Resource affordanceId, Optional<State> precondition, Optional<State> objective, Set<Plan> plans, Model model, Optional<InteractionAffordance> interactionAffordance) {
        super(affordanceId, precondition, objective, plans, model);
        this.interactionAffordance = interactionAffordance;
    }

    public Optional<InteractionAffordance> getInteractionAffordance(){
        return interactionAffordance;
    }

    public static Model getModelFromInteractionAffordance(InteractionAffordance affordance){
        ModelBuilder builder = new ModelBuilder();
        return builder.build();

    }

    public static class Builder extends Affordance.Builder {

    protected Optional<InteractionAffordance> interactionAffordance;

        public Builder(Resource affordanceId) {
            super(affordanceId);
            interactionAffordance = Optional.empty();
        }



        public Builder addInteractionAffordance(InteractionAffordance interactionAffordance) {
            this.interactionAffordance = Optional.of(interactionAffordance);
            Model m = getModelFromInteractionAffordance(interactionAffordance);
            this.graphBuilder.addModel(m);
            return this;
        }

            public HypermediaAffordance build(){
                return new HypermediaAffordance(affordanceId, precondition, objective, plans, graphBuilder.build(), interactionAffordance);
            }
        }

}