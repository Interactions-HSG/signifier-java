package org.hyperagents.hypermedia;

import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.clients.TDHttpRequest;
import ch.unisg.ics.interactions.wot.td.schemas.ArraySchema;
import ch.unisg.ics.interactions.wot.td.schemas.DataSchema;
import ch.unisg.ics.interactions.wot.td.schemas.ObjectSchema;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.hyperagents.util.Plan;

import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.util.*;

public class HypermediaPlan extends Plan {
    String operationType;
    Form form;
    Map<String, String> headers;
    Optional<String> payload;
    Optional<DataSchema> payloadSchema;




    protected HypermediaPlan(Resource planId, String operationType, Form form, Map<String, String> headers, Optional<String> payload, Optional<DataSchema> payloadSchema, Model model) {
        super(planId, model);
        this.operationType = operationType;
        this.form = form;
        this.headers = headers;
        this.payload = payload;
        this.payloadSchema = payloadSchema;
    }


    public Map<String, String> getHeaders(){
        return headers;
    }

    public Optional<String> getPayload(){
        return payload;
    }

    public Optional<DataSchema> getPayloadSchema(){
        return payloadSchema;
    }

    public TDHttpRequest createRequest(){
        TDHttpRequest request = new TDHttpRequest(form, operationType);
        for (String key : headers.keySet()){
            request.addHeader(key, headers.get(key));
        }
        setPayload(request);
        return request;

    }

    private void setPayload(TDHttpRequest request){
        if (payload.isPresent() && payloadSchema.isPresent()){
            String payloadString = payload.get();
            DataSchema schema = payloadSchema.get();
            if (schema.getDatatype().equals(DataSchema.BOOLEAN)){
                boolean b = getBooleanPayload(payloadString);
                request.setPrimitivePayload(schema, b);

            }
            else if (schema.getDatatype().equals(DataSchema.INTEGER)){
                int n = getIntegerPayload(payloadString);
                request.setPrimitivePayload(schema,n);

            }
            else if (schema.getDatatype().equals(DataSchema.STRING)){
                request.setPrimitivePayload(schema, payloadString);

            }
            else if (schema.getDatatype().equals(DataSchema.NUMBER)){
                double n = getNumberPayload(payloadString);
                request.setPrimitivePayload(schema, n);


            }
            else if (schema.getDatatype().equals(DataSchema.ARRAY)){
                List<Object> objects = getArrayPayload(payloadString);
                request.setArrayPayload((ArraySchema)schema, objects);

            }
            else if (schema.getDatatype().equals(DataSchema.OBJECT)){
                Map<String, Object> map = getObjectPayload(payloadString);
                request.setObjectPayload((ObjectSchema) schema, map);

            }
            else if (schema.getDatatype().equals(DataSchema.NULL)){

            }
        }

    }

    public boolean getBooleanPayload(String payload){
        return Boolean.getBoolean(payload);
    }

    public int getIntegerPayload(String payload){
        return Integer.getInteger(payload);
    }

    public double getNumberPayload(String payload){
        return Double.parseDouble(payload);
    }

    public List<Object> getArrayPayload(String payload){
        List<Object> objects = new ArrayList<>();
        return objects;
    }

    public Map<String, Object> getObjectPayload(String payload){
        Map<String, Object> map = new HashMap<>();
        return map;
    }

    public static class Builder extends Plan.Builder {
        String operationType;
        Form form;
        Map<String, String> headers;
        Optional<String> payload;
        Optional<DataSchema> payloadSchema;

        public Builder(Resource planId, String operationType, Form form) {
            super(planId);
            this.operationType = operationType;
            this.form = form;
            headers = new HashMap<>();
            payload = Optional.empty();
            payloadSchema = Optional.empty();
        }

        public Builder addHeader(String key, String value){
            this.headers.put(key, value);
            return this;
        }

        public Builder addPayload(String payload){
            this.payload = Optional.of(payload);
            return this;
        }

        public Builder addPayloadSchema(DataSchema payloadSchema){
            this.payloadSchema = Optional.of(payloadSchema);
            return this;
        }

        public HypermediaPlan build(){
            return new HypermediaPlan(planId, operationType, form, headers, payload, payloadSchema, modelBuilder.build());
        }
    }
}
