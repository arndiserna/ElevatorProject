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
			/*try {

			
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}*/
			int numberOfFloor = scene.getNumberOfFloors();
			try {
				
				
				for(int i = 0; i < numberOfFloor; i++){
					
					ElevatorScene.elevatorWaitMutex.acquire();
					int leaveElevator = scene.leaveThisFloor(scene.getCurrentFloorForElevator(0));
					if(leaveElevator == 0){
						ElevatorScene.allOut.release();
					}
					ElevatorScene.elevatorWaitMutex.release();
					for(int x = 0; x < leaveElevator ; x++) {
						Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
						//ElevatorScene.elevatorWaitMutex.acquire();
						ElevatorScene.out[scene.getCurrentFloorForElevator(0)].release();
						//ElevatorScene.elevatorWaitMutex.release();
					}
					ElevatorScene.allOut.acquire();
					ElevatorScene.elevatorWaitMutex.acquire();
					int space = scene.checkSpaceInElevator();
					//int waitingAtFloor = scene.getNumberOfPeopleWaitingAtFloor(scene.getCurrentFloorForElevator(0));
					//System.out.println(waitingAtFloor + " bíða");
					ElevatorScene.elevatorWaitMutex.release();
					if(space != 0) { //&& 6 < waitingAtFloor
						for(int x = 0; x < space; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.in[scene.getCurrentFloorForElevator(0)].release();
						}
					}
					/*else {     //else if(waitingAtFloor != 0)
						
						for(int x = 0; x < waitingAtFloor; x++) {
							try {
								Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
								ElevatorScene.in[scene.getCurrentFloorForElevator(0)].release();
								
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					
					}*/
					Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
					if(numberOfFloor - 1 != scene.getCurrentFloorForElevator(0)) {
						scene.incrementFloor(0);
					}
					System.out.println("up");
	
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				for(int i = 0; i < numberOfFloor; i++) {
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
