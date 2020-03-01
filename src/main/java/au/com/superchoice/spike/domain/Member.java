package au.com.superchoice.spike.domain;

public class Member {
    public final Employee employee;
    public final Fund fund;
    public final String memberNumber;

    public Member(Employee employee, Fund fund, String memberNumber) {
        this.employee = employee;
        this.fund = fund;
        this.memberNumber = memberNumber;
    }
}
