package au.com.superchoice.spike.domain;

public class Member {
    public final Employee employee;
    public final Fund fund;

    public Member(Employee employee, Fund fund) {
        this.employee = employee;
        this.fund = fund;
    }
}
