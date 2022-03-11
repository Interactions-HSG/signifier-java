package org.hyperagents.hypermedia;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.RDFS;

import java.util.Map;
import java.util.Set;

public class HypermediaModelBuilder extends ModelBuilder {

    private ValueFactory rdf = RDFS.rdf;

    public void addModel(Model m){
        for (Statement s: m){
            add(s.getSubject(), s.getPredicate(), s.getObject());
        }
    }

    public void addHypermediaPlan(Resource planId){
        add(planId, RDF.TYPE, rdf.createIRI(HypermediaOntology.HypermediaPlan));
        add(planId, RDF.TYPE, rdf.createIRI(SignifierOntology.Plan));
    }

    public void addUrl(Resource planId, String url){
        add(planId, rdf.createIRI(HypermediaOntology.hasUrl), url);
    }

    public void addOperationType(Resource planId, String operationType){
        add(planId, rdf.createIRI(HypermediaOntology.hasOperationType), operationType);
    }

    public void addOperationTypes(Resource id, Set<String> operationTypes){
        for (String operationType: operationTypes){
            addOperationType(id, operationType);
        }
    }

    public void setMethod(Resource planId, String method){
        add(planId, rdf.createIRI(HypermediaOntology.hasMethod), method);
    }

    public void addHeader(Resource planId, Header header){
        add(planId, rdf.createIRI(HypermediaOntology.hasHeader), header.getHeaderId() );
        addModel(header.getModel());
    }

    public void addHeaders(Resource id, Map<String, String> headers){
        for (String key: headers.keySet()){
            String value = headers.get(key);
            Header h = new Header(key, value);
            addHeader(id, h);
        }
    }

    public void setPayload(Resource planId, String payload){
        add(planId, rdf.createIRI(HypermediaOntology.hasPayload), payload);
    }


}
