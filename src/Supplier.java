public class Supplier {
    int supply;
    int sellingPrice;
    int supplierNr;
    public Supplier(int supply, int sellingPrice, int supplierNr)
    {
        this.supply = supply;
        this.sellingPrice = sellingPrice;
        this.supplierNr = supplierNr;
    }
    public void printData()
    {
        System.out.println("data of supplier no "+this.supplierNr+": ");
        System.out.println("supply: "+this.supply+"\nselling price per unit: "+this.sellingPrice);
    }
}
