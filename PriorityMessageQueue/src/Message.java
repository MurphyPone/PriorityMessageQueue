/** 
 * Class Description: This class does XYZ
 * @author MurphyP1
 * @date 4/16/18
 */

public class Message {
	private int arrival; //Time of arrival
	private int priority; //0,4 with 0 being highest priority 
	private String content; //Content of the message
	private int wait; //Amount of time before the message was processed
	
	//Default constructor assigns random priority
	Message(int a) {
		priority = (int) (Math.random()  * 4) + 1; //random priority 0-4;
		arrival = a;
		content = "This Message has priority " + priority; 
	}
	
	/*Constructor which accepts a priority
	Message(int a, int p) {
		priority = p;
		arrival = a;
		content = "This Message has priority " + priority; 
	} */
	
	// Getters and Setters //
	public int getArrival() { return arrival; }
	public void setArrival(int arrival) { this.arrival = arrival; }
	
	public int getPriority() { return priority; }
	public void setPriority(int priority) { this.priority = priority; }

	public void setContent(String content) { this.content = content; }

	public String toString() {	return content; }
	public void setWait(int t) { //"global" time passed from the PMQ
		wait = t - arrival; //time spent waiting in the Queue 
	}
	public int getWait() { return this.wait; }
}
