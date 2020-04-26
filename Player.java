package cs3700_final1;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.io.IOException;
import java.net.DatagramPacket; 
import java.util.*;

public class Player {
	private DatagramSocket socket; 
	private int playerId; 
	private int totalScore; 
	private int hand; 
	private DatagramSocket opp1;
	private DatagramSocket opp2; 
	private int opp1Hand; 
	private int opp2Hand; 
	private InetAddress aHost;
	
	public Player(DatagramSocket ds, DatagramSocket ds1, DatagramSocket ds2, int p, int s) {
		this.socket = ds;
		this.playerId = p;
		this.totalScore = s; 
		this.opp1 = ds1;
		this.opp2 = ds2; 
		this.hand = -1; 
		this.opp1Hand = -1; 
		this.opp2Hand = -1;
		try {
			this.aHost = InetAddress.getLocalHost();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void reset() {
		this.hand = -1; 
		this.opp1Hand = -1; 
		this.opp2Hand = -1; 
	}
	
	public void calculateHand() {
		Random rand = new Random(); 
		int tempHand = rand.nextInt() % 3; 
		if (this.playerId == 1) {
			//do nothing
		} else if (this.playerId == 2) {
			//add three
			tempHand += 3; 
		} else {
			//add six
			tempHand += 6; 
		}
		this.hand = tempHand; 
	}
	
	public int getScore() {
		return totalScore; 
	}
	
	public void setScore(int s) {
		this.totalScore = s; 
	}
	
	public void broadcastHand() {
		//put hand in datagram packet and send to both opponents
		String handStr = Integer.toString(this.hand); 
		DatagramPacket sendPkt = new DatagramPacket(handStr.getBytes(), handStr.length(), this.aHost, this.opp1.getLocalPort()); 
		DatagramPacket sendPkt2 = new DatagramPacket(handStr.getBytes(), handStr.length(), this.aHost, this.opp2.getLocalPort());
		try {
			this.socket.send(sendPkt); 
			this.socket.send(sendPkt2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//wait to receive packets from opponents. take their hands and 
		//input to calculateScore() 
	}
	
	public void receiveHands() {
		//receive packets from opponents id whose hand is whose
		byte[] buff = new byte[1024];
		byte[] buff2 = new byte[1024]; 
		DatagramPacket first = new DatagramPacket(buff, 1024); 
		DatagramPacket second = new DatagramPacket(buff2, 1024); 
		try {
			this.socket.receive(first);
			this.socket.receive(second);
			String receivedHand = new String(first.getData(), 0, first.getLength());
			String receivedHand2 = new String(second.getData(), 0, second.getLength());
			int tempHand = Integer.parseInt(receivedHand);
			int tempHand2 = Integer.parseInt(receivedHand2); 
			this.opp1Hand = tempHand; 
			this.opp2Hand = tempHand2; 
			//calculate scores 
			calculateScore(this.opp1Hand, this.opp2Hand);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void calculateScore(int hand1, int hand2) {
		//revert scores back to 0-2 to compare 
		//rock = 0, paper = 1, scissors = 2
		if (this.hand <= 2 && this.hand>= 0) {
			//do nothing 
		} else if (this.hand <= 5 && this.hand >= 3) {
			this.hand -= 3;
		} else if (this.hand <= 8 && this.hand >= 6){
			this.hand -= 6; 
		}
		if (this.opp1Hand <= 2 && this.opp1Hand>= 0) {
			//do nothing
		} else if (this.opp1Hand <= 5 && this.opp1Hand >= 3) {
			this.opp1Hand -= 3;
		} else if (this.opp1Hand <= 8 && this.opp1Hand >= 6){
			this.opp1Hand -= 6; 
		}
		if (this.opp2Hand <= 2 && this.opp2Hand>= 0) {
			//do nothing
		} else if (this.opp2Hand <= 5 && this.opp2Hand >= 3) {
			this.opp2Hand -= 3; 
		} else if (this.opp2Hand <= 8 && this.opp2Hand >= 6){
			this.opp2Hand -= 6; 
		}
		if (this.hand == 0) {
			//if beat both players, award two points 
			if (this.opp1Hand == 2 && this.opp2Hand == 2) {
				this.totalScore += 2; 
			} else if (this.opp1Hand == 0 && this.opp2Hand == 2) {
				this.totalScore += 1; 
			} else if (this.opp2Hand == 0 && opp1Hand == 2) {
				this.totalScore += 1; 
			} 
			//if it has the same value as another player and then BEATS the other one, then award one point 
			//other award no points
		} else if (this.hand == 1) {
			if (this.opp1Hand == 0 && this.opp2Hand == 0) {
				this.totalScore += 2; 
			} else if (this.opp1Hand == 1 && this.opp2Hand == 0) {
				this.totalScore += 1; 
			} else if (this.opp2Hand == 1 && this.opp1Hand == 0) {
				this.totalScore += 1; 
			}
		} else if (this.hand == 2) {
			if (this.opp1Hand == 1 && this.opp2Hand == 1) {
				this.totalScore += 2; 
			} else if (this.opp1Hand == 2 && this.opp2Hand == 1) {
				this.totalScore += 1; 
			} else if (this.opp2Hand == 2 && this.opp1Hand == 1) {
				this.totalScore += 1; 
			}
		}
	}

}
