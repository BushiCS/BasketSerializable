import au.com.bytecode.opencsv.CSVWriter;
import org.w3c.dom.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {

    private final List<String[]> logList = new ArrayList<>();

    public void log(int productNumber, int productCount) {
        logList.add(new String[]{String.valueOf(productNumber), String.valueOf(productCount)});
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

    public void logWithXML(Node root, File fileCSV) {
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) { // перебор по деревьям-детям
            if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String enabled = "";
            String fileName = "";
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("log")) {
                NodeList loadNodes = node.getChildNodes();
                for (int j = 0; j < loadNodes.getLength(); j++) {
                    if (loadNodes.item(j).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    switch (loadNodes.item(j).getNodeName()) {
                        case "enabled" -> enabled = loadNodes.item(j).getTextContent();
                        case "fileName" -> fileName = loadNodes.item(j).getTextContent();
                    }
                }
                if (enabled.equals("true")) {
                    if (fileName.equals("log.csv")) {
                        exportAsCSV(fileCSV);
                    }
                }
            }
        }
    }

    public List<String[]> getLogList() {
        return logList;
    }
}
