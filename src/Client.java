public class Client {
    private int demand;
    private int buyingPrice;
    private int clientNr;
    public Client(int demand, int buyingPrice, int clientNr)
    {
        this.demand = demand;
        this.buyingPrice = buyingPrice;
        this.clientNr = clientNr;
    }
    public void printData()
    {
        System.out.println("data of client no "+this.clientNr+": ");
        System.out.println("demand: "+this.demand+"\nbuying price per unit: "+this.buyingPrice);
    }

    public int getDemand() {
        return demand;
    }

    public void setDemand(int demand) {
        this.demand = demand;
    }

    public void addDemand(int delta) {
        this.demand += delta;
    }

    public void subsDemand(int delta) {
        this.demand -= delta;
    }

    public int getBuyingPrice() {
        return buyingPrice;
    }

    public void setBuyingPrice(int buyingPrice) {
        this.buyingPrice = buyingPrice;
    }

    public int getClientNr() {
        return clientNr;
    }

    public void setClientNr(int clientNr) {
        this.clientNr = clientNr;
    }
}
