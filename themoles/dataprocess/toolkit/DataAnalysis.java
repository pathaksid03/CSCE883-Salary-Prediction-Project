package themoles.dataprocess.toolkit;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by Ying Meng on 11/28/15.
 */
public class DataAnalysis {
    static final String DELIM = ",";
    static final String CODE = "UTF-8";

    public static ArrayList<String> loadFile(String fileName) {
        File file = new File(fileName);
        Scanner fileScanner = null;
        ArrayList<String> contents = new ArrayList<String>();

        try {
            fileScanner = new Scanner(file, CODE);

            while (fileScanner.hasNext()) {
                contents.add(fileScanner.nextLine());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileScanner != null) {
                fileScanner.close();
            }
        }

        return contents;
    }

    public static ArrayList<String> merge(ArrayList<String> contents,
                             ArrayList<String> predictions) {
        ArrayList<String> result = new ArrayList<String>();
        String[] predict;
        int count = 0;

        for (String data: contents) {
            for (String pred: predictions) {
                predict = pred.split(DELIM);
                System.out.println(predict[0] + " = " + data.split(DELIM)[0]
                        + " " + predict[0].equals(data.split(DELIM)[0]));
                if (predict[0].equals(data.split(DELIM)[0])) {
                    System.out.println(data + " contains " + predict[0]);
                    result.add(data + DELIM + predict[1]);
                    System.out.println("Add >>> " + data + DELIM + predict[1]);
                    count++;
                    if (count == 3) {
                        return result;
                    }
                    break;
                }
            }
        }
        return result;
    }

    public static void print(ArrayList<String> contents, String fileName) {
        File file = new File(fileName);
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(fileName, CODE);
            for (String line: contents) {
                writer.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    public static void main(String[] args) {
        String file1 = "dataset/training/all/Vrandom_forest_benchmark.csv";
        String file2 = "dataset/test/Test_all.csv";
        String file3 = "dataset/test/test_pred.csv";

        System.out.println("loading " + file1);
        ArrayList<String> predictions = loadFile(file1);
        System.out.println("file 1 loaded.");
        System.out.println("loading " + file2);
        ArrayList<String> test = loadFile(file2);
        System.out.println("file 2 loaded.");
        System.out.println("merging data");
        ArrayList<String> pred_valid = merge(test, predictions);
        System.out.println("done merging data");

        System.out.println("printing verifying data");
        print(pred_valid, file3);

        System.out.println("Done");
    }

}
