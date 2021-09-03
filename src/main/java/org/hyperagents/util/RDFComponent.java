package org.hyperagents.util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.ByteArrayOutputStream;

public abstract class RDFComponent {

    public abstract Resource getId();

    public abstract Model getModel();

    public String getTextTriples(RDFFormat format){
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        Rio.write(getModel(),output,format);
        return output.toString();
    }

    @Override
    public String toString(){
        return getTextTriples(RDFFormat.TURTLE);
    }
}
