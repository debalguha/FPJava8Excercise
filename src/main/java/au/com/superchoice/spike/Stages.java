package au.com.superchoice.spike;

import au.com.superchoice.spike.domain.Cover;
import io.reactivex.rxjava3.flowables.GroupedFlowable;

import java.util.List;
import java.util.Map;

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
    public static MatchingOutCome runMatching(GroupedFlowable<String, Map<String, String>> groupedFlowable) {
        return null;
    }
    public static Cover persist(MatchingOutCome outCome){
        return crushCovers(outCome.covers);
    }

    private static Cover crushCovers(List<Cover> covers) {
        return null;
    }
}
