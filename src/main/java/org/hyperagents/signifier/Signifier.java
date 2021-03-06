package org.hyperagents.signifier;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.io.SignifierReader;
import org.hyperagents.io.SignifierWriter;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.Creator;
import org.hyperagents.util.Location;
import org.hyperagents.util.RDFComponent;
import org.hyperagents.util.RDFS;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

public class Signifier extends RDFComponent {
    private Optional<Instant> expirationDate;
    private Optional<Integer> salience;
    private Optional<Creator> creator;
    private Optional<Location> location;
    private Set<Affordance> affordances;
    private Model model;

    protected Signifier(Resource signifierId, Optional<Instant> expirationDate, Optional<Integer> salience, Optional<Creator> creator, Optional<Location> location, Set<Affordance>  affordances, Model model){
        super(signifierId);
        this.expirationDate = expirationDate;
        this.salience = salience;
        this.creator = creator;
        this.location = location;
        this.affordances = affordances;
        this.model = model;
    }


    public Optional<Instant> getExpirationDate() { return expirationDate; }

    public Optional<Integer> getSalience() { return salience; }

    public Optional<Creator> getCreator() { return creator; }

    public Optional<Location> getLocation() { return location; }

    public Set<Affordance> getAffordances() { return affordances; }

    public List<Affordance> getAffordanceList() { return new ArrayList<>(affordances); }

    public Model getModel(){
        return model;
    }



    public static Signifier readSignifier(Resource signifierId,Model m){
        Model model = SignifierReader.retrieveBlock(signifierId, m);
        Signifier.Builder builder = new Signifier.Builder(signifierId);
        Optional<Literal> optionalExpirationDate = Models.objectLiteral(model.filter(signifierId, RDFS.rdf.createIRI(SignifierOntology.hasExpirationDate), null));
        if (optionalExpirationDate.isPresent()) {
            Literal expirationDate = optionalExpirationDate.get();
            TemporalAccessor ta = DateTimeFormatter.ISO_DATE_TIME.parse(expirationDate.stringValue());
            Instant instant = Instant.from(ta);
            builder.setExpirationDate(instant);
        }
        Optional<Literal> optionalSalience = Models.objectLiteral(model.filter(signifierId, RDFS.rdf.createIRI(SignifierOntology.hasSalience), null));
        if (optionalSalience.isPresent()) {
            Literal salience = optionalSalience.get();
            builder.setSalience(salience.intValue());
        }
        Creator creator = Creator.getCreator(signifierId, model);
        if (creator != null){
            builder.setCreator(creator);
        }
        Set<Resource> affordanceIds = Models.objectResources(model.filter(signifierId,RDFS.rdf.createIRI(SignifierOntology.hasAffordance),null));
        for (Resource aId : affordanceIds){
            Affordance a = Affordance.retrieveAffordance(aId,model);
            builder.addAffordance(a);

        }
        builder.add(model);
        return builder.build();


    }

    public static class Builder{
        private Resource signifierId;
        private Optional<Instant> expirationDate;
        private Optional<Integer> salience;
        private Optional<Creator> creator;
        private Optional<Location> location;
        private Set<Affordance> affordances;
        private SignifierModelBuilder graphBuilder;

        public Builder(Resource signifierId){
            this.signifierId=signifierId;
            this.expirationDate = Optional.empty();
            this.salience = Optional.empty();
            this.creator = Optional.empty();
            this.location = Optional.empty();
            this.affordances =  new HashSet<>();
            this.graphBuilder= new SignifierModelBuilder();
            this.graphBuilder.addType(signifierId, RDFS.rdf.createIRI(SignifierOntology.Signifier));
        }

        public Builder add(Resource subject, IRI predicate, Value object){
            this.graphBuilder.add(subject, predicate, object);
            return this;
        }

        public Builder add(Statement s){
            this.graphBuilder.add(s.getSubject(), s.getPredicate(), s.getObject());
            return this;
        }



        public Builder add(Model m){
            this.graphBuilder.addModel(m);
            return this;
        }

        public Builder add(IRI predicate, Value object){
            this.graphBuilder.add(signifierId,predicate,object);
            return this;
        }

        public Builder addAffordance(Affordance affordance){
            this.affordances.add(affordance);
            this.graphBuilder.addAffordance(signifierId,affordance);
            return this;
        }

        public Builder addAffordances(Set<Affordance> affordanceSet){
            for (Affordance a : affordanceSet){
                addAffordance(a);
            }
            return this;
        }

        public Builder setExpirationDate(Date date) {
            this.expirationDate = Optional.of(date.toInstant());
            this.graphBuilder.addExpirationDate(signifierId,date);
            return this;
        }

        public Builder setExpirationDate(Instant instant){
            this.expirationDate = Optional.of(instant);
            this.graphBuilder.addExpirationDate(signifierId, instant);
            return this;
        }

        public Builder setSalience(int salience){
            this.salience = Optional.of(salience);
            this.graphBuilder.addSalience(signifierId, salience);
            return this;
        }

        public Builder setCreator(Creator c){
            this.creator = Optional.of(c);
            this.graphBuilder.addCreator(signifierId, c);
            return this;
        }

        public Builder setLocation(Location l){
            this.location = Optional.of(l);
            this.graphBuilder.addLocation(signifierId, l);
            return this;
        }

        public Signifier build(){
            return new Signifier(signifierId, expirationDate, salience, creator, location,  affordances, graphBuilder.build());
        }
    }
}
