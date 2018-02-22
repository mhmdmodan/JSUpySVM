import TwoClass.Predictor;
import TwoClass.TwoHull;
import Vectors.Vector;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class WSVM {

    private Map<String, List<Vector>> vectorsMap;
    private Map<ClassPair, Predictor> predictorMap;

    private void trainer(List<Vector> aVec,
                         List<Vector> bVec,
                         String classA,
                         String classB) {
        ClassPair pair = new ClassPair(classA, classB);

        if (!predictorMap.containsKey(pair)) {
            TwoHull curTwoHull = new TwoHull(aVec.toArray(new Vector[0]),
                    bVec.toArray(new Vector[0]), classA, classB);
            Predictor curPred =
        }
    }

    private void assignClass(List<Vector> vecList, int whichClass) {
        vecList
                .parallelStream()
                .forEach(vec -> vec.setWhichClass(whichClass));
    }
}
