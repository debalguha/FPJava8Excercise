package au.com.superchoice.spike.domain;

public class Cover {
    public enum CoverType{
        LIFE, TPD, IP, SCB
    }
    public final CoverType coverType;
    public final double coverAmount;
    public final Contribution contribution;

    public Cover(CoverType coverType, double coverAmount, Contribution contribution) {
        this.coverType = coverType;
        this.coverAmount = coverAmount;
        this.contribution = contribution;
    }
}
