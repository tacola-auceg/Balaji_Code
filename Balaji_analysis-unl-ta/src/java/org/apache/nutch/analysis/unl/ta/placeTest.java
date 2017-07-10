package org.apache.nutch.analysis.unl.ta;
//package org.apache.nutch.template.unl;

import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author karthikeyan.S
 * @version 2.0
 * @since AUCEG
 */

public class placeTest {

    public static boolean isPlaceLoaded = false;
    public static HashMap<String, String> place;

    /**
     * This method define for load the place Object only first time
     */
    public void loadPlace() {
        if (isPlaceLoaded) {
            return;
        }
        ObjectInputStream placeReader = IOHelper.getObjectInputStream("./crawl-unl/newsummary/placeList.ser");
        place = (HashMap<String, String>) IOHelper.readObjectFromInputStream(placeReader);
        isPlaceLoaded = true;
    }

    /**
     *This method define for get the place of document
     */
    public void getTestSummary() {
        loadPlace();
        Set key = place.keySet();
        for (Object docids : key) {
            System.out.println("Docid" + docids.toString());
            System.out.println("Places--->" + place.get(docids.toString()));
        }
    }

    public static void main(String[] args) {
        placeTest PlaceTest = new placeTest();
        PlaceTest.getTestSummary();
    }
}
