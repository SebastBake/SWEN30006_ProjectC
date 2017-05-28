package controller;

public class RearAlign_Reverse extends Driver {
	
	public static final float R_SPEED = (float)1.8;
	public static final float T_SPEED = 2;
	public static final int T_PROXIMITY = 2;

	@Override
	public void behave(MyAIController controller, float delta) {
		float angle = controller.getCarNodeOrientation(null);
		float velocity = controller.getVelocity();
		if(Math.abs(Math.toDegrees(angle)) >= 185 ){
			// not sure if this should be > or <
			if(velocity > R_SPEED){
				controller.applyReverseAcceleration();
			} else if (Math.abs(velocity) < R_SPEED){
				controller.applyReverseAcceleration();
			}
		} else {
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
		return true;
	}

	
	public String toString(){
		return "RearAlign_Reverse";
	}
}
