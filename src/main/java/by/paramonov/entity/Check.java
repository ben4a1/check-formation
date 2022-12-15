package by.paramonov.entity;


import by.paramonov.Main;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * Класс для создания и вывода чека в магазине
 * vat - НДС
 * discount - скидка (0.1 - 10%) при покупке @quantityForDiscount единиц товара
 */
@NoArgsConstructor
public class Check {
    private static final File INPUT_PRICE_FILE = new File("src/main/resources/price.txt");
    private static final File OUTPUT_CHECK_FILE = new File("src/main/resources/check.txt");
    static final String CHECK_START = """
            \t\t\tCASH RECEIPT
            =========================================
            QTY\tDESCRIPTION\t\t\tPRICE\t\tTOTAL""";
    static double vat = 20;
    static double totalPrice = 0;

    // discount if quantity product > 'quantityForDiscount'
    static double quantityDiscount = 0.1;

    // discount for cardholders
    static double cardDiscount = 0.03;

    // count of product for use 'quantityDiscount'
    static int quantityForDiscount = 5;
    static String cardHolder = "Michael Jackson";
    static Map<Integer, List<String>> priceList = new HashMap<>();

    static {
        priceList.put(1, new LinkedList<>(Arrays.asList("24.2", "Milk")));
        priceList.put(2, new LinkedList<>(Arrays.asList("35.2", "Cheese")));
        priceList.put(3, new LinkedList<>(Arrays.asList("2.3", "Bread")));
        priceList.put(4, new LinkedList<>(Arrays.asList("154.99", "Spear")));
        priceList.put(5, new LinkedList<>(Arrays.asList("1.0", "Button")));
    }

    private DiscountCard discountCard;

    // HashMap with Integer idProduct and Integer countProduct
    private final Map<Integer, Integer> orderMap = new HashMap<>();
    private List<String> orderList;

    public Check(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    /**
     * @param inputArgs - parameter set from cmd
     */
    public void setOrderMapAndDiscountCardIfExists(String[] inputArgs) {
        if (inputArgs.length != 0) {
            // Check first char in inputArgs - if digit -> product with id and count
            for (int i = 0; i < inputArgs.length; i++) {
                String[] split = inputArgs[i].split("-");
                if (Character.isDigit(split[0].charAt(0))) {
                    int tempId = Integer.parseInt(split[0]);
                    int tempCount = Integer.parseInt(split[1]);
                    // If map already contains key (idProduct) - count increases
                    if (!orderMap.containsKey(tempId)) {
                        orderMap.put(tempId, tempCount);
                    } else {
                        int existingCount = orderMap.get(tempId);
                        orderMap.put(tempId, tempCount + existingCount);
                    }
                }
                // else -> card with number/id
                else if (split[0].equalsIgnoreCase("card")) {
                    discountCard = new DiscountCard(Long.parseLong(split[1]), cardHolder, cardDiscount);
                }
            }
        }
    }

    // Formation of positions in the check from List
    private void createCheckPositions() {
        orderList = new LinkedList<>();
        orderMap.forEach((key, value) -> {
            try {
                int quantity = value;
                String description = priceList.get(key).get(1);
                double price = Double.parseDouble(priceList.get(key).get(0));
                double total = quantity * price;
                if (quantity > quantityForDiscount) {
                    total -= (total * quantityDiscount);
                }
                if (discountCard != null) {
                    total -= (total * discountCard.getDiscountValue());
                }
                totalPrice += total;
                orderList.add(String.format("%d %s %.2f %.2f", quantity, description, price, total));
            } catch (NullPointerException nullPointerException) {
                nullPointerException.printStackTrace(); //TODO else Exception (ParseException)
            }
        });
    }

    private void createCheckPositionsFromFileSource(){

    }

    public void printCheck() {
        createCheckPositions();
        double vatValue = totalPrice * vat * 0.01;
        double totalPriceWithVatValue = totalPrice + vatValue;
        System.out.println(CHECK_START);
        orderList.forEach(x -> {
            String[] tempStr = x.split(" ");
            System.out.printf("%n%s" + "\t%s" + "\t\t\t\t%s" + "\t\t%s", tempStr[0], tempStr[1], tempStr[2], tempStr[3]);
        });
        System.out.printf("%n=========================================");
        System.out.printf("%nTAXABLE TOT.\t\t\t\t\t\t$%.2f", totalPrice);
        System.out.printf("%nVAT%2.0f%%\t\t\t\t\t\t\t\t$%.2f%n", vat, vatValue);
        System.out.printf("TOTAL\t\t\t\t\t\t\t\t$%.2f%n", totalPriceWithVatValue);
    }

    public void printCheckToFile(){
        createCheckPositions();
        double vatValue = totalPrice * vat * 0.01;
        double totalPriceWithVatValue = totalPrice + vatValue;

        try {
            @SuppressWarnings("resource") FileWriter fw = new FileWriter(OUTPUT_CHECK_FILE);
            fw.write(CHECK_START);
            orderList.forEach(x -> {
                String[] tempStr = x.split(" ");
                try {
                    fw.write(String.format("%n%s" + "\t%s" + "\t\t\t\t%s" + "\t\t%s", tempStr[0], tempStr[1], tempStr[2], tempStr[3]));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Deprecated
    public void printCheck(String[] inputArgs) {

        if (inputArgs.length != 0) {
            System.out.println(CHECK_START);
            for (int i = 0; i < inputArgs.length; i++) {
                if (Character.isDigit(inputArgs[i].charAt(0))) {
                    String[] split = inputArgs[i].split("-");
                    List<String> strings = Main.priceList.get(Integer.valueOf(split[0]));
                    int quantity = Integer.parseInt(split[1]);
                    double price = Double.parseDouble(strings.get(0));
                    double total = quantity * price;
                    if (quantity > quantityForDiscount) {
                        total -= (total * quantityDiscount);
                    } else if (discountCard != null) {
                        total -= (total * discountCard.getDiscountValue() * 0.01);
                    }
                    totalPrice += total;
                    String description = strings.get(1);
                    System.out.printf("%n%d" + "\t%s" + "\t\t\t\t$%.2f" + "\t\t$%.2f", quantity, description, price, total);
                }
            }
            double vatValue = totalPrice * vat * 0.01;
            double totalPriceWithVatValue = totalPrice + vatValue;
            System.out.println("\n=========================================");
            System.out.printf("%nTAXABLE TOT.\t\t\t\t\t\t$%.2f", totalPrice);
            System.out.printf("%nVAT%2.0f%%\t\t\t\t\t\t\t\t$%.2f%n", vat, vatValue);
            System.out.printf("TOTAL\t\t\t\t\t\t\t\t$%.2f%n", totalPriceWithVatValue);
        }
    }
}
