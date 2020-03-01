package au.com.superchoice.spike.domain;

public class Employee {
    public final Employer employer;
    public final String surname;
    public final String givenName;
    public final String address;


    public Employee(Employer employer, String surname, String givenName, String address) {
        this.employer = employer;
        this.surname = surname;
        this.givenName = givenName;
        this.address = address;
    }
}
