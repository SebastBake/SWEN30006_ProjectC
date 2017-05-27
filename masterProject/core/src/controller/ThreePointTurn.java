package controller;

import utilities.Coordinate;
import world.World;
import world.WorldSpatial;

public class ThreePointTurn extends Driver {
	
	public final float F_SPEED = 3;
	public final float R_SPEED = 3;
	public final static float SIDE_SPACE_REQ = 2;
	
	// stop the acceleration
	private boolean shouldStop = false;
	
	// change made
	// an attribute to store the previous Direction
	private WorldSpatial.Direction previousDirection;

	@Override
	public void behave(MyAIController controller, float delta) {
		
		if(previousDirection == null){
			previousDirection = controller.getOrientation();
		}
		WorldSpatial.Direction direction = controller.getOrientation();
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		
		// the first time it makes a turn
		boolean isTurnLeft = true;
		
		// three locations stand for 3 points in 3-point turn
		Coordinate firstLocation = null;
		Coordinate secondLocation = null;
		Coordinate thirdLocation = null;
		
		if(firstLocation == null){
			firstLocation = new Coordinate(controller.getPosition());
		}
		
		// first point
		if(controller.getOrientation().equals(previousDirection)){
			int distToLeft = 10;
			int distToRight = 10;
			switch(direction){
			case NORTH:
				for(int i = 0 ; i < 3; i++){
					if(!World.lookUp(firstLocation.x - i, firstLocation.y).getName().equals("Road")){
						distToLeft = i;
					}
					if(!World.lookUp(firstLocation.x + i, firstLocation.y).getName().equals("Road")){
						distToRight = i;
					}
				}
				break;
			case SOUTH:
				for(int i = 0 ; i < 3; i++){
					if(!World.lookUp(firstLocation.x + i, firstLocation.y).getName().equals("Road")){
						distToLeft = i;
					}
					if(!World.lookUp(firstLocation.x - i, firstLocation.y).getName().equals("Road")){
						distToRight = i;
					}
				}
				break;
			case WEST:
				for(int i = 0 ; i < 3; i++){
					if(!World.lookUp(firstLocation.x, firstLocation.y - i).getName().equals("Road")){
						distToLeft = i;
					}
					if(!World.lookUp(firstLocation.x, firstLocation.y + i).getName().equals("Road")){
						distToRight = i;
					}
				}
				break;
			case EAST:
				for(int i = 0 ; i < 3; i++){
					if(!World.lookUp(firstLocation.x, firstLocation.y + i).getName().equals("Road")){
						distToLeft = i;
					}
					if(!World.lookUp(firstLocation.x, firstLocation.y - i).getName().equals("Road")){
						distToRight = i;
					}
				}
				break;
			}
			if(controller.getVelocity() < F_SPEED && !shouldStop){
				controller.applyForwardAcceleration();
			}
			// only do turn once
			if(controller.getPosition().equals(firstLocation.toString())){
				if(distToLeft > distToRight){
					controller.turnLeft(delta);
					isTurnLeft = true;
				}else{
					controller.turnRight(delta);
					isTurnLeft = false;
				}
			}
			switch(previousDirection){
			case NORTH:
				if(isTurnLeft){
					if(!World.lookUp(currentPosition.x - 2, currentPosition.y).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}else{
					if(!World.lookUp(currentPosition.x + 2, currentPosition.y).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}
				break;
			case SOUTH:
				if(isTurnLeft){
					if(!World.lookUp(currentPosition.x + 2, currentPosition.y).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}else{
					if(!World.lookUp(currentPosition.x - 2, currentPosition.y).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}
				break;
			case WEST:
				if(isTurnLeft){
					if(!World.lookUp(currentPosition.x, currentPosition.y - 2).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}else{
					if(!World.lookUp(currentPosition.x, currentPosition.y + 2).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}
				break;
			case EAST:
				if(isTurnLeft){
					if(!World.lookUp(currentPosition.x, currentPosition.y + 2).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}else{
					if(!World.lookUp(currentPosition.x, currentPosition.y - 2).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}
				break;	
			}
		}
		
		// change points when car has stopped
		if(controller.getVelocity() == 0){
			if(secondLocation == null){
				secondLocation = new Coordinate(controller.getPosition());
				shouldStop = false;
			}else{
				thirdLocation = new Coordinate(controller.getPosition());
				shouldStop = false;
			}				
		}
		
		// second point
		if(secondLocation != null && thirdLocation == null){
			if(controller.getVelocity() < R_SPEED && !shouldStop){
				controller.applyReverseAcceleration();
			}
			// only turn once
			if(secondLocation.toString().equals(controller.getPosition())){
				if(isTurnLeft){
					controller.turnRight(delta);
					controller.turnRight(delta);
				}else{
					controller.turnLeft(delta);
					controller.turnLeft(delta);
				}
			}
			switch(previousDirection){
			case NORTH:
				if(isTurnLeft){
					if(!World.lookUp(currentPosition.x + 2, currentPosition.y).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}else{
					if(!World.lookUp(currentPosition.x - 2, currentPosition.y).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}
				break;
			case SOUTH:
				if(isTurnLeft){
					if(!World.lookUp(currentPosition.x - 2, currentPosition.y).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}else{
					if(!World.lookUp(currentPosition.x + 2, currentPosition.y).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}
				break;
			case WEST:
				if(isTurnLeft){
					if(!World.lookUp(currentPosition.x, currentPosition.y + 2).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}else{
					if(!World.lookUp(currentPosition.x, currentPosition.y - 2).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}
				break;
			case EAST:
				if(isTurnLeft){
					if(!World.lookUp(currentPosition.x, currentPosition.y - 2).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}else{
					if(!World.lookUp(currentPosition.x, currentPosition.y + 2).getName().equals("Road")){
						controller.applyBrake();
						shouldStop = true;
					}
				}
				break;
			}		
		}
		
		// third point
		if(secondLocation != null && thirdLocation != null){
			if(controller.getVelocity() < F_SPEED && !shouldStop){
				controller.applyForwardAcceleration();
			}
			// only turn once
			if(secondLocation.toString().equals(controller.getPosition())){
				if(isTurnLeft){
					controller.turnLeft(delta);
					controller.turnLeft(delta);
				}else{
					controller.turnRight(delta);
					controller.turnRight(delta);
				}
			}
		}
		
	}

	@Override
	public boolean isDone(MyAIController controller) {
		WorldSpatial.Direction direction = controller.getOrientation();
		switch(previousDirection){
		case NORTH:
			if(direction.equals(WorldSpatial.Direction.SOUTH));
			return true;
		case SOUTH:
			if(direction.equals(WorldSpatial.Direction.NORTH));
			return true;
		case WEST:
			if(direction.equals(WorldSpatial.Direction.EAST));
			return true;
		case EAST:
			if(direction.equals(WorldSpatial.Direction.WEST));
			return true;		
		}
		return false;
	}

}
