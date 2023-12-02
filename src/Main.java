import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Bank bank = new Bank();
        try {
            System.out.println("\n**********************Welcome to our Bank*************************");
            Bank.mainMenu();
        }
         catch (Exception e) {
            System.out.println("\nWe triggered an Error");
        }
    }
}
