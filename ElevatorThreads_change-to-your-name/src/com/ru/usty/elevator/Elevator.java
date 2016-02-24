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
				if(scene.getNumberOfPeopleInElevator(0) < 6) {
					for(int i = 0; i < 6; i++) {
						try {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							System.out.println("open");
							ElevatorScene.in.release();
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						for(int i = 0; i < scene.getNumberOfFloors(); i++){
							for(int x = 0; x < scene.getNumberOfPeopleInElevator(0); x++) {
								try {
									Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
									ElevatorScene.out[scene.getCurrentFloorForElevator(0)].release();
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								
							}
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							if(scene.getNumberOfFloors() - 1 != scene.getCurrentFloorForElevator(0)) {
								scene.incrementFloor(0);
							}
							System.out.println("up");
							System.out.println(scene.getCurrentFloorForElevator(0) + " had");
			
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
				for(int i = 0; i < scene.getNumberOfFloors(); i++) {
					//Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
					System.out.println(scene.getCurrentFloorForElevator(0) + " had");
					/*for(int x = 0; x < scene.getNumberOfPeopleInElevator(0); x++) {
						
						ElevatorScene.out[scene.getCurrentFloorForElevator(0)].release();
						
					}*/
					Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
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
