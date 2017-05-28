package controller;

public class FrontAlign_Forward extends Driver {
	
	public static final float F_SPEED = 3;
	public static final float T_SPEED = 2;
	public static final int T_PROXIMITY = 2;

	@Override
	public void behave(MyAIController controller, float delta) {
		float angle = controller.getCarNodeOrientation(null);
		float velocity = controller.getVelocity();
		if(angle <= 10 && angle >= -10){
			if(velocity < F_SPEED){
				controller.applyForwardAcceleration();
			}
		} else {
			if(angle < -10){
				if(velocity < T_SPEED){
					controller.applyForwardAcceleration();
				} else if(velocity > T_SPEED){
					controller.applyBrake();
				}
				controller.turnLeft(delta);
			} else if (angle > 10){
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
		return true;
	}
	
	public String toString(){
		return "FrontAlign_Forward";
	}

}
