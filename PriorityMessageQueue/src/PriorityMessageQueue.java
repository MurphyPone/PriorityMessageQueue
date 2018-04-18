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
	private int time;
	//Could add global priority cap var --> Messages as well
	private ArrayList<Queue<Message>> Qs;//The Queues for Messages to be stored within
	private ArrayList<Message> output;

	//Constructor
	PriorityMessageQueue() {
		Qs = new ArrayList<Queue<Message>>(numQs);	//Instantiate the Qs
		for(int i = 0; i < numQs; i++)
			Qs.add(new LinkedList<Message>() ); //Create 5 Queues
		time = 0; //keep track of the # minutes that have transpired 
		output = new ArrayList<Message>(); //save time resizing later
	}

	//QUEUE Methods//
	
	//Add -- according to Message priority
	public void add(Message m) {
		int p = m.getPriority();	//get priority of item to be added
		Queue<Message> pq = Qs.get(p);	//Get the specific PriorityQueue
		pq.add(m);	//Add the message to the Queue associated with that priority 
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
		Message m = null;	//init as null to check if exists later
		for(Queue<Message> q : Qs) {		
			if( !q.isEmpty() ) {		//Find first Queue that isn't empty
				m = q.remove();	//Snatch the message
				m.setWait(time);		//Calculate how long it has been waiting
			}
		}
		if(m != null) 
			return m;
		else 
			throw new NoSuchElementException();
	}
	
	//Process 
	public void process() {
		while( !isEmpty() ) { //TODO re-Add condition to stop after x minutes
			if(time % 4 == 0) {   //every 4 minutes
				output.add( remove() );	//Add the highest priority message to the Queue 		
			}
			
			//add(new Message(time) );	//Create a new message every minute //TODO 99% of the Messages are priority 4 even though it should be evenly distr...
			time++;	//every iteration = 1 minute
			//System.out.println(toString());
		}
		//After processing all, output analysis
		System.out.print( analyze() );
	}
	
	public String analyze() {
		String result = "PriorityMessageQueue wait time analysis:";
		double avg = 0;	//overall avg wait time
		double[] avgs = new double[numQs]; //stores the avgs for each of the individual pqs
		int[] msgCount = new int[numQs]; //Keeps track of the # of messages per Queue
		int numMessages = output.size();
		
		//sum wait times 
		for(Message m : output) {
			avg += m.getTimeOfArrival();	//overall avg
			int p = m.getPriority(); //Queue specific avg
			//if(p == 0) System.out.println(output.indexOf(m) ); //TODO REMOVE THIS DEBUGGING HELPER
			avgs[p] += m.getTimeOfArrival(); //fill first
			msgCount[p]++;	//keep track of how many messages in each queue
		}
		
		//divides by total messages
		avg = (double) avg / numMessages;	//total avg
		//Build String
		result += "\n\t" + numMessages + " Messages processed in " + time + " minutes";
		result += "\n\tOverall avg: " + avg + " minutes";

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
		//Create some test msgs
		int nm = 100; //num messages
		PriorityMessageQueue x = new PriorityMessageQueue(); //test PMQ
		
		//debugging tracker
		int[] priorityTracker = new int[5];
		int[] actual = new int[5];
		//
 
		//Add initial messages
		for(int i = 0; i < nm; i++) {
			int makeSureThatTheyHaveARandomPriority = (int) (Math.random() * 5);
			priorityTracker[makeSureThatTheyHaveARandomPriority]++; //Debugging tracker
			Message m = new Message(0); //add with beginning time 0
			m.setPriority(makeSureThatTheyHaveARandomPriority);
			x.add( m );	
		}
		
		x.process();
		
		//Debugging tracker
		for(Message m : x.output) {
			int p = m.getPriority();
			actual[p]++;
		}
		
		for(int i = 0; i < 5; i++) {
			String r = "\nProjected q" + i + ": " + priorityTracker[i];
				r += "\nActual q" + i + ": " + actual[i];
			System.out.println(r);
		} 
		//End debugging tracker --> pulling hair out
			
	}
}
