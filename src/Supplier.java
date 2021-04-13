public class Supplier {
    private int supply;
    private int sellingPrice;
    private int supplierNr;
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

    public int getSupply() {
        return supply;
    }

    public void setSupply(int supply) {
        this.supply = supply;
    }

    public void addSupply(int delta) {
        this.supply += delta;
    }

    public void subsSupply(int delta) {
        this.supply -= delta;
    }

    public int getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(int sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public int getSupplierNr() {
        return supplierNr;
    }

    public void setSupplierNr(int supplierNr) {
        this.supplierNr = supplierNr;
    }
}
