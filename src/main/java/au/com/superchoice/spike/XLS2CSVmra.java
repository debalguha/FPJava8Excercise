package au.com.superchoice.spike;

import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.PrintStream;

public class XLS2CSVmra implements HSSFListener {
    private int minColumns;
    private POIFSFileSystem fs;
    private PrintStream output;


    @Override
    public void processRecord(Record record) {

    }
}
