
package controller;

public abstract class Driver {
	
	public Driver changeBehavior(MyAIController controller){
		float angle = controller.getCarNodeOrientation();
		if(java.lang.Math.abs(angle) <= 90){
			// Detect collisions and grass edges
			Driver driver = new FrontAlign_Forward();
			
			if(controller.detectCollision()){
				driver = new RearAlign_Reverse();
			} else if (controller.detectGrassEdge()){
				driver = new StopAndRedirect();
			} //else {
//				Driver simpleDriver = new FrontAlign_Forward();
//				controller.newDriver(simpleDriver);
//			}
			
			controller.newDriver(driver);
			
		} else {
			//	RearAlign_Reverse is the slowest way to reverse,
			//	so I assign that first and then if there is space I
			//	change it to be a more suitable turning method
			
			float space = controller.getMaxSideSpace();
			Driver turn = new RearAlign_Reverse();
			
			if(space < UTurn.SIDE_SPACE_REQ){
				turn = new UTurn();
			} else if (space < ThreePointTurn.SIDE_SPACE_REQ){
				turn = new ThreePointTurn();
			}
			
			controller.newDriver(turn);
		}
		return null;
		
	}
	
	public abstract void behave(MyAIController controller, float delta);
	public abstract boolean isDone(MyAIController controller);

}
