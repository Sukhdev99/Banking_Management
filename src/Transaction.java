import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class Transaction {
    private String type; // "Deposit" or "Withdrawal"
    private double amount;
    private LocalDate date;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.date = LocalDate.now();
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }
}
