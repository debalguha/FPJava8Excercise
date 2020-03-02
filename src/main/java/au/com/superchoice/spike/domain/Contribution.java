package au.com.superchoice.spike.domain;

import java.util.Map;

public class Contribution implements Mappable{
    public final Member member;

    public Contribution(Member member) {
        this.member = member;
    }

    @Override
    public Map<String, String> toMap() {
        return member.toMap();
    }
}
