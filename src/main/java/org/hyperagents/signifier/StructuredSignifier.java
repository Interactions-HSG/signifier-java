package org.hyperagents.signifier;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Models;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.io.SignifierReader;
import org.hyperagents.io.SignifierWriter;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.RDFS;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;

public class StructuredSignifier {

    private Resource signifierId;
    private Optional<Instant> expirationDate;
    private Optional<Integer> salience;
    private Set<Affordance> affordances;
    private Model triples;

    protected StructuredSignifier(Resource signifierId, Optional<Instant> expirationDate, Optional<Integer> salience,
                                  Set<Affordance> affordances, Model triples){
        this.signifierId=signifierId;
        this.expirationDate=expirationDate;
        this.salience=salience;
        this.affordances=affordances;
        this.triples=triples;
    }

    public Optional<Instant> getExpirationDate(){
        return expirationDate;
    }

    public Optional<Integer> getSalience(){
        return salience;
    }

    public Set<Affordance> getAffordances(){
        return affordances;
    }

    public List<Affordance> getListAffordances(){ return new ArrayList<>(affordances); }

    public Signifier toSignifier(){
        return new Signifier.Builder(signifierId).add(triples).build();
    }

    public Model getModel(){ return triples; }

    @Override
    public String toString(){
        return SignifierWriter.writeModel(triples);
    }

    public static StructuredSignifier readStructuredSignifier(Resource signifierId,Model m){
        Model model = SignifierReader.getBlock(signifierId,m);
        StructuredSignifier.Builder builder = new StructuredSignifier.Builder(signifierId);
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
        Set<Resource> affordanceIds = Models.objectResources(model.filter(signifierId,RDFS.rdf.createIRI(SignifierOntology.hasAffordance),null));
        for (Resource aId : affordanceIds){
            Affordance a = Affordance.retrieveAffordance(aId,model);
            builder.addAffordance(a);

        }
        return builder.build();


    }

    public static StructuredSignifier getAsStructuredSignifier(Signifier signifier){
        Resource signifierId = signifier.getSignifierId();
        Model model = signifier.getModel();
        return readStructuredSignifier(signifierId,model);

    }



    public static class Builder {

        private Resource signifierId;
        private Optional<Instant> expirationDate;
        private Optional<Integer> salience;
        private Set<Affordance> affordances;
        private SignifierModelBuilder graphBuilder;

        public Builder(Resource signifierId){
            this.signifierId=signifierId;
            this.expirationDate=Optional.empty();
            this.salience=Optional.empty();
            this.affordances=new HashSet<>();
            this.graphBuilder=new SignifierModelBuilder();
        }

        public Builder setExpirationDate(Date date){
            Instant instant = date.toInstant();
            this.expirationDate = Optional.of(instant);
            this.graphBuilder.addExpirationDate(signifierId, instant);
            return this;
        }

        public Builder setExpirationDate(Instant instant){
            this.expirationDate=Optional.of(instant);
            this.graphBuilder.addExpirationDate(signifierId,instant);
            return this;
        }

        public Builder setSalience(int salience){
            this.salience=Optional.of(salience);
            this.graphBuilder.addSalience(signifierId,salience);
            return this;
        }

        public Builder addAffordance(Affordance affordance){
            this.affordances.add(affordance);
            this.graphBuilder.add(signifierId,RDFS.rdf.createIRI(SignifierOntology.hasAffordance),affordance.getAffordanceId());
            for (Statement s : affordance.getModel()){
                this.graphBuilder.add(s.getSubject(),s.getPredicate(),s.getObject());
            }
            return this;
        }

        public Builder add(Model m){
            for (Statement statement: m)
            this.graphBuilder.add(statement.getSubject(),statement.getPredicate(),statement.getObject());
            return this;
        }

        public StructuredSignifier build(){
            return new StructuredSignifier(signifierId,expirationDate,salience, affordances, graphBuilder.build());
        }

    }
}
