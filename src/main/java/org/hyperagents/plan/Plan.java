package org.hyperagents.plan;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.hyperagents.io.SignifierReader;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.signifier.SignifierModelBuilder;
import org.hyperagents.util.RDFComponent;
import org.hyperagents.util.RDFS;

import java.util.Set;

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

    public boolean isDirectPlan(){
        boolean b = true;
        Set<Resource> types = Models.objectResources(model.filter(planId, RDF.TYPE, null));
        for (Resource type : types){
            if (type.equals(RDFS.rdf.createIRI(SignifierOntology.AffordancePlan))){
                b = false;
            }
        }
        return b;
    }

    public DirectPlan toDirectPlan(){
        return DirectPlanImpl.getDirectPlan(this);
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
