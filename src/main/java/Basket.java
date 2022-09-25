import com.fasterxml.jackson.databind.ObjectMapper;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import java.io.*;
import java.util.Arrays;
import static java.lang.System.out;

public class Basket {
    private int sumProducts;
    private int[] prices;
    private String[] products;
    private int[] purchasesCount;

    public Basket() {
    }

    public Basket(int[] prices, String[] products) {
        this.prices = prices;
        this.products = products;
        purchasesCount = new int[prices.length];
    }

    public void printCatalog() { // вывод товаров
        out.println("Список возможных товаров для покупки");
        for (int i = 0; i < products.length; i++) {
            out.println((i + 1)
                    + ". "
                    + products[i]
                    + " "
                    + prices[i]
                    + " руб/шт "
                    + "(в корзине "
                    + purchasesCount[i]
                    + " шт)");
        }
    }

    public void addToCart(int productNumber, int productCount) { // добавление товара в корзину
        out.println(products[productNumber] + " " + (productCount + purchasesCount[productNumber]) + " шт. (по цене " + prices[productNumber] + " руб. за штуку)");
        purchasesCount[productNumber] += productCount;
        out.println("Сумма: " + (prices[productNumber] * purchasesCount[productNumber]) + " руб.");
    }

    public void printCart() { // подсчет и вывод всей корзины
        out.println("Ваша корзина: ");
        for (int i = 0; i < purchasesCount.length; i++) {
            if (purchasesCount[i] != 0) {
                int sum = prices[i] * purchasesCount[i];
                sumProducts += sum;
                out.println(products[i]
                        + " - "
                        + purchasesCount[i]
                        + " шт. "
                        + "("
                        + prices[i]
                        + " руб/шт): "
                        + sum
                        + " руб в сумме");
            }
        }
        out.println("Итого: " + sumProducts + " руб");
    }

    public void saveTxt(File textFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(textFile))) {
            for (int i = 0; i < products.length; i++) {
                writer.write(products[i] + " " + purchasesCount[i] + " " + prices[i] + " ");
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Basket loadFromTxtFile(File textFile) {
        try (BufferedReader reader = new BufferedReader(new BufferedReader(new FileReader(textFile)))) {
            if (reader.ready()) {
                String[] readerArray = reader.lines().toArray(String[]::new); // разбив на массив строк
                String[] productsLoad = new String[readerArray.length];
                String[] countLoad = new String[readerArray.length];
                String[] pricesLoad = new String[readerArray.length];
                for (int i = 0; i < readerArray.length; i++) {
                    String[] stringSplitArray = readerArray[i].split(" "); // разделение каждой строки [ячейки массива] (на каждой итерации) в массив
                    productsLoad[i] = stringSplitArray[0];  // присвоение в нужный массив нужных данных
                    countLoad[i] = stringSplitArray[1];
                    pricesLoad[i] = stringSplitArray[2];
                }
                int[] intCountLoad = Arrays.stream(countLoad).mapToInt(Integer::parseInt).toArray();
                int[] intPricesLoad = Arrays.stream(pricesLoad).mapToInt(Integer::parseInt).toArray();
                Basket basketLoad = new Basket(intPricesLoad, productsLoad);
                basketLoad.purchasesCount = intCountLoad;
                return basketLoad;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int[] getPurchasesCount() {
        return purchasesCount;
    }

    public int[] getPrices() {
        return prices;
    }

    public String[] getProducts() {
        return products;
    }

    public int getSumProducts() {
        return sumProducts;
    }

    public static Basket loadWithXML(Node root, File jsonFile, File backupFile) {
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) { // перебор по деревьям-детям
            if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String enabled = "";
            String fileName = "";
            String format = "";
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("load")) {
                NodeList loadNodes = node.getChildNodes();
                for (int j = 0; j < loadNodes.getLength(); j++) {
                    if (loadNodes.item(j).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    switch (loadNodes.item(j).getNodeName()) {
                        case "enabled" -> enabled = loadNodes.item(j).getTextContent();
                        case "fileName" -> fileName = loadNodes.item(j).getTextContent();
                        case "format" -> format = loadNodes.item(j).getTextContent();
                    }
                }
                if (enabled.equals("true")) {
                    if (fileName.equals("basket.json")) {
                        if (format.equals("json")) {
                            return loadJson(jsonFile);
                        }
                    } else if (fileName.equals("basket.txt")) {
                        if (format.equals("txt")) {
                            return loadFromTxtFile(backupFile);
                        }
                    }
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public void saveWithXML(Node root, File jsonFile, File txtFile, Basket basket) {
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) { // перебор по деревьям-детям
            if (nodeList.item(i).getNodeType() != Node.ELEMENT_NODE) {
                continue;
            }
            String enabled = "";
            String fileName = "";
            String format = "";
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("save")) {
                NodeList loadNodes = node.getChildNodes();
                for (int j = 0; j < loadNodes.getLength(); j++) {
                    if (loadNodes.item(j).getNodeType() != Node.ELEMENT_NODE) {
                        continue;
                    }
                    switch (loadNodes.item(j).getNodeName()) {
                        case "enabled" -> enabled = loadNodes.item(j).getTextContent();
                        case "fileName" -> fileName = loadNodes.item(j).getTextContent();
                        case "format" -> format = loadNodes.item(j).getTextContent();
                    }
                }
                if (enabled.equals("true")) {
                    if (fileName.equals("basket.json")) {
                        if (format.equals("json")) {
                            saveJSON(jsonFile, basket);
                        }
                    } else if (fileName.equals("basket.txt")) {
                        if (format.equals("txt")) {
                            saveTxt(txtFile);
                        }
                    }
                }
            }
        }
    }

    public static Basket loadJson(File jsonFile) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonFile, Basket.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void saveJSON(File jsonFile, Basket basket) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.writeValue(jsonFile, basket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


