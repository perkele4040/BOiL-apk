
public class Income {
    Supplier whereFrom;
    Client whereTo;
    int income;
    boolean done;
    int amountSent;
    public Income(int income, Supplier whereFrom, Client whereTo)
    {
        this.done=false;
        this.income = income;
        this.whereFrom = whereFrom;
        this.whereTo = whereTo;
        this.amountSent = 0;
    }
}
