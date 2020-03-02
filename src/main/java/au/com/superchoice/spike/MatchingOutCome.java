package au.com.superchoice.spike;

import au.com.superchoice.spike.domain.Contribution;
import au.com.superchoice.spike.domain.Cover;
import au.com.superchoice.spike.domain.Mappable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatchingOutCome implements Mappable {
    public final Contribution contribution;
    public final List<Cover> covers;

    public MatchingOutCome(Contribution contribution, List<Cover> covers) {
        this.contribution = contribution;
        this.covers = covers;
    }
    public MatchingOutCome mergeOther(MatchingOutCome otherOutcome) {
        this.covers.addAll(otherOutcome.covers);
        return this;
    }

    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.putAll(contribution.toMap());
        covers.stream().map(Cover::toMap).reduce((m1, m2) -> {
            m1.putAll(m2);
            return m1;
        }).ifPresent(m -> map.putAll(m));
        return map;
    }

    @Override
    public String toString() {
        return "MatchingOutCome{" +
                "contribution=" + contribution +
                ", covers=" + covers +
                '}';
    }
}
