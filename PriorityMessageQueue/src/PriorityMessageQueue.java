/** 
 * Class Description: This class processes and analyzes the wait time of Messages of various priorities
 * @author MurphyP1
 * @date 4/22/18
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class PriorityMessageQueue {
	public static final int numQs = 5; //Number of Queues supported by the PMQ
	private int time;	//Keeps track of iterations/minutes elapsed in the processing of Messages
	private int limit; //How long the PMQ will run (unless empty), before stopping the process
	private ArrayList<Queue<Message>> Qs;//The Queues for Messages to be stored within
	private ArrayList<Message> output; //All Messages are sent here for analysis after being processed

	/**
	 * The default constructor for a PriorityMessageQueue which initializes the Queues, output, and limit for the PMQ as 10,000
	 */
	PriorityMessageQueue() {
		Qs = new ArrayList<Queue<Message>>(numQs);	//Instantiate the ArrayList holding the Queues
		for(int i = 0; i < numQs; i++)
			Qs.add(new LinkedList<Message>() ); //Create the Queues
		time = 0; //keep track of the # minutes that have transpired 
		limit = 10000; //minutes 
		output = new ArrayList<Message>(limit); //save time resizing later
	}

	//Standard Queue Methods//

	/** 
	 * A standard Queue method which adds a Message into the PMQ according to its respective priority
	 * @param m the Message to be added
	 */ 
	public void add( Message m ) {
		int p = m.getPriority();	//get priority of item to be added
		Queue<Message> pq = Qs.get(p);	//Get the specific PriorityQueue
		pq.add(m);	//Add the message to the Queue (LinkedList) associated with that priority 
	}
	
	/**
	 * A standard Queue method which returns the Message of highest priority within the PMQ without removing it 
	 * @return the Message with the highest priority (highest meaning "most", so highest priority = p0)
	 */
	public Message peek() {
		Queue<Message> q;
		for(int i = 0; i < numQs; i++) { //force the order bc for each doesn't seem to be ordered
			q = Qs.get(i);
			if( !q.isEmpty() ) {
				Message m = q.peek();
				m.setWait(time);		//Calculate how long it has been waiting
				return m;
			}
		}
		throw new NoSuchElementException();	//If it iterates through all of them and they're all empty, throw exc.
	}
	
	/**
	 * A standard Queue method which returns true if all of the underlying Queues are empty
	 * @return true if all are empty, false if any are not empty
	 */
	public boolean isEmpty() {
		for(Queue<Message> q : Qs) //For every Queue in Qs
			if( !q.isEmpty() )	//If any of them are not empty
				return false;	//the PMQ is not empty
		return true;		//if it iterates through all of them without breaking out, then they're all empty
	}
	
	/**
	 * A standard Queue method which returns the Message of highest priority within the PMQ and removes it 
	 * @return the Message with the highest priority (highest meaning "most", so highest priority = p0)
	 */
	public Message remove() {
		Queue<Message> q;
		for(int i = 0; i < numQs; i++) { //force the order bc for each doesn't seem to be ordered
			q = Qs.get(i);
			if( !q.isEmpty() ) {		//Find first Queue that isn't empty
				Message m = q.peek();
				m.setWait(time);		//Calculate how long it has been waiting
				if(m.getWait() >= 4) {
					m = q.remove();
					m.setWait(time);		//Calculate how long it has been waiting
					return m; //Return the configured message 
				}
			}
		}
		throw new NoSuchElementException();
	}
	
	//Additional PMQ Methods
	
	/**
	 * The operating method of the PMQ which 
	 * 		1. Adds a few initial Messages to the PMQ 
	 *		2. Continues to add messages until the limit has been reached while also removing the highest priority Message that has been waiting for at least four minutes
	 * 		3. Removes all remaining Messages that have been waiting at least four minutes without adding any new Messages
	 */
	public void process() {	
		//Populate and process
		while( time < limit ) {	//Add another <limit = 10,000> Messages and begin to process
			add(new Message( time++ ) ); //Add one new, random Message every minute and increment the time counter	

			if(peek().getWait() >= 4 )	//Remove the highest priority Message that has been processed for >= 4 minutes
				output.add( remove() );
		}
		
		//Process until empty
		while ( !isEmpty() ) {	//While the PMQ has Messages in it
			time++;	//Keep incrementing time 
			if(peek().getWait() >= 4 )	//Remove the highest priority Message that has been processed for >= 4 minutes
				output.add( remove() );
		}
		
		//Analyze --> does not count towards Big-O run time of process, but is called within for concise testing
		System.out.print( analyze() ); //After processing all, output analysis		
	}
	
	/**
	 * An additional helper method which returns describes the average wait time and Message distribution for each each underlying Queue 
	 * @return a String with analysis 
	 */
	private String analyze() {
		String result = "PriorityMessageQueue wait time analysis:";
		int numMessages = output.size(); //Tracks total # messages processed
		double[] avgs = new double[numQs]; //stores the avg time for Messages within each of the individual pQueues
		int[] msgCount = new int[numQs]; //Keeps track of the # of messages per Queue
		
		//sum wait times 
		for(Message m : output) {  
			int p = m.getPriority(); //Queue specific avg
			avgs[p] += m.getWait(); //fill first
			msgCount[p]++;	//keep track of how many messages in each queue
		}
				
		//Build String
		result += "\n\t" + numMessages + " Messages processed with " + numQs + " Queues in " + time + " minutes";

		for(int i = 0; i < avgs.length; i++ ) {
			avgs[i] = (double) avgs[i] / msgCount[i];	//individual Queue avg
			result += "\n\tp" + i + " avg: " + avgs[i] + " minutes, " + msgCount[i] + " Messages processed" ;
		}
		
		//comparison
		double heapTime = ((time * Math.log( (double) time) ) / Math.log(2)) / numQs; //convert from log base e to log base 2 for big O analysis (constants are negligible) 
		result += "\n\n\t PMQ implementation: O(n) vs. Heap implementation: O(n log n) "
				+ "\n\t--> O(" + time +") = " + time +" minutes vs. "
						+ "O(" + time + " log " + time + ") = " + heapTime +  " minutes";
		
		return result;
	}
	
	/**
	 * Displays the size of each of the underlying Queues
	 * @return a String of formatted information about the Queues
	 */
	public String toString() {
		String result = "";
		int i = 0;
		for(Queue<Message> q : Qs) 
			result += "q" + i++ + ": " + q.size() + ", ";
		return result;
	}
	
	/**
	 * The standard testing method
	 * @param args a String array which contains arguments passed in by the command line
	 */
	public static void main(String[] args) {
		PriorityMessageQueue x = new PriorityMessageQueue(); //test PMQ
		x.process();
	}
}
