package au.com.superchoice.spike.domain;

import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.Map;

public class Fund implements Mappable {
    public final String fundUsi;

    public Fund(String fundUsi) {
        this.fundUsi = fundUsi;
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("USI", fundUsi);
        return map;
    }
}
