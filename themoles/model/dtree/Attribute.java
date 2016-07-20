package themoles.model.dtree;

import themoles.model.util.Utility;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Data Structure Definition - attribute / feature
 * @author meng
 * Purpose: An attribute / feature object
 */
public class Attribute {
    public static final String CONTINUOUS = "continuous";
    public static final String DISCRETE = "discrete";
    public static final String REAL = "real";

    private String name; // feature name, attribute name
    private String dataType; // discrete value or continuous value
    private ArrayList<String> values; // distinct values for discrete value, "real" for continuous value

    public Attribute() {
        name = null;
        dataType = null;
        values = null;
    }

    /**
     * Constructor
     * @param name - name of attribute / feature
     * @param values - distinct values of discrete attribute, "real" for continuous attribute
     * @throws IOException
     */
    public Attribute(String name, String values) throws IOException {
        // Initialize name
        this.name = name;

        // Initialize pattern
        if (values.equalsIgnoreCase(REAL)) {
            dataType = CONTINUOUS;
        } else {
            dataType = DISCRETE;
        }

        // Initialize values
        if (values.equalsIgnoreCase(REAL)) {
            this.values = new ArrayList<String>();
            this.values.add(REAL);
        } else {
            this.values = constructValues(values);
        }
    }

    /**
     * Constructor
     * @param attrName - attribute name
     * @param valuesList - list of values
     * @throws IOException
     */
    public Attribute(String attrName, ArrayList<String> valuesList) throws IOException{
        name = attrName;

        if (valuesList.get(0).equalsIgnoreCase(REAL)) {
            dataType = CONTINUOUS;
        } else {
            dataType = DISCRETE;
        }

        values = valuesList;
    }

    public String toString() {
        return "@Attribute Name: " + name + "; " +
                "@Attribute DataType: " + dataType + "; " +
                "@Attribute Values: " + values;
    }

    // Accessors
    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public ArrayList<String> getValues() {
        return values;
    }

    // Mutators
    public void setName(String aName) {
        name = aName;
    }

    public void setDataType(String aType) {
        dataType = aType;
    }

    public void setValues(ArrayList<String> valueList) {
        values = valueList;
    }

    private ArrayList<String> constructValues(String s) {
        ArrayList<String> values = new ArrayList<String>();
        try {
            if (s == null || s.length() < 2) throw new IOException("Invalid input format");
            s = s.substring(1, s.length() - 1);
            String[] sArr = s.split(Utility.DELIM);
            for (String item : sArr) {
                values.add(item);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return values;
    }

    public static void main(String[] args) throws IOException {
        String name = "Location";
        String values = "{London, Beijing, NewYork}";
        Attribute attr = new Attribute(name, values);

        System.out.println(attr.toString());

        String attrName = "Company";
        ArrayList<String> list = new ArrayList<String>();

        list.add("Google");
        list.add("LinkedIn");
        list.add("Facebook");
        list.add("Amazon");
        list.add("eBay");

        Attribute attr2 = new Attribute(attrName, list);
        System.out.println(attr2.toString());
    }
}
