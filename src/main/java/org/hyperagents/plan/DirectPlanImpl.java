package org.hyperagents.plan;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.util.Models;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.hyperagents.ontologies.SignifierOntology;
import org.hyperagents.util.RDFS;

import java.util.Set;

public class DirectPlanImpl extends DirectPlan {
    protected DirectPlanImpl(Resource planId, Model model) {
        super(planId, model);
    }

    public static DirectPlan getDirectPlan(Plan p){
        Resource planId = p.getId();
        Model model = p.getModel();
        boolean b = true;
        Set<Resource> types = Models.objectResources(model.filter(planId, RDF.TYPE, null));
        for (Resource type : types){
            if (type.equals(RDFS.rdf.createIRI(SignifierOntology.AffordancePlan))){
                b = false;
            }
        }
        if (b){
            DirectPlan directPlan = new DirectPlanImpl(planId, model);
            return directPlan;
        }
        return null;

    }
}
