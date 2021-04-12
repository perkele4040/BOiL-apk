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
    	//supplier and client data from a file
		//todo: read no of suppliers and clients from file and refactor the code to dynamic size
	    Scanner readData = new Scanner(new File("resources/data.txt"));
	    Supplier supplier1 = new Supplier(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), 1);
	    Supplier supplier2 = new Supplier(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), 2);
	    Client client1 = new Client(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), 1);
	    Client client2 = new Client(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), 2);
	    Client client3 = new Client(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), 3);

	    //test case 2
		/*Supplier supplier1 = new Supplier(40, 1, 1);
		Supplier supplier2 = new Supplier(30, 1, 2);
		Client client1 = new Client(27, 1, 1);
		Client client2 = new Client(27, 1, 2);
		Client client3 = new Client(16, 1, 3);*/

		//todo: also from file
		int[][] transportCostTable = new int[2][3];
		transportCostTable[0][0] = 8;
		transportCostTable[0][1] = 14;
		transportCostTable[0][2] = 17;
		transportCostTable[1][0] = 12;
		transportCostTable[1][1] = 9;
		transportCostTable[1][2] = 19;

		//computing overall cost of transport on each path
		//overall cost = selling price - (production price + transport price)
		Income[][] incomeTable = new Income[3][4];
		incomeTable[0][0] = new Income(client1.buyingPrice-supplier1.sellingPrice-transportCostTable[0][0], supplier1, client1);
		incomeTable[0][1] = new Income(client2.buyingPrice-supplier1.sellingPrice-transportCostTable[0][1], supplier1, client2);
		incomeTable[0][2] = new Income(client3.buyingPrice-supplier1.sellingPrice-transportCostTable[0][2], supplier1, client3);
		incomeTable[1][0] = new Income(client1.buyingPrice-supplier2.sellingPrice-transportCostTable[1][0], supplier2, client1);
		incomeTable[1][1] = new Income(client2.buyingPrice-supplier2.sellingPrice-transportCostTable[1][1], supplier2, client2);
		incomeTable[1][2] = new Income(client3.buyingPrice-supplier2.sellingPrice-transportCostTable[1][2], supplier2, client3);

		//test case 2
		/*incomeTable[0][0] = new Income(3, supplier1, client1);
		incomeTable[0][1] = new Income(9, supplier1, client2);
		incomeTable[0][2] = new Income(11, supplier1, client3);
		incomeTable[1][0] = new Income(2, supplier2, client1);
		incomeTable[1][1] = new Income(7, supplier2, client2);
		incomeTable[1][2] = new Income(8, supplier2, client3);*/

		//adding fictional supplier/client to hold overall supply/demand
		Supplier overallSupply = new Supplier((client1.demand+client2.demand+client3.demand), 0, 3);
		Client overallDemand = new Client((supplier1.supply+supplier2.supply), 0, 4);
		incomeTable[2][0] = new Income(0, overallSupply, client1);
		incomeTable[2][1] = new Income(0, overallSupply, client2);
		incomeTable[2][2] = new Income(0, overallSupply, client3);
		incomeTable[2][3] = new Income(0, overallSupply, overallDemand);
		incomeTable[0][3] = new Income(0, supplier1, overallDemand);
		incomeTable[1][3] = new Income(0, supplier2, overallDemand);

		//copying the income table to enable changes in data
		Income[][] tempIncomeTable = new Income[3][4];
		for(int i=0; i<3; i++)
			for(int j=0; j<4; j++)
			{
				tempIncomeTable[i][j] = incomeTable[i][j];
			}

		while(1==1) {
			//finding a cell with highest income
			int iMax = 0, jMax = 0;
			int tempIncome = -10000;
			for (int i = 0; i < 2; i++)
				for (int j = 0; j < 3; j++) {
					if (tempIncomeTable[i][j].income > tempIncome && !tempIncomeTable[i][j].done) {
						iMax = i;
						jMax = j;
						tempIncome = tempIncomeTable[iMax][jMax].income;
					}
				}
			System.out.println("wybrano income = "+tempIncome);

			//if all cells done = stop looking
			if (tempIncome == -10000)
				break;

			//computing the amount available to be sent on this path
			if (tempIncomeTable[iMax][jMax].whereFrom.supply > tempIncomeTable[iMax][jMax].whereTo.demand)
				tempIncomeTable[iMax][jMax].amountSent = tempIncomeTable[iMax][jMax].whereTo.demand;
			else
				tempIncomeTable[iMax][jMax].amountSent = tempIncomeTable[iMax][jMax].whereFrom.supply;
			tempIncomeTable[iMax][jMax].done = true;

			//taking the amount sent on this path from supply/demand
			tempIncomeTable[iMax][jMax].whereFrom.supply -= tempIncomeTable[iMax][jMax].amountSent;
			tempIncomeTable[iMax][jMax].whereTo.demand -= tempIncomeTable[iMax][jMax].amountSent;

			//checking if any suppliers/clients have run out of supply/demand
			if (tempIncomeTable[iMax][jMax].whereTo.demand == 0)
				for (int i = 0; i < 3; i++)
					if (!tempIncomeTable[i][jMax].done) {
						tempIncomeTable[i][jMax].amountSent = -1;
						tempIncomeTable[i][jMax].done = true;
					}
			if (tempIncomeTable[iMax][jMax].whereFrom.supply == 0)
				for (int j = 0; j < 4; j++)
					if (!tempIncomeTable[iMax][j].done) {
						tempIncomeTable[iMax][j].amountSent = -1;
						tempIncomeTable[iMax][j].done = true;
					}

			//printing the table for quality control :)
			System.out.println("Przesłane: ");
			System.out.println("     "+tempIncomeTable[0][0].whereTo.demand+"  "+tempIncomeTable[0][1].whereTo.demand+"  "+tempIncomeTable[0][2].whereTo.demand+"  "+tempIncomeTable[0][3].whereTo.demand);
			System.out.println("   ------------");
			for(int i = 0; i < 3; i ++)
			{
				System.out.print(tempIncomeTable[i][0].whereFrom.supply);
				System.out.print(" | ");
				for(int j = 0; j < 4; j++)
				{
					System.out.print(tempIncomeTable[i][j].amountSent);
					System.out.print("  ");
				}
				System.out.println();
			}
		} //the whole table of real suppliers/clients have been processed

		//splitting the remaining supply/demand between the fictional characters
		for(int i=0; i<2; i++)
		{
			if(!tempIncomeTable[i][3].done && tempIncomeTable[i][3].whereFrom.supply > 0)
			{
				tempIncomeTable[i][3].done=true;
				tempIncomeTable[i][3].amountSent+=tempIncomeTable[i][3].whereFrom.supply;
				tempIncomeTable[i][3].whereFrom.supply=0;
				tempIncomeTable[2][3].whereTo.demand-=incomeTable[i][3].amountSent;
			}
		}
		for(int j=0; j<3; j++)
		{
			if(!tempIncomeTable[2][j].done && tempIncomeTable[2][j].whereTo.demand > 0)
			{
				tempIncomeTable[2][j].done=true;
				tempIncomeTable[2][j].amountSent=tempIncomeTable[2][j].whereTo.demand;
				tempIncomeTable[2][j].whereTo.demand=0;
				tempIncomeTable[2][3].whereFrom.supply-=tempIncomeTable[2][j].amountSent;
			}
		}
		tempIncomeTable[2][3].done=true;
		tempIncomeTable[2][3].amountSent = tempIncomeTable[2][3].whereTo.demand;
		tempIncomeTable[2][3].whereTo.demand=0;
		tempIncomeTable[2][3].whereFrom.supply=0;

		System.out.println("Przesłane: ");
		System.out.println("     "+tempIncomeTable[0][0].whereTo.demand+"  "+tempIncomeTable[0][1].whereTo.demand+"  "+tempIncomeTable[0][2].whereTo.demand+"  "+tempIncomeTable[0][3].whereTo.demand);
		System.out.println("   ------------");
		for(int i = 0; i < 3; i ++)
		{
			System.out.print(tempIncomeTable[i][0].whereFrom.supply);
			System.out.print(" | ");
			for(int j = 0; j < 4; j++)
			{
				System.out.print(tempIncomeTable[i][j].amountSent);
				System.out.print("  ");
			}
			System.out.println();
		}

    }

}
