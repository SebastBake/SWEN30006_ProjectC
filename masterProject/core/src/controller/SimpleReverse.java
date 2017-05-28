package controller;

import utilities.Coordinate;

public class SimpleReverse extends Driver {
	
	public static final float R_SPEED = 2;
	public static final float T_SPEED = 2;

	@Override
	public void behave(MyAIController controller, float delta) {
		// Just go back a little
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
