import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.ByteArrayOutputStream;
import java.time.Instant;
import java.util.Date;

public class Signifier {
    private Resource signifierId;
    private Model model;

    protected Signifier(Resource signifierId,Model model){
        this.signifierId=signifierId;
        this.model=model;
    }

    public Resource getSignifierId(){
        return signifierId;
    }

    public Model getModel(){
        return model;
    }

    public String getTextTriples(RDFFormat format){
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        Rio.write(model,output,format);
        return output.toString();
    }

    public String toString(){
        return getTextTriples(RDFFormat.TURTLE);
    }

    public static class Builder{
        private Resource signifierId;
        private SignifierModelBuilder graphBuilder;

        public Builder(Resource signifierId){
            this.signifierId=signifierId;
            this.graphBuilder= new SignifierModelBuilder();
            this.graphBuilder.addType(signifierId, RDFS.rdf.createIRI(SignifierOntology.Signifier));
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
            this.graphBuilder.addAffordance(signifierId,affordance);
            return this;
        }

        public Builder addExpirationDate(Date date) {
            this.graphBuilder.addExpirationDate(signifierId,date);
            return this;
        }

        public Builder addExpirationDate(Instant instant){
            this.graphBuilder.addExpirationDate(signifierId, instant);
            return this;
        }

        public Builder addSalience(int salience){
            this.graphBuilder.addSalience(signifierId, salience);
            return this;
        }

        public Signifier build(){
            return new Signifier(signifierId,graphBuilder.build());
        }
    }
}
