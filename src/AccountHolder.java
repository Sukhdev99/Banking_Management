class AccountHolder extends Person {
    private int accountNumber;
    private String pin;
    private double amount;

    public AccountHolder(String name, int accountNumber, String pin, double amount) {
        super(name);
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.amount = 1000 + amount;
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
}
