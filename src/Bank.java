import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLOutput;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

class Bank {
    static ArrayList<AccountHolder> AL = new ArrayList<>();
    static int account_number;
    static int accountNumber_login;

    public static void mainMenu() {
        Scanner input = new Scanner(System.in);
        System.out.println("\n1 - Create new Account");
        System.out.println("2 - Login");
        System.out.println("3 - Exit");
        System.out.print("Enter your choice: ");
        int choose;
        while (true) {
            choose = input.nextInt();
            switch (choose) {
                case 1:
                    addNewRecord();
                    break;
                case 2:
                    Login();
                    break;
                case 3:
                    System.out.println("Bye Bye...");
                    accountNumber_login = 0;
                    saveArrayListToFile("BankRecord.txt");
                    System.exit(1);
                default:
                    System.out.println("\nWrong Input");
                    mainMenu();

            }
        }
    }

    public static void addNewRecord () {
        int number_of_accounts = 0;
        boolean account_match = false;
        Scanner input = new Scanner(System.in);

        System.out.print("\nPlease enter your name: ");
        String n = input.nextLine();
        if (n.matches("\\d+")) { // to validate if name has any digit
            System.out.print("--------------ERROR-----------------\n");
            System.out.print("Name should not contain any digit.\n");
            System.out.print("-------------------------------------\n");
            mainMenu();
        } else {
            System.out.print("Enter an 8 digit desired Account Number (Must be digits and no Alphabets.): ");
            int a = input.nextInt();
            if (String.valueOf(a).length() == 8) { // to validate the length of the account number
                // to validate if the account number already existed or not.
                for (AccountHolder accountHolder : AL) {
                    if (accountHolder.getAccountNumber() == a) {
                        account_match = true;
                        break;
                    }
                }
                if (account_match) {
                    System.out.print("--------------ERROR-----------------\n");
                    System.out.print("Account number already existed!!!\n");
                    System.out.print("-------------------------------------\n");
                    mainMenu();

                } else {
                    number_of_accounts = number_of_accounts + 1;
                    System.out.print("Enter PIN for Account Holder: ");
                    String p = input.next();
                    System.out.print("Default amount of 1000 is already added to the account, to add more money, write that amount else enter zero: ");
                    double am = input.nextDouble();
                    AccountHolder ac = new AccountHolder(n, a, p, am);
                    AL.add(ac);
                    account_number = a;
                    System.out.println("\nAccount Created Successfully\n");
                    saveArrayListToFile("BankRecord.txt");
                    mainMenu();
                }
            } else {
                System.out.print("---------------------ERROR----------------------\n");
                System.out.print("Account number Should be only 8 digit long!!!\n");
                System.out.print("-------------------------------------------------\n");
                mainMenu();
            }

        }
    }


    public static void transfer () {
        Scanner input = new Scanner(System.in);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
        System.out.print("Enter Sender's pin code: ");
        String s_pin = input.next();

        int sender_index = -1;
        for (int i = 0; i < AL.size(); i++) {
            if (AL.get(i).getAccountNumber() == accountNumber_login && AL.get(i).getPIN().equals(s_pin))
                sender_index = i;
        }

        if (sender_index == -1) {
            System.out.print("--------------ERROR-----------------\n");
            System.out.println("\nPin is Incorrect.");
            System.out.print("-------------------------------------\n");
            return;
        }

        System.out.print("\nEnter receiver's 8 digit account number: ");
        int r_acc = input.nextInt();

        int receiver_index = -1;
        for (int i = 0; i < AL.size(); i++) {
            if (AL.get(i).getAccountNumber() == r_acc && AL.get(i).getAccountNumber() != accountNumber_login)
                receiver_index = i;
        }

        if (receiver_index == -1) {
            System.out.print("--------------ERROR-----------------\n");
            System.out.println("\n Receiver's account not Found");
            System.out.print("--------------------------------------\n");
            return;
        }

        System.out.print("\nAmount to be transferred: ");
        double amount = input.nextDouble();
        if (AL.get(sender_index).getAmount() >= amount) {
            AL.get(receiver_index).transfer_receive(amount, AL.get(sender_index).getAccountNumber());
            AL.get(sender_index).transfer_Send(amount, AL.get(receiver_index).getAccountNumber());
            System.out.print("\n" + amount + " is transferred into " + r_acc + " from " + accountNumber_login + ".\n");
            System.out.println("\nLatest Balance: $" + decimalFormat.format(AL.get(sender_index).getAmount()) + "\n");
            saveArrayListToFile("BankRecord.txt");
            return;
        } else {
            System.out.print("-------------------------ERROR----------------------------\n");
            System.out.println("\nSender does not have this much balance in his account");
            System.out.print("-----------------------------------------------------------\n");
            return;
        }
    }

    public static void withdraw () {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter User's pin code: ");
        String p_pin = input.next();

        int person_index = -1;
        for (int i = 0; i < AL.size(); i++) {
            if ((AL.get(i).getAccountNumber() == accountNumber_login) && (AL.get(i).getPIN().equals(p_pin))) {
                person_index = i;
            }
        }

        if (person_index == -1) {
            System.out.print("--------------ERROR-----------------\n");
            System.out.println("Pin is Incorrect.");
            System.out.print("-------------------------------------\n");
            return;
        }

        System.out.print("\nAmount to be Withdrawn: ");
        double amount = input.nextDouble();
        AL.get(person_index).withdraw(amount);
        System.out.println("Previous Balance: $" + (AL.get(person_index).getAmount() + amount));
        System.out.println(amount + " is withdrawn from " + accountNumber_login + " account.");
        System.out.println("Latest Balance: $" + AL.get(person_index).getAmount() + "\n");
        saveArrayListToFile("BankRecord.txt");
        return;
    }

    static ArrayList<FixedDeposit> FDList = new ArrayList<>();

    public static void createFixedDeposit () {
        Scanner input = new Scanner(System.in);
        double interestRate;
        boolean account_match = false;
        System.out.print("\nEnter account number for Fixed Deposit: ");
        int accountNumber = input.nextInt();
        for (AccountHolder accountHolder : AL) {
            if (accountHolder.getAccountNumber() == accountNumber && accountNumber_login == accountNumber) {
                account_match = true;
                break;
            }
        }
        if (account_match) {
            System.out.print("Do you want a Long Term Fixed Deposit (>=12 Months) or Short Term Deposit (<12 months).\n");
            System.out.print("Interest Rate for Long Term Deposit is 3.50%.\n");
            System.out.print("Interest Rate for Short Term Deposit is 1.00%\n ");
            System.out.print("If you break the FD before 3 Months, you will only get the amount you deposited.\n");
            System.out.print("Enter the Duration of Deposit in months:\n");
            int duration = input.nextInt();
            if (duration >= 12) {
                interestRate = 3.50;
                System.out.println("Thank you for choosing Long Term Fixed Deposit.\n");
            } else {
                interestRate = 1.00;
                System.out.println("Thank you for choosing Short Term Fixed Deposit.\n");

            }
            System.out.print("Enter principal amount for Fixed Deposit: ");
            double principal = input.nextDouble();

            FixedDeposit fd = new FixedDeposit(accountNumber, principal, interestRate, duration);
            FDList.add(fd);

            System.out.println("\nFixed Deposit created successfully.\n");
            saveArrayListToFile("BankRecord.txt");
        } else {
            System.out.println("\nProvided Account number is not valid.\n");
        }


    }

    public static void breakFixedDeposit () {
        Scanner input = new Scanner(System.in);
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
        System.out.print("\nEnter account number for the Fixed Deposit to be broken: ");
        int accountNumber = input.nextInt();
        double amount_to_paid = 0;
        int indexToRemove = -1;
        int indexToadd = -1;
        for (int i = 0; i < AL.size(); i++) {
            if (AL.get(i).getAccountNumber() == accountNumber) {
                indexToadd = i;
                break;
            }
        }
        for (int i = 0; i < FDList.size(); i++) {
            if (FDList.get(i).getAccountNumber() == accountNumber && accountNumber_login == accountNumber) {
                indexToRemove = i;
                LocalDate currentDate = LocalDate.now();
                LocalDate creationDate = FDList.get(i).getCreationDate();
                System.out.println("Creation Date: " + creationDate);
                Period period = Period.between(currentDate, creationDate);
                int months = period.getYears() * 12 + period.getMonths();
                System.out.println("Months difference: " + months);
                if (months < 3) {
                    AL.get(indexToadd).setAmount(AL.get(indexToadd).getAmount() + FDList.get(indexToRemove).getPrincipal());
                    amount_to_paid = FDList.get(indexToRemove).getPrincipal();
                    System.out.println("It's been less than 3 months, so you are not getting the interest benefits.\nAnd the amount is deposited in your account.");
                    break;
                } else {
                    AL.get(indexToadd).setAmount(AL.get(indexToadd).getAmount() + FDList.get(indexToRemove).getMaturityAmount());
                    amount_to_paid = FDList.get(indexToRemove).getMaturityAmount();
                    System.out.println("It's been more than 3 months, so you are getting the interest benefits.\nAnd the amount is deposited in your account.");
                    saveArrayListToFile("BankRecord.txt");
                    break;

                }
            }
            else {
                System.out.println("Provided Account number is not valid.");
            }
        }
        if (indexToRemove != -1) {
            FDList.remove(indexToRemove);
            System.out.println("\nFixed Deposit broken successfully.");
            System.out.println("Maturity Amount: $" + decimalFormat.format(amount_to_paid)+ ".\n");
            saveArrayListToFile("BankRecord.txt");
        } else {
            System.out.println("\nFixed Deposit not found for the given account number.");
        }
    }

    public static void print () {
        System.out.println("-----------------Regular Accounts----------------");
        System.out.println("-------------------------------------------------");
        for (AccountHolder account : AL) {
            if (account.getAccountNumber() == accountNumber_login) {
                DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
                System.out.println("\nName: " + account.getName());
                System.out.println("Account Number: " + account.getAccountNumber());
                System.out.println("Balance: $" + decimalFormat.format(account.getAmount()) + "\n");

                // Print transaction history
                ArrayList<Transaction> transactions = account.getTransactions();
                System.out.println("---------------------Transaction History------------------");
                System.out.println("----------------------------------------------------------");
                if (!transactions.isEmpty()) {
                    for (Transaction transaction : transactions) {
                        System.out.println("Type: " + transaction.getType());
                        System.out.println("Amount: $" + decimalFormat.format(transaction.getAmount()));
                        System.out.println("Date: " + transaction.getDate() + "\n");
                    }
                } else {
                    System.out.println("No transaction history.\n");
                }
                System.out.println("----------------------------------------------------------");

                for (FixedDeposit fd : FDList) {
                    System.out.println("---------------------FD Accounts--------------------");
                    System.out.println("----------------------------------------------------");
                    if (fd.getAccountNumber() == accountNumber_login) {
                        Period period = Period.between(fd.getCreationDate(), fd.getMaturityDate());
                        int months = period.getYears() * 12 + period.getMonths();
                        if (months >= 12) {
                            System.out.println("Long Term Policy - for " + months + " months.\n");
                            System.out.println("----------------------------------------------------");
                        } else {
                            System.out.println("Short Term Policy - for " + months + " months.\n");
                            System.out.println("----------------------------------------------------");
                        }
                        System.out.println("Fixed Deposit Account: " + fd.getAccountNumber());
                        System.out.println("Fixed Deposit Account: " + fd.getPrincipal());
                        System.out.println("Maturity Amount: $" + decimalFormat.format(fd.getMaturityAmount()));
                        System.out.println("Creation Date: " + fd.getCreationDate());
                        System.out.println("Maturity Date: " + fd.getMaturityDate() + "\n");
                        System.out.println("----------------------------------------------------");
                    } else {
                        System.out.println("This Account does not have any Fixed Deposit.\n");
                    }

                }
            }

        }
    }


    public static void saveArrayListToFile (String filePath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            int flag_racc = 0;
            writer.write("-------------------Account Information-------------------------\n");
            for (AccountHolder element : AL) {
                flag_racc++;
                writer.write("Account: " + flag_racc);
                writer.newLine();
                writer.write("Name: " + element.getName());
                writer.newLine();
                writer.write("Account Number: ");
                writer.write(String.valueOf(element.getAccountNumber()));
                writer.newLine();
                writer.write("PIN: *******" );
                writer.newLine();
                writer.newLine();
                ArrayList<Transaction> transactions = element.getTransactions();
                writer.write("-------------------Account Transactions--------------------------");
                writer.newLine();
                writer.write("-----------------------------------------------------------------");
                writer.newLine();
                for (Transaction T : transactions){
                    writer.write("Type: " + T.getType());
                    writer.newLine();
                    writer.write("Amount: " + T.getAmount());
                    writer.newLine();
                    writer.write("Date: " + T.getDate());
                    writer.newLine();
                }
//                writer.write("---------------------FD Accounts--------------------");
//                writer.write("----------------------------------------------------");
//                for (FixedDeposit fd : FDList) {
//                    if (AL.get(flag_racc-1).getAccountNumber() == fd.getAccountNumber()) {
//                        writer.newLine();
//                        writer.write("FD Creation Date: ");
//                        writer.write(String.valueOf(fd.getCreationDate()));
//                        writer.newLine();
//                        writer.write("Maturity date of the FD: ");
//                        writer.write(String.valueOf(fd.getMaturityDate()));
//                        writer.newLine();
//                        writer.write("Amount user deposited for the FD: ");
//                        writer.write(String.valueOf(fd.getPrincipal()));
//                        writer.newLine();
//                        writer.write("Amount user will get after the completion of the FD: ");
//                        writer.write(String.valueOf(fd.getMaturityAmount()));
//                        writer.newLine();
//                        writer.newLine();
//                    } else {
//                        writer.write("This Account does not have any Fixed Deposit.\n");
//                    }
//
//                }
//                writer.newLine();// Add a newline for each element
            }
            int flag_fdacc = 0;
            writer.write("---------------------FD Accounts--------------------");
            writer.newLine();
            writer.write("----------------------------------------------------");
            writer.newLine();
            for (FixedDeposit element : FDList) {
                flag_fdacc++;
                writer.write("Account: " + flag_fdacc);
                writer.newLine();
                writer.write("Account Number: ");
                writer.write(String.valueOf(element.getAccountNumber()));
                writer.newLine();
                writer.write("FD Creation Date: ");
                writer.write(String.valueOf( element.getCreationDate()));
                writer.newLine();
                writer.write("Maturity date of the FD: ");
                writer.write(String.valueOf( element.getMaturityDate()));
                writer.newLine();
                writer.write("Amount user deposited for the FD: ");
                writer.write(String.valueOf( element.getPrincipal()));
                writer.newLine();
                writer.write("Amount user will get after the completion of the FD: ");
                writer.write(String.valueOf( element.getMaturityAmount()));
                writer.newLine();
                writer.newLine();// Add a newline for each element
            }
    }
    catch (IOException ex) {
            throw new RuntimeException(ex);
    }
}



    public static void Login() {
        Scanner input = new Scanner(System.in);
        boolean account_match = false;
        String AH_name = null;
        System.out.print("Enter Account Number: ");
        account_number = input.nextInt();
        input.nextLine(); // consume the newline character

        System.out.print("Enter PIN: ");
        String pin = input.nextLine();

        // Check if the provided credentials are valid
        for (AccountHolder accountHolder : AL) {
            if (accountHolder.getAccountNumber() == account_number && Objects.equals(accountHolder.getPIN(), pin)) {
                account_match = true;
                AH_name = accountHolder.getName();
                break;
            }
        }
        if (account_match) {
            System.out.println("\nLogin Successful!\n");
            System.out.println("Welcome " + AH_name +"\n");
            accountNumber_login = account_number;
            int choice;
            while (true) {
                System.out.println("1 - Transfer money from an existing account to another existing account");
                System.out.println("2 - Withdraw money from your account");
                System.out.println("3 - Create Fixed Deposit");
                System.out.println("4 - Break Fixed Deposit");
                System.out.println("5 - Print all information about your account");
                System.out.println("6 - Logout");
                System.out.print("Enter your choice: ");
                choice = input.nextInt();
                switch (choice) {
                    case 1:
                        transfer();
                        break;
                    case 2:
                        withdraw();
                        break;

                    case 3:
                        createFixedDeposit();
                        break;
                    case 4:
                        breakFixedDeposit();
                        break;
                    case 5:
                        print();
                        break;
                    case 6:
                        System.out.println("\n" + AH_name+", You are logged out... ");
                        mainMenu();
                    default:
                        System.out.println("\nWrong Input");
                }
            }
        } else {
            System.out.println("\nInvalid Credentials. Login Failed.");
            mainMenu();
        }

    }
}

