package controller;

import utilities.Coordinate;

public class SimpleReverse extends Driver {
	
	public static final float R_SPEED = 2;
	public static final float T_SPEED = 2;
	
	public Coordinate currentCoord = null;
	private boolean done = false;

	@Override
	public void behave(MyAIController controller, float delta) {
		Coordinate tempCoord = controller.currentLoc;
		if(currentCoord == null){
			currentCoord = tempCoord;
		}
		
		if(currentCoord.x != tempCoord.x || currentCoord.y != tempCoord.y){
			done = true;
		}
		
		if(controller.getVelocity() < R_SPEED){
			controller.applyReverseAcceleration();
		}
	}

	@Override
	public boolean isDone(MyAIController controller) {
		return true;
	}
	
	public String toString(){
		return "SimpleReverse";
	}

}
