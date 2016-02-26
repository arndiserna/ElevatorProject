package com.ru.usty.elevator;

public class Elevator implements Runnable {
	
	ElevatorScene scene;
	public Elevator(ElevatorScene scene) {
		this.scene = scene;
	}
	@Override
	public void run() {
		
		while(true) {
			
			if(ElevatorScene.elevatorsMayDie) {
				return;
			}
			if(scene.getCurrentFloorForElevator(0) == 0) {
				try {
					ElevatorScene.elevatorWaitMutex.acquire();
				
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				int space = scene.checkSpaceInElevator();
				ElevatorScene.elevatorWaitMutex.release();
				if(space != 0) {
					for(int i = 0; i < space; i++) {
						try {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.in[scene.getCurrentFloorForElevator(0)].release();
							
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					try {
						
						
						for(int i = 0; i < scene.getNumberOfFloors(); i++){
							ElevatorScene.elevatorWaitMutex.acquire();
							int leaveElevator = scene.leaveThisFloor(scene.getCurrentFloorForElevator(0));
							ElevatorScene.elevatorWaitMutex.release();
							for(int x = 0; x < leaveElevator ; x++) {
								try {
									Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
									//ElevatorScene.elevatorWaitMutex.acquire();
									ElevatorScene.out[scene.getCurrentFloorForElevator(0)].release();
									//ElevatorScene.elevatorWaitMutex.release();
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
							//System.out.println(scene.getCurrentFloorForElevator(0) + " had");
			
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			try {
				for(int i = 0; i < scene.getNumberOfFloors(); i++) {
					//Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
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
	
		}

	}
}
