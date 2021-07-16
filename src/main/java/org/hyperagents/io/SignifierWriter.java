package org.hyperagents.io;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.hyperagents.signifier.Signifier;

import java.io.ByteArrayOutputStream;

public class SignifierWriter {

    ModelBuilder graphBuilder;

    public SignifierWriter(Signifier signifier){
        graphBuilder=new ModelBuilder();
        for (Statement statement: signifier.getModel()){
            graphBuilder.add(statement.getSubject(),statement.getPredicate(),statement.getObject());

        }
    }

    public SignifierWriter setNamespace(String prefix, String namespace){
        this.graphBuilder.setNamespace(prefix,namespace);
        return this;
    }

    public void setBasicNamespace(){
        setNamespace("rdf","http://www.w3.org/1999/02/22-rdf-syntax-ns#");
        setNamespace("rdfs","http://www.w3.org/2000/01/rdf-schema#");
        setNamespace("xsd","http://www.w3.org/2001/XMLSchema#");
        setNamespace("td", "https://www.w3.org/2019/wot/td#");
        setNamespace("htv", "http://www.w3.org/2011/http#");
        setNamespace("hctl", "https://www.w3.org/2019/wot/hypermedia#");
        setNamespace("wotsec", "https://www.w3.org/2019/wot/security#");
        setNamespace("dct", "http://purl.org/dc/terms/");
        setNamespace("js", "https://www.w3.org/2019/wot/json-schema#");
        setNamespace("saref", "https://saref.etsi.org/core/");
        setNamespace("signifier","http://example.com/");
    }

    public String write(){
        Model m=graphBuilder.build();
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        Rio.write(m,output, RDFFormat.TURTLE);
        return output.toString();

    }

    public String writeDefault(){
        this.setBasicNamespace();
        return this.write();
    }

    public static String writeModel(Model m){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Rio.write(m,output,RDFFormat.TURTLE);
        String description = output.toString();
        return description;
    }


}
