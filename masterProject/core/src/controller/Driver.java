
package controller;

public abstract class Driver {
	
	// change the current driver to another driver
	public Driver changeBehavior(MyAIController controller){
		float angle = ((float)Math.toDegrees( controller.getCarNodeOrientation(null) ));
		System.out.println(angle);
		Driver driver;
		if(controller.detectCollision()){
			driver = new  SimpleReverse();
		} else if(java.lang.Math.abs(angle) <= 90){
			// Detect collisions and grass edges
			driver = new FrontAlign_Forward();
			
			if (controller.detectGrassEdge()){
				driver = new StopAndRedirect();
			} //else {
//				Driver simpleDriver = new FrontAlign_Forward();
//				controller.newDriver(simpleDriver);
//			}
			
		} else {
			//	SimpleReverse is the slowest way to reverse,
			//	so I assign that first and then if there is space I
			//	change it to be a more suitable turning method
			
			float space = controller.getMaxSideSpace();
			driver = new RearAlign_Reverse();
			if(space >= UTurn.SIDE_SPACE_REQ){
				driver = new UTurn();
//			} else if (space >= ThreePointTurn.SIDE_SPACE_REQ){
//				driver = new ThreePointTurn();
			}
		}
		System.out.println(driver.toString());
		controller.newDriver(driver);
		return null;
		
	}
		
	public abstract void behave(MyAIController controller, float delta);
	public abstract boolean isDone(MyAIController controller);

}
