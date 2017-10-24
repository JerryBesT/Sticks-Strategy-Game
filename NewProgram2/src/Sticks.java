//
//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title:            Sticks
// Files:            Config.java, Sticks.java, TestStics.java
// Semester:         (CS302) Fall 2016
//
// Author:           Zhenyu Zou
// Email:            zzou24@wisc.edu
// CS Login:         zzou
// Lecturer's Name:  Gary Dahl
// Lab Section:      1350
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name:     Haoning Xia 
// Partner Email:    hxia22@wisc.edu
// Partner CS Login: haoning
// Lecturer's Name:  Gary Dahl
// Lab Section:      1350
// 
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
//    _X_ Write-up states that Pair Programming is allowed for this assignment.
//    _X_ We have both read the CS302 Pair Programming policy.
//    _X_ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully 
// acknowledge and credit those sources of help here.  Instructors and TAs do 
// not need to be credited here, but tutors, friends, relatives, room mates 
// strangers, etc do.
//
// Persons:          (identify each person and describe their help in detail)
// Online Sources:   (identify each URL and describe its assistance in detail)
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

//TODO  file header comment

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.MaximizeAction;

//TODO class header comment

public class Sticks {
	
	/**
	 * This is the main method for the game of Sticks. 
	 * In milestone 1 this contains the whole program for playing
	 * against a friend.
	 * In milestone 2 this contains the welcome, name prompt, 
	 * how many sticks question, menu, calls appropriate methods
	 * and the thank you message at the end.
	 * One method called in multiple places is promptUserForNumber.
	 * When the menu choice to play against a friend is chosen,
	 * then playAgainstFriend method is called.
	 * When the menu choice to play against a computer is chosen,
	 * then playAgainstComputer method is called.  If the
	 * computer with AI option is chosen then trainAI is called
	 * before calling playAgainstComputer.  Finally, 
	 * call strategyTableToString to prepare a strategy table
	 * for printing. 
	 * 
	 * @param args (unused)
	 */
	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to the Game of Sticks!");
		System.out.println("==============================");
		System.out.println("");
		
		System.out.print("What is your name? "); 
		String userName =input.nextLine(); // input the user's name.
		System.out.println("Hello "+userName.trim()+"."); // trim the whitespace that the user might type
		String sticksOnTable = "How many sticks are there on the table initially (" + Config.MIN_STICKS + "-" + Config.MAX_STICKS + ")? ";
		//call the promptuserfornumber method to try to avoid incorrect input, such as charaters or out of bounce numbers.
		int numSticksOnTable = promptUserForNumber(input, sticksOnTable, Config.MIN_STICKS, Config.MAX_STICKS); 
		
		//print out the menu for user to choose.
	    System.out.println("");
        System.out.println("Would you like to:");
    	System.out.println(" 1) Play against a friend");
    	System.out.println(" 2) Play against computer (basic)");
    	System.out.println(" 3) Play against computer with AI");
    	//first player's name
    	String player1Name = "";
    	//second player's name
    	String player2Name = "";
    	
    	//store the choice that user chose
    	int numChoice =0;
		
    	// set the condition that the loop will continue until the user's choice is in the range.
		while(numChoice < 1 || numChoice >3) 
		{
			System.out.print("Which do you choose (1,2,3)? ");
			//check if the input contains numbers or not
			if(input.hasNextInt()) 
			{
				int num1 = input.nextInt();
				//To avoid and take out that unnecessary whitespace is entered.
				if(input.hasNextLine()) 
				{
					input.nextLine();
				}
				//check is the choice is in the range.
				if(num1<1 || num1>3)
					System.out.println("Please enter a number between 1 and 3.");
				else
					numChoice = num1;
			} 
			// it executes when the choice contains characters
			else 
			{
				String errorInput1 = input.nextLine();
				System.out.println("Error: expected a number between 1 and 3 but found: " + errorInput1);
			}
		}
    	
		// it executes when choose 1
    	if(numChoice == 1)
    	{
    		playAgainstFriend(input, numSticksOnTable, player1Name, player2Name);
    	}
    	//it executes when choose 2
    	else if(numChoice == 2)
    	{
    		//call the method to prompt the random choices that computer input.
    		basicChooseAction(numSticksOnTable);
    		//call the method to initialize the program for playing against the computer with random choices.
    		playAgainstComputer(input, numSticksOnTable, userName, null);
    	}
    	//it executes when choose 3
    	else if(numChoice == 3)
    	{
    		int games = 0;
    		String prompt = "How many games should the AI learn from (" + Config.MIN_GAMES + " to " + Config.MAX_GAMES + ")? ";
    		System.out.println("");
    		games = promptUserForNumber(input, prompt, Config.MIN_GAMES, Config.MAX_GAMES);
    		int [][] StrategyTable = trainAi(numSticksOnTable, games);
    		playAgainstComputer(input, numSticksOnTable, userName, trainAi(numSticksOnTable, games));
    		System.out.print("Would you like to see the strategy table (Y/N)? ");
    		String choose = input.nextLine();
    		if(choose == "Y" || choose == "y")
    		{
    			System.out.println(strategyTableToString(StrategyTable));
    		}
    	}
		  
		System.out.println("");
		System.out.println("=========================================");
		System.out.println("Thank you for playing the Game of Sticks!");
		 
		input.close();
		
		}

	/**
	 * This method encapsulates the code for prompting the user for a number and
	 * verifying the number is within the expected bounds.
	 * 
	 * @param input
	 *            The instance of the Scanner reading System.in.
	 * @param prompt
	 *            The prompt to the user requesting a number within a specific
	 *            range.
	 * @param min
	 *            The minimum acceptable number.
	 * @param max
	 *            The maximum acceptable number.
	 * @return The number entered by the user between and including min and max.
	 */
	static int promptUserForNumber(Scanner input, String prompt, 
			int min, int max) 
	{
		//store the value of input to the following variable
		int numSticksOnTable = 0;
		
		//keep the loop running while the input is out of range
		while(numSticksOnTable < min || numSticksOnTable > max) 
		{
			System.out.print(prompt);
			//check if the input contains number or not
			if(input.hasNextInt()) 
			{
				//store the input into variable num
				int num = input.nextInt();
				//try to avoid the unnecessary whitespace
				if(input.hasNextLine()) 
				{
					input.nextLine();
				}
				//check if the number is in the range or not
				if(num < min || num > max)
					System.out.println("Please enter a number between " + min + " and " + max + ".");
				else
					numSticksOnTable = num;
			} 
			// if the input does not contain number, the following program executes
			else 
			{
				//variable errorInput store the input if input contains characters
				String errorInput = input.nextLine();
				System.out.println("Error: expected a number between " + min+ " and " + max + " but found: " + errorInput);
			}
		}
		return numSticksOnTable;  //TODO change to return valid user input.
	}
	
	/**
	 * This method has one person play the Game of Sticks against another
	 * person.
	 * 
	 * @param input
	 *            An instance of Scanner to read user answers.
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param player1Name
	 *            The name of one player.
	 * @param player2Name
	 *            The name of the other player.
	 * 
	 *            As a courtesy, player2 is considered the friend and gets to
	 *            pick up sticks first.
	 * 
	 */
	static void playAgainstFriend(Scanner input, int numSticksOnTable, 
			String player1Name, String player2Name) {

		// userName equals player1Name
		String userName = player1Name;
		int sticksTaken; // set the sticks that have been taken.
		System.out.println("");
		System.out.print("What is your friend's name? ");
		String friendName =input.nextLine(); //input the friend's name
		System.out.println("Hello "+friendName.trim()+".");
		System.out.println("");
		System.out.println("There are " + numSticksOnTable +" sticks on the board.");
		String playerName; // we set a "middle" person to return a winner
		player1Name = userName; 
		player2Name =friendName;
		playerName = player2Name;
		do{
			// it executes when the remaining sticks larger than 3, in order to print different statements, such as (1-3),(1-2),(1-1)
			if(numSticksOnTable >= 3)
			{
				System.out.print(playerName.trim() + ": How many sticks do you take (" + Config.MIN_ACTION+"-" + Config.MAX_ACTION+")? ");
			}
			else
			{
				System.out.print(playerName.trim()+": How many sticks do you take (1-"+numSticksOnTable+")? ");
			}
			// it works when the input is a number.
		    if (input.hasNextInt())
		    {
		    	 sticksTaken=input.nextInt();
		    	 if(input.hasNextLine())
		    	 {
		    		 input.nextLine();
		    	 }
		    	 
		    	 // it checks if the action of the sticks is within the range
				 if(sticksTaken >= Config.MIN_ACTION && sticksTaken <= Config.MAX_ACTION)
				 {
					 // decrease the sticks in total every time the loop runs
					 numSticksOnTable = numSticksOnTable - sticksTaken;
					 //to change the number of the statement
					 switch(numSticksOnTable)
					 {
					 case 1:
						 System.out.println("There is 1 stick on the board.");
						 break;
						 //if the number is zero, break out.
					 case 0:
						 break;
					 default:
						 System.out.println("There are " + numSticksOnTable +" sticks on the board.");
					 }
					 // change the player into different variable to take the turns
					 if(playerName.equals(player1Name))
					 {
						 playerName=player2Name;
					 }
					 else
					 {
						 playerName=player1Name;
					 }
				 }
				 //if the number is out of the range, prompt the statement and go back to the loop
				 else
				 {
					 System.out.println("Please enter a number between " + Config.MIN_ACTION +" and "+ Config.MAX_ACTION +".");
				 }
		    }
		    	
		    
		    else{ 
		    		String wrongSticks=input.nextLine();
		    		System.out.println("Error: expected a number between " + Config.MIN_ACTION + " and " + Config.MAX_ACTION + " but found: "+ wrongSticks);
		    	}
		} while(numSticksOnTable != 0); // if the sticks are not decreased to zero, keep the loop running.
		
		// to decide the winner
		  if(playerName.equalsIgnoreCase(player1Name))
		  {
			  System.out.println(player1Name.trim()+" wins. "+player2Name.trim() +" loses.");
			  
		  }
		  else
		  {
			  System.out.println(player2Name.trim()+" wins. "+player1Name.trim() +" loses.");
		  }
		
		
	}	
		
	
	/**
	 * Make a choice about the number of sticks to pick up when given the number
	 * of sticks remaining.
	 * 
	 * Algorithm: If there are less than Config.MAX_ACTION sticks remaining, 
	 * then pick up the minimum number of sticks (Config.MIN_ACTION). 
	 * If Config.MAX_ACTION sticks remain, randomly choose a number between 
	 * Config.MIN_ACTION and Config.MAX_ACTION. Use Config.RNG.nextInt(?) 
	 * method to generate an appropriate random number.
	 * 
	 * @param sticksRemaining
	 *            The number of sticks remaining in the game.
	 * @return The number of sticks to pick up, or 0 if sticksRemaining is <= 0.
	 */
	static int basicChooseAction(int numSticksOnTable) 
	{
		int sticksTaken = 0;
		//if the sticks are greater the max action, return a random number
		if(numSticksOnTable > Config.MAX_ACTION)
		{
			sticksTaken = Config.RNG.nextInt(Config.MAX_ACTION) +1;
		}
		else if(numSticksOnTable <= Config.MAX_ACTION & numSticksOnTable > 0)
		{
			sticksTaken = Config.MIN_ACTION;
		}
		// if any of the situation does not fit above, set the stick action to zero.
		else
		{
			sticksTaken = 0;
		}
		
		return sticksTaken;  //TODO change to appropriate value
	}
	
	/**
	 * This method has a person play against a computer.
	 * Call the promptUserForNumber method to obtain user input.  
	 * Call the aiChooseAction method with the actionRanking row 
	 * for the number of sticks remaining. 
	 * 
	 * If the strategyTable is null, then this method calls the 
	 * basicChooseAction method to make the decision about how 
	 * many sticks to pick up. If the strategyTable parameter
	 * is not null, this method makes the decision about how many sticks to 
	 * pick up by calling the aiChooseAction method. 
	 * 
	 * @param input
	 *            An instance of Scanner to read user answers.
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param playerName
	 *            The name of one player.
	 * @param strategyTable
	 *            An array of action rankings. One action ranking for each stick
	 *            that the game begins with.
	 * 
	 */
	static void playAgainstComputer(Scanner input, int startSticks, 
			String playerName, int[][] strategyTable) {
		// the number of sticks that takes from the whole sticks.
		int sticksTaken;
		boolean AI = true;
		if (strategyTable == null){
			//set the AI to flase if the strategyTable is null
			AI = false;
		}
		System.out.println("There are " + startSticks+ " sticks on the board.");
		
		//start the do while loop
		do{
			// it executes when the remaining sticks larger than 3, in order to print different statements, such as (1-3),(1-2),(1-1)
			if(startSticks>=3){
				System.out.print(playerName.trim() + ": How many sticks do you take ("+Config.MIN_ACTION+"-" + Config.MAX_ACTION + ")? ");
			}
			else{
				System.out.print(playerName.trim() + ": How many sticks do you take (1-" + startSticks + ")? ");
			}
			// it works when the input is a number.
		    if (input.hasNextInt())//
		    {
		    	 sticksTaken=input.nextInt();
		    	 if(input.hasNextLine())
		    	 {
		    		 input.nextLine();
		    	 }
		    	 // it checks if the action of the sticks is within the range
				 if(sticksTaken>=Config.MIN_ACTION && sticksTaken<=Config.MAX_ACTION)
				 {
					//To do the calculation decreasing the sticks
					 startSticks=startSticks-sticksTaken;
					 switch(startSticks)
					 {
					 //when startSticks is 1.
					 case 1:
						 System.out.println("There is 1 stick on the board.");
						 break;
					 //when startSticks is 0.
					 case 0:
						 break;
				     // when startSticks is other number.
					 default:
						 System.out.println("There are " + startSticks +" sticks on the board.");
					 }
					 if(startSticks==0){
						 break;
					 }
					 //The number of sticks that will draw from the remaining sticks
					 int selectSticks =0;
					 if (AI==false){
						 selectSticks= basicChooseAction(startSticks);
					 }
					 else{
						 selectSticks= aiChooseAction(startSticks, strategyTable[startSticks-1]);
					 }
					 //when the selectSticks number is greater than 1.
					 if(selectSticks>Config.MIN_ACTION){
					     System.out.println("Computer selects "+selectSticks+" sticks.");
					 }
					 // when the selectSticks number is 1.
					 else{
						 System.out.println("Computer selects "+selectSticks+" stick.");
						 
					 }
					 
					 if(selectSticks>=Config.MIN_ACTION && selectSticks<=Config.MAX_ACTION)
					 {
						 startSticks=startSticks-selectSticks;
						 switch(startSticks)
						 {
						 case 1:
							 System.out.println("There is 1 stick on the board.");
							 break;
						 case 0:
							 break;
						 default:
							 System.out.println("There are " + startSticks +" sticks on the board.");
						 }
					 }
				 }
				 else
				 {
					 // determine who will win the game.
					 if (startSticks>=Config.MAX_ACTION){
					     System.out.println("Please enter a number between "+Config.MIN_ACTION +" and "+ Config.MAX_ACTION +".");
					 }
					 else{
						 System.out.println("Please enter a number between "+Config.MIN_ACTION +" and "+ startSticks +".");
					 }
				 }
		    }
		    else{ 
		    	
		    	String wrongSticks=input.nextLine();
		    	System.out.println("Error: expected a number between "+Config.MIN_ACTION +" and "+ Config.MAX_ACTION +" but found: "+ wrongSticks);
		    	}
		} while(startSticks>0);
		
		
		  if(!playerName.equalsIgnoreCase("computer"))
		  {
			  System.out.println("Computer wins. "+playerName.trim()+" loses.");
			  
		  }
		  else {
			  System.out.println(playerName.trim()+" wins. " +"Computer loses.");
		  }
		
	}
	
		
	/**
	 * This method chooses the number of sticks to pick up based on the
	 * sticksRemaining and actionRanking parameters.
	 * 
	 * Algorithm: If there are less than Config.MAX_ACTION sticks remaining 
	 * then the chooser must pick the minimum number of sticks (Config.MIN_ACTION). 
	 * For Config.MAX_ACTION or more sticks remaining then pick based on the 
	 * actionRanking parameter.
	 * 
	 * The actionRanking array has one element for each possible action. The 0
	 * index corresponds to Config.MIN_ACTION and the highest index corresponds
	 * to Config.MAX_ACTION. For example, if Config.MIN_ACTION is 1 and 
	 * Config.MAX_ACTION is 3, an action can be to pick up 1, 2 or 3 sticks. 
	 * actionRanking[0] corresponds to 1, actionRanking[1] corresponds to 2, etc. 
	 * The higher the element for an action in comparison to other elements, 
	 * the more likely the action should be chosen.
	 * 
	 * First calculate the total number of possibilities by summing all the
	 * element values. Then choose a particular action based on the relative
	 * frequency of the various rankings. 
	 * For example, if Config.MIN_ACTION is  1 and Config.MAX_ACTION is 3: 
	 * If the action rankings are {9,90,1}, the total is 100. Since 
	 * actionRanking[0] is 9, then an action of picking up 1 should be chosen 
	 * about 9/100 times. 2 should be chosen about 90/100 times and 1 should 
	 * be chosen about 1/100 times. Use Config.RNG.nextInt(?) method to 
	 * generate appropriate random numbers.
	 * 
	 * @param sticksRemaining
	 *            The number of sticks remaining to be picked up.
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @return The number of sticks to pick up. 0 is returned for the following
	 *         conditions: actionRanking is null, actionRanking has a length of
	 *         0, or sticksRemaining is <= 0.
	 * 
	 */
	static int aiChooseAction(int sticksRemaining, int[] actionRanking) 
	{
		int action = 0;
		//make sure the action is not null
		if(actionRanking != null)
		{
			int sum = 0; //declare the sum for the array
			for(int i=0; i < actionRanking.length;++i) //go sum up the index the actionRanking array
				sum = sum + actionRanking[i];
			
			int sumArray [] = new int [sum]; // this way we can get the number for the array
			if(sticksRemaining <= 0 || actionRanking.length <= 0) //if the sticks or array is not valid, return zero.
			{
				action = 0;
				return action;
			}
			else if(sticksRemaining < Config.MAX_ACTION) //if the sticks are less than max actions, return min action
			{
				action = Config.MIN_ACTION;
				return action;
			}
			else
			{
				
				int z = 0;// use this variable for index
				for(int x=0;x < actionRanking.length;x++) //use nested loop to create a new array
					for(int y=0;y < actionRanking[x];y++) //spread the old array[1,2,3] to [1,2,2,3,3,3]
					{
						sumArray[z] = x + 1;
						z++;
					}
			}
			action = sumArray[Config.RNG.nextInt(sum)]; //randomly pick a number from the new array
		}
		else
			action = 0;
		
		return action; 
	}
	

	/**
	 * This method initializes each element of the array to 1. If actionRanking
	 * is null then method simply returns.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. Use the length of the
	 *            actionRanking array rather than rely on constants for the
	 *            function of this method.
	 */
	static void initializeActionRanking(int []actionRanking) 
	{
		if(actionRanking == null)
			return;
		//to initialize all the indexes in the actionRanking array to one
		for(int i=0;i<actionRanking.length;i++)
		{
			actionRanking[i] = 1;
		}
	}
	
	/**
	 * This method returns a string with the number of sticks left and the
	 * ranking for each action as follows.
	 * 
	 * An example: 10     3,4,11
	 * 
	 * The string begins with a number (number of sticks left), then is followed
	 * by 1 tab character, then a comma separated list of rankings, one for each
	 * action choice in the array. The string is terminated with a newline (\n) 
	 * character.
	 * 
	 * @param sticksLeft
	 *            The number of sticks left.
	 * @param actionRanking
	 *            The counts of each action to take. Use the length of the
	 *            actionRanking array rather than rely on constants for the
	 *            function of this method.
	 * @return A string formatted as described.
	 */
	static String actionRankingToString(int sticksLeft, int[]actionRanking) {
		
		//store the second part of the array
		String b = "";
		for(int i=0; i < actionRanking.length; ++i)
		{
			if(i > 0)
			{
				b = b +",";
			}
			b = b + "" + actionRanking[i];
		}
		//combine the index of the sticks with the array.
		String a = sticksLeft + "\t" + b;
		return a + "\n"; 
	}


	/**
	 * This method updates the actionRanking based on the action. Since the game
	 * was lost, the actionRanking for the action is decremented by 1, but not
	 * allowing the value to go below 1.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @param action
	 *            A specific action between and including Config.MIN_ACTION and
	 *            Config.MAX_ACTION.
	 */
	static void updateActionRankingOnLoss(int []actionRanking, int action) {
		// it executes when action is larger than or equal to the min sticks to pick up
		if(action >= Config.MIN_ACTION){
			if(actionRanking[action-1] > 1){
				//the actionTanking for action will decrease 1, since the game was lost.
				actionRanking[action - 1] = actionRanking[action - 1] - 1;
			}
		}	
	}
	
	/**
	 * This method updates the actionRanking based on the action. Since the game
	 * was won, the actionRanking for the action is incremented by 1.
	 * 
	 * @param actionRanking
	 *            The counts of each action to take. The 0 index corresponds to
	 *            Config.MIN_ACTION and the highest index corresponds to
	 *            Config.MAX_ACTION.
	 * @param action
	 *            A specific action between and including Config.MIN_ACTION and
	 *            Config.MAX_ACTION.
	 */
	static void updateActionRankingOnWin(int []actionRanking, int action) {
		// it executes when action is larger than or equal to the min sticks to pick up
		if (action>= Config.MIN_ACTION){
			//the actionTanking for action will increase 1, since the game was won.
			actionRanking[action - 1] = actionRanking[action - 1] + 1;	
		}	
	}
	
	/**
	 * Allocates and initializes a 2 dimensional array. The number of rows
	 * corresponds to the number of startSticks. Each row is an actionRanking
	 * with an element for each possible action. The possible actions range from
	 * Config.MIN_ACTION to Config.MAX_ACTION. Each actionRanking is initialized
	 * with the initializeActionRanking method.
	 * 
	 * @param startSticks
	 *            The number of sticks the game is starting with.
	 * @return The two dimensional strategyTable, properly initialized.
	 */
	static int[][] createAndInitializeStrategyTable(int startSticks) {
		// Define a new 2 dimensional array named strtegyTable with the number of starting sticks rows and the number of max sticks to pick up columns.
		int[][] strategyTable = new int [startSticks][Config.MAX_ACTION];
		// We want to initialize  each unit of the 2 dimensional array.
		for(int i=0;i < startSticks;++i)
			for(int j=0;j < Config.MAX_ACTION;++j)
			{
				//we initialize each unit of 2 dimensional array to 1.
				strategyTable[i][j] = 1;
			}
		
		return strategyTable;
	}	
		
	/**
	 * This formats the whole strategyTable as a string utilizing the
	 * actionRankingToString method. For example:
	 * 
	 * Strategy Table 
	 * Sticks Rankings 
	 * 10	  3,4,11 
	 * 9      6,2,5 
	 * 8      7,3,1 etc.
	 * 
	 * The title "Strategy Table" should be proceeded by a \n.
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * @return A string containing the properly formatted strategy table.
	 */
	static String strategyTableToString(int[][] strategyTable) {
		// The new string that we will store the unit of strategyTable
		String Table = "";
		Table = "\nStrategy Table\n";
		Table = Table + "Sticks	Rankings\n";
		for(int i=strategyTable.length;i >= 0;--i)
		{
			if(i == 0) break;
			// Format the whole strategyTable to a string.
			Table = Table + actionRankingToString(i, strategyTable[i - 1]);
		}
		
		
		
		
		return Table; //TODO change to return the formatted String
	}	
	
	
	/**
	 * This updates the strategy table since a game was won.
	 * 
	 * The strategyTable has the set of actionRankings for each number of sticks
	 * left. The actionHistory array records the number of sticks the user took 
	 * when a given number of sticks remained on the table. Remember that 
	 * indexing starts at 0. For example, if actionHistory at index 6 is 2, 
	 * then the user took 2 sticks when there were 7 sticks remaining on the 
	 * table.  
	 * For each action noted in the history, this calls the 
	 * updateActionRankingOnWin method passing the corresponding action 
	 * and actionRanking. After calling this method, the actionHistory is
	 * cleared (all values set to 0).
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * 
	 * @param actionHistory
	 *            An array where the index indicates the sticks left and the
	 *            element is the action that was made.
	 */
	static void updateStrategyTableOnWin(int[][] strategyTable, int[] actionHistory){
		for (int i = strategyTable.length; i > 0; i--){
			updateActionRankingOnWin(strategyTable[i-1],actionHistory[i-1]);
		}
		for (int j = 0;j < actionHistory.length; j++){
		    actionHistory[j]=0;
			
		}
	}
	
	/**
	 * This updates the strategy table for a loss.
	 * 
	 * The strategyTable has the set of actionRankings for each number of sticks
	 * left. The actionHistory array records the number of sticks the user took 
	 * when a given number of sticks remained on the table. Remember that 
	 * indexing starts at 0. For example, if actionHistory at index 6 is 2, 
	 * then the user took 2 sticks when there were 7 sticks remaining on the 
	 * table. 
	 * For each action noted in the history, this calls the 
	 * updateActionRankingOnLoss method passing the corresponding action 
	 * and actionRanking. After calling this method, the actionHistory is 
	 * cleared (all values set to 0).
	 * 
	 * @param strategyTable
	 *            An array of actionRankings.
	 * @param actionHistory
	 *            An array where the index indicates the sticks left and the
	 *            element is the action that was made.
	 */
	static void updateStrategyTableOnLoss(int[][] strategyTable, int[] actionHistory) {
	    for (int i = strategyTable.length; i > 0; i--){
			updateActionRankingOnLoss(strategyTable[i-1],actionHistory[i-1]);
		}
	    for (int j = 0;j < actionHistory.length; j++){
				actionHistory[j]=0;
			
		}
	}	

	/**
	 * This method simulates a game between two players using their
	 * corresponding strategyTables. Use the aiChooseAction method
	 * to choose an action for each player. Record each player's 
	 * actions in their corresponding history array. 
	 * This method doesn't print out any of the actions being taken. 
	 * Player 1 should make the first move in the game.
	 * 
	 * @param startSticks
	 *            The number of sticks to start the game with.
	 * @param player1StrategyTable
	 *            An array of actionRankings.
	 * @param player1ActionHistory
	 *            An array for recording the actions that occur.
	 * @param player2StrategyTable
	 *            An array of actionRankings.
	 * @param player2ActionHistory
	 *            An array for recording the actions that occur.
	 * @return 1 or 2 indicating which player won the game.
	 */
	
	static int playAiVsAi(int startSticks, int[][] player1StrategyTable, 
			int[] player1ActionHistory, int[][] player2StrategyTable, 
			int[] player2ActionHistory) {
		
		int player1Move = 0;// define the number of player1 that chooses the sticks.
		int player2Move = 0;// define the number of player2 that chooses the sticks.
	    for(int i=0; startSticks > 0;i++)
		{
			//player1
			if(i%2 == 0)
			{
				
				player1Move = aiChooseAction(startSticks, player1StrategyTable[startSticks - 1]);
				player1ActionHistory[startSticks - 1] = player1Move;
				startSticks -= player1Move;
				
			}
			
			//player2
			if(i%2 != 0)
			{
				player2Move = aiChooseAction(startSticks, player2StrategyTable[startSticks - 1]);
				player2ActionHistory[startSticks - 1] = player2Move;
				startSticks -= player2Move;
				
			}
			if(startSticks == 0)
			{
			if(i%2 == 0)
				return 2;
			else
				return 1;
			}
			
		}
		return -1;
		
 //TODO change to return the winning player.
	}

	/**
	 * This method has the computer play against itself many times. Each time 
	 * it plays it records the history of its actions and uses those actions 
	 * to improve its strategy.
	 * 
	 * Algorithm: 
	 * 1) Create a strategy table for each of 2 players with 
	 *    createAndInitializeStrategyTable. 
	 * 2) Create an action history for each player.  An action history is a 
	 *    single dimension array of int. Each index in action history 
	 *    corresponds to the number of sticks remaining where the 0 index is
	 *    1 stick remaining.
	 * 3) For each game, 
	 * 		4) Call playAiVsAi with the return value indicating the winner. 
	 * 		5) Call updateStrategyTableOnWin for the winner and 
	 * 		6) Call updateStrategyTableOnLoss for the loser. 
	 * 7) After the games are played then the strategyTable for whichever 
	 * 	  strategy won the most games is returned. When both players win the 
	 *    same number of games, return the first player's strategy table.
	 * 
	 * @param startSticks
	 *            The number of sticks to start with.
	 * @param numberOfGamesToPlay
	 *            The number of games to play and learn from.
	 * @return A strategyTable that can be used to make action choices when
	 *         playing a person. Returns null if startSticks is less than
	 *         Config.MIN_STICKS or greater than Config.MAX_STICKS. Also returns
	 *         null if numberOfGamesToPlay is less than 1.
	 */
static int[][] trainAi(int startSticks, int numberOfGamesToPlay) {
		
		//initialize two players' strategy tables and action histories
		int [][] Table1 = createAndInitializeStrategyTable(startSticks);//new int[numberOfGamesToPlay - 1];
		int [][] Table2 = createAndInitializeStrategyTable(startSticks);
		int [] actionHistory1 = new int[startSticks];
		int [] actionHistory2 = new int[startSticks];
		int TempPlayer = 0; // set a temporary "middle" variable to store the result from mentod AiVSAi
		int player1 = 0;
		int player2 = 0;
		//if under these conditions, return null
		if(startSticks < Config.MIN_STICKS || startSticks > Config.MAX_STICKS || numberOfGamesToPlay < 1)
			return null;
		else
		{
			//for how many games that have been inputed, it runs how many times.
			for(int i=0; i < numberOfGamesToPlay; i++) 
			{
				//the temporary player stored a number suggesting the winner
				TempPlayer = playAiVsAi(startSticks, Table1, actionHistory1, Table2, actionHistory2);
				if(TempPlayer == 1)
				{
					//increment the variable in order to compare later to return a winner array.
					player1 += 1;
					//update the table for loss or win
					updateStrategyTableOnWin(Table1, actionHistory1);
					updateStrategyTableOnLoss(Table2, actionHistory2);
				}
				// it works as above
				else if(TempPlayer == 2)
				{
					player2 += 1;
					updateStrategyTableOnWin(Table2, actionHistory2);
					updateStrategyTableOnLoss(Table1, actionHistory1);
				}
			}
		}
		//after the increment, the winner will be the larger one
		if(player1 > player2)
			return Table1;
		else if(player1 == player2)
			return Table1;
		else
			return Table2;
		
		
	}
}
