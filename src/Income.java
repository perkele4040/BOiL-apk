package com.company;

public class Income {
    Supplier whereFrom;
    Client whereTo;
    int income;
    public Income(int income, Supplier whereFrom, Client whereTo)
    {
        this.income = income;
        this.whereFrom = whereFrom;
        this.whereTo = whereTo;
    }
}
