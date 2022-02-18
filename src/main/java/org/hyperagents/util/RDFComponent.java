package org.hyperagents.util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.hyperagents.io.SignifierReader;
import org.hyperagents.signifier.SignifierModelBuilder;

import java.io.ByteArrayOutputStream;

public abstract class RDFComponent {

    protected Resource id;


    protected RDFComponent(Resource id){
        this.id = id;
    }

    public  Resource getId(){
        return id;
    }

    public abstract Model getModel();

    public String getTextTriples(RDFFormat format){
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        Rio.write(getModel(),output,format);
        return output.toString();
    }

    protected Model getBlock(Resource id, Model m){
        return SignifierReader.getBlock(id, m);
    }

    @Override
    public String toString(){
        return getTextTriples(RDFFormat.TURTLE);
    }

    protected static abstract class Builder {

        protected Resource id;

        protected SignifierModelBuilder modelBuilder;

        protected Builder(Resource id){
            this.id = id;
            this.modelBuilder = new SignifierModelBuilder();
        }

        protected Builder addModel(Model model){
            modelBuilder.addModel(model);
            return this;
        }

        protected abstract RDFComponent build();
    }
}
