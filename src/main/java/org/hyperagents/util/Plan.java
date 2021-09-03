package org.hyperagents.util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.signifier.SignifierModelBuilder;

import java.io.ByteArrayOutputStream;

public class Plan extends RDFComponent{

    protected Resource planId;

    protected Model model;

    protected Plan(Resource planId, Model model){
        this.planId = planId;
        this.model = model;
    }

    public Resource getId(){ return planId; }

    public Model getModel(){
        return model;
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
