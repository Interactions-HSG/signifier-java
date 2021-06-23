import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.time.Instant;
import java.util.Date;

public class SignifierModelBuilder extends ModelBuilder {

    ValueFactory rdf = RDFS.rdf;

    public void addModel(Model model){
        for (Statement s : model){
            add(s.getSubject(),s.getPredicate(),s.getObject());
        }
    }

    public void addReifiedStatement(ReifiedStatement statement){
        addModel(statement.getModel());

    }

    public void addReifiedStatement(Resource stateId, ReifiedStatement statement){
        this.add(stateId, rdf.createIRI(SignifierOntology.hasStatement),statement.getStatementId());
        addReifiedStatement(statement);
    }

    public void addState(State state){
        addModel(state.getModel());
    }

    public void addPrecondition(Resource affordanceId,State state){
        add(affordanceId,rdf.createIRI(SignifierOntology.hasPrecondition),state.getStateId());
        addState(state);
    }

    public void addObjective(Resource affordanceId,State state){
        add(affordanceId,rdf.createIRI(SignifierOntology.hasObjective),state.getStateId());
        addState(state);
    }

    public void addOption(Resource affordanceId, Affordance affordance){
        add(affordanceId,rdf.createIRI(SignifierOntology.hasOption),affordance.getAffordanceId());
        addAffordance(affordance);
    }

    public void addAffordance(Affordance affordance){
        addModel(affordance.getModel());
    }

    public void addAffordance(Resource signifierId, Affordance affordance){
        add(signifierId, rdf.createIRI(SignifierOntology.hasAffordance),affordance.getAffordanceId());
        addAffordance(affordance);
    }

    public void addType(Resource id, Value type){
        add(id, RDF.TYPE,type);
    }

    public void addExpirationDate(Resource signifierId, Date date){
        Value v= rdf.createLiteral(date);
        add(signifierId,RDFS.rdf.createIRI(SignifierOntology.hasExpirationDate),v);

    }

    public void addExpirationDate(Resource signifierId, Instant instant){
        Date date = Date.from(instant);
        Value v= rdf.createLiteral(date);
        add(signifierId,RDFS.rdf.createIRI(SignifierOntology.hasExpirationDate),v);

    }

    public void addSalience(Resource signifierId, int salience){
        Value v = rdf.createLiteral(salience);
        add(signifierId,RDFS.rdf.createIRI(SignifierOntology.hasSalience),v);
    }
}
