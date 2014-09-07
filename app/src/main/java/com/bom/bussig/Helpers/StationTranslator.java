package com.bom.bussig.Helpers;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Oskar on 2014-09-07.
 */
public class StationTranslator {

    Map<String, String> translateTable = new HashMap<String, String>();

    public StationTranslator() {
        initTable();
    }


    public String translateStation(String direction) {
        if (translateTable.containsKey(direction))
            return translateTable.get(direction);
        return direction;
    }
    private void initTable() {
        translateTable.put("Linköping Centralstation", "Linköping Resecentrum");
        translateTable.put("Landbogatan", "Lambohov");
        translateTable.put("Stenåldersgatan", "Skäggetorp/Vidingsjö");
        translateTable.put("Ryd ändhållpl.", "Ryd");

    }


}
