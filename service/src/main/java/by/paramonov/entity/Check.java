package by.paramonov.entity;


import by.paramonov.Main;

import java.util.List;

/**
 * Класс для создания и вывода чека в магазине
 * vat - НДС
 * discount - скидка (0.1 - 10%) при покупке @quantityForDiscount единиц товара
 */
public class Check {
    static double vat = 20;
    static double totalPrice = 0;
    static double discount = 0.1;
    static int quantityForDiscount = 5;

    private final DiscountCard discountCard;

    public Check(DiscountCard discountCard) {
        this.discountCard = discountCard;
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
                        total -= (total * discount);
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
