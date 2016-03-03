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
	
	public static Semaphore setIDSemaphore;
	public static Semaphore getIDSemaphore;
	public static Semaphore checkedOutSemaphore;
	public static Semaphore personCountMutex;
	public static Semaphore elevatorWaitMutex;
	public static Semaphore elevatorWaitMutex1;
	public static Semaphore elevatorWaitMutex2;
	public static Semaphore elevatorWaitMutex3;
	public static Semaphore elevatorWaitMutex4;
	public static Semaphore insideMutex;
	public static Semaphore outsideMutex;
	public static Semaphore elvatorUpMutex;
	public static Semaphore elvatorDownMutex;
	public static Semaphore elvatorInMutex;
	public static Semaphore elvatorOutMutex;
	public static Semaphore[] in;
	public static Semaphore[][] out;
	public static Semaphore[] allOut;	
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
	ArrayList<Integer> exitedCount = null;
	ArrayList<Integer> personCount; //use if you want but
									//throw away and
									//implement differently
									//if it suits you

	//Base function: definition must not change
	//Necessary to add your code in this one
	public void restartScene(int numberOfFloors, int numberOfElevators) {

		// drepur þræðir sem voru lifandi
		elevatorsMayDie = true;
		if(elevatorThread != null) {
			for(int i = 0; i < this.numberOfElevators; i++) {
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
		
		setIDSemaphore = new Semaphore(0);
		getIDSemaphore = new Semaphore(0);
		checkedOutSemaphore = new Semaphore(0);
		personCountMutex = new Semaphore(1);
		elevatorWaitMutex = new Semaphore(1);
		elevatorWaitMutex1  = new Semaphore(1);
		elevatorWaitMutex2  = new Semaphore(1);
		elevatorWaitMutex3  = new Semaphore(1);
		elevatorWaitMutex4 = new Semaphore(1);
		elvatorOutMutex = new Semaphore(1);
		elvatorInMutex = new Semaphore(1);
		insideMutex = new Semaphore(1);
		outsideMutex = new Semaphore(1);
		elvatorUpMutex = new Semaphore(1);
		elvatorDownMutex = new Semaphore(1);
		exitedCountMutex = new Semaphore(1);
		
		in = new Semaphore[numberOfFloors];
		for(int i = 0; i < numberOfFloors; i++) {
			in[i] = new Semaphore(0);
		}
		
		out = new Semaphore[numberOfElevators][numberOfFloors];
		for(int i = 0; i < numberOfElevators; i++) {
			for(int k = 0; k < numberOfFloors; k++) {
				out[i][k] = new Semaphore(0);
			}
		}
		
		allOut = new Semaphore[numberOfElevators];
		for (int i = 0; i < numberOfElevators; i++) {
			allOut[i] = new Semaphore(0);
		}
		

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
			ArrayList<Integer> elevetorLeave = new ArrayList<Integer>();
			this.leaveCount.add(i, elevetorLeave);
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
	
	//Base function: definition must not change
	//Necessary to add your code in this one
	public Thread addPerson(int sourceFloor, int destinationFloor) {
		Person person = new Person(sourceFloor, destinationFloor, this);
		Thread thread = new Thread(person);
		thread.start();
		
		return thread;
	}
	
	// setur ID á lyftuni fyrir perónu
	public void setElevatorID(int elevator) {
		elevatorID = elevator;
	}
	// til að sækja id á lyftunni sem persóna er að fara í
	public int getElevatorID() {		
		return elevatorID;
	}
	// lækkar lyftu hæð
	public void decrementFloor(int elevator){
		try {
			elvatorDownMutex.acquire();
			if(elevatorFloor.get(elevator) != 0)
			{
				elevatorFloor.set(elevator, (elevatorFloor.get(elevator) - 1));
			}
			elvatorDownMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// hækkar lyftu hæð
	public void incrementFloor(int elevator){
		try {
			elvatorUpMutex.acquire();
			elevatorFloor.set(elevator, (elevatorFloor.get(elevator) + 1));
			elvatorUpMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	// hækkar hversu margar persónu fór út á þessari hæð
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
	// sýnir hversu margir eru farnir út á þessari hæð
	public int getExitedCountAtFloor(int floor) {
		if(floor < getNumberOfFloors()) {
			return exitedCount.get(floor);
		}
		else {
			return 0;
		}
	}
	// til að sækja hversu margir eru að fara út á þessari hæð í þessari lyftu
	public int leaveThisFloor(int floor, int elevator) {
		return leaveCount.get(floor).get(elevator);
	}
	// til að hækka count á hversu margir eru að fara út á hæð
	public void incLeaveThisFloor(int floor, int elevator) {
		leaveCount.get(floor).set(elevator, leaveCount.get(floor).get(elevator) + 1);
	}
	//til að lækka count á hversu margir eru að fara út á hæð
	public void decLeaveThisFloor(int floor, int elevator) {
		leaveCount.get(floor).set(elevator, leaveCount.get(floor).get(elevator) - 1);
	}
	// til að hækka fólk fjölda sem er í lyftuni
	public void incrementPeopleInElevator(int elevator){
		pepsInElevator.set(elevator, (pepsInElevator.get(elevator) + 1));
	}
	// til að lækka fólk fjölda sem er í lyftuni
	public void decrementPeopleInElevator(int elevator){

		if(pepsInElevator.get(elevator) != 0){
			pepsInElevator.set(elevator, (pepsInElevator.get(elevator) - 1));
		}
	}
	// til að lækka fjölda sem bíða á hæð
	public void decrementNumberOfPeopleWaitingAtFloor(int floor){
		personCount.set(floor, (personCount.get(floor)- 1));
	}
	// til að hækka fjölda sem bíða á hæð
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
