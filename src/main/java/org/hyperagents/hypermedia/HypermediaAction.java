package org.hyperagents.hypermedia;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.hyperagents.action.Action;
import org.hyperagents.util.RDFComponent;

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

        public Builder setPayload(String payload){
            this.payload = Optional.of(payload);
            return this;
        }

        public HypermediaAction build(){
            return new HypermediaAction(id, url, method, operationTypes, headers, payload);
        }


    }
}
