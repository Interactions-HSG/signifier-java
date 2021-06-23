import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;

import java.util.Optional;

public class ReifiedStatement {

    private Resource statementId;

    private Resource subject;

    private IRI predicate;

    private Value object;

    public ReifiedStatement(Resource statementId, Statement statement){
        this.statementId = statementId;
        this.subject = statement.getSubject();
        this.predicate = statement.getPredicate();
        this.object = statement.getObject();
    }

    public ReifiedStatement(Resource statementId, Resource subject, IRI predicate, Value object){
        this.statementId = statementId;
        this.subject = subject;
        this.predicate = predicate;
        this.object = object;
    }

    public Resource getStatementId(){
        return statementId;
    }

    public Resource getSubject(){
        return subject;
    }

    public IRI getPredicate(){
        return predicate;
    }

    public Value getObject(){
        return object;
    }

    public Statement getStatement(){
        return RDFS.rdf.createStatement(subject,predicate,object);
    }

    public Model getModel(){
        ModelBuilder builder = new ModelBuilder();
        builder.add(statementId,RDFS.rdf.createIRI(RDFSOntology.subject),subject);
        builder.add(statementId,RDFS.rdf.createIRI(RDFSOntology.predicate),predicate);
        builder.add(statementId,RDFS.rdf.createIRI(RDFSOntology.object),object);
        return builder.build();
    }

    @Override
    public String toString(){
        return SignifierWriter.writeModel(getModel());
    }

    public static ReifiedStatement readReifiedStatement(Resource statementId, Model model){
        Optional<Resource> subject = Models.objectResource(model.filter(statementId,
                RDFS.rdf.createIRI(RDFSOntology.subject),null));
        Optional<IRI> predicate = Models.objectIRI(model.filter(statementId,
                RDFS.rdf.createIRI(RDFSOntology.predicate),null));
        Optional<Value> object = Models.object(model.filter(statementId,
                RDFS.rdf.createIRI(RDFSOntology.object),null));
        if (subject.isPresent() && predicate.isPresent() && object.isPresent()){
            return new ReifiedStatement(statementId, subject.get(),predicate.get(),object.get());
        }
        return null;
    }
}
