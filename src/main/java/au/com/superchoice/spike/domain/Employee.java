package au.com.superchoice.spike.domain;

public class Employee {
    @Override
    public String toString() {
        return "Employee{" +
                "employer=" + employer +
                ", surname='" + surname + '\'' +
                ", givenName='" + givenName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

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
