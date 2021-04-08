package com.company;

public class Client {
    int demand;
    int buyingPrice;
    int clientNr;
    public Client(int demand, int buyingPrice, int clientNr)
    {
        this.demand = demand;
        this.buyingPrice = buyingPrice;
        this.clientNr = clientNr;
        System.out.println("added Client no "+this.clientNr);
    }
    public void printData()
    {
        System.out.println("data of client no "+this.clientNr+": ");
        System.out.println("demand: "+this.demand+"\nbuying price per unit: "+this.buyingPrice);
    }
}
