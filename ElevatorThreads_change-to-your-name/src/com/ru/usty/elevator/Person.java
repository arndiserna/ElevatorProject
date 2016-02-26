package com.ru.usty.elevator;

public class Person implements Runnable{
	int in, out;
	ElevatorScene scene;
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
				ElevatorScene.in[in].acquire();
				scene.incLeaveThisFloor(out);
				scene.incrementPeopleInElevator(0);
				scene.decrementNumberOfPeopleWaitingAtFloor(in);
				System.out.println("in");
				//ElevatorScene.elevatorWaitMutex.acquire();
				ElevatorScene.out[out].acquire();
				System.out.println("UT");
				scene.decLeaveThisFloor(out);
				//ElevatorScene.elevatorWaitMutex.release();
				scene.decrementPeopleInElevator(0);

			

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println("Person thread released");
	}

}
