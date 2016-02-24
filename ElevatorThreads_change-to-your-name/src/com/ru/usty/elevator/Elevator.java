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
			
				//ElevatorScene.personSemaphore.release(); // signal
			if(scene.getCurrentFloorForElevator(0) == 0) {
				if(scene.getNumberOfPeopleInElevator(0) == 0) {
					for(int i = 0; i < 6; i++) {
						try {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.in.release();
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						for(int i = 0; i < scene.getNumberOfFloors() - 1; i++){
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							scene.incrementFloor(0);
							System.out.println("up");
							for(int x = 0; x < 6; x++) {
								try {
									Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
									ElevatorScene.out[scene.getCurrentFloorForElevator(0)].release();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			//if(scene.getCurrentFloorForElevator(0) == 1) {
	
			
			//System.out.println(scene.getNumberOfFloors()+ " numbers of floor " + " had "+ scene.getCurrentFloorForElevator(0));
			
			try {
				for(int i = 0; i < scene.getNumberOfFloors() - 1; i++) {
					//Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
					for(int x = 0; x < 6; x++) {
						Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
						ElevatorScene.out[scene.getCurrentFloorForElevator(0)].release();
						
					}
					scene.decrementFloor(0);
					System.out.println("nidur");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
				/*for(int x = 0; x < 6; x++) {
					try {
						Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}*/
			
			
			//}
		}

	}
}
