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
			//ElevatorScene.elevatorWaitMutex.acquire();
				//ElevatorScene.personSemaphore.acquire();//Wait
				
				scene.incrementNumberOfPeopleWaitingAtFloor(in);
				//ElevatorScene.personSemaphore.release();
				ElevatorScene.in[in].acquire();
				ElevatorScene.insideMutex.acquire();
				inElevatorId = scene.getElevatorID();
				
				scene.incLeaveThisFloor(out, inElevatorId);
				scene.incrementPeopleInElevator(inElevatorId);
				scene.decrementNumberOfPeopleWaitingAtFloor(in);
				System.out.println("in");
				ElevatorScene.insideMutex.release();
				//ElevatorScene.elevatorWaitMutex.acquire();
				ElevatorScene.out[out].acquire();
				System.out.println("UT");
				scene.decLeaveThisFloor(out, inElevatorId);
				scene.personExitsAtFloor(out);
				//ElevatorScene.elevatorWaitMutex.release();
				scene.decrementPeopleInElevator(0);
				if(scene.leaveThisFloor(out, inElevatorId) == 0) {
					ElevatorScene.allOut.release();
				}

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println("Person thread released");
	}

}
