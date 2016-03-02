package com.ru.usty.elevator;

import java.util.ArrayList;
import java.util.concurrent.Semaphore;

/**
 * The base function definitions of this class must stay the same
 * for the test suite and graphics to use.
 * You can add functions and/or change the functionality
 * of the operations at will.
 *
 */

public class ElevatorScene {
	
	public static Semaphore personSemaphore;
	public static Semaphore personCountMutex;
	public static Semaphore elevatorWaitMutex;
	public static Semaphore[] in;
	public static Semaphore[] out;
	public static Semaphore allOut;
	public static ElevatorScene scene;	
	public static Semaphore exitedCountMutex;
	public static boolean elevatorsMayDie;

	//TO SPEED THINGS UP WHEN TESTING,
	//feel free to change this.  It will be changed during grading
	public static final int VISUALIZATION_WAIT_TIME = 500;  //milliseconds

	private int numberOfFloors;
	private int numberOfElevators;
	private int elevatorID;
	private Thread [] elevatorThread = null;
	
	ArrayList<Integer> elevatorFloor;
	ArrayList<Integer> pepsInElevator;
	
	ArrayList<ArrayList<Integer>> leaveCount;
	ArrayList<Integer> elevetorLeave;
	ArrayList<Integer> exitedCount = null;
	ArrayList<Integer> personCount; //use if you want but
									//throw away and
									//implement differently
									//if it suits you

	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {

		elevatorsMayDie = true;
		if(elevatorThread != null) {
			for(int i = 0; i < numberOfElevators; i++) {
				if(elevatorThread[i].isAlive()) {
					try {
						elevatorThread[i].join();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		elevatorsMayDie = false;
		scene = this;
		allOut = new Semaphore(0);
		personSemaphore = new Semaphore(0);
		personCountMutex = new Semaphore(1);
		elevatorWaitMutex = new Semaphore(1);
		exitedCountMutex = new Semaphore(1);
		in = new Semaphore[numberOfFloors];
		for(int i = 0; i < numberOfFloors; i++) {
			in[i] = new Semaphore(0);
		}
		out = new Semaphore[numberOfFloors];
		for(int i = 0; i < numberOfFloors; i++) {
			out[i] = new Semaphore(0);
		}
		//Elevator elevator = new Elevator(this);
		

		this.numberOfFloors = numberOfFloors;
		this.numberOfElevators = numberOfElevators;
		
		personCount = new ArrayList<Integer>();
		pepsInElevator = new ArrayList<Integer>();
		elevatorFloor = new ArrayList<Integer>();
		
		for(int i = 0; i < numberOfElevators; i++) {
			this.pepsInElevator.add(0);
		}
		
		for(int i = 0; i < numberOfElevators; i++) {
			this.elevatorFloor.add(0);
		}
		
		for(int i = 0; i < numberOfFloors; i++) {
			this.personCount.add(0);
		}
		if(exitedCount == null) {
			exitedCount = new ArrayList<Integer>();
		}else {
			exitedCount.clear();
		}
		for(int i = 0; i < numberOfFloors; i++) {
			this.exitedCount.add(0);
		}
		
		leaveCount = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < numberOfFloors; i++) {
			elevetorLeave = new ArrayList<Integer>();
			this.leaveCount.add(0, elevetorLeave);
			for(int k = 0; k < numberOfElevators; k++) {
				elevetorLeave.add(0);
			}
		}
		elevatorThread = new Thread[numberOfElevators];

		for(int i = 0; i < numberOfElevators; i++) {
			elevatorThread[i] = new Thread(new Elevator(this, i));
			elevatorThread[i].start();
		}
	}
	
	public void setElevatorID(int elevator) {
		try {
			elevatorWaitMutex.acquire();
				elevatorID = elevator;
			elevatorWaitMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getElevatorID() {		
		return elevatorID;
	}
	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {
		Person person = new Person(sourceFloor, destinationFloor, this);
		Thread thread = new Thread(person);
		thread.start();
		/**
		 * Important to add code here to make a
		 * new thread that runs your person-runnable
		 * 
		 * Also return the Thread object for your person
		 * so that it can be reaped in the testSuite
		 * (you don't have to join() yourself)
		 */
		
		return thread;
	}
	
	public void decrementFloor(int elevator){
		try {
			elevatorWaitMutex.acquire();
			if(elevatorFloor.get(elevator) != 0)
			{
				elevatorFloor.set(elevator, (elevatorFloor.get(elevator) - 1));
			}
			elevatorWaitMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void incrementFloor(int elevator){
		try {
			elevatorWaitMutex.acquire();
				elevatorFloor.set(elevator, (elevatorFloor.get(elevator) + 1));
			elevatorWaitMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void personExitsAtFloor(int floor) {
		try {
			
			exitedCountMutex.acquire();
			exitedCount.set(floor, (exitedCount.get(floor) + 1));
			exitedCountMutex.release();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public int getExitedCountAtFloor(int floor) {
		if(floor < getNumberOfFloors()) {
			return exitedCount.get(floor);
		}
		else {
			return 0;
		}
	}
	public int leaveThisFloor(int floor, int elevator) {
		return leaveCount.get(floor).get(elevator);
	}
	
	public void incLeaveThisFloor(int floor, int elevator) {
		try {
			personCountMutex.acquire();
			leaveCount.get(floor).set(elevator, leaveCount.get(floor).get(elevator) + 1);
			personCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void decLeaveThisFloor(int floor, int elevator) {
		try {
			personCountMutex.acquire();
			leaveCount.get(floor).set(elevator, leaveCount.get(floor).get(elevator) - 1);
			personCountMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	
	public void incrementPeopleInElevator(int elevator){
		try {
			elevatorWaitMutex.acquire();
				pepsInElevator.set(elevator, (pepsInElevator.get(elevator) + 1));
			elevatorWaitMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void decrementPeopleInElevator(int elevator){
		try {
			elevatorWaitMutex.acquire();
			if(pepsInElevator.get(elevator) != 0){
				pepsInElevator.set(elevator, (pepsInElevator.get(elevator) - 1));
			}
			elevatorWaitMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void decrementNumberOfPeopleWaitingAtFloor(int floor){
		try {
			personCountMutex.acquire();
				personCount.set(floor, (personCount.get(floor)- 1));
			personCountMutex.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void incrementNumberOfPeopleWaitingAtFloor(int floor){
		try {
			personCountMutex.acquire();
				personCount.set(floor, (personCount.get(floor) + 1));
			personCountMutex.release();
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	//Base function: definition must not change, but add your code
	public int getCurrentFloorForElevator(int elevator) {

		//dumb code, replace it!
		return elevatorFloor.get(elevator);
	}

	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleInElevator(int elevator) {
		return pepsInElevator.get(elevator);
	}
	
	public int checkSpaceInElevator(int elevator) {
		return 6 - pepsInElevator.get(elevator);
	}
	
	//Base function: definition must not change, but add your code
	public int getNumberOfPeopleWaitingAtFloor(int floor) {

		return personCount.get(floor);
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfFloors() {
		return numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfFloors(int numberOfFloors) {
		this.numberOfFloors = numberOfFloors;
	}

	//Base function: definition must not change, but add your code if needed
	public int getNumberOfElevators() {
		return numberOfElevators;
	}

	//Base function: definition must not change, but add your code if needed
	public void setNumberOfElevators(int numberOfElevators) {
		this.numberOfElevators = numberOfElevators;
	}

	//Base function: no need to change unless you choose
	//				 not to "open the doors" sometimes
	//				 even though there are people there
	public boolean isElevatorOpen(int elevator) {

		return isButtonPushedAtFloor(getCurrentFloorForElevator(elevator));
	}
	//Base function: no need to change, just for visualization
	//Feel free to use it though, if it helps
	public boolean isButtonPushedAtFloor(int floor) {

		return (getNumberOfPeopleWaitingAtFloor(floor) > 0);
	}

}
