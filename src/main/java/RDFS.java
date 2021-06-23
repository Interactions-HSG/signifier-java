import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class RDFS {

    public static ValueFactory rdf=SimpleValueFactory.getInstance();

    public static Model createList(Resource listId, List<Value> values) {
        Model m = new DynamicModelFactory().createEmptyModel();
        Iterator<Value> iterator = values.iterator();
        Resource currentListId = listId;
        while (iterator.hasNext()) {
            Value v = iterator.next();
            m.add(currentListId, rdf.createIRI(RDFSOntology.first), v);
            if (iterator.hasNext()) {
                BNode newListId = rdf.createBNode();
                m.add(currentListId, rdf.createIRI(RDFSOntology.rest), newListId);
                currentListId = newListId;

            } else {
                m.add(currentListId, rdf.createIRI(RDFSOntology.rest), rdf.createIRI(RDFSOntology.nil));
            }

        }
        return m;
    }

    public static String getTurtle(Model m){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Rio.write(m,outputStream, RDFFormat.TURTLE);
        return outputStream.toString();
    }

    public static List<Value> readList(Resource resourceId,Model model){
        List<Value> list = new ArrayList<>();
        Resource currentId = resourceId;
        while (!currentId.equals(rdf.createIRI(RDFSOntology.nil))){
            Optional<Value> value = Models.object(model.filter(currentId,rdf.createIRI(RDFSOntology.first),null));
            if (value.isPresent()){
                list.add(value.get());
            }
            Optional<Resource> newResourceId = Models.objectResource(model.filter(currentId,rdf.createIRI(RDFSOntology.rest),null));
            if (newResourceId.isPresent()){
                currentId = newResourceId.get();
            }
            else{
                return list;
            }

        }
        return list;
    }

    public static List<Resource> readResourceList(Resource resourceId,Model model){
        List<Resource> list = new ArrayList<>();
        Resource currentId = resourceId;
        while (!currentId.equals(rdf.createIRI(RDFSOntology.nil))){
            Optional<Resource> value = Models.objectResource(model.filter(currentId,rdf.createIRI(RDFSOntology.first),null));
            value.ifPresent(list::add);
            Optional<Resource> newResourceId = Models.objectResource(model.filter(currentId,rdf.createIRI(RDFSOntology.rest),null));
            if (newResourceId.isPresent()){
                currentId = newResourceId.get();
            }
            else{
                return list;
            }

        }
        return list;
    }

}
