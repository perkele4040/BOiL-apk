import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
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
				incomeTable[i][j] = new Income(clients[j].getBuyingPrice() - suppliers[i].getSellingPrice() - transportCostTable[i][j], suppliers[i], clients[j]);


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
			overallDemand.addDemand(suppliers[i].getSupply());
		for(int j=0; j<nClients; j++)
			overallSupply.addSupply(clients[j].getDemand());

		for(int i=0; i<nSuppliers; i++)
			incomeTable[i][nClients] = new Income(0, suppliers[i], overallDemand);
		for(int j=0; j<nClients; j++)
			incomeTable[nSuppliers][j] = new Income(0, overallSupply, clients[j]);
		incomeTable[nSuppliers][nClients] = new Income(0, overallSupply, overallDemand);

		//block tracks
		int i_blocked = Integer.parseInt(readData.nextLine());
		int j_blocked = Integer.parseInt(readData.nextLine());
		incomeTable[i_blocked][j_blocked].setBlocked(true);
		incomeTable[i_blocked][j_blocked].setDone(true);
		incomeTable[i_blocked][j_blocked].setAmountSent(-1);
		incomeTable[i_blocked][j_blocked].setIncome(-9999);
		readData.close();

		//copying the income table to enable changes in data
		Income[][] tempIncomeTable = new Income[nSuppliers+1][nClients+1];
		for(int i=0; i<nSuppliers+1; i++)
			System.arraycopy(incomeTable[i], 0, tempIncomeTable[i], 0, nClients + 1);

		//resolving column with blocked path first
		boolean blockedPathDone;

		while(true) {
			//finding a cell with highest income
			int iMax = 0, jMax = 0;
			int tempIncome = -10000;

			blockedPathDone=true;
			for (int i = 0; i < nSuppliers; i++)
				if(!tempIncomeTable[i][j_blocked].isDone())
					blockedPathDone=false;

			if(!blockedPathDone)
			{
				jMax=j_blocked;
				for (int i = 0; i < nSuppliers; i++)
					if (tempIncomeTable[i][jMax].getIncome() > tempIncome && !tempIncomeTable[i][jMax].isDone()) {
						iMax = i;
						tempIncome = tempIncomeTable[iMax][jMax].getIncome();
					}
			}
			else {

				for (int i = 0; i < nSuppliers; i++)
					for (int j = 0; j < nClients; j++) {
						if (tempIncomeTable[i][j].getIncome() > tempIncome && !tempIncomeTable[i][j].isDone()) {
							iMax = i;
							jMax = j;
							tempIncome = tempIncomeTable[iMax][jMax].getIncome();
						}
					}
			}

			//if all cells done = stop looking
			if (tempIncome == -10000)
				break;
			System.out.println("chosen income = "+tempIncome);

			//computing the amount available to be sent on this path
			if (tempIncomeTable[iMax][jMax].getWhereFrom().getSupply() > tempIncomeTable[iMax][jMax].getWhereTo().getDemand())
				tempIncomeTable[iMax][jMax].setAmountSent(tempIncomeTable[iMax][jMax].getWhereTo().getDemand());
			else
				tempIncomeTable[iMax][jMax].setAmountSent(tempIncomeTable[iMax][jMax].getWhereFrom().getSupply());
			tempIncomeTable[iMax][jMax].setDone(true);

			//taking the amount sent on this path from supply/demand
			tempIncomeTable[iMax][jMax].getWhereFrom().subsSupply(tempIncomeTable[iMax][jMax].getAmountSent());
			tempIncomeTable[iMax][jMax].getWhereTo().subsDemand(tempIncomeTable[iMax][jMax].getAmountSent());

			//checking if any suppliers/clients have run out of supply/demand
			if (tempIncomeTable[iMax][jMax].getWhereTo().getDemand() == 0)
				for (int i = 0; i < nSuppliers+1; i++)
					if (!tempIncomeTable[i][jMax].isDone()) {
						tempIncomeTable[i][jMax].setAmountSent(-1);
						tempIncomeTable[i][jMax].setDone(true);
					}
			if (tempIncomeTable[iMax][jMax].getWhereFrom().getSupply() == 0)
				for (int j = 0; j < nClients+1; j++)
					if (!tempIncomeTable[iMax][j].isDone()) {
						tempIncomeTable[iMax][j].setAmountSent(-1);
						tempIncomeTable[iMax][j].setDone(true);
					}

			printIncomeTable(tempIncomeTable, nSuppliers, nClients);
		} //the whole table of real suppliers/clients has been processed

		//splitting the remaining supply/demand between the fictional characters
		for(int i=0; i<nSuppliers; i++)
		{
			if(!tempIncomeTable[i][nClients].isDone() && tempIncomeTable[i][nClients].getWhereFrom().getSupply() > 0)
			{
				tempIncomeTable[i][nClients].setDone(true);
				tempIncomeTable[i][nClients].addAmountSent(tempIncomeTable[i][nClients].getWhereFrom().getSupply());
				tempIncomeTable[i][nClients].getWhereFrom().setSupply(0);
				tempIncomeTable[2][nClients].getWhereTo().subsDemand(incomeTable[i][nClients].getAmountSent());
			}
		}
		for(int j=0; j<nClients; j++)
		{
			if(!tempIncomeTable[nSuppliers][j].isDone() && tempIncomeTable[nSuppliers][j].getWhereTo().getDemand() > 0)
			{
				tempIncomeTable[nSuppliers][j].setDone(true);
				tempIncomeTable[nSuppliers][j].setAmountSent(tempIncomeTable[nSuppliers][j].getWhereTo().getDemand());
				tempIncomeTable[nSuppliers][j].getWhereTo().setDemand(0);
				tempIncomeTable[nSuppliers][3].getWhereFrom().subsSupply(tempIncomeTable[nSuppliers][j].getAmountSent());
			}
		}
		tempIncomeTable[nSuppliers][nClients].setDone(true);
		tempIncomeTable[nSuppliers][nClients].setAmountSent(tempIncomeTable[nSuppliers][nClients].getWhereTo().getDemand());
		tempIncomeTable[nSuppliers][nClients].getWhereTo().setDemand(0);
		tempIncomeTable[nSuppliers][nClients].getWhereFrom().setSupply(0);
		//all product has been assigned to paths

		printIncomeTable(tempIncomeTable, nSuppliers, nClients);

		/*do
		{
			//reset demand/supply and done attributes for next iteration
			for(int i=0; i<nSuppliers+1; i++) {
				tempIncomeTable[i][0].setWhereFrom(incomeTable[i][0].getWhereFrom());
				for(int j=0; j<nClients+1; j++)
					tempIncomeTable[i][j].setDone(false);
				for(int j=0; j<nClients+1; j++)
					tempIncomeTable[0][j].setWhereTo(incomeTable[0][j].getWhereTo());

			}*/

		boolean optimised;
		while(true) {
			//figuring out alfas and betas and building an array for petla zmian :)
			int[] alfas = new int[nSuppliers + 1];
			for (int i = 0; i < nSuppliers + 1; i++) alfas[i] = -10000;
			int[] betas = new int[nClients + 1];
			for (int j = 0; j < nClients + 1; j++) betas[j] = -10000;
			alfas[0] = 0;
			for (int i = 0; i < nSuppliers + 1; i++)
				for (int j = 0; j < nClients + 1; j++)
					tempIncomeTable[i][j].setDone(false);

			int doneCounter = 1;
			while (doneCounter < (nClients + nSuppliers + 2))
				for (int i = 0; i < nSuppliers + 1; i++)
					for (int j = 0; j < nClients + 1; j++) {
						if (!tempIncomeTable[i][j].isDone() && tempIncomeTable[i][j].getAmountSent() != -1) {
							if (alfas[i] != -10000) {

								betas[j] = tempIncomeTable[i][j].getIncome() - alfas[i];
								System.out.println("changed beta nr " + j);
								tempIncomeTable[i][j].setDone(true);
								doneCounter++;
							} else if (betas[j] != -10000) {
								alfas[i] = tempIncomeTable[i][j].getIncome() - betas[j];
								System.out.println("changed alfa nr " + i);
								tempIncomeTable[i][j].setDone(true);
								doneCounter++;
							}

						/*for(int k=0; k<nSuppliers+1; k++)
							tempIncomeTable[k][j].done=true;
						for(int k=0; k<nClients+1; k++)
							tempIncomeTable[i][k].done=true;*/
							System.out.println("alfas:  ");
							for (int z = 0; z < nSuppliers + 1; z++) {
								System.out.print(alfas[z]);
								System.out.print("   ");
							}
							System.out.println("\nbetas:  ");
							for (int z = 0; z < nClients + 1; z++) {
								System.out.print(betas[z]);
								System.out.print("   ");
							}
							System.out.println("\ndoneCounter= " + doneCounter);
							System.out.println("--------------\n");
						}
					}


			int[][] changesTable = new int[nSuppliers+1][nClients+1];
			for(int i=0; i<nSuppliers+1; i++)
				for(int j=0; j<nClients+1; j++) {
					if (tempIncomeTable[i][j].getAmountSent() != -1)
						changesTable[i][j] = -10000;
					else if(i==i_blocked && j==j_blocked)
						changesTable[i][j] = -9999;
					else
						changesTable[i][j] = (tempIncomeTable[i][j].getIncome() - alfas[i] - betas[j]);
				}

			System.out.println("changes: ");
			for(int i = 0; i < nSuppliers+1; i ++)
			{
				for(int j = 0; j < nClients+1; j++)
				{
					System.out.print(changesTable[i][j]);
					System.out.print("  ");
				}
				System.out.println();
			}

			int overallIncome = 0;
			for(int i=0; i<nSuppliers+1; i++)
				for(int j=0; j<nClients+1; j++)
					if(tempIncomeTable[i][j].getAmountSent()!=-1)
						overallIncome+=tempIncomeTable[i][j].getAmountSent()*tempIncomeTable[i][j].getIncome();
			System.out.println("Overall income = "+overallIncome);

			optimised = true;
			for(int i=0; i<nSuppliers+1; i++)
				for(int j=0; j<nClients+1; j++)
					if(changesTable[i][j] > 0)
						optimised = false;
			if(optimised) break;

			int iMax=0, jMax=0;
			//finding the worst cell ( highest > 0 )
			for(int i=0; i<nSuppliers+1; i++)
				for(int j=0; j<nClients+1; j++)
					if(changesTable[i][j] > changesTable[iMax][jMax])
					{
						iMax=i;
						jMax=j;
					}
			System.out.println("max in changesTable: "+changesTable[iMax][jMax]);

			int[] indexesForChange = new int [8];
			indexesForChange[0] = iMax;
			indexesForChange[1] = jMax;
			//finding appropriate cells for petla zmian ::)
			for(int i=0; i<nSuppliers+1; i++)
				if(changesTable[i][jMax]==-10000 && i!=iMax) {
					indexesForChange[2] = i;
					indexesForChange[3] = jMax;
					for (int j = 0; j < nClients + 1; j++) {
						if(changesTable[i][j]==-10000 && j!=jMax)
						{
							indexesForChange[4]=i;
							indexesForChange[5]=j;
							if(changesTable[iMax][j]==-10000)
							{
								indexesForChange[6]=iMax;
								indexesForChange[7]=j;
							}
						}
					}
				}
			System.out.println("wybrane indeksy: ");
			System.out.print(Arrays.toString(indexesForChange));

			int delta=tempIncomeTable[indexesForChange[2]][indexesForChange[3]].getAmountSent();
			if( tempIncomeTable[indexesForChange[6]][indexesForChange[7]].getAmountSent() < delta )
				delta = tempIncomeTable[indexesForChange[6]][indexesForChange[7]].getAmountSent();
			for(int i=0; i<8; i+=4)
			{
				if(tempIncomeTable[indexesForChange[i]][indexesForChange[i+1]].getAmountSent() == -1)
					tempIncomeTable[indexesForChange[i]][indexesForChange[i+1]].setAmountSent(delta);
				else
					tempIncomeTable[indexesForChange[i]][indexesForChange[i+1]].addAmountSent(delta);
				if(tempIncomeTable[indexesForChange[i+2]][indexesForChange[i+3]].getAmountSent() == delta)
					tempIncomeTable[indexesForChange[i+2]][indexesForChange[i+3]].setAmountSent(-1);
				else
					tempIncomeTable[indexesForChange[i+2]][indexesForChange[i+3]].subsAmountSent(delta);
			}
			printIncomeTable(tempIncomeTable, nSuppliers, nClients);
		}

    }

	private static void printIncomeTable(Income[][] tempIncomeTable, int nSuppliers, int nClients) {
		System.out.println("\nIncome Table: ");
		System.out.print("     ");
		for(int j=0; j<nClients+1; j++)
		{
			System.out.print(tempIncomeTable[0][j].getWhereTo().getDemand());
			System.out.print("  ");
		}
		System.out.println();
		System.out.println("   ------------");
		for(int i = 0; i < nSuppliers+1; i ++)
		{
			System.out.print(tempIncomeTable[i][0].getWhereFrom().getSupply());
			System.out.print(" | ");
			for(int j = 0; j < nClients+1; j++)
			{
				System.out.print(tempIncomeTable[i][j].getAmountSent());
				System.out.print("  ");
			}
			System.out.println();
		}
	}

}
