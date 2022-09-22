import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] products = new String[]{"Хлеб", "Яблоки", "Молоко"};
        int[] prices = new int[]{50, 20, 80};
        File backupFile = new File("basket.txt");
        File jsonFile = new File("basket.json");
        Basket basket;
        ClientLog purchasesLog = new ClientLog();
        ObjectMapper mapper = new ObjectMapper();

        if (jsonFile.exists()) {
            try {
                basket = mapper.readValue(jsonFile, Basket.class);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        } else {
            basket = new Basket(prices, products);
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
        purchasesLog.exportAsCSV(new File("log.csv"));
        try {
            mapper.writeValue(jsonFile, basket);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}