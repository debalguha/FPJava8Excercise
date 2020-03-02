package au.com.superchoice.spike.domain;

import java.util.HashMap;
import java.util.Map;

public class Employee implements Mappable {
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

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("name", givenName);
        map.put("surname", surname);
        map.put("address", address);
        map.putAll(employer.toMap());
        return map;
    }
}
