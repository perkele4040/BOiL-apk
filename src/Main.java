package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//put data in data.txt file in this order:
//supplier 1 supply
//supplier 1 selling price
//supplier 2 ...
//client 1 demand
//client 1 buying price
//client 2 ...
//

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
	    Scanner readData = new Scanner(new File("data.txt"));
	    Supplier supplier1 = new Supplier(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), 1);
	    Supplier supplier2 = new Supplier(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), 2);
	    Client client1 = new Client(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), 1);
	    Client client2 = new Client(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), 2);
	    Client client3 = new Client(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), 3);
		int[][] transportCostTable = new int[2][3];
		transportCostTable[0][0] = 8;
		transportCostTable[0][1] = 14;
		transportCostTable[0][2] = 17;
		transportCostTable[1][0] = 12;
		transportCostTable[1][1] = 9;
		transportCostTable[1][2] = 19;
		/*for(int i = 0; i < 2; i ++)
		{
			for(int j = 0; j < 3; j++)
			{
				System.out.print(transportCostTable[i][j]);
				System.out.print("  ");
			}
			System.out.println();
		}*/
		Income[][] incomeTable = new Income[3][4];
		incomeTable[0][0] = new Income(client1.buyingPrice-supplier1.sellingPrice-transportCostTable[0][0], supplier1, client1);
		incomeTable[0][1] = new Income(client2.buyingPrice-supplier1.sellingPrice-transportCostTable[0][1], supplier1, client2);
		incomeTable[0][2] = new Income(client3.buyingPrice-supplier1.sellingPrice-transportCostTable[0][2], supplier1, client3);
		incomeTable[1][0] = new Income(client1.buyingPrice-supplier2.sellingPrice-transportCostTable[1][0], supplier2, client1);
		incomeTable[1][1] = new Income(client2.buyingPrice-supplier2.sellingPrice-transportCostTable[1][1], supplier2, client2);
		incomeTable[1][2] = new Income(client3.buyingPrice-supplier2.sellingPrice-transportCostTable[1][2], supplier2, client3);

		Supplier overallSupply = new Supplier((client1.demand+client2.demand+client3.demand), 0, 3);
		Client overallDemand = new Client((supplier1.supply+supplier2.supply), 0, 4);
		incomeTable[2][0] = new Income(0, overallSupply, client1);
		incomeTable[2][1] = new Income(0, overallSupply, client2);
		incomeTable[2][2] = new Income(0, overallSupply, client3);
		incomeTable[2][3] = new Income(0, overallSupply, overallDemand);
		incomeTable[0][3] = new Income(0, supplier1, overallDemand);
		incomeTable[1][3] = new Income(0, supplier2, overallDemand);

		for(int i = 0; i < 3; i ++)
		{
			for(int j = 0; j < 4; j++)
			{
				System.out.print(incomeTable[i][j].income);
				System.out.print("  ");
			}
			System.out.println();
		}
    }

}
