package au.com.superchoice.spike.domain;

import java.util.HashMap;
import java.util.Map;

public class Cover {
    public enum CoverType{
        LIFE, TPD, IP, SCB
    }
    public final CoverType coverType;
    public final String coverAmount;
    public final Contribution contribution;

    public Cover(CoverType coverType, String coverAmount, Contribution contribution) {
        this.coverType = coverType;
        this.coverAmount = coverAmount;
        this.contribution = contribution;
    }

    @Override
    public String toString() {
        return "Cover{" +
                "coverType=" + coverType +
                ", coverAmount=" + coverAmount +
                ", contribution=" + contribution +
                '}';
    }

    public Map<String, String> toMap() {
        Map<String, String> valueMap = new HashMap<>();
        valueMap.put(coverType.name(), coverAmount);
        return valueMap;
    }
}
