package au.com.superchoice.spike.domain;

public class Member {
    public final Employee employee;
    public final Fund fund;
    public final String memberNumber;

    @Override
    public String toString() {
        return "Member{" +
                "employee=" + employee +
                ", fund=" + fund +
                ", memberNumber='" + memberNumber + '\'' +
                '}';
    }

    public Member(Employee employee, Fund fund, String memberNumber) {
        this.employee = employee;
        this.fund = fund;
        this.memberNumber = memberNumber;
    }
}
