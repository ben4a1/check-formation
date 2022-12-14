package by.paramonov.service;


import by.paramonov.CheckRunner;
import by.paramonov.entity.DiscountCard;
import lombok.NoArgsConstructor;

import java.io.*;
import java.util.*;

/**
 * Класс для создания и вывода чека в магазине
 * vat - НДС
 * discount - скидка (0.1 - 10%) при покупке @quantityForDiscount единиц товара
 */
@NoArgsConstructor
public class CheckService {
    private static final String INPUT_PRICE_FILE_PATH = "src/main/resources/price.txt";
    private static final String OUTPUT_CHECK_FILE_PATH = "src/main/resources/check.txt";
    private static final File INPUT_PRICE_FILE = new File(INPUT_PRICE_FILE_PATH);
    private static final File OUTPUT_CHECK_FILE = new File(OUTPUT_CHECK_FILE_PATH);
    static final String CHECK_START = """
            \t\t\tCASH RECEIPT
            =========================================
            QTY\tDESCRIPTION\t\t\tPRICE\t\tTOTAL""";
    static double vat = 20;

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

    static Map<Integer, String[]> priceListFromFile = new HashMap<>();

    static {
        try {
            FileReader fr = new FileReader(INPUT_PRICE_FILE);
            Scanner scanner = new Scanner(fr);
            if (scanner.hasNextLine()) {
                String[] split = scanner.nextLine().split(" ");
                int id = Integer.parseInt(split[0]);
                String[] priceAndDescription = new String[]{split[1], split[2]};
                priceListFromFile.put(id, priceAndDescription);
                while (scanner.hasNextLine()) {
                    split = scanner.nextLine().split(" ");
                    id = Integer.parseInt(split[0]);
                    priceAndDescription[0] = split[1];
                    priceAndDescription[1] = split[2];
                    priceListFromFile.put(id, priceAndDescription);
                }
            }
            fr.close();
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }

    private double totalPrice = 0;
    private DiscountCard discountCard;

    // HashMap with Integer idProduct and Integer countProduct
    private final Map<Integer, Integer> orderMap = new HashMap<>();
    private List<String> orderList;
    private List<String[]> orderListArray;

    public CheckService(DiscountCard discountCard) {
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


    /**
     * Method for parse input args[] and fill orderListArray
     */
    public void setOrderListArray(String[] inputArgs) {
        orderListArray = new ArrayList<>();
        if (inputArgs.length != 0) {
            for (int i = 0; i < inputArgs.length; i++) {
                String[] split = inputArgs[i].split("-");
                orderListArray.add(split);
            }
        }
    }

    public void setOrderList() {
        orderList = new LinkedList<>();
        orderListArray.forEach(x -> {
            try {
                int id = Integer.parseInt(x[0]);
                int quantity = Integer.parseInt(x[1]);
                double price = Double.parseDouble(priceListFromFile.get(id)[0]);
                String description = priceListFromFile.get(id)[1];
                setTempTotal(quantity, price, description);
            } catch (NullPointerException nullPointerException) {
                System.out.printf("Product with ID='%s' does not exist yet", x[0]);
            } catch (NumberFormatException nfe) {
                if (x[0].equalsIgnoreCase("card")) {
                    discountCard = new DiscountCard(Long.parseLong(x[1]), cardHolder, cardDiscount);
                }
            }
        });
    }

    private void setTempTotal(int quantity, double price, String description) {
        double total = quantity * price;
        if (quantity > quantityForDiscount) {
            total -= (total * quantityDiscount);
        }
        if (discountCard != null) {
            total -= (total * discountCard.getDiscountValue());
        }
        totalPrice += total;
        orderList.add(String.format("%d %s %.2f %.2f", quantity, description, price, total));
    }

    /**
     * Formation of positions in the check from List
     */
    private void createCheckPositions() {
        orderList = new LinkedList<>();
        orderMap.forEach((key, value) -> {
            try {
                int quantity = value;
                String description = priceList.get(key).get(1);
                double price = Double.parseDouble(priceList.get(key).get(0));
                setTempTotal(quantity, price, description);
            } catch (NullPointerException nullPointerException) {
                System.out.printf("Product with ID='%d' does not exist yet", key);
            }
        });
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

    public void printCheckToFile() {
        createCheckPositions();
        double vatValue = totalPrice * vat * 0.01;
        double totalPriceWithVatValue = totalPrice + vatValue;

        try {
            FileWriter fw = new FileWriter(OUTPUT_CHECK_FILE);
            fw.write(CHECK_START);
            orderList.forEach(x -> {
                String[] tempStr = x.split(" ");
                try {
                    fw.write(String.format("%n%s" + "\t%s" + "\t\t\t\t%s" + "\t\t%s", tempStr[0], tempStr[1], tempStr[2], tempStr[3]));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            fw.write("\n=========================================");
            fw.write(String.format("%nTAXABLE TOT.\t\t\t\t\t\t$%.2f", totalPrice));
            fw.write(String.format("%nVAT%2.0f%%\t\t\t\t\t\t\t\t$%.2f%n", vat, vatValue));
            fw.write(String.format("TOTAL\t\t\t\t\t\t\t\t$%.2f%n", totalPriceWithVatValue));
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
                    List<String> strings = CheckRunner.priceList.get(Integer.valueOf(split[0]));
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
