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
				//System.out.println("Personsema");
				//System.out.println("wait");
				scene.incrementNumberOfPeopleWaitingAtFloor(in);
				ElevatorScene.in.acquire();
				scene.incLeaveThisFloor(out);
				//System.out.println(out +" here ");
				scene.incrementPeopleInElevator(0);
				scene.decrementNumberOfPeopleWaitingAtFloor(in);
				//System.out.println(scene.getNumberOfPeopleWaitingAtFloor(0));
				System.out.println("in");
			//ElevatorScene.elevatorWaitMutex.release();
				
			//ElevatorScene.elevatorWaitMutex.acquire();
				//ElevatorScene.elevatorWaitMutex.acquire();
				ElevatorScene.out[out].acquire();
				System.out.println("UT");
				scene.decLeaveThisFloor(out);
				//ElevatorScene.elevatorWaitMutex.release();
				scene.decrementPeopleInElevator(0);
				//System.out.println("ut");
			//ElevatorScene.elevatorWaitMutex.release();
					
				
			
			

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println("Person thread released");
	}

}
