package controller;

import utilities.Coordinate;
import world.*;

public class UTurn extends Driver {
	
	public final float F_SPEED = 3;
	public final float SIDE_SPACE_REQ = 4;
	// change made
	// an attribute to store the previous Direction
	private WorldSpatial.Direction previousDirection;

	@Override
	public void behave(MyAIController controller, float delta) {
		WorldSpatial.Direction direction = controller.getOrientation();
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		int distToLeft = 10;
		int distToRight = 10;
		switch(direction){
		case NORTH:
			for(int i = 0 ; i < 3; i++){
				if(!World.lookUp(currentPosition.x - i, currentPosition.y).getName().equals("Road")){
					distToLeft = i;
				}
				if(!World.lookUp(currentPosition.x + i, currentPosition.y).getName().equals("Road")){
					distToRight = i;
				}
			}
			break;
		case SOUTH:
			for(int i = 0 ; i < 3; i++){
				if(!World.lookUp(currentPosition.x + i, currentPosition.y).getName().equals("Road")){
					distToLeft = i;
				}
				if(!World.lookUp(currentPosition.x - i, currentPosition.y).getName().equals("Road")){
					distToRight = i;
				}
			}
			break;
		case WEST:
			for(int i = 0 ; i < 3; i++){
				if(!World.lookUp(currentPosition.x, currentPosition.y - i).getName().equals("Road")){
					distToLeft = i;
				}
				if(!World.lookUp(currentPosition.x, currentPosition.y + i).getName().equals("Road")){
					distToRight = i;
				}
			}
			break;
		case EAST:
			for(int i = 0 ; i < 3; i++){
				if(!World.lookUp(currentPosition.x, currentPosition.y + i).getName().equals("Road")){
					distToLeft = i;
				}
				if(!World.lookUp(currentPosition.x, currentPosition.y - i).getName().equals("Road")){
					distToRight = i;
				}
			}
			break;
		}
		controller.applyBrake();
		if(controller.getVelocity() < F_SPEED){
			controller.applyForwardAcceleration();
		}
		if(distToLeft > distToRight){
			switch(direction)
			// keep turn left until the car is heading opposite direction
			if(controller.getOrientation().equals(WorldSpatial.Direction.EAST)){
				
			}
			controller.turnLeft(delta);
		}else{
			// keep turn right until the car is heading opposite direction
			controller.turnRight(delta);
		}
		
	}

	@Override
	public boolean isDone(MyAIController controller) {
		// TODO Auto-generated method stub
		return false;
	}

}
