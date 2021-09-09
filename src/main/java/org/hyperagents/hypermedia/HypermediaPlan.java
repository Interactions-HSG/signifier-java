package org.hyperagents.hypermedia;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.plan.BasicPlan;
import org.hyperagents.plan.Plan;
import org.hyperagents.util.RDFS;

import java.util.*;

public class HypermediaPlan extends BasicPlan {
    String url;
    private String method;
    private Set<String> operationTypes;
    private Map<String, String> headers;
    private Optional<String> payload;

    protected HypermediaPlan(Resource planId, String url, String method, Set<String> operationTypes,
                             Map<String, String> headers,Optional<String> payload){
        super(planId, createModel(planId, url, method, operationTypes, headers, payload));
        this.url = url;
        this.method = method;
        this.operationTypes = operationTypes;
        this.headers = headers;
        this.payload = payload;
    }

    public String getUrl(){
        return url;
    }

    public String getMethod(){
        return method;
    }

    public Set<String> getOperationTypes(){
        return operationTypes;
    }



    public Map<String, String> getHeaders(){
        return headers;
    }

    public Optional<String> getPayload(){
        return payload;
    }

    public static Model createModel(Resource planId, String url, String method, Set<String> operationTypes,
                             Map<String, String> headers,Optional<String> payload){
        HypermediaModelBuilder builder = new HypermediaModelBuilder();
        builder.addHypermediaPlan(planId);
        builder.addUrl(planId, url);
        for (String operationType : operationTypes) {
            builder.addOperationType(planId, operationType);
        }
        builder.setMethod(planId, method);
        Set<Header> headerSet = retrieveHeaders(headers);
        for (Header h: headerSet){
            builder.addHeader(planId, h);
        }
        if (payload.isPresent()){
            builder.setPayload(planId, payload.get());
        }
        Model model = builder.build();
        return model;

    }

    public static Set<Header> retrieveHeaders(Map<String, String> headers){
        Set<Header> headerSet =  new HashSet<>();
        Set<String> keySet = headers.keySet();
        for (String key: keySet){
            String value = headers.get(key);
            Header h = new Header(key, value);
            headerSet.add(h);
        }
        return headerSet;
    }

    public static HypermediaPlan retrieveHypermediaPlan(Resource planId, Model model){
        Optional<Literal> opUrlLiteral = Models.objectLiteral(model.filter(planId,
                RDFS.rdf.createIRI(HypermediaOntology.hasUrl),null));
        Optional<Literal> opMethodLiteral = Models.objectLiteral(model.filter(planId,
                RDFS.rdf.createIRI(HypermediaOntology.hasMethod),null));
        if (opUrlLiteral.isPresent() && opMethodLiteral.isPresent()){
            String url = opUrlLiteral.get().stringValue();
            Literal methodLiteral = opMethodLiteral.get();
            String method = methodLiteral.stringValue();
            HypermediaPlan.Builder builder = new HypermediaPlan.Builder(planId, url, method);
            Set<Literal> operationTypeLiterals = Models.objectLiterals(model.filter(
                    planId, RDFS.rdf.createIRI(HypermediaOntology.hasOperationType),null
            ));
            for (Literal l : operationTypeLiterals){
                String operationType = l.stringValue();
                builder.addOperationType(operationType);
            }
            Set<Resource> headerIds = Models.objectResources(model.filter(
                    planId, RDFS.rdf.createIRI(HypermediaOntology.hasHeader), null
            ));
            for (Resource headerId : headerIds){
                Header h = Header.retrieveHeader(headerId, model);
                builder.addHeader(h);
            }
            Optional<Literal> opPayloadLiteral = Models.objectLiteral(model.filter(
                    planId, RDFS.rdf.createIRI(HypermediaOntology.hasPayload),null
            ));
            if (opPayloadLiteral.isPresent()){
                String payload = opPayloadLiteral.get().stringValue();
                builder.setPayload(payload);
            }
            return builder.build();
        }
        return null;
    }

    public static HypermediaPlan getAsHypermediaPlan(Plan p){
        return retrieveHypermediaPlan(p.getId(), p.getModel());
    }

    public static class Builder extends Plan.Builder {
        private String url;
        private String method;
        private Set<String> operationTypes;
        private Map<String, String> headers;
        private Optional<String> payload;

        public Builder(Resource planId, String url, String method) {
            super(planId);
            this.url = url;
            this.method = method;
            this.operationTypes = new HashSet<>();
            this.headers = new HashMap<>();
            this.payload = Optional.empty();

        }

        public Builder addOperationType(String operationType){
            this.operationTypes.add(operationType);
            return this;
        }

        public Builder addHeader(Header h){
            this.headers.put(h.getName(), h.getValue());
            return this;

        }

        public Builder setPayload(String p){
            this.payload = Optional.of(p);
            return this;
        }

        public HypermediaPlan build(){
            return new HypermediaPlan(planId, url, method, operationTypes, headers, payload);
        }
    }
}
