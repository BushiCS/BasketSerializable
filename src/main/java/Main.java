import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        Scanner sc = new Scanner(System.in);
        String[] products = new String[]{"Хлеб", "Яблоки", "Молоко"};
        int[] prices = new int[]{50, 20, 80};
        File txtFile = new File("basket.txt");
        File jsonFile = new File("basket.json");
        Basket basket;
        ClientLog purchasesLog = new ClientLog();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); // Создается построитель документа
        DocumentBuilder builder = factory.newDocumentBuilder(); // Создается дерево DOM документа из файла
        Document doc = builder.parse(new File("shop.xml"));
        Node root = doc.getDocumentElement(); // Получаем корневой элемент

        if (jsonFile.exists() || txtFile.exists()) { // если файлы для считывания существуют
            if (Basket.loadWithXML(root, jsonFile, txtFile) != null) { // если метод не возвращает null
                basket = Basket.loadWithXML(root, jsonFile, txtFile); // то работаем с возвращенной корзиной
            } else { // если метод вернул null
                basket = new Basket(prices, products); // то создаем новую корзину
            }
        } else { // если файлы не существуют
            basket = new Basket(prices, products); // то создаем новую корзину
        }

        assert basket != null;
        basket.printCatalog();
        while (true) {
            System.out.println("Выберите товар и количество или введите `end` ");
            String input = sc.nextLine();
            if (input.equals("end")) {
                break;
            }
            String[] parts = input.split(" ");
            int productNumber = Integer.parseInt(parts[0]) - 1;
            int productCount = Integer.parseInt(parts[1]);
            purchasesLog.log(productNumber, productCount); // добавление данных в лог
            if (productCount == 0) {
                basket.getPurchasesCount()[productNumber] = 0;
                out.println("Продукт обнулен");
            }
            basket.addToCart(productNumber, productCount); // добавление товара и его кол-во в корзину
        }
        basket.printCart(); // подсчет и вывод корзины
        purchasesLog.logWithXML(root, new File("log.csv"));
        basket.saveWithXML(root, jsonFile, txtFile, basket);
    }
}