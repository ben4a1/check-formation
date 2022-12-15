package by.paramonov.entity;


import by.paramonov.Main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Класс для создания и вывода чека в магазине
 * vat - НДС
 * discount - скидка (0.1 - 10%) при покупке @quantityForDiscount единиц товара
 */
public class Check {
    static double vat = 20;
    static double totalPrice = 0;

    //discount if quantity product > 'quantityForDiscount'
    static double quantityDiscount = 0.1;

    //discount for cardholders
    static double cardDiscount = 0.03;

    //count of product for use 'quantityDiscount'
    static int quantityForDiscount = 5;
    static String cardHolder = "Michael Jackson";
    static Map<Integer, List<String>> priceList = new HashMap<>();

    static {
        priceList.putAll(Main.priceList);
    }

    private DiscountCard discountCard;
    private List<String> orderList;

    public Check(DiscountCard discountCard) {
        this.discountCard = discountCard;
    }

    public Map<Integer, Integer> parseInputArgs(String[] inputArgs) {
        //HashMap with Integer idProduct and Integer countProduct
        Map<Integer, Integer> parsedMap = new HashMap<>();
        if (inputArgs.length != 0) {
            // Check first char in inputArgs - if digit -> product with id and count, else -> card
            for (int i = 0; i < inputArgs.length; i++) {
                String[] split = inputArgs[i].split("-");
                if (Character.isDigit(inputArgs[i].charAt(0))) {
                    int tempId = Integer.parseInt(split[0]);
                    int tempCount = Integer.parseInt(split[1]);
                    // If map already contains key (idProduct) - count increases
                    if (!parsedMap.containsKey(tempId)) {
                        parsedMap.put(tempId, tempCount);
                    } else {
                        int existingCount = parsedMap.get(tempId);
                        parsedMap.put(tempId, tempCount + existingCount);
                    }

                } else if (inputArgs[i].startsWith("card")) {
                    discountCard = new DiscountCard(Long.parseLong(split[1]), cardHolder, cardDiscount);
                }
            }
        }
        return parsedMap;
    }

    public void printCheck(String[] inputArgs) {

        if (inputArgs.length != 0) {
            System.out.println("CASH RECEIPT");
            System.out.println("=========================================");
            System.out.println("QTY"
                    + "\tDESCRIPTION"
                    + "\t\t\tPRICE"
                    + "\t\tTOTAL");
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
