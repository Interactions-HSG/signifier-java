package org.hyperagents.util;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.ontologies.SignifierOntology;

import java.util.Optional;

public class Location {

    private Value value;

    private Optional<Model> model;

    protected Location(Value value, Optional<Model> model){
        this.value = value;
        this.model = model;
    }

    public Value getValue(){
        return value;
    }

    public Optional<Model> getModel(){
        return model;
    }

    public static Location getLocation(Resource affordanceId, Model m){
        Location l = null;
        Optional<Value> optionalValue = Models.object(m.filter(affordanceId,
                RDFS.rdf.createIRI(SignifierOntology.hasLocation),null));
        if (optionalValue.isPresent()){
            Value value = optionalValue.get();
            Location.Builder builder = new Location.Builder(value);
            if (value.isResource()){
                Model creatorModel = RDFS.retrieveBlock((Resource) value, m);
                builder.addModel(creatorModel);
            }
            l = builder.build();
        }
        return l;
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

        public Location build(){
            return new Location(value, model);
        }
    }
}
