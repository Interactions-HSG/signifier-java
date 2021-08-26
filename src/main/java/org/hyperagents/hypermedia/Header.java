package org.hyperagents.hypermedia;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.util.RDFS;

import java.util.Optional;

public class Header {

    private Resource headerId;

    private String name;

    private String value;

    public Header(String name, String value){
        this.headerId = RDFS.rdf.createBNode();
        this.name = name;
        this.value = value;
    }

    public Header(Resource headerId, String name, String value){
        this.headerId = headerId;
        this.name = name;
        this.value = value;
    }

    public Resource getHeaderId(){
        return headerId;
    }

    public String getName(){
        return name;
    }

    public String getValue(){
        return value;
    }

    public Model getModel(){
        ModelBuilder builder = new ModelBuilder();
        builder.add(headerId, RDFS.rdf.createIRI(HypermediaOntology.hasName), name);
        builder.add(headerId, RDFS.rdf.createIRI(HypermediaOntology.hasValue), value);
        return builder.build();
    }

    public static Header retrieveHeader(Resource headerId, Model m){
        Header h = null;
        Optional<Literal> opName = Models.objectLiteral(m.filter(
                headerId, RDFS.rdf.createIRI(HypermediaOntology.hasName), null
        ));
        Optional<Literal> opValue = Models.objectLiteral(m.filter(
                headerId, RDFS.rdf.createIRI(HypermediaOntology.hasValue), null
        ));
        if (opName.isPresent() && opValue.isPresent()){
            String name = opName.get().stringValue();
            String value = opValue.get().stringValue();
            h = new Header(headerId, name, value);
        }
        return h;
    }
}
