package au.com.superchoice.spike.domain;

import java.util.HashMap;
import java.util.Map;

public class Employer implements Mappable {
    public final String employerAbn;

    public Employer(String employerAbn) {
        this.employerAbn = employerAbn;
    }

    @Override
    public String toString() {
        return "Employer{" +
                "employerAbn='" + employerAbn + '\'' +
                '}';
    }

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("abn", employerAbn);
        return map;
    }
}
