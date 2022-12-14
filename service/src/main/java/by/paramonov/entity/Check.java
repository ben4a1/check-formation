package by.paramonov.entity;


import by.paramonov.Main;

import java.util.List;

public class Check {
    static double vat = 20;
    static double totalPrice = 0;
    static double discount = 0.01;

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
                    if (quantity > 5) {
                        total *= discount;
                    }
                    totalPrice += total;
                    String description = strings.get(1);
                    System.out.println(quantity + "\t" + description + "\t\t\t\t$" + price + "\t\t$" + total);
                }
            }
            System.out.println("=========================================");
        }
    }
}
