package themoles.model.dtree;

import java.util.HashMap;

/**
 * Data Structure Definition - attribute instance
 * @author meng
 */
public class Instance {
    private static int count = 0;
    private int index;
    private HashMap<String, String> attributeValuePairs;

    public Instance() {
        index = count;
        attributeValuePairs = new HashMap<String, String>();
        count++;
    }

    public void addAttribute(String name, String value) {
        attributeValuePairs.put(name, value);
    }

    public int getInstanceIndex() {
        return index;
    }

    public HashMap<String, String> getAttributeValuePairs() {
        return attributeValuePairs;
    }

    public String toString() {
        return "@Instance Index: " + index + "; " +
                "Attribute Value Pairs: " + attributeValuePairs;
    }

    // test
    public static void main(String[] args) {
        Instance inst1 = new Instance();
        inst1.addAttribute("Google", "Company");
        inst1.addAttribute("Facebook", "Company");
        inst1.addAttribute("Amazon", "Company");
        System.out.println(inst1);

        Instance inst2 = new Instance();
        inst2.addAttribute("London", "Location");
        inst2.addAttribute("Beijing", "Location");
        inst2.addAttribute("NewYork", "Location");
        System.out.println(inst2);

        Instance test1 = new Instance();
        test1.addAttribute("Service_type", "Fund");
        test1.addAttribute("Servi", "Fund");
        System.out.println(test1);

        Instance test2 = new Instance();
        test2.addAttribute("Service_type", "Fund");
        test2.addAttribute("Servi", "Fund");
        System.out.println(test2);
    }
}
