package com.ru.usty.elevator;

public class Elevator implements Runnable {
	
	ElevatorScene scene;
	public Elevator(ElevatorScene scene) {
		this.scene = scene;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			if(ElevatorScene.elevatorsMayDie) {
				return;
			}
			// TODO Auto-generated method stub
			//for(int i = 0; i < 6; i++) {
				
			ElevatorScene.personSemaphore.release(); // signal
			if(scene.getCurrentFloorForElevator(0) == 0) {
				if(scene.getNumberOfPeopleInElevator(0) == 0) {
					for(int i = 0; i < 6; i++){
					ElevatorScene.in.release();
					}
					
					try {
						Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
						
						scene.incrementFloor(0);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			if(scene.getCurrentFloorForElevator(0) == 1) {
				ElevatorScene.out.release();
				try {
					Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				scene.decrementPeopleInElevator(0);
				scene.decrementFloor(0);
			}
	}

	}
}
