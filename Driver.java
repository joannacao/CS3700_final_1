package cs3700_final1;

import java.util.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.rmi.UnknownHostException; 
import java.io.IOException;

public class Driver {
	
	public static void main(String[] args) {
		//ask user for how many games 
		Scanner scan = new Scanner(System.in);
		System.out.println("How many games do you want to play?");
		int numGames = scan.nextInt(); 
		int defaultPort = 5000; 
		
		try {
			//create players 
			DatagramSocket socketOne = new DatagramSocket(5000);
			DatagramSocket socketTwo = new DatagramSocket(20); 
			DatagramSocket socketThree = new DatagramSocket(80); 
			Player playerOne = new Player(socketOne, socketTwo, socketThree, 1, 0); 
			Player playerTwo = new Player(socketTwo, socketOne, socketThree, 2,0); 
			Player playerThree = new Player(socketThree, socketOne, socketTwo, 3, 0);
			int i = 1;
			while (i <= numGames) {
				//have player calculate hand
				playerOne.calculateHand();
				playerTwo.calculateHand(); 
				playerThree.calculateHand(); 
				//send message of what hand they got to other players 
				playerOne.broadcastHand();
				playerTwo.broadcastHand();
				playerThree.broadcastHand();
				//check for messages from other players, identify who it came from
				//calculate individual scores 
				playerOne.receiveHands(); 
				playerTwo.receiveHands(); 
				playerThree.receiveHands(); 
				//reset hands 
				playerOne.reset(); 
				playerTwo.reset();
				playerThree.reset();
				System.out.println("Game " + i + ": ");
				System.out.println("Player one score: " + playerOne.getScore()); 
				System.out.println("Player two score: " + playerTwo.getScore());
				System.out.println("Player three score: " + playerThree.getScore());
				System.out.println(); 
				i++; 
			}
			//print total score of each player 
			System.out.println("Player one total score: " + playerOne.getScore()); 
			System.out.println("Player two total score: " + playerTwo.getScore()); 
			System.out.println("Player three total score: " + playerThree.getScore()); 
			socketOne.close();
			socketTwo.close();
			socketThree.close(); 
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

}
