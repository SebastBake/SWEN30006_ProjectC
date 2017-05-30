package mycontroller;

public class RearAlign_Reverse extends Driver {
	/**
	 * The base reversing Driver.
	 * Very Similar to FrontAlign_Forward.
	 */
	
	public static final float R_SPEED = (float)1.8;
	public static final float T_SPEED = 2;
	public static final int T_PROXIMITY = 2;
	
	// The angle of sensitivity before the driver should be changed.
	// this is a possible cause of misbehaviour.
	public static final int SENSITIVE_ANGLE = 185;

	@Override
	public void behave(MyAIController controller, float delta) {
		// Get angle between car (current angle) and node
		float angle = controller.getCarNodeOrientation(null);
		float velocity = controller.getVelocity();
		
		if(Math.abs(Math.toDegrees(angle)) >= SENSITIVE_ANGLE ){
			//Only Reverse
			if(velocity > R_SPEED){
				controller.applyReverseAcceleration();
			} else if (Math.abs(velocity) < R_SPEED){
				controller.applyReverseAcceleration();
			}
		} else {
			//Reverse and turn
			if(angle > 0){
				if(Math.abs(velocity) > T_SPEED){
					controller.applyBrake();
				} else if (Math.abs(velocity) < R_SPEED){
					controller.applyReverseAcceleration();
				}
				controller.turnLeft(delta);
			} else if (angle < 0){
				if(Math.abs(velocity) > T_SPEED){
					controller.applyBrake();
				} else if (Math.abs(velocity) < R_SPEED){
					controller.applyReverseAcceleration();
				}
				controller.turnRight(delta);
			}
		}
		
	}

	@Override
	public boolean isDone(MyAIController controller) {
		// Always ready to be changed to a different strategy
		return true;
	}

	
	public String toString(){
		return "RearAlign_Reverse";
	}
}
