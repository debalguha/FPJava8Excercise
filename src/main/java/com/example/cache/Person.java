package com.example.cache;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class Person {
    public final String firstName;
    public final String lastName;
    public final Date dob;
    public final String tfn;

    public Person(String firstName, String lastName, Date dob, String tfn) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dob = dob;
        this.tfn = tfn;
    }

    public Person(String []split) {
        this(split[0], split[1], parseDate(split[2]), split[3]);
    }

    public Person(String s) {
        this(s.split(",", -1));
    }

    private static Date parseDate(String s) {
        return Date.from(LocalDate.from( DateTimeFormatter.ISO_DATE.parse(s)).atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Date getDob() {
        return dob;
    }

    public String getTfn() {
        return tfn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(tfn, person.tfn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tfn);
    }
}
