package by.paramonov;

import by.paramonov.entity.Check;

import java.util.*;

public class Main {

    public static void main(String[] args) {
        Map<Integer, List<String>> priceList = new HashMap<>();
        priceList.put(1, new LinkedList<>(Arrays.asList("24.2", "milk")));
        priceList.put(2, new LinkedList<>(Arrays.asList("35.2", "cheese")));
        priceList.put(3, new LinkedList<>(Arrays.asList("2.3", "bread")));
        priceList.put(4, new LinkedList<>(Arrays.asList("154.99", "spear")));
        priceList.put(5, new LinkedList<>(Arrays.asList("1.0", "button")));

        System.out.println(priceList.get(3));

        Check check = new Check();
        check.printCheck(args);
    }
}