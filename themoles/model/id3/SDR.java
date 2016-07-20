package themoles.model.id3;

import themoles.model.util.Utility;

import java.util.ArrayList;

/**
 * We use decision tree for regression / prediction real values,
 * so rather than information gain,
 * we calculate SD ( S = sqrt(sum((x - miu) ^ 2) / n)).
 *
 * @author meng.
 * Purpose: calculate SDR (Standard Deviation Reduction),
 *                  Conditional SDR,
 *                  and splitSDR.
 */
public class SDR {
    Utility tool = new Utility();

    /**
     * calculate SD (Standard Deviation) for given dataset
     * S = sqrt(sum((x - miu) ^ 2) / n)
     *
     * @param dataset - dataset, with target at the last column
     * @return S (standard deviation)
     */
    public double calculateSD(ArrayList<String> dataset) {
        double sd = 0; // standard deviation
        double miu;
        double sqrSum = 0;
        double x;
        int amount = dataset.size();
        String[] items;

        if (dataset == null) {
            System.out.println("Null set!!");
            return sd;
        }

        // get miu
        miu = MIU(dataset);
        // calculate sum(x - miu)^2
        for (int i = 0; i < amount; i++) {
            items = dataset.get(i).split(Utility.DELIM);
            x = Double.valueOf(items[items.length - 1].trim());
            sqrSum += Math.pow((x - miu), 2);
        }
        sd = Math.sqrt(sqrSum);

        return sd;
    }

    /**
     * Caculate conditional SD
     * S(TARGET, X) = sum(P(x)S(x)), for x in X
     * P(x) = count(x) / size(X)
     *
     *
     * @param dataset
     * @return
     */
    public double calcConditionalSD(ArrayList<String> dataset, String attribute) {
        double condSD = 0;
        ArrayList<String> values = tool.extractAttrValues(dataset, attribute);
        int sizeX = dataset.size();
        double[] probX = new double[values.size()];
        double[] sdX = new double[values.size()];
        ArrayList<String> subset;
        int index = 0;

        for (String value: values) {
            subset = (ArrayList<String>)tool.fetchObservations(dataset, attribute, value);
            probX[index] = (subset.size() * 0.1) / sizeX;
            sdX[index] = calculateSD(subset);
            index++;
        }

        for (int i = 0; i < probX.length; i++) {
            condSD += probX[i] * sdX[i];
        }

        return condSD;
    }

    /**
     * Calculate SDR (Standard Deviation Reduction)
     * SDR(TARGET, X) = S(TARGET) - S(TARGET, X)
     *
     * @return sdr
     */
    public double SDR(ArrayList<String> dataset, String attribute) {
        double sdr = 0;
        double sd = calculateSD(dataset);
        double condSD = calcConditionalSD(dataset, attribute);

        sdr = sd - condSD;

        return sdr;
    }

    /**
     * Calculate miu, given dataset
     * miu = sum(x) / n
     * @param dataset - dataset, with target at the last column
     * @return miu
     */
     public double MIU(ArrayList<String> dataset) {
        double miu = 0;
        double sum = 0;
        String[] items;

        // calcualte sum of targets
        for (int i = 0; i < dataset.size(); i++) {
            items = dataset.get(i).split(Utility.DELIM);
            sum += Double.valueOf(items[items.length - 1]);
        }

        miu = sum / dataset.size();

        return miu;
    }
}
