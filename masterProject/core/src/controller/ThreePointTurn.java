package controller;

import utilities.Coordinate;
import world.World;
import world.WorldSpatial;

public class ThreePointTurn extends Driver {
	
	public final float F_SPEED = 3;
	public final float R_SPEED = 3;
	public final static float SIDE_SPACE_REQ = 1;
	
	// stop the acceleration
	private boolean shouldStop = false;
	
	// change made
	// an attribute to store the previous Direction
	private WorldSpatial.Direction previousDirection = null;
	
	// three locations stand for 3 points in 3-point turn
	private Coordinate firstLocation = null;
	private Coordinate secondLocation = null;
	private Coordinate thirdLocation = null;

	@Override
	public void behave(MyAIController controller, float delta) {
		int distToLeft = 10;
		int distToRight = 10;
		
		if(previousDirection == null){
			previousDirection = controller.getOrientation();
		}
		WorldSpatial.Direction direction = controller.getOrientation();
		Coordinate currentPosition = new Coordinate(controller.getPosition());
		
		// the first time it makes a turn
		boolean isTurnLeft = true;
		
		if(firstLocation == null){
			firstLocation = new Coordinate(controller.getPosition());
			if(controller.getOrientation().equals(previousDirection)){
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
		}
		
		// first point
		if(firstLocation != null && secondLocation == null){
			
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
						if(!World.lookUp(currentPosition.x - 1, currentPosition.y).getName().equals("Road")){
							controller.applyBrake();
							shouldStop = true;
						}
					}else{
						if(!World.lookUp(currentPosition.x + 1, currentPosition.y).getName().equals("Road")){
							controller.applyBrake();
							shouldStop = true;
						}
					}
					break;
				case SOUTH:
					if(isTurnLeft){
						if(!World.lookUp(currentPosition.x + 1, currentPosition.y).getName().equals("Road")){
							controller.applyBrake();
							shouldStop = true;
						}
					}else{
						if(!World.lookUp(currentPosition.x - 1, currentPosition.y).getName().equals("Road")){
							controller.applyBrake();
							shouldStop = true;
						}
					}
					break;
				case WEST:
					if(isTurnLeft){
						if(!World.lookUp(currentPosition.x, currentPosition.y - 1).getName().equals("Road")){
							controller.applyBrake();
							shouldStop = true;
						}
					}else{
						if(!World.lookUp(currentPosition.x, currentPosition.y + 1).getName().equals("Road")){
							controller.applyBrake();
							shouldStop = true;
						}
					}
					break;
				case EAST:
					if(isTurnLeft){
						if(!World.lookUp(currentPosition.x, currentPosition.y + 1).getName().equals("Road")){
							controller.applyBrake();
							shouldStop = true;
						}
					}else{
						if(!World.lookUp(currentPosition.x, currentPosition.y - 1).getName().equals("Road")){
							controller.applyBrake();
							shouldStop = true;
						}
					}
					break;	
				}
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
				}else{
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
			if(direction.equals(WorldSpatial.Direction.SOUTH)){
				return true;
			}	
			break;
		case SOUTH:
			if(direction.equals(WorldSpatial.Direction.NORTH)){
				return true;
			}
			break;
		case WEST:
			if(direction.equals(WorldSpatial.Direction.EAST)){
				return true;
			}
			break;
		case EAST:
			if(direction.equals(WorldSpatial.Direction.WEST)){
				return true;
			}
			break;
		}
		return false;
	}
	
	public String toString(){
		return "ThreePointTurn";
	}

}
