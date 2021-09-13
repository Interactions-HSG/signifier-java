package org.hyperagents.util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.hyperagents.io.SignifierReader;

import java.io.ByteArrayOutputStream;

public abstract class RDFComponent {

    public abstract Resource getId();

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
}
