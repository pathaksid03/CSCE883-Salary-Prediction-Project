package themoles.model.util;

/**
 * @author meng
 * Purpose: Parse and organize train / test data
 *          and any neccesary methods that assist building and running the model.
 */

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;

public class Utility {
    public static final String DELIM = ",";

    /**
     * Load and return the entire data set
     * @param dataFile data set
     * @return all records in given data file
     */
    public List<String> loadData(String dataFile) {
        List<String> dataset = new ArrayList<String>();

        try {
            InputStream fis = new FileInputStream(dataFile);
            InputStreamReader isr = new InputStreamReader(fis, Charset.forName("UTF-8"));
//            BufferedReader reader = new BufferedReader(isr);
            Scanner fileScanner = new Scanner(isr);

            while (fileScanner.hasNext()) {
                dataset.add(fileScanner.nextLine() + "\n");
            }

           fileScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return dataset;
    }

    /**
     *
     * @param originalDataSet
     * @param count
     * @return
     */
    public ArrayList<String> randSelectDataset(ArrayList<String> originalDataSet, int count) {
        ArrayList<String> selectedDataset = new ArrayList<String>();
        Random random = new Random();
        int amount = originalDataSet.size();
        int index = 0;
        String observation;

        for (int i = 0; i < count; i++) {
            index  = random.nextInt(amount);
            observation = originalDataSet.get(index);

            if (selectedDataset == null) {
                selectedDataset.add(observation);
            }
            if (selectedDataset.contains(observation)) {
                i--;
            } else {
                selectedDataset.add(observation);
            }
        }

        return selectedDataset;
    }

    public ArrayList<String> deleteAttribute(ArrayList<String> set, int indexOfAttribute) {
        ArrayList<String> subset = new ArrayList<String>();
        String line = "";
        String[] items;

        for (String observation: set) {
            items = observation.split(DELIM);
            for (int i = 0; i < items.length; i++) {
                if (i == indexOfAttribute) {
                    continue;
                }
                line += items[i];
            }
            subset.add(line);
            line = "";
        }

        return subset;
    }

    /**
     *
     * @param set
     * @param attribute
     * @param attrValue
     * @return
     */
    public ArrayList<String> fetchObservations(ArrayList<String> set, String attribute, String attrValue) {
        ArrayList<String> subset = new ArrayList<>();
        String line = set.get(0);
        String[] items = line.split(DELIM);
        int index = lookForFeatureIndex(items, attribute);

        if (set == null || attrValue == null || attrValue.trim().length() == 0) {
            System.out.println("Null/Empty set or Null/Empty key!");
            return null;
        }

        if (index < 2 || index == items.length - 1) {
            System.out.println("Invalid feature!");
            return null;
        }

        if (attribute == null || attribute.trim().length() == 0) {
            // if no specific feature, return the universe
            System.out.println("No specific feature, return universe.");
            subset = set;
        } else {
            for (int i = 1; i < set.size(); i++) {
                line = set.get(i);
                items = line.split(DELIM);
                if (attrValue.equalsIgnoreCase(items[index].trim())) {
                    subset.add(line);
                }
            }
        }
        return subset;
    }

    /**
     * Get keyword list and corresponding sum of corresponding target
     * from given feature
     * @param dataset - data set to extract information from
     * @param attribute - feature name, of which the keywords are statistic
     * @return keywords and corresponding sum of target in given feature
     */
    public ArrayList<String> extractAttrValues(List<String> dataset, String attribute) {
        ArrayList<String> valueList = new ArrayList<String>();
        String[] items = dataset.get(0).split(DELIM);
        int index = lookForFeatureIndex(items, attribute);

        if (dataset == null || attribute == null || attribute.trim().length() == 0) {
            System.out.println("NULL contents or NULL/Empty feature!");
            return null;
        }

        if (index < 2 || index == items.length - 1) {
            // feature not found, or feature is ID, or feature is target
            // return null
            System.out.println("Invalid feature!");
            return null;
        }

        // get keyword list in given feature
        // and statistic frequency of each keyword
        for (int i = 1; i < dataset.size(); i++) {
            items = dataset.get(i).split(DELIM);
            if (!valueList.contains(items[index].trim())) {
                valueList.add(items[index]);
            }
        }

        return valueList;
    }

    /**
     *
     * @param headers
     * @param attribute
     * @return
     */
    private int lookForFeatureIndex(String[] headers, String attribute) {
        int index = -1;

        // look for the location of specific feature
        for (int i = 0; i < headers.length; i++) {
            if (attribute.equalsIgnoreCase(headers[i].trim())) {
                index = i;
                break;
            }
        }

        return index;
    }

}
