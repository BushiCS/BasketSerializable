import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {

    private List<String[]> logList = new ArrayList<>();

    public void log(int productNumber, int productCount) {
        logList.add(new String[]{String.valueOf(productNumber),String.valueOf(productCount)});
    }

    public void exportAsCSV(File textFile) {
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(textFile, true))) {
            for (String[] strings : logList) {
                csvWriter.writeNext(strings);
            }
            } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<String[]> getLogList() {
        return logList;
    }
}
