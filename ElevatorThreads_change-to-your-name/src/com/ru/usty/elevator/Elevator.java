package com.ru.usty.elevator;

public class Elevator implements Runnable {
	int counter = 0;
	int id;
	ElevatorScene scene;
	public Elevator(ElevatorScene scene, int id) {
		this.scene = scene;
		this.id = id;
	}
	@Override
	public void run() {
		
		/*try {
			ElevatorScene.personSemaphore.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
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
					int leaveElevator = scene.leaveThisFloor(scene.getCurrentFloorForElevator(id), id);
					System.out.println(leaveElevator + " asdad");
					if(leaveElevator == 0){
		
						ElevatorScene.allOut.release();
					}
					ElevatorScene.elevatorWaitMutex.release();
					for(int x = 0; x < leaveElevator ; x++) {
						Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
						//ElevatorScene.elevatorWaitMutex.acquire();
						ElevatorScene.out[scene.getCurrentFloorForElevator(id)].release();
						//ElevatorScene.elevatorWaitMutex.release();
					}
					ElevatorScene.allOut.acquire();
					ElevatorScene.elevatorWaitMutex.acquire();
					int space = scene.checkSpaceInElevator(id);
					int waitingAtFloor = scene.getNumberOfPeopleWaitingAtFloor(scene.getCurrentFloorForElevator(id));
					scene.setElevatorID(id);
					
					System.out.println(waitingAtFloor + " bíða");
					if((space > waitingAtFloor && counter == 1)){
						System.out.println("tekk");
						for(int x = 0; x < waitingAtFloor; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
						}
					}
					else if(space != 0) {
						for(int x = 0; x < space; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
						}
					}
					ElevatorScene.elevatorWaitMutex.release();
					Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
					if(numberOfFloor - 1 != scene.getCurrentFloorForElevator(id)) {
						scene.incrementFloor(id);
					}
					System.out.println("up");
	
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				for(int i = 0; i < numberOfFloor; i++) {
					
					ElevatorScene.elevatorWaitMutex.acquire();
					int leaveElevator = scene.leaveThisFloor(scene.getCurrentFloorForElevator(id), id);
					if(leaveElevator == 0){
						ElevatorScene.allOut.release();
					}
					ElevatorScene.elevatorWaitMutex.release();
					for(int x = 0; x < leaveElevator ; x++) {
						Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
						//ElevatorScene.elevatorWaitMutex.acquire();
						ElevatorScene.out[scene.getCurrentFloorForElevator(id)].release();
						//ElevatorScene.elevatorWaitMutex.release();
					}
					ElevatorScene.allOut.acquire();
					ElevatorScene.elevatorWaitMutex.acquire();
					int space = scene.checkSpaceInElevator(id);
					int waitingAtFloor = scene.getNumberOfPeopleWaitingAtFloor(scene.getCurrentFloorForElevator(id));
					scene.setElevatorID(id);
					if((space > waitingAtFloor && counter == 1)){
						System.out.println("tekk");
						for(int x = 0; x < waitingAtFloor; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
						}
					}
					else if(space != 0) { //&& 6 < waitingAtFloor
						for(int x = 0; x < space; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
						}
					}
					ElevatorScene.elevatorWaitMutex.release();
					Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
					scene.decrementFloor(id);
					System.out.println("nidur");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			counter = 1;
		}

	}
}
