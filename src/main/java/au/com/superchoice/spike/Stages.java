package au.com.superchoice.spike;

import au.com.superchoice.spike.domain.*;
import com.google.common.base.Strings;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.flowables.GroupedFlowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.List;
import java.util.Map;

import static java.util.Optional.*;
import static org.apache.commons.compress.utils.Lists.newArrayList;

public class Stages {
    public static Map<String, String> columnMapping(Map<String, String> dataMap) {
        return dataMap;
    }
    public static Map<String, String> regExEnrichment(Map<String, String> dataMap) {
        return dataMap;
    }
    public static Map<String, String> juelEnrichment(Map<String, String> dataMap) {
        return dataMap;
    }
    public static @NonNull Maybe<MatchingOutCome> runMatching(GroupedFlowable<String, Map<String, String>> groupedFlowable) {
        @Nullable final String memberClientIdentifier = groupedFlowable.getKey();
        @NonNull final Maybe<MatchingOutCome> matchingOutComeMaybe = groupedFlowable.map(m -> createMatchingOutcome(m, memberClientIdentifier))
                .doOnError(e -> e.printStackTrace())
                .reduce((mo1, mo2) -> mo1.mergeOther(mo2))
                .subscribeOn(Schedulers.computation());
        return matchingOutComeMaybe;
    }



    public static MatchingOutCome createMatchingOutcome(Map<String, String> dataMap, String memberClientIdentifier) {
        final Contribution contribution = createContribution(dataMap, memberClientIdentifier);
        return new MatchingOutCome(contribution, createCovers(dataMap, contribution));
    }
    public static List<Cover> createCovers(Map<String, String> dataMap, Contribution contribution) {
        List<Cover> covers = newArrayList();
        if(!Strings.isNullOrEmpty(dataMap.get("BTCover")))
            covers.add(createLifeCover(dataMap, contribution));
        if(!Strings.isNullOrEmpty(dataMap.get("VDCover")))
            covers.add(createTPDCover(dataMap, contribution));
        if(!Strings.isNullOrEmpty(dataMap.get("BDCover")))
            covers.add(createIPCover(dataMap, contribution));
        if(!Strings.isNullOrEmpty(dataMap.get("VTCover")))
            covers.add(createSCBCover(dataMap, contribution));
        return covers;
    }
    public static Cover persist(MatchingOutCome outCome){
        return crushCovers(outCome.covers);
    }

    public static Cover crushCovers(List<Cover> covers) {
        return covers.get(0);
    }
    public static Contribution createContribution(Map<String, String> dataMap, String memberNumber) {
        String surname = dataMap.get("Surname");
        String givenName = dataMap.get("GivenName");
        String employerAbn = "12345676";
        String fundUsi = "123456";
        String address = dataMap.get("Address1");
        return new Contribution(new Member(new Employee(new Employer(employerAbn), surname, givenName, address), new Fund(fundUsi), memberNumber));
    }

    public static Cover createLifeCover(Map<String, String> dataMap, Contribution contribution) {
        return new Cover(Cover.CoverType.LIFE, ofNullable(dataMap.get("BTCover")).filter(s -> !s.isBlank()).orElse("0"), contribution);
    }
    public static Cover createTPDCover(Map<String, String> dataMap, Contribution contribution) {
        return new Cover(Cover.CoverType.TPD, ofNullable(dataMap.get("VDCover")).filter(s -> !s.isBlank()).orElse("0"), contribution);
    }
    public static Cover createIPCover(Map<String, String> dataMap, Contribution contribution) {
        return new Cover(Cover.CoverType.IP, ofNullable(dataMap.get("BDCover")).filter(s -> !s.isBlank()).orElse("0"), contribution);
    }
    public static Cover createSCBCover(Map<String, String> dataMap, Contribution contribution) {
        return new Cover(Cover.CoverType.SCB, ofNullable(dataMap.get("VTCover")).filter(s -> !s.isBlank()).orElse("0"), contribution);
    }

    public static MatchingOutCome match(Map<String, String> dataMap) {
        String memberClientIdentifier = dataMap.get("Member");
        return createMatchingOutcome(dataMap, memberClientIdentifier);
    }
}
