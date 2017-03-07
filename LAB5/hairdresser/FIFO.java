package hairdresser;

import java.util.ArrayList;
import hairdresser.Customer;


import generalSimulator.EventStore;
import generalSimulator.Simulator;
/**
 * 
 * @author Dexmo ,Aron ,Fanny
 *
 */


public class FIFO extends EventStore{
	private static int numLost = 0;
	private static int numCustomers = 0;
	public static int getCustomers() {
		return numCustomers - numLost;
	}
	
	private static ArrayList<Customer> newCustomerQueue = new ArrayList<Customer>();
	private static ArrayList<Customer> oldCustomerQueue = new ArrayList<Customer>();
	private static ArrayList<Customer> customerGettingHaircut = new ArrayList<Customer>();
	
	private static int queueLength = HairdressState.getQueueLength(); 
	private static int numberOfCuttingChairs = HairdressState.getNumberOfChairs(); 
	
	/**
	 * 
	 * @return the number of hairCuttingChairs that are not being used at this time.
	 */
	public static int idle(){
		return numberOfCuttingChairs - customerGettingHaircut.size();
	}
	/**
	 * 
	 * @param customer is the customer object that will be added to the list: customerGettingHaircut
	 */

	
	
	public static void addCustomer(Customer customer) {
		customerGettingHaircut.add(customer);
		numCustomers++;
	}
	
	public static void addNew(Customer customer) {
		if((oldCustomerQueue.size() + newCustomerQueue.size()) == queueLength){ 
			numLost ++;
		}
		else{  								

			newCustomerQueue.add(customer); 
		}
		numCustomers++;
	}
		
	
	public static void addOld(Customer customer) {
		if((oldCustomerQueue.size() + newCustomerQueue.size()) == queueLength){ 
			removeLast();    
			numLost ++;
		}
		oldCustomerQueue.add(customer);
	}
	
	
	public int numWaiting() {
		return (oldCustomerQueue.size() + newCustomerQueue.size());
	}
	
	public static void removeLast(){
		newCustomerQueue.remove(-1);	
	}

	public static void addGetHaircut(Customer readyCustomer, HairdressState state, EventStore store){  
		for(int i = 0; i < customerGettingHaircut.size() ; i++ ){  
			if(customerGettingHaircut.get(i) == readyCustomer){
				customerGettingHaircut.remove(i);
			}
		}
		int i = 1; 
		Customer customerInQueue;
		if(oldCustomerQueue.size() >= i) {  
			customerInQueue = oldCustomerQueue.get(0);
			customerGettingHaircut.add(customerInQueue);
			store.add(new CustomerLeaves(customerInQueue, Simulator.getSimTime() + state.getCutTime(), store)); 
			oldCustomerQueue.remove(0); 
		}
		else if(newCustomerQueue.size() >= i) {  
			customerInQueue = newCustomerQueue.get(0); 
			customerGettingHaircut.add(customerInQueue);
			store.add(new CustomerLeaves(customerInQueue, Simulator.getSimTime() + state.getCutTime(), store)); 
			newCustomerQueue.remove(0);	
		}	
		//l�gg tull event leave
	}
	
}
