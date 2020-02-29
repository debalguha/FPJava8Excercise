package au.com.superchoice.spike;

import au.com.superchoice.spike.domain.Contribution;
import au.com.superchoice.spike.domain.Cover;

import java.util.List;

public class MatchingOutCome {
    public final Contribution contribution;
    public final List<Cover> covers;

    public MatchingOutCome(Contribution contribution, List<Cover> covers) {
        this.contribution = contribution;
        this.covers = covers;
    }
}
