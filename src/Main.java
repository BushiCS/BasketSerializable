import java.util.Scanner;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String[] products = new String[]{"Хлеб", "Яблоки", "Молоко", "Сахар", "Соль"};
        int[] prices = new int[]{50, 20, 80, 60, 30};
        int[] purchasesCount = new int[products.length];

        String[] saleProducts = new String[]{"Яйца", "Масло", "Гречка"};
        int[] salePrices = new int[]{80, 100, 90};
        int[] salePurchasesCount = new int[saleProducts.length];

        out.println("Список возможных товаров для покупки");
        for (int i = 0; i < products.length; i++) {
            out.println((i
                    + 1)
                    + ". "
                    + products[i]
                    + " "
                    + prices[i]
                    + " руб/шт");
        }
        out.println("Список товаров по акции");
        for (int i = 0; i < saleProducts.length; i++) {
            out.println(i
                    + (products.length + 1)
                    + ". "
                    + saleProducts[i]
                    + " "
                    + salePrices[i]
                    + " руб/шт");
        }
        int sumProducts = 0;
        int productNumber;
        int productCount;
        int sum;
        int saleSum;
        while (true) {
            out.println("Выберите товар и количество или введите `end` "); // ВЫХОД
            String input = sc.nextLine();
            if (input.equals("end")) {
                break;
            }
            String[] parts = input.split(" "); // РАЗДЕЛЕНИЕ
            if (parts.length != 2) {
                out.println("Некорректный ввод 2 значений, нужно ввести 2 числа");
                continue;
            }
            try {
                productNumber = Integer.parseInt(parts[0]) - 1; // ВВОД
                productCount = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                out.println("Неверный формат ввода");
                continue;
            }

            if ((productNumber > (products.length + saleProducts.length) - 1 || productNumber < 0)) {
                out.println("Неверный продукт");
                continue;
            }
            if (productNumber < products.length) { // 1 массив
                if (productCount == 0) {
                    purchasesCount[productNumber] = 0;
                    out.println("Вы полностью убрали из корзины: " + products[productNumber]);
                    continue;
                } else if (productCount < 0) {
                    purchasesCount[productNumber] += productCount;
                    if (purchasesCount[productNumber] < 0) {
                        purchasesCount[productNumber] = 0;
                        out.println("Вы полностью убрали из корзины: " + products[productNumber]);
                    } else {
                        out.println("Количество продукта " + products[productNumber] + " уменьшено на " + productCount + " шт.");
                    }
                    continue;
                }
                out.println(products[productNumber]
                        + " "
                        + (productCount + purchasesCount[productNumber])
                        + " шт.");
                purchasesCount[productNumber] += productCount;
            } else { // 2 массив (скидки)
                int saleProductNumber = productNumber - products.length;
                if (productCount == 0) {
                    salePurchasesCount[saleProductNumber] = 0;
                    out.println("Вы полностью убрали из корзины: " + saleProducts[saleProductNumber]);
                    continue;
                } else if (productCount < 0) {
                    salePurchasesCount[saleProductNumber] += productCount;
                    if (salePurchasesCount[saleProductNumber] < 0) {
                        salePurchasesCount[saleProductNumber] = 0;
                        out.println("Вы полностью убрали из корзины: " + saleProducts[saleProductNumber]);
                    } else {
                        out.println("Количество продукта " + saleProducts[saleProductNumber] + " уменьшено на " + productCount + " шт.");
                    }
                    continue;
                }
                out.println(saleProducts[saleProductNumber]
                        + " "
                        + (productCount + salePurchasesCount[saleProductNumber])
                        + " шт. ");
                salePurchasesCount[saleProductNumber] += productCount;
            }
        }
        out.println("Ваша корзина: ");
        for (int i = 0; i < products.length; i++) {
            if (purchasesCount[i] != 0) {
                sum = prices[i] * purchasesCount[i];
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
        for (int i = 0; i < saleProducts.length; i++) {
            if (salePurchasesCount[i] != 0) {
                saleSum = (2 * (salePurchasesCount[i] / 3) + salePurchasesCount[i] % 3) * salePrices[i];
                sumProducts += saleSum;
                out.println(saleProducts[i]
                        + " - "
                        + salePurchasesCount[i]
                        + " шт. "
                        + "("
                        + salePrices[i]
                        + "руб/шт): "
                        + saleSum
                        + " руб в сумме");
            }
        }
        out.println("Итого: " + sumProducts + " руб");
    }
}

