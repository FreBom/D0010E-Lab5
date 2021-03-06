package hairdresser;

import java.util.Arrays;
import java.util.Observable;
import generalSimulator.State;
import generalSimulator.View;

/**
 * This class is the specific view and prints the first section which is the state settings and
 * then a table of event ending with statistics
 * 
 * @author arostr-5, Fanny, Dexmo 
 *
 */
public class HairdressView extends View {

	private String eventDisplay = "";
	private HairdressState HSState;
	
	/**
	 * Constructor needs the specified state with data and makes it observer to always update itself
	 * 
	 * @param state
	 */
	public HairdressView(State state) {
		super(state);
		HSState = (HairdressState) state;
		HSState.addObserver(this);

	}
	
	/**
	 * Method holds data to then be used in update
	 * 
	 * @param display which event to print data about
	 */
	public void getTable(String display) {

		System.out.format(eventDisplay, HSState.getTime(), HSState.getEventName(),
				HSState.getCustomerID(), HSState.getFIFO().idle(), HSState.getTotalIdleTime(),
				HSState.getTotalWaitingTime(), HSState.getFIFO().numWaiting(),
				HSState.getCutCustomer(), HSState.getFIFO().getNumLost(), HSState.getReturnCustomer());
	}
	
	/**
	 * When changed run this 
	 * 
	 */
	public void update(Observable o, Object arg) {

		if (HSState.getEventName().equals("Enter")) {

			eventDisplay = "%6.2f %2s %7s %6s %6.2f %7.2f %6s %6s %6s %6s %n";
			getTable(eventDisplay);

		} else if (HSState.getEventName().equals("Done")) {
			eventDisplay = "%6.2f %2s %8s %6s %6.2f %7.2f %6s %6s %6s %6s %n";
			getTable(eventDisplay);

		} else if (HSState.getEventName().equals("Return")) {
			eventDisplay = "%6.2f %2s %6s %6s %6.2f %7.2f %6s %6s %6s %6s %n";
			getTable(eventDisplay);

		} else if (HSState.getEventName().equals("StopHSS")) {

			System.out.format("%6.2f %2s %5s %6s %6.2f %7.2f %6s %6s %6s %6s %n", HSState.getTime(),
					HSState.getEventName(), "", HSState.getFIFO().idle(),
					HSState.getTotalIdleTime(), HSState.getTotalWaitingTime(),
					HSState.getFIFO().numWaiting(), HSState.getCutCustomer(), HSState.getFIFO().getNumLost(),
					HSState.getReturnCustomer());

		} else {
			System.out.format("%6.2f %2s %n", HSState.getTime(), HSState.getEventName());
		}
	}
	/**
	 * The first print section holding the settings
	 * 
	 */
	public void startPrint() {

		System.out.println("Closing time of the day ..............: " + HairdressState.getSimStopTime());
		System.out.println("Total number of chairs ...............: " + HairdressState.getNumberOfChairs());
		System.out.println("Maximum queue size ...................: " + HairdressState.getQueueLength());
		System.out.println("Lambda (customers/timeunit entering)..: " + HairdressState.getLambda());
		System.out.println("hmin and hmax (cutting time interval) : " + Arrays.toString(HairdressState.hArray()));
		System.out.println("dmin and dmax (return time interval) .: " + Arrays.toString(HairdressState.dArray()));
		System.out.println("Risk dissatisfied returns: ...........: " + HairdressState.probDissatisfied);
		System.out.println("Seed used in pseudo random generator .: " + HairdressState.seed);
		System.out.println("---------------------------------------------------------------------");
		System.out.format("%s %2s %6s %6s %6s %7s %6s %6s %6s %6s %n", "- Time", "Event", "Id", "Idle", "TIdle",
				"TWait", "InQ", "Cut", "Lost", "Ret -");

	}
	/**
	 * Ending print section with the statistics
	 * 
	 */
	public void stopPrint() {
		
		System.out.println("---------------------------------------------------------------------");
		System.out.println("Number of customers cut: ......: " + HSState.getCutCustomer());
		System.out.format("%s %.2f %n", "Average cutting time...........:", HSState.getAverageCutTime());
		System.out.format("%s %.2f %n", "Average queueing time: ........:" , HSState.getAverageQueueTime());
		System.out.println("Largest queue (max NumWaiting) : " + HSState.getFIFO().getMax());
		System.out.println("Customers not cut (NumLost) ...: " + HSState.getFIFO().getNumLost());
		System.out.println("Dissatisfied customers: .......: " + HSState.getReturnCustomer());
		System.out.format("%s %.2f %n", "Time chairs were idle: ........:", HSState.getTotalIdleTime());

	}

}
