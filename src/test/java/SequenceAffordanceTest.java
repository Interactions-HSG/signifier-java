import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.ValueFactory;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class SequenceAffordanceTest {

    ValueFactory rdf;
    SequenceAffordance exit;

    static class EnvironmentOntology {
        public static String env1 = "https://example.com/";

        public static String isIn = env1 + "isIn";

        public static String hasMethod = env1 + "hasMethod";

        public static String hasParameter= env1 + "hasParameter";

        public static String hasResult= env1 + "hasResult";

        public static String room1 = env1 + "room1";

        public static String room2 = env1 + "room2";

        public static String room3 = env1 + "room3";

        public static String room4 = env1 + "room4";

        public static String room5 = env1 + "room5";

        public static String room6 = env1 + "room6";

        public static String room7 = env1 + "room7";

        public static String room8 = env1 + "room8";

        public static String room9 = env1 + "room9";
    }

    @Before
    public void init(){
        rdf = RDFS.rdf;
        Resource thisAgent = rdf.createIRI(SignifierOntology.thisAgent);
        IRI isIn = rdf.createIRI(EnvironmentOntology.isIn);
        Resource room1 = rdf.createIRI(EnvironmentOntology.room1);
        Resource room2 = rdf.createIRI(EnvironmentOntology.room2);
        Resource room3 = rdf.createIRI(EnvironmentOntology.room3);
        Resource room4 = rdf.createIRI(EnvironmentOntology.room4);
        Resource room5 = rdf.createIRI(EnvironmentOntology.room5);
        Resource room6 = rdf.createIRI(EnvironmentOntology.room6);
        Resource room7 = rdf.createIRI(EnvironmentOntology.room7);
        Resource room8 = rdf.createIRI(EnvironmentOntology.room8);
        Resource room9 = rdf.createIRI(EnvironmentOntology.room9);
        Resource room1Id = rdf.createBNode("room1Id");
        Resource state1StatementId = rdf.createBNode("room1S");
        ReifiedStatement state1Statement = new ReifiedStatement(state1StatementId,thisAgent,isIn,room1);
        State state1 = new State.Builder(room1Id).addStatement(state1Statement).build();

        Resource room2Id = rdf.createBNode("room2Id");
        Resource state2StatementId = rdf.createBNode("room2S");
        ReifiedStatement state2Statement = new ReifiedStatement(state2StatementId,thisAgent,isIn,room2);
        State state2 = new State.Builder(room2Id).addStatement(state2Statement).build();

        Resource room3Id = rdf.createBNode("room3Id");
        Resource state3StatementId = rdf.createBNode("room3S");
        ReifiedStatement state3Statement = new ReifiedStatement(state3StatementId,thisAgent,isIn,room3);
        State state3 = new State.Builder(room3Id).addStatement(state3Statement).build();

        Resource room4Id = rdf.createBNode("room4Id");
        Resource state4StatementId = rdf.createBNode("room4S");
        ReifiedStatement state4Statement = new ReifiedStatement(state4StatementId,thisAgent,isIn,room4);
        State state4 = new State.Builder(room4Id).addStatement(state4Statement).build();

        Resource room5Id = rdf.createBNode("room5Id");
        Resource state5StatementId = rdf.createBNode("room5S");
        ReifiedStatement state5Statement = new ReifiedStatement(state5StatementId,thisAgent,isIn,room5);
        State state5 = new State.Builder(room5Id).addStatement(state5Statement).build();

        Resource room6Id = rdf.createBNode("room6Id");
        Resource state6StatementId = rdf.createBNode("room6S");
        ReifiedStatement state6Statement = new ReifiedStatement(state6StatementId,thisAgent,isIn,room6);
        State state6 = new State.Builder(room6Id).addStatement(state6Statement).build();

        Resource room7Id = rdf.createBNode("room7Id");
        Resource state7StatementId = rdf.createBNode("room7S");
        ReifiedStatement state7Statement = new ReifiedStatement(state7StatementId,thisAgent,isIn,room7);
        State state7 = new State.Builder(room7Id).addStatement(state7Statement).build();

        Resource room8Id = rdf.createBNode("room8Id");
        Resource state8StatementId = rdf.createBNode("room8S");
        ReifiedStatement state8Statement = new ReifiedStatement(state8StatementId,thisAgent,isIn,room8);
        State state8 = new State.Builder(room8Id).addStatement(state8Statement).build();

        Resource room9Id = rdf.createBNode("room9Id");
        Resource state9StatementId = rdf.createBNode("room9S");
        ReifiedStatement state9Statement = new ReifiedStatement(state9StatementId,thisAgent,isIn,room9);
        State state9 = new State.Builder(room9Id).addStatement(state1Statement).build();

        List<Affordance> affordanceList = new ArrayList<>();
        Resource firstMoveId = rdf.createBNode();
        Resource firstMethod = rdf.createBNode();
        Affordance firstMove = new Affordance.Builder(firstMoveId)
                .setPrecondition(state1)
                .setObjective(state2)
                .add(rdf.createIRI(EnvironmentOntology.hasMethod),firstMethod)
                .add(firstMethod,rdf.createIRI(EnvironmentOntology.hasParameter),rdf.createLiteral(1))
                .add(firstMethod,rdf.createIRI(EnvironmentOntology.hasResult),rdf.createLiteral(2))
                .build();
        affordanceList.add(firstMove);
        Resource secondMoveId = rdf.createBNode();
        Resource secondMethod = rdf.createBNode();
        Affordance secondMove = new Affordance.Builder(secondMoveId)
                .setPrecondition(state2)
                .setObjective(state5)
                .add(rdf.createIRI(EnvironmentOntology.hasMethod),secondMethod)
                .add(secondMethod,rdf.createIRI(EnvironmentOntology.hasParameter),rdf.createLiteral(0))
                .add(secondMethod,rdf.createIRI(EnvironmentOntology.hasResult),rdf.createLiteral(5))
                .build();
        affordanceList.add(secondMove);
        Resource thirdMoveId = rdf.createBNode();
        Resource thirdMethod = rdf.createBNode();
        Affordance thirdMove = new Affordance.Builder(thirdMoveId)
                .setPrecondition(state5)
                .setObjective(state8)
                .add(rdf.createIRI(EnvironmentOntology.hasMethod),thirdMethod)
                .add(thirdMethod,rdf.createIRI(EnvironmentOntology.hasParameter),rdf.createLiteral(0))
                .add(thirdMethod,rdf.createIRI(EnvironmentOntology.hasResult),rdf.createLiteral(8))
                .build();
        affordanceList.add(thirdMove);
        Resource fourthMoveId = rdf.createBNode();
        Resource fourthMethod = rdf.createBNode();
        Affordance fourthMove = new Affordance.Builder(fourthMoveId)
                .setPrecondition(state8)
                .setObjective(state9)
                .add(rdf.createIRI(EnvironmentOntology.hasMethod),fourthMethod)
                .add(fourthMethod,rdf.createIRI(EnvironmentOntology.hasParameter),rdf.createLiteral(1))
                .add(fourthMethod,rdf.createIRI(EnvironmentOntology.hasResult),rdf.createLiteral(9))
                .build();
        affordanceList.add(fourthMove);
        Resource exitId = rdf.createBNode();
        SequenceAffordance.Builder builder = (SequenceAffordance.Builder) new SequenceAffordance.Builder(exitId)
                .addSequence(affordanceList)
                .setPrecondition(state1)
                .setObjective(state9);
        exit = builder.build();
    }

    @Test
    public void checkSubAffordances(){
        List<Affordance> subAffordances = exit.getSequence();
        int n = subAffordances.size();
        for (int i = 0; i<n; i++){
            if (i==0){
                assertEquals(exit.getPrecondition(),subAffordances.get(i).getPrecondition());
            }
            else if (i<n-1) {
                assertEquals(subAffordances.get(i-1).getObjective(),subAffordances.get(i).getPrecondition());
            }
            else {
                assertEquals(subAffordances.get(i-1).getObjective(),subAffordances.get(i).getPrecondition());
                assertEquals(exit.getObjective(),subAffordances.get(i).getObjective());
            }
        }
    }

}
