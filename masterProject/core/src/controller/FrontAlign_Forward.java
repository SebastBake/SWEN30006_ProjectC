package controller;

public class FrontAlign_Forward extends Driver {
	/**
	 * The base movement Driver:
	 * Moves the car forwards and adjusts its angle to point to the next node
	 */
	
	public static final float F_SPEED = 3;
	public static final float T_SPEED = 2;
	public static final int T_PROXIMITY = 2;
	
	// The angle of sensitivity before the driver should be changed.
	// this is a possible cause of misbehaviour.
	public static final int SENSITIVE_ANGLE = 10;

	@Override
	public void behave(MyAIController controller, float delta) {
		// Get the angle between the car and the node
		float angle = controller.getCarNodeOrientation(null);
		float velocity = controller.getVelocity();
		
		
		if(angle <= SENSITIVE_ANGLE && angle >= -SENSITIVE_ANGLE){
			if(velocity < F_SPEED){
				controller.applyForwardAcceleration();
			}
		} else {
			// Move forwards and turn (but not too fast)
			if(angle < -SENSITIVE_ANGLE){
				if(velocity < T_SPEED){
					controller.applyForwardAcceleration();
				} else if(velocity > T_SPEED){
					controller.applyBrake();
				}
				controller.turnLeft(delta);
			} else if (angle > SENSITIVE_ANGLE){
				if(velocity < T_SPEED){
					controller.applyForwardAcceleration();
				} else if(velocity > T_SPEED){
					controller.applyBrake();
				}
				controller.turnRight(delta);
			}
		}
		
	}

	@Override
	public boolean isDone(MyAIController controller) {
		// How to tell if FrontAlign_Forward is done (It will always hand over since it is
		// the default state.
		return true;
	}
	
	public String toString(){
		return "FrontAlign_Forward";
	}

}
