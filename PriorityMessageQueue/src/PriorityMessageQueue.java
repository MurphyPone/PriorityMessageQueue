/** 
 * Class Description: This class does XYZ
 * @author MurphyP1
 * @date 4/16/18
 */
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

public class PriorityMessageQueue {
	private static final int numQs = 5;
	private int time;	//Keeps track of iterations/minutes elapsed in the processing of Messages
	private int limit; //How long the PMQ will run (unless empty), before stopping the process
	private ArrayList<Queue<Message>> Qs;//The Queues for Messages to be stored within
	private ArrayList<Message> output;

	//Constructor
	PriorityMessageQueue() {
		Qs = new ArrayList<Queue<Message>>(numQs);	//Instantiate the Qs
		for(int i = 0; i < numQs; i++)
			Qs.add(new LinkedList<Message>() ); //Create 5 Queues
		time = 0; //keep track of the # minutes that have transpired 
		limit = 10000; //minutes
		output = new ArrayList<Message>(limit); //save time resizing later
	}

	//QUEUE Methods//
	
	//Add -- according to Message priority
	public void add( Message m ) {
		int p = m.getPriority();	//get priority of item to be added
		Queue<Message> pq = Qs.get(p);	//Get the specific PriorityQueue
		pq.add(m);	//Add the message to the Queue (LinkedList) associated with that priority 
	}
	
	//Peek -- returns highest priority message
	public Message peek() {
		for(Queue<Message> q : Qs) 	//For every Queue in Qs
			if( !q.isEmpty() ) 	//Find first Queue that isn't empty
				return q.peek();		//Return the highest priority item
		
		throw new NoSuchElementException();	//If it iterates through all of them and they're all empty, throw exc.
	}
	
	//isEmpty --> returns true if ALL queues are empty
	public boolean isEmpty() {
		for(Queue<Message> q : Qs) //For every Queue in Qs
			if( !q.isEmpty() )	//If any of them are not empty
				return false;	//the PMQ is not empty
		return true;		//if it iterates through all of them without breaking out, then they're all empty
	}
	
	//Remove highest priority
	public Message remove() {
		Queue<Message> q;
		for(int i = 0; i < numQs; i++) { //force the order bc for each doesn't seem to be ordered
			q = Qs.get(i);
			if( !q.isEmpty() ) {		//Find first Queue that isn't empty
				Message m = q.remove();
				m.setWait(time);		//Calculate how long it has been waiting
				System.out.println("current time - arrival time = " + (time - m.getTimeOfArrival()) );
				m.updateContent();	//Changes the toString display info 
				return m; //Return the configured message 
			}
		}
		throw new NoSuchElementException();
	}
	
	//Process 
	public void process() {
		/* old
		while( !isEmpty() && time < limit) { 
			if(time % 4 == 0) {   //every 4 minutes
				output.add( remove() );	//Add the highest priority message to the Queue
			}
			add( new Message(time) );	//Create a new message every minute 
			time++;	//every iteration = 1 minute
		} */
		
		//Create all the messages
		for(int i = 0; i < limit; i++) {
			add(new Message(i) ); //Add one new message for every minute; i = time, without incrementing time		
		}
		
		//Process
		while( !isEmpty() ) {
			if(time % 4 == 0) { 
				output.add( remove() );
			}
			time++;
		}
		
		System.out.print( analyze() ); //After processing all, output analysis
	}
	
	public String analyze() {
		String result = "PriorityMessageQueue wait time analysis:";
		int numMessages = output.size(); //Tracks total # messages processed
		double[] avgs = new double[numQs]; //stores the avg time for Messages within each of the individual pQueues
		int[] msgCount = new int[numQs]; //Keeps track of the # of messages per Queue
		
		//sum wait times 
		for(Message m : output) {  
			int p = m.getPriority(); //Queue specific avg
			avgs[p] += m.getTimeOfArrival(); //fill first
			msgCount[p]++;	//keep track of how many messages in each queue
		}
				
		//Build String
		result += "\n\t" + numMessages + " Messages processed in " + time + " minutes";

		for(int i = 0; i < avgs.length; i++ ) {
			avgs[i] = (double) avgs[i] / numMessages;	//individual pq avg
			result += "\n\tp" + i + " avg: " + avgs[i] + " minutes, " + msgCount[i] + " Messages processed";
		}
		
		return result;
	}
	
	//toString 
	public String toString() {
		String result = "";
		int i = 0;
		for(Queue<Message> q : Qs) 
			result += "q" + i++ + ": " + q.size() + ", ";
		return result;
	}
	
	public static void main(String[] args) {
		PriorityMessageQueue x = new PriorityMessageQueue(); //test PMQ
		x.process();
	}
}
