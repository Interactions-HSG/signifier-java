package org.hyperagents.util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.ontologies.SignifierOntology;

import java.util.Optional;

public class Creator {

    private Value value;

    private Optional<Model> model;

    protected Creator(Value value, Optional<Model> model){
        this.value = value;
        this.model = model;
    }

    public Value getValue(){
        return value;
    }

    public Optional<Model> getModel(){
        return model;
    }

    public static Creator getCreator(Resource affordanceId, Model m){
        Creator c = null;
        Optional<Value> optionalValue = Models.object(m.filter(affordanceId,
                RDFS.rdf.createIRI(SignifierOntology.hasCreator),null));
        if (optionalValue.isPresent()){
            Value value = optionalValue.get();
            Creator.Builder builder = new Creator.Builder(value);
            if (value.isResource()){
                Model creatorModel = RDFS.retrieveBlock((Resource) value, m);
                builder.addModel(creatorModel);
            }
            c = builder.build();
        }
        return c;
    }

    public static class Builder {
        private Value value;

        private Optional<Model> model;

        public Builder(Value value){
            this.value = value;
            this.model = Optional.empty();
        }

        public Builder addModel(Model m){
            if (value.isResource()){
                this.model = Optional.of(m);
            }
            return this;
        }

        public Creator build(){
            return new Creator(value, model);
        }
    }
}
