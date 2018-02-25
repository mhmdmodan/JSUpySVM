import Holders.ClassPair;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MasterPredictorTest {
    @Test
    public void runOffTester() {
        Map<ClassPair, String> testMap = new HashMap<>();
        testMap.put(new ClassPair("A", "B"), "A");
        testMap.put(new ClassPair("A", "C"), "C");
        testMap.put(new ClassPair("B", "C"), "B");
        System.out.println(MasterPredictor.runOffVoting(testMap));
    }
}
