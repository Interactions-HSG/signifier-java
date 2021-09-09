package org.hyperagents.plan;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.hyperagents.io.SignifierReader;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.signifier.SignifierModelBuilder;
import org.hyperagents.util.RDFComponent;
import org.hyperagents.util.RDFS;

public class Plan extends RDFComponent {

    protected Resource planId;

    protected Model model;

    protected Plan(Resource planId, Model model){
        this.planId = planId;
        this.model = model;
    }

    protected Plan(Resource planId){
        this.planId = planId;
        this.model = new ModelBuilder().build();
    }

    public Resource getId(){ return planId; }

    public Model getModel(){
        return model;
    }

    public static Plan retrievePlan(Resource planId, Model model){
        Model m = SignifierReader.getBlock(planId, model);
        return new Plan(planId, model);
    }

    public static class Builder {

        protected Resource planId;

        protected SignifierModelBuilder modelBuilder;

        public Builder(Resource planId){
            this.planId = planId;
            this.modelBuilder = new SignifierModelBuilder();
        }

        public Builder addModel(Model m){
            this.modelBuilder.addModel(m);
            return this;
        }


        public Plan build(){
            modelBuilder.addType(planId, RDFS.rdf.createIRI(SignifierOntology.Plan));
            return new Plan(planId, modelBuilder.build());
        }

    }
}
