package org.hyperagents.util;

import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFHandler;
import org.eclipse.rdf4j.rio.helpers.RDFHandlerWrapper;
import org.hyperagents.affordance.Affordance;
import org.hyperagents.ontologies.RDFSOntology;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.DynamicModelFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

public class RDFS {

    public static ValueFactory rdf=SimpleValueFactory.getInstance();

    public static String printModel(Model model){
        OutputStream output = new ByteArrayOutputStream();
        Rio.write(model, output, RDFFormat.TURTLE);
        return output.toString();
    }

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

    public static Model createAffordanceList(Resource listId, List<Affordance> list) {
        Model m = new DynamicModelFactory().createEmptyModel();
        Iterator<Affordance> iterator = list.iterator();
        Resource currentListId = listId;
        while (iterator.hasNext()) {
            Affordance a = iterator.next();
            m.add(currentListId, rdf.createIRI(RDFSOntology.first), a.getId());
            m.addAll(a.getModel());
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

    public static List<Value> readSeq(Resource resourceId,Model model){
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

    public static List<Literal> readLiteralList(Resource resourceId,Model model){
        List<Literal> list = new ArrayList<>();
        Resource currentId = resourceId;
        while (!currentId.equals(rdf.createIRI(RDFSOntology.nil))){
            Optional<Literal> value = Models.objectLiteral(model.filter(currentId,rdf.createIRI(RDFSOntology.first),null));
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

    public static void readBlock(Resource blockId, Model model, Model newModel) {
        Model m = model.filter(blockId, null, null);
        newModel.addAll(m);
        Set<Resource> newResources = Models.objectResources(m);
        for (Resource resourceId : newResources) {
            readBlock(resourceId, model, newModel);
        }
    }

    public static Model retrieveBlock(Resource blockId, Model model){
        Model newModel = new ModelBuilder().build();
        readBlock(blockId, model, newModel);
        return newModel;

    }



}
