package by.paramonov;

import by.paramonov.service.CheckService;
import by.paramonov.entity.DiscountCard;

import java.util.*;

public class CheckRunner {
    public static Map<Integer, List<String>> priceList = new HashMap<>();
    static {
        priceList.put(1, new LinkedList<>(Arrays.asList("24.2", "Milk")));
        priceList.put(2, new LinkedList<>(Arrays.asList("35.2", "Cheese")));
        priceList.put(3, new LinkedList<>(Arrays.asList("2.3", "Bread")));
        priceList.put(4, new LinkedList<>(Arrays.asList("154.99", "Spear")));
        priceList.put(5, new LinkedList<>(Arrays.asList("1.0", "Button")));
    }

    public static void main(String[] args) {

//        System.out.println(priceList.get(3));
//        DiscountCard discountCard = new DiscountCard((long)(Math.random() * 1_000_000), "Consumer Albertovich", 3);
//        CheckService check = new CheckService(discountCard);
//        String[] argsForDebug = new String[]{"3-5", "4-25", "1-4", "2-5"};
//        check.printCheck(argsForDebug);
//
        String[] argsForDebug2 = new String[]{"3-5", "4-25", "1-4", "2-5", "card-14239999"};
        CheckService check2 = new CheckService();
//        check2.setOrderMapAndDiscountCardIfExists(argsForDebug2);
//        check2.printCheck();
//        check2.printCheckToFile();
        check2.setOrderListArray(argsForDebug2);
        check2.setOrderList();
        check2.printCheckToFile();


        System.out.println();
    }
}