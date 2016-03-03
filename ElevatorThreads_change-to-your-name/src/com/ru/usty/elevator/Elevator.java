package com.ru.usty.elevator;

public class Elevator implements Runnable {
	boolean oneRound = false;
	int id;
	ElevatorScene scene;
	public Elevator(ElevatorScene scene, int id) {
		this.scene = scene;
		this.id = id;
	}
	@Override
	public void run() {
		
		while(true) {
			// kill thread
			if(ElevatorScene.elevatorsMayDie) {
				return;
			}
			
			int numberOfFloor = scene.getNumberOfFloors();
			try {
				
				// forloop fyrir að láta lyftuna fara upp
				for(int i = 0; i < numberOfFloor; i++){
					ElevatorScene.elevatorWaitMutex.acquire();
					// lyfta nær í hversu margir eru að fara út á hæð lyftuna ef það er 0 þá release() allout semaphoreinu
					int leaveElevator = scene.leaveThisFloor(scene.getCurrentFloorForElevator(id), id);
					if(leaveElevator == 0) {
		
						ElevatorScene.allOut[id].release();
					}
					ElevatorScene.elevatorWaitMutex.release();
					// hleypur fólk út
					for(int x = 0; x < leaveElevator ; x++) {
						Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
						ElevatorScene.elvatorOutMutex.acquire();
						ElevatorScene.out[id][scene.getCurrentFloorForElevator(id)].release();
						ElevatorScene.elvatorOutMutex.release();		
					}
					
					
					ElevatorScene.elevatorWaitMutex1.acquire();
					// tekkja hvort allir eru komnir út
					ElevatorScene.allOut[id].acquire();
					int space = scene.checkSpaceInElevator(id);
					int waitingAtFloor = scene.getNumberOfPeopleWaitingAtFloor(scene.getCurrentFloorForElevator(id));
					ElevatorScene.elevatorWaitMutex1.release();
					// ef pláss í lyftu er meira en þeir sem eru að bíða þessar hæð ef svo hleypum þeim fjölda inn 
					if(space > waitingAtFloor && oneRound) {
						for(int x = 0; x < waitingAtFloor; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.elvatorInMutex.acquire();
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
							scene.setElevatorID(id);
							ElevatorScene.setIDSemaphore.release();
							ElevatorScene.getIDSemaphore.acquire();
							ElevatorScene.elvatorInMutex.release();
						}
					}
					else if(space != 0) {  //ef pláss er ekki 0 taka fólk inn þá
						for(int x = 0; x < space; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.elvatorInMutex.acquire();
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
							scene.setElevatorID(id);
							ElevatorScene.setIDSemaphore.release();
							ElevatorScene.getIDSemaphore.acquire();
							ElevatorScene.elvatorInMutex.release();
						}
					}
					
					Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
					ElevatorScene.elevatorWaitMutex4.acquire();
					if(numberOfFloor - 1 != scene.getCurrentFloorForElevator(id)) {
						scene.incrementFloor(id);
					}
					ElevatorScene.elevatorWaitMutex4.release();
					oneRound = true;
	
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				// forloopa fyrir lyfta fari niður og allt endurtekið
				for(int i = 0; i < numberOfFloor; i++) {
					ElevatorScene.elevatorWaitMutex2.acquire();
					int leaveElevator = scene.leaveThisFloor(scene.getCurrentFloorForElevator(id), id);
					if(leaveElevator == 0) {
						ElevatorScene.allOut[id].release();
					}
					ElevatorScene.elevatorWaitMutex2.release();
					
					for(int x = 0; x < leaveElevator ; x++) {
						Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
						ElevatorScene.elvatorOutMutex.acquire();
						ElevatorScene.out[id][scene.getCurrentFloorForElevator(id)].release();
						ElevatorScene.elvatorOutMutex.release();
						
					}
					
					ElevatorScene.elevatorWaitMutex3.acquire();
					ElevatorScene.allOut[id].acquire();
					int space = scene.checkSpaceInElevator(id);
					int waitingAtFloor = scene.getNumberOfPeopleWaitingAtFloor(scene.getCurrentFloorForElevator(id));
					ElevatorScene.elevatorWaitMutex3.release();
					if(space > waitingAtFloor && oneRound) {
						for(int x = 0; x < waitingAtFloor; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.elvatorInMutex.acquire();
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
							scene.setElevatorID(id);
							ElevatorScene.setIDSemaphore.release();
							ElevatorScene.getIDSemaphore.acquire();
							ElevatorScene.elvatorInMutex.release();
						}
					}
					else if(space != 0) { 
						for(int x = 0; x < space; x++) {
							Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
							ElevatorScene.elvatorInMutex.acquire();
							ElevatorScene.in[scene.getCurrentFloorForElevator(id)].release();
							scene.setElevatorID(id);
							ElevatorScene.setIDSemaphore.release();
							ElevatorScene.getIDSemaphore.acquire();
							ElevatorScene.elvatorInMutex.release();
						}
					}
					
					Thread.sleep(ElevatorScene.VISUALIZATION_WAIT_TIME);
					scene.decrementFloor(id);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}
