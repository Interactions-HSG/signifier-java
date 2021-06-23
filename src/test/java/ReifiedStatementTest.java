import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ReifiedStatementTest {


    private ValueFactory rdf;
    private Resource statementId;
    private Resource subject;
    private IRI predicate;
    private Value object;
    private ReifiedStatement statement;

    @Before
    public void init(){
        rdf = RDFS.rdf;
        statementId = rdf.createBNode("statement");
        subject = rdf.createBNode("subject");
        predicate = rdf.createIRI(RDFSOntology.TYPE);
        object = rdf.createLiteral(30);
        this.statement=new ReifiedStatement(statementId,subject,predicate,object);
    }

    @Test
    public void equalsStatement(){
        Statement s = rdf.createStatement(subject,predicate,object);
        assertEquals(s,statement.getStatement());
    }

    @Test
    public void checkModel(){
        ModelBuilder graphBuilder = new ModelBuilder();
        graphBuilder.add(statementId,rdf.createIRI(RDFSOntology.subject),subject);
        graphBuilder.add(statementId,rdf.createIRI(RDFSOntology.predicate),predicate);
        graphBuilder.add(statementId,rdf.createIRI(RDFSOntology.object),object);
        Model model = graphBuilder.build();
        assertEquals(model,statement.getModel());
    }


}
