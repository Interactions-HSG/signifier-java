package hypermedia;

import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.hyperagents.hypermedia.HypermediaAction;
import org.hyperagents.signifier.SignifierModelBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.*;

public class HypermediaActionTest {
    ValueFactory rdf;
    Resource actionId;
    String url;

    @Before
    public void init(){
        rdf = SimpleValueFactory.getInstance();
        actionId = rdf.createBNode("action");
        url = "http://example.org";
    }

    @Test
    public void GETTest(){
        HypermediaAction action = new HypermediaAction.Builder(actionId, url, "GET")
                .build();
        assertEquals(actionId, action.getId());
        assertEquals(url, action.getUrl());
        assertEquals("GET",action.getMethod());
        assertEquals(new HashSet<>(), action.getOperationTypes());
        assertEquals(new Hashtable<>(), action.getHeaders());
        assertEquals(Optional.empty(), action.getPayload());
    }

    @Test
    public void POSTTest(){
        String payload = "{\"name\": \"action\"}";
        Map<String, String> headers = new Hashtable<>();
        headers.put("Content-Type", "application/json");
        HypermediaAction action = new HypermediaAction.Builder(actionId, url, "POST")
                .addHeader("Content-Type", "application/json")
                .setPayload(payload)
                .build();
        assertEquals(actionId, action.getId());
        assertEquals(url, action.getUrl());
        assertEquals("POST",action.getMethod());
        assertEquals(new HashSet<>(), action.getOperationTypes());
        assertEquals(headers, action.getHeaders());
        assertEquals(Optional.of(payload), action.getPayload());

    }
}
