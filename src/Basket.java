import java.io.*;
import java.util.Arrays;
import static java.lang.System.out;

public class Basket implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private int sumProducts;
    private final int[] prices;
    private final String[] products;
    private final int[] purchasesCount;

    public Basket(int[] prices, String[] products) {
        this.prices = prices;
        this.products = products;
        purchasesCount = new int[prices.length];
    }

    public void printCatalog() { // вывод достпных для покупки товаров
        out.println("Список возможных товаров для покупки");
        for (int i = 0; i < products.length; i++) {
            out.println((i + 1)
                    + ". "
                    + products[i]
                    + " "
                    + prices[i]
                    + " руб/шт " + "(в корзине " + purchasesCount[i] + " шт)");
        }
    }

    public void addToCart(int productNumber, int productCount) { // добавление товара в корзину
        out.println(products[productNumber]
                + " "
                + (productCount + purchasesCount[productNumber])
                + " шт. (по цене " + prices[productNumber]
                + " руб. за штуку)");
        purchasesCount[productNumber] += productCount; // общее кол-во взятого товара[номер выбранного товара] += кол-во выбранного товара
        out.println("Сумма: "
                + (prices[productNumber] * purchasesCount[productNumber])
                + " руб.");
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

    public int[] getPurchasesCount() {
        return purchasesCount;
    }

    public void saveBin(File file) {
        try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file))) {
            writer.writeObject(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Basket loadFromBinFile(File file) {
        try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
            return (Basket) reader.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "Basket{" +
                "sumProducts=" + sumProducts +
                ", prices=" + Arrays.toString(prices) +
                ", products=" + Arrays.toString(products) +
                ", purchasesCount=" + Arrays.toString(purchasesCount) +
                '}';
    }
}


