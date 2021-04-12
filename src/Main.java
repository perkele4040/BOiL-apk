import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//put data in data.txt file in this order:
//number of suppliers
//numbers of clients
//supplier 1 supply
//supplier 1 selling price
//supplier 2 ...
//client 1 demand
//client 1 buying price
//client 2 ...
//cost of transport on path(1,1)
//path(1,2) ...

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
    	//no of suppliers/clients from file
		Scanner readData = new Scanner(new File("resources/data.txt"));
    	int nSuppliers = Integer.parseInt(readData.nextLine());
    	int nClients = Integer.parseInt(readData.nextLine());
		Supplier[] suppliers = new Supplier[nSuppliers];
		Client[] clients = new Client[nClients];

    	//supplier/client data from file
		for(int i=0; i<nSuppliers; i++)
	    	suppliers[i] = new Supplier(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), i+1);
		for(int j=0; j<nClients; j++)
			clients[j] = new Client(Integer.parseInt(readData.nextLine()), Integer.parseInt(readData.nextLine()), j+1);

	    //test case 2
		/*Supplier supplier1 = new Supplier(40, 1, 1);
		Supplier supplier2 = new Supplier(30, 1, 2);
		Client client1 = new Client(27, 1, 1);
		Client client2 = new Client(27, 1, 2);
		Client client3 = new Client(16, 1, 3);*/

		//costs of transport from file
		int[][] transportCostTable = new int[nSuppliers][nClients];
		for(int i=0; i<nSuppliers; i++)
			for(int j=0; j<nClients; j++)
				transportCostTable[i][j] = Integer.parseInt(readData.nextLine());

		//computing overall cost of transport on each path
		//overall cost = selling price - (production price + transport price)
		Income[][] incomeTable = new Income[nSuppliers+1][nClients+1];
		for(int i=0; i<nSuppliers; i++)
			for(int j=0; j<nClients; j++)
				incomeTable[i][j] = new Income(clients[j].buyingPrice - suppliers[i].sellingPrice - transportCostTable[i][j], suppliers[i], clients[j]);

		//test case 2
		/*incomeTable[0][0] = new Income(3, supplier1, client1);
		incomeTable[0][1] = new Income(9, supplier1, client2);
		incomeTable[0][2] = new Income(11, supplier1, client3);
		incomeTable[1][0] = new Income(2, supplier2, client1);
		incomeTable[1][1] = new Income(7, supplier2, client2);
		incomeTable[1][2] = new Income(8, supplier2, client3);*/

		//adding fictional supplier/client to hold overall supply/demand
		Supplier overallSupply = new Supplier(0, 0, nSuppliers+1);
		Client overallDemand = new Client(0, 0, nClients+1);
		for(int i=0; i<nSuppliers; i++)
			overallDemand.demand+=suppliers[i].supply;
		for(int j=0; j<nClients; j++)
			overallSupply.supply+=clients[j].demand;

		for(int i=0; i<nSuppliers; i++)
			incomeTable[i][nClients] = new Income(0, suppliers[i], overallDemand);
		for(int j=0; j<nClients; j++)
			incomeTable[nSuppliers][j] = new Income(0, overallSupply, clients[j]);
		incomeTable[nSuppliers][nClients] = new Income(0, overallSupply, overallDemand);

		//copying the income table to enable changes in data
		Income[][] tempIncomeTable = new Income[nSuppliers+1][nClients+1];
		for(int i=0; i<nSuppliers+1; i++)
			System.arraycopy(incomeTable[i], 0, tempIncomeTable[i], 0, nClients + 1);

		while(1==1) {
			//finding a cell with highest income
			int iMax = 0, jMax = 0;
			int tempIncome = -10000;
			for (int i = 0; i < nSuppliers; i++)
				for (int j = 0; j < nClients; j++) {
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
				for (int i = 0; i < nSuppliers+1; i++)
					if (!tempIncomeTable[i][jMax].done) {
						tempIncomeTable[i][jMax].amountSent = -1;
						tempIncomeTable[i][jMax].done = true;
					}
			if (tempIncomeTable[iMax][jMax].whereFrom.supply == 0)
				for (int j = 0; j < nClients+1; j++)
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
		for(int i=0; i<nSuppliers; i++)
		{
			if(!tempIncomeTable[i][nClients].done && tempIncomeTable[i][nClients].whereFrom.supply > 0)
			{
				tempIncomeTable[i][nClients].done=true;
				tempIncomeTable[i][nClients].amountSent+=tempIncomeTable[i][nClients].whereFrom.supply;
				tempIncomeTable[i][nClients].whereFrom.supply=0;
				tempIncomeTable[2][nClients].whereTo.demand-=incomeTable[i][nClients].amountSent;
			}
		}
		for(int j=0; j<nClients; j++)
		{
			if(!tempIncomeTable[nSuppliers][j].done && tempIncomeTable[nSuppliers][j].whereTo.demand > 0)
			{
				tempIncomeTable[nSuppliers][j].done=true;
				tempIncomeTable[nSuppliers][j].amountSent=tempIncomeTable[nSuppliers][j].whereTo.demand;
				tempIncomeTable[nSuppliers][j].whereTo.demand=0;
				tempIncomeTable[nSuppliers][3].whereFrom.supply-=tempIncomeTable[nSuppliers][j].amountSent;
			}
		}
		tempIncomeTable[nSuppliers][nClients].done=true;
		tempIncomeTable[nSuppliers][nClients].amountSent = tempIncomeTable[nSuppliers][nClients].whereTo.demand;
		tempIncomeTable[nSuppliers][nClients].whereTo.demand=0;
		tempIncomeTable[nSuppliers][nClients].whereFrom.supply=0;

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

		int[] alfas = new int[nSuppliers+1];
		for(int i=0; i<nSuppliers+1; i++) alfas[i] = -10000;
		int[] betas = new int[nClients+1];
		for(int j=0; j<nClients+1; j++) betas[j] = -10000;
		alfas[0] = 0;
		for(int i=0; i<nSuppliers+1; i++)
			for(int j=0; j<nClients+1; j++)
				tempIncomeTable[i][j].done = false;

		int doneCounter=1;
		while( doneCounter < (nClients+nSuppliers+2) )
		for(int i=0; i<nSuppliers+1; i++)
			for(int j=0; j<nClients+1; j++)
			{
				if(!tempIncomeTable[i][j].done && tempIncomeTable[i][j].amountSent!=-1)
				{
					if(alfas[i]!=-10000)
					{

						betas[j] = tempIncomeTable[i][j].income - alfas[i];
						System.out.println("zmieniłem betas nr "+j);
						tempIncomeTable[i][j].done=true;
						doneCounter++;
					}
					else if(betas[j]!=-10000)
					{
						alfas[i] = tempIncomeTable[i][j].income - betas[j];
						System.out.println("zmieniłem alfa nr "+i);
						tempIncomeTable[i][j].done=true;
						doneCounter++;
					}

					/*for(int k=0; k<nSuppliers+1; k++)
						tempIncomeTable[k][j].done=true;
					for(int k=0; k<nClients+1; k++)
						tempIncomeTable[i][k].done=true;*/
					System.out.println("alfas:  ");
					for(int z=0; z<nSuppliers+1; z++) { System.out.print(alfas[z]); System.out.print("   "); }
					System.out.println("\nbetas:  ");
					for(int z=0; z<nClients+1; z++) { System.out.print(betas[z]); System.out.print("   "); }
					System.out.println("\ndoneCounter= "+doneCounter);
				}
			}


		/*char[][] changesTable = new char[nSuppliers][nClients];
		for(int i=0; i<nSuppliers; i++)
			for(int j=0; j<nClients; j++)
				if(tempIncomeTable[i][j].amountSent!=0)
					changesTable[i][j] = 'X';
				else
					changesTable[i][j] = <char>tempIncomeTable[i][j].income*/

    }

}
