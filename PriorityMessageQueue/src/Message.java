/** 
 * Class Description: This class represent a Message which arrives in a PriorityMessageQueue and keeps track of how long it waited in the Queue before getting delivered.
 * @author MurphyP1
 * @date 4/21/18
 */

public class Message {
	private int arrival; //Arrival in PQM
	private int priority; //0,4 with 0 being highest priority 
	private String content; //Content of the message
	private int wait; //Amount of time before the message was processed
	
	/**
	 * The default constructor for a Message which accepts an argument for the time it arrived in the PMQ
	 * @param a the arrival time
	 */
	Message(int a) {
		priority = (int) (Math.random() * PriorityMessageQueue.numQs); //random priority 0-numQs;
		arrival = a;
		content = "This Message has priority " + priority; 
	}
	
	// Getters and Setters //
	/**
	 * A getter method which returns the time the Message arrived in the PMQ
	 * @return the arrival time
	 */
	public int getArrival() { return arrival; }
	
	/**
	 * A setter method which modifies the arrival field of a Message
	 * @param arrival the arrival time of the Message
	 */
	public void setArrival(int arrival) { this.arrival = arrival; }
	
	/**
	 * A getter method which returns the priority of the Message
	 * @return the priority of the Message
	 */
	public int getPriority() { return priority; }
	
	/**
	 * A setter method which modifies the priority field of a Message
	 * @param priority the priority of the Message
	 */
	public void setPriority(int priority) { this.priority = priority; }

	/**
	 * The toString method for the Message class which replaces a getContent Message by adding additional information/formatting about the content
	 * @return the formatted content of the Message
	 */
	public String toString() { return content; }
	
	/**
	 * A helper method which modifies the content of the Message when its wait field is changed
	 */
	public void updateContent() { this.content = "This Message has priority " + priority +", and waited " + this.wait + " minutes";  }
	
	/**
	 * A setter method which modifies the wait field of a Message according to the time argument which represents the elapsed minutes of the PMQ
	 * @param t how much time has elapsed within the PMQ
	 */
	public void setWait(int t) { //"global" time passed from the PMQ
		wait = t - arrival; //time spent waiting in the Queue
		updateContent();
	}
	
	/**
	 * A getter method which returns a value representing how long a Message was waiting within a PMQ
	 * @return
	 */
	public int getWait() { return this.wait; }
}
