import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.helpers.StatementCollector;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

public class SignifierReader {

    public static ValueFactory rdf = RDFS.rdf;

    public static Signifier readSignifier(String s, RDFFormat format){
        Model model=new ModelBuilder().build();
        RDFParser parser= Rio.createParser(format);
        parser.setRDFHandler(new StatementCollector(model));
        ByteArrayInputStream stream=new ByteArrayInputStream(s.getBytes());
        try{
            parser.parse(stream);
            Optional<Resource> optionalSignifierId=Models.subject(model.filter(null,RDF.TYPE,RDFS.rdf.createIRI(SignifierOntology.Signifier)));
            if (optionalSignifierId.isPresent()){
                Resource signifierId = optionalSignifierId.get();
                return new Signifier.Builder(signifierId).add(model).build();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }



    /*A block is constituted by all the statements whose subject is the given resource
     as well as the blocks whose subjects are the objects of a statement whose subject is the given resource.
     */
    public static void readBlock(Resource blockId, Model model, Model newModel) {
        Model m = model.filter(blockId, null, null);
        newModel.addAll(m);
        Set<Resource> newResources = Models.objectResources(m);
        for (Resource resourceId : newResources) {
            readBlock(resourceId, model, newModel);
        }
    }

        public static Model getBlock(Resource blockId, Model model){
        Model newModel = new ModelBuilder().build();
        readBlock(blockId, model, newModel);
        return newModel;

        }





}
