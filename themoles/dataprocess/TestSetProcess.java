package themoles.dataprocess;

import java.util.ArrayList;

/**
 * Created by meng on 11/30/15.
 */
public class TestSetProcess {
    public static void main(String[] args) {
        DataProcesser processer = new DataProcesser();
        String originalTestSet = "dataset/test/splitdata/TestSet.csv";
        String keywordFile = "dataset/dictionary/keywords.txt";
        ArrayList<String> listOfKeyWords = processer.loadFile(keywordFile);

        // reorganize validation set by keywords
        processer.organizeDatasetByKey(originalTestSet, listOfKeyWords);
    }
}
