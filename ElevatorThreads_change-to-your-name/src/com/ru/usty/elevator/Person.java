package com.ru.usty.elevator;

public class Person implements Runnable{
	int in, out;
	ElevatorScene scene;
	int inElevatorId;
	public Person(int in, int out, ElevatorScene scene){
		this.in = in;
		this.out = out;
		this.scene = scene;
	}
	//private ElevatorScene scene;
	
	
	@Override
	public void run() {
		try {
				
				scene.incrementNumberOfPeopleWaitingAtFloor(in);
				ElevatorScene.in[in].acquire();
				ElevatorScene.insideMutex.acquire();
				
				ElevatorScene.getIDSemaphore.acquire();
				inElevatorId = scene.getElevatorID();
				ElevatorScene.personSemaphore.release();
				scene.incLeaveThisFloor(out, inElevatorId);
				//ElevatorScene.personSemaphore.acquire();
				scene.incrementPeopleInElevator(inElevatorId);
				scene.decrementNumberOfPeopleWaitingAtFloor(in);
				//System.out.println("in");
				ElevatorScene.insideMutex.release();
				//ElevatorScene.out[out].acquire();
				ElevatorScene.testout[inElevatorId][out].acquire();
				ElevatorScene.outsideMutex.acquire();
				//System.out.println("UT");
				scene.decLeaveThisFloor(out, inElevatorId);
				scene.personExitsAtFloor(out);
				scene.decrementPeopleInElevator(inElevatorId);
				ElevatorScene.checkedOutSemaphore.release();
				//while(scene.leaveThisFloor(out, inElevatorId) != 0 || scene.leaveThisFloor(out, inElevatorId) == 0) {
				System.out.println(scene.leaveThisFloor(out, inElevatorId) + " eftir " +inElevatorId);
				if(scene.leaveThisFloor(out, inElevatorId) == 0) {
					System.out.println("lyfta laus "+ inElevatorId);
					ElevatorScene.allOut[inElevatorId].release();
				}
				
				ElevatorScene.outsideMutex.release();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		//System.out.println("Person thread released");
	}

}
