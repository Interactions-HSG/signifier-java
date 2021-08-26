package org.hyperagents.hypermedia;

import ch.unisg.ics.interactions.wot.td.affordances.Form;
import ch.unisg.ics.interactions.wot.td.io.InvalidTDException;
import ch.unisg.ics.interactions.wot.td.schemas.DataSchema;
import ch.unisg.ics.interactions.wot.td.vocabularies.TD;
import ch.unisg.ics.interactions.wot.td.vocabularies.HTV;
import ch.unisg.ics.interactions.wot.td.vocabularies.HCTL;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class TDProcessor {

    private static ValueFactory rdf = SimpleValueFactory.getInstance();

    public static Optional<Form> readForm(Resource formId, Model model){
        Optional<Form> opForm = Optional.empty();
        return opForm;
    }

    private static List<Form> readForms(Resource affordanceId, String affordanceType, Model model) {
        List<Form> forms = new ArrayList<Form>();

        Set<Resource> formIdSet = Models.objectResources(model.filter(affordanceId,
                rdf.createIRI(TD.hasForm), null));

        for (Resource formId : formIdSet) {
            Optional<IRI> targetOpt = Models.objectIRI(model.filter(formId, rdf.createIRI(HCTL.hasTarget),
                    null));

            if (!targetOpt.isPresent()) {
                continue;
            }

            Optional<Literal> methodNameOpt = Models.objectLiteral(model.filter(formId,
                    rdf.createIRI(HTV.methodName), null));

            Optional<Literal> contentTypeOpt = Models.objectLiteral(model.filter(formId,
                    rdf.createIRI(HCTL.forContentType), null));
            String contentType = contentTypeOpt.isPresent() ? contentTypeOpt.get().stringValue()
                    : "application/json";

            Optional<Literal> subprotocolOpt = Models.objectLiteral(model.filter(formId,
                    rdf.createIRI(HCTL.forSubProtocol), null));

            Set<IRI> opsIRIs = Models.objectIRIs(model.filter(formId, rdf.createIRI(HCTL.hasOperationType),
                    null));
            Set<String> ops = opsIRIs.stream().map(op -> op.stringValue()).collect(Collectors.toSet());

            String target = targetOpt.get().stringValue();

            Form.Builder builder = new Form.Builder(target)
                    .setContentType(contentType)
                    .addOperationTypes(ops);

            if (methodNameOpt.isPresent()) {
                builder.setMethodName(methodNameOpt.get().stringValue());
            }

            if (subprotocolOpt.isPresent()) {
                builder.addSubProtocol(subprotocolOpt.get().stringValue());
            }

            forms.add(builder.build());
        }

        if (forms.isEmpty()) {
            throw new InvalidTDException("[" + affordanceType + "] All interaction affordances should have "
                    + "at least one valid.");
        }

        return forms;
    }
}
