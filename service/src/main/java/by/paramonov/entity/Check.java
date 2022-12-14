package by.paramonov.entity;


public class Check {

    public void printCheck(String[] inputArgs) {
        if (inputArgs.length != 0) {
            System.out.println("CASH RECEIPT");
            System.out.println("================");
            System.out.println("QTY"
                    + "\tDESCRIPTION"
                    + "\tPRICE"
                    + "\tTOTAL");
            for (int i = 0; i < inputArgs.length; i++) {
                if (Character.isDigit(inputArgs[i].charAt(0))) {
                    String[] split = inputArgs[i].split("-");
                    System.out.println(split[0]);
                }
            }
        }
    }
}
