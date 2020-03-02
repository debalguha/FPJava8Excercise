package au.com.superchoice.spike;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.awaitility.Awaitility;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import static com.google.common.base.Strings.isNullOrEmpty;
import static java.time.Duration.ofSeconds;
import static java.util.stream.Collectors.toMap;
import static org.awaitility.Awaitility.await;

public class SpreadSheetTest {
    public static void main(String args[]) throws Exception {
        Path inputCSV = Path.of(URI.create("file:///"+args[0]));

        Flowable.fromObservable(
                Observable.<Map<String, String>>create(emitter -> readCSVAndEmitRecord(inputCSV, emitter)), BackpressureStrategy.BUFFER)
                .observeOn(Schedulers.computation())
                .map(Stages::columnMapping)
                .map(Stages::regExEnrichment)
                .map(Stages::juelEnrichment)
                .map(Stages:: match)
                .groupBy(mo -> mo.contribution.member.memberNumber)
                .map(gf -> gf.reduce((mo1, mo2) -> mo1.mergeOther(mo2)).map(MatchingOutCome::toMap))
                .subscribe(matchingOutComeMaybe -> matchingOutComeMaybe.subscribe(mo -> System.out.println(mo)));
        Thread.sleep(3000);
        System.out.println("End");
    }

    private static void readCSVAndEmitRecord(Path inputCSV, ObservableEmitter<Map<String, String>> emitter) {
        try {
            final CSVParser csvParser = new CSVParser(Files.newBufferedReader(inputCSV),CSVFormat.DEFAULT.withIgnoreEmptyLines().withFirstRecordAsHeader());
            Map<String, String> variables = new HashMap<>();
            while(csvParser.iterator().hasNext()) {
                final CSVRecord csvRecord = csvParser.iterator().next();
                if(isSkipRow(csvRecord)){
                    variables = populateVariableMap(csvRecord.toMap(), variables);
                } else {
                    emitter.onNext(synthesizeWithVariable(csvRecord.toMap(), variables));
                }
            }
            emitter.onComplete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Map<String, String> synthesizeWithVariable(Map<String, String> dataMap, Map<String, String> variables) {
        final HashMap<String, String> newDataMap = new HashMap<>(dataMap);
        newDataMap.putAll(variables);
        return newDataMap;
    }

    private static Map<String, String> populateVariableMap(Map<String, String> dataMap, Map<String, String> variables) {
        final HashMap<String, String> newVariableMap = new HashMap<>(variables);
        newVariableMap.putAll(createVariables(dataMap));
        return newVariableMap;
    }

    private static Map<? extends String, ? extends String> createVariables(Map<String, String> dataMap) {
        return dataMap.entrySet().stream()
                .filter(e -> !isNullOrEmpty(e.getValue()))
                .map(e -> new SimpleEntry<>(e.getValue().split("\\|")[0], e.getValue().split("\\|")[1]))
                .collect(toMap(Entry:: getKey, Entry:: getValue));
    }

    private static boolean isSkipRow(CSVRecord csvRecord) {
        return csvRecord.get(0).equals("#");
    }

    public static Flowable<CSVRecord> readRecordsFromFile(Path inputFile, CSVFormat csvFormat) {
        return Flowable.using(() -> Files.newBufferedReader(inputFile),
                bufferedReader -> csvRecordFlowable(bufferedReader, csvFormat.withHeader()),
                BufferedReader::close
        );
    }

    private static Flowable<CSVRecord> csvRecordFlowable(BufferedReader br, CSVFormat csvFormat) {
        try{
            final CSVParser csvParser = new CSVParser(br,csvFormat);
            return Flowable.fromIterable(() -> csvParser.iterator());
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
