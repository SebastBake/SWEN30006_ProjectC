package controller;

public class FrontAlign_Forward extends Driver {
	
	public static final float F_SPEED = 4;
	public static final float T_SPEED = 2;
	public static final int T_PROXIMITY = 2;

	@Override
	public void behave(MyAIController controller, float delta) {
		float angle = controller.getCarNodeOrientation(null);
		float velocity = controller.getVelocity();
		if(angle == 0){
			if(velocity < F_SPEED){
				controller.applyForwardAcceleration();
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
		return true;
	}

}
