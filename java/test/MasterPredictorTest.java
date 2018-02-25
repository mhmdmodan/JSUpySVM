import Holders.ClassPair;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MasterPredictorTest {
    @Test
    public void infLoopTester() {
        Map<ClassPair, String> testMap = new HashMap<>();
        testMap.put(new ClassPair("A", "B"), "A");
        testMap.put(new ClassPair("A", "C"), "C");
        testMap.put(new ClassPair("B", "C"), "B");
        for (int i=0; i<50; i++) {
            String result = MasterPredictor.runOffVoting(testMap);
            assert (result.equals("A") ||
                    result.equals("B") ||
                    result.equals("C"));
        }
    }

    @Test
    public void bigInfLoopTest() {
        Map<ClassPair, String> testMap = new HashMap<>();
        testMap.put(new ClassPair("A", "B"), "A");
        testMap.put(new ClassPair("A", "C"), "A");
        testMap.put(new ClassPair("A", "D"), "D");
        testMap.put(new ClassPair("B", "C"), "C");
        testMap.put(new ClassPair("B", "D"), "D");
        testMap.put(new ClassPair("C", "D"), "C");

        for (int i=0; i<50; i++) {
            String result = MasterPredictor.runOffVoting(testMap);
            assert (result.equals("D") ||
                    result.equals("A") ||
                    result.equals("C"));
        }
    }

    @Test
    public void simpleRunOffTest() {
        Map<ClassPair, String> testMap = new HashMap<>();
        testMap.put(new ClassPair("A", "B"), "A");
        testMap.put(new ClassPair("A", "C"), "A");
        testMap.put(new ClassPair("A", "D"), "D");
        testMap.put(new ClassPair("B", "C"), "B");
        testMap.put(new ClassPair("B", "D"), "D");
        testMap.put(new ClassPair("C", "D"), "C");

        String result = MasterPredictor.runOffVoting(testMap);
        assert(result.equals("D"));
    }
}
