import java.util.ArrayList;

class AccountHolder extends Person {
    private ArrayList<Transaction> transactions;
    private int accountNumber;
    private String pin;
    private double amount;
    private double send_amount;
    private double received_amount;

    private int recipientAccountNumber;

    public AccountHolder(String name, int accountNumber, String pin, double amount) {
        super(name);
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.amount = 1000 + amount;
        this.transactions = new ArrayList<>();
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setPIN(String pin) {
        this.pin = pin;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getPIN() {
        return pin;
    }

    public double getAmount() {
        return amount;
    }

    public double getSend_amount() {
        return send_amount;
    }

    public void setSend_amount(double send_amount) {
        this.send_amount = send_amount;
    }

    public double getReceived_amount() {
        return received_amount;
    }

    public void setReceived_amount(double received_amount) {
        this.received_amount = received_amount;
    }
    public void transfer_receive(double amount, int recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber;
        this.amount += amount;
        transactions.add(new Transaction("Transfer (RECEIVED) from "+ recipientAccountNumber , amount));
    }

    public void withdraw(double amount) {
        if (this.amount >= amount) {
            this.amount -= amount;
            transactions.add(new Transaction("Withdrawal", amount));
        }
    }
    public void transfer_Send(double amount, int recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber;
        if (this.amount >= amount) {
            this.amount -= amount;
            transactions.add(new Transaction("Transfer (SEND) to "+ recipientAccountNumber, amount));
        }
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }
}
