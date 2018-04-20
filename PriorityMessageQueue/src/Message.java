/** 
 * Class Description: This class does XYZ
 * @author MurphyP1
 * @date 4/16/18
 */

public class Message {
	private int arrival; //Arrival in PQM
	private int priority; //0,4 with 0 being highest priority 
	private String content; //Content of the message
	private int wait; //Amount of time before the message was processed
	
	//Default constructor assigns random priority
	Message(int a) {
		priority = (int) (Math.random() * PriorityMessageQueue.numQs); //random priority 0-numQs;
		arrival = a;
		content = "This Message has priority " + priority; 
	}
	
	// Getters and Setters //
	public int getArrival() { return arrival; }
	public void setArrival(int arrival) { this.arrival = arrival; }
	
	public int getPriority() { return priority; }
	public void setPriority(int priority) { this.priority = priority; }

	public void setContent(String content) { this.content = content; }

	public String toString() { return content; }
	public void updateContent() { this.content = "This Message has priority " + priority +", and waited " + this.wait + " minutes";  }
	
	public void setWait(int t) { //"global" time passed from the PMQ
		wait = t - arrival; //time spent waiting in the Queue
		updateContent();
	}
	
	public int getWait() { return this.wait; }
}
