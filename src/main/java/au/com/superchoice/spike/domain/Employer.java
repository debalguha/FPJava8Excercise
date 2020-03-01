package au.com.superchoice.spike.domain;

public class Employer {
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
}
