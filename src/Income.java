
public class Income {

    private Supplier whereFrom;
    private Client whereTo;
    private int income;
    private boolean done;
    private int amountSent;
    private boolean blocked;
    public Income(int income, Supplier whereFrom, Client whereTo)
    {
        this.done=false;
        this.income = income;
        this.whereFrom = whereFrom;
        this.whereTo = whereTo;
        this.amountSent = 0;
        this.blocked=false;
    }

    public Supplier getWhereFrom() {
        return whereFrom;
    }

    public void setWhereFrom(Supplier whereFrom) {
        this.whereFrom = whereFrom;
    }

    public Client getWhereTo() {
        return whereTo;
    }

    public void setWhereTo(Client whereTo) {
        this.whereTo = whereTo;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public void addIncome(int delta) {
        this.income += delta;
    }

    public void subsIncome(int delta) {
        this.income -= delta;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public int getAmountSent() {
        return amountSent;
    }

    public void setAmountSent(int amountSent) {
        this.amountSent = amountSent;
    }

    public void addAmountSent(int delta) {
        this.amountSent += delta;
    }

    public void subsAmountSent(int delta) {
        this.amountSent -= delta;
    }

    public boolean isBlocked()
    {
        return blocked;
    }

    public void setBlocked(boolean blocked)
    {
        this.blocked = blocked;
    }
}


