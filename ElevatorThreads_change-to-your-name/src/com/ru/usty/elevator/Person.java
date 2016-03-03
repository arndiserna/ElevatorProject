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
	
	@Override
	public void run() {
		try {
				
				scene.incrementNumberOfPeopleWaitingAtFloor(in);
				ElevatorScene.in[in].acquire();
				
				ElevatorScene.insideMutex.acquire();
				ElevatorScene.setIDSemaphore.acquire();
				inElevatorId = scene.getElevatorID();
				ElevatorScene.getIDSemaphore.release();
				scene.incLeaveThisFloor(out, inElevatorId);
				scene.incrementPeopleInElevator(inElevatorId);
				scene.decrementNumberOfPeopleWaitingAtFloor(in);
				ElevatorScene.insideMutex.release();
				
				ElevatorScene.out[inElevatorId][out].acquire();
				ElevatorScene.outsideMutex.acquire();
				scene.decLeaveThisFloor(out, inElevatorId);
				scene.personExitsAtFloor(out);
				scene.decrementPeopleInElevator(inElevatorId);
				//ElevatorScene.checkedOutSemaphore.release();
				// ef allir eru farnir út sem áttu að fara út á þessari hæð þá release() allout
				if(scene.leaveThisFloor(out, inElevatorId) == 0) {
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
