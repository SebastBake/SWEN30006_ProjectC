package controller;

public class RearAlign_Reverse extends Driver {
	
	public static final float R_SPEED = 2;
	public static final float T_SPEED = 2;
	public static final int T_PROXIMITY = 2;

	@Override
	public void behave(MyAIController controller, float delta) {
		float angle = controller.getCarNodeOrientation();
		float velocity = controller.getVelocity();
		if(angle == 0){
			// not sure if this should be > or <
			if(velocity > R_SPEED){
				controller.applyReverseAcceleration();
			}
		} else {
			if(angle < 0){
				if(velocity > T_SPEED){
					controller.applyBrake();
				}
				controller.turnRight(delta);
			} else if (angle > 0){
				if(velocity > T_SPEED){
					controller.applyBrake();
				}
				controller.turnLeft(delta);
			}
		}
		
	}

	@Override
	public boolean isDone(MyAIController controller) {
		// TODO Auto-generated method stub
		return false;
	}

}
