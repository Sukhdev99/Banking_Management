import java.time.LocalDate;

class FixedDeposit {
    private int accountNumber;
    private double principal;
    private double interestRate ;
    private int duration; // in months
    private double maturityAmount;
    private LocalDate creationDate;
    private LocalDate maturityDate;

    public FixedDeposit(int accountNumber, double principal, double interestRate, int duration) {
        this.accountNumber = accountNumber;
        this.principal = principal;
        this.interestRate = interestRate;
        this.duration = duration;
        calculateMaturityAmount();
    }

    private void calculateMaturityAmount() {
        // M = P * (1 + r/n)^(nt)
        // M is the maturity amount
        // P is the principal amount
        // r is the annual interest rate (as a decimal)
        // n is the number of times that interest is compounded per unit `t`
        // t is the time the money is invested for in years

        int n = 12; // assuming interest is compounded monthly

        double r = interestRate / 100; // convert interest rate to decimal

        double t = duration / 12.0; // convert duration to years

        this.maturityAmount = principal * Math.pow(1 + r / n, n * t);
        LocalDate currentDate = LocalDate.now();
        this.maturityDate = currentDate.plusMonths(duration);
        this.creationDate = currentDate;
    }

    public int getAccountNumber() {

        return accountNumber;
    }

    public double getMaturityAmount() {

        return maturityAmount;
    }
    public LocalDate getMaturityDate() {

        return maturityDate;
    }
    public LocalDate getCreationDate() {

        return creationDate;
    }
    public double getPrincipal(){
        return principal;
    }

}
