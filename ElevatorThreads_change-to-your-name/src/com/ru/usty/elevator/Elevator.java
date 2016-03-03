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
		
		while(true) {
			
			if(ElevatorScene.elevatorsMayDie) {
				return;
			}

			int numberOfFloor = scene.getNumberOfFloors();
			try {
				
				
				for(int i = 0; i < numberOfFloor; i++){
					ElevatorScene.elevatorWaitMutex.acquire();
					//System.out.println("here "+ id);
					int leaveElevator = scene.leaveThisFloor(scene.getCurrentFloorForElevator(id), id);
					if(leaveElevator == 0) {
		
						ElevatorScene.allOut[id].release();
					}
					ElevatorScene.elevatorWaitMutex.release();
					
					for(int x = 0; x < leaveElevator ; x++) {
						System.out.println(leaveElevator + " leavaCount "+ id);
						Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
						ElevatorScene.elvatorOutMutex.acquire();
						//ElevatorScene.out[scene.getCurrentFloorForElevator(id)].release();
						ElevatorScene.testout[id][scene.getCurrentFloorForElevator(id)].release();
						ElevatorScene.checkedOutSemaphore.acquire();
						ElevatorScene.elvatorOutMutex.release();		
					}
					
					
					ElevatorScene.elevatorWaitMutex1.acquire();
					System.out.println("hii "+ id);
					ElevatorScene.allOut[id].acquire();
					System.out.println("here "+ id);
					int space = scene.checkSpaceInElevator(id);
					int waitingAtFloor = scene.getNumberOfPeopleWaitingAtFloor(scene.getCurrentFloorForElevator(id));
				
					ElevatorScene.elevatorWaitMutex1.release();
					if((space > waitingAtFloor && counter == 1)){
						System.out.println("tekk");
						for(int x = 0; x < waitingAtFloor; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.elvatorInMutex.acquire();
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
							scene.setElevatorID(id);
							ElevatorScene.getIDSemaphore.release();
							ElevatorScene.personSemaphore.acquire();
							ElevatorScene.elvatorInMutex.release();
						}
					}
					else if(space != 0) {
						System.out.println("tekk1");
						for(int x = 0; x < space; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.elvatorInMutex.acquire();
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
							scene.setElevatorID(id);
							ElevatorScene.getIDSemaphore.release();
							ElevatorScene.personSemaphore.acquire();
							ElevatorScene.elvatorInMutex.release();
						}
						System.out.println("tekk2");
					}
					
					Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
					ElevatorScene.elevatorWaitMutex4.acquire();
					if(numberOfFloor - 1 != scene.getCurrentFloorForElevator(id)) {
						scene.incrementFloor(id);
					}
					ElevatorScene.elevatorWaitMutex4.release();
					counter = 1;
					//System.out.println("up "+ id);
	
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				for(int i = 0; i < numberOfFloor; i++) {
					//System.out.println("rdy for down part");
					ElevatorScene.elevatorWaitMutex2.acquire();
					int leaveElevator = scene.leaveThisFloor(scene.getCurrentFloorForElevator(id), id);
					if(leaveElevator == 0) {
						ElevatorScene.allOut[id].release();
					}
					ElevatorScene.elevatorWaitMutex2.release();
					//ElevatorScene.elevatorWaitMutex.release();
					
					for(int x = 0; x < leaveElevator ; x++) {
						Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
						ElevatorScene.elvatorOutMutex.acquire();
						ElevatorScene.testout[id][scene.getCurrentFloorForElevator(id)].release();
						ElevatorScene.checkedOutSemaphore.acquire();
						ElevatorScene.elvatorOutMutex.release();
						
					}
					
					ElevatorScene.elevatorWaitMutex3.acquire();
					System.out.println("hii "+ id);
					ElevatorScene.allOut[id].acquire();
					System.out.println("here "+ id);
	
					int space = scene.checkSpaceInElevator(id);
					int waitingAtFloor = scene.getNumberOfPeopleWaitingAtFloor(scene.getCurrentFloorForElevator(id));
					ElevatorScene.elevatorWaitMutex3.release();
					if((space > waitingAtFloor && counter == 1)){
						System.out.println("tekk");
						for(int x = 0; x < waitingAtFloor; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.elvatorInMutex.acquire();
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
							scene.setElevatorID(id);
							ElevatorScene.getIDSemaphore.release();
							ElevatorScene.personSemaphore.acquire();
							ElevatorScene.elvatorInMutex.release();
						}
					}
					else if(space != 0) { //&& 6 < waitingAtFloor
						for(int x = 0; x < space; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.elvatorInMutex.acquire();
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
							scene.setElevatorID(id);
							ElevatorScene.getIDSemaphore.release();
							ElevatorScene.personSemaphore.acquire();
							ElevatorScene.elvatorInMutex.release();
						}
					}
					
					Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
					scene.decrementFloor(id);
					//System.out.println("nidur");
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
