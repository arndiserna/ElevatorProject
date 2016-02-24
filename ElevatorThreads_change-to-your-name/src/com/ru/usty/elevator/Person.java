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
			System.out.println("wait");
			ElevatorScene.elevatorWaitMutex.acquire();
				ElevatorScene.personSemaphore.acquire();//Wait
				System.out.println("Personsema");
					ElevatorScene.in.acquire();
					System.out.println("in");
					scene.incrementPeopleInElevator(0);
					scene.decrementNumberOfPeopleWaitingAtFloor(in);
					
					ElevatorScene.out.acquire();
					
					
				
			
			ElevatorScene.elevatorWaitMutex.release();

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		System.out.println("Person thread released");
	}

}
