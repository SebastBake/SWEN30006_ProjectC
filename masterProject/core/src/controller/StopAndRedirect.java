package controller;

import java.util.HashMap;

import tiles.MapTile;
import utilities.Coordinate;

public class StopAndRedirect extends Driver {
	
	public static final float F_SPEED = 3;
	public static final float T_SPEED = 1;
	public static final int T_PROXIMITY = 2;
	private enum State {Stopping, Aligning, Done};
	State state = State.Stopping;
	private Node stoppingNode = null;
	private Node nextNode = null;

	@Override
	public void behave(MyAIController controller, float delta) {
		if(stoppingNode == null){
			stoppingNode = controller.pathList.get(0);
			nextNode = controller.pathList.get(1);
		}
		if(state == State.Stopping){
			controller.applyBrake();
			if(controller.distanceTo(stoppingNode) < T_PROXIMITY){
				
				if(controller.getVelocity() < T_SPEED){
					state = State.Aligning;
				}
			}
		} else if(state == State.Aligning){
			float angle = controller.getCarNodeOrientation(nextNode);
			if(angle > 0){
				controller.turnRight(delta);
			} else if(angle < 0){
				controller.turnLeft(delta);
			}
		}
	}

	@Override
	public boolean isDone(MyAIController controller) {
//		if(state == State.Done){
//			return true;
//		}
		return true;
	}
	
	public String toString(){
		return "StopAndRedirect";
	}
}
