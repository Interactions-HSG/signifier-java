package org.hyperagents.hypermedia;

import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.action.Action;
import org.hyperagents.util.RDFS;

import java.util.*;

public class HypermediaAction extends Action {
    String url;
    private String method;
    private Set<String> operationTypes;
    private Map<String, String> headers;
    private Optional<String> payload;

    protected HypermediaAction(Resource actionId, String url, String method, Set<String> operationTypes, Map<String,String> headers, Optional<String> payload) {
        super(actionId, new ModelBuilder().build());
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

    public Model getModel(){
        HypermediaModelBuilder builder = new HypermediaModelBuilder();
        builder.addUrl(id, url);
        builder.setMethod(id, method);
        builder.addOperationTypes(id, operationTypes);
        builder.addHeaders(id, headers);
        if (payload.isPresent()){
            builder.setPayload(id, payload.get());
        }
        return builder.build();
    }

    @Override
    public boolean equals(Object obj){
        boolean b = true;
        HypermediaAction a = (HypermediaAction) obj;
        if (!this.url.equals(a.url)){
            b = false;
        }
        if (!this.method.equals(a.method)){
            b = false;
        }
        for (String key: headers.keySet()){
            if (!a.headers.containsKey(key)){
                b = false;
            } else {
                String value = headers.get(key);
                if (!value.equals(a.headers.get(key))){
                    b = false;
                }
            }
        }
        Optional<String> payload2 = a.payload;
        if (payload.isPresent() && !payload2.isPresent()){
            b = false;
        }
        else if (!payload.isPresent() && payload2.isPresent()){
            b = false;
        } else if (payload.isPresent() && payload2.isPresent()){
            String p1 = payload.get();
            String p2 = payload2.get();
            if (!p1.equals(p2)){
                b = false;
            }
        }

        return b;
    }

    public static HypermediaAction retrieveHypermediaAction(Resource actionId, Model model){
        Optional<Literal> opUrlLiteral = Models.objectLiteral(model.filter(actionId,
                RDFS.rdf.createIRI(HypermediaOntology.hasUrl),null));
        Optional<Literal> opMethodLiteral = Models.objectLiteral(model.filter(actionId,
                RDFS.rdf.createIRI(HypermediaOntology.hasMethod),null));
        if (opUrlLiteral.isPresent() && opMethodLiteral.isPresent()){
            String url = opUrlLiteral.get().stringValue();
            Literal methodLiteral = opMethodLiteral.get();
            String method = methodLiteral.stringValue();
            HypermediaAction.Builder builder = new HypermediaAction.Builder(actionId, url, method);
            Set<Literal> operationTypeLiterals = Models.objectLiterals(model.filter(
                    actionId, RDFS.rdf.createIRI(HypermediaOntology.hasOperationType),null
            ));
            for (Literal l : operationTypeLiterals){
                String operationType = l.stringValue();
                builder.addOperationType(operationType);
            }
            Set<Resource> headerIds = Models.objectResources(model.filter(
                    actionId, RDFS.rdf.createIRI(HypermediaOntology.hasHeader), null
            ));
            for (Resource headerId : headerIds){
                Header h = Header.retrieveHeader(headerId, model);
                builder.addHeader(h);
            }
            Optional<Literal> opPayloadLiteral = Models.objectLiteral(model.filter(
                    actionId, RDFS.rdf.createIRI(HypermediaOntology.hasPayload),null
            ));
            if (opPayloadLiteral.isPresent()){
                String payload = opPayloadLiteral.get().stringValue();
                builder.setPayload(payload);
            }
            return builder.build();
        }
        return null;
    }

    public static HypermediaAction getAsHypermediaAction(Action action){
        return retrieveHypermediaAction(action.getId(), action.getModel());
    }

    public static class Builder extends Action.Builder {

        String url;
        private String method;
        private Set<String> operationTypes;
        private Map<String, String> headers;
        private Optional<String> payload;

        public Builder(Resource id, String url, String method) {
            super(id);
            this.url = url;
            this.method = method;
            this.operationTypes = new HashSet<>();
            this.headers = new Hashtable<>();
            this.payload = Optional.empty();
        }

        public Builder addOperationType(String operationType){
            this.operationTypes.add(operationType);
            return this;
        }

        public Builder addHeader(String key, String value){
            this.headers.put(key, value);
            return this;

        }

        public Builder addHeader(Header header){
            this.headers.put(header.getName(), header.getValue());
            return this;
        }

        public Builder setPayload(String payload){
            this.payload = Optional.of(payload);
            return this;
        }

        public HypermediaAction build(){
            return new HypermediaAction(id, url, method, operationTypes, headers, payload);
        }


    }
}
