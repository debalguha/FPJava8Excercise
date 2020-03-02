package au.com.superchoice.spike.domain;

import java.util.HashMap;
import java.util.Map;

public class Member implements Mappable {
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

    @Override
    public Map<String, String> toMap() {
        Map<String, String> map = new HashMap<>();
        map.put("MemberNumber", memberNumber);
        map.putAll(employee.toMap());
        map.putAll(fund.toMap());
        return map;
    }
}
