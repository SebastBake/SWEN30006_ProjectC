package controller;

import world.WorldSpatial;

public class ThreePointTurn extends Driver {
	
	public final float F_SPEED = 3;
	public final float R_SPEED = 3;
	public final float SIDE_SPACE_REQ = 2;
	
	// change made
	// an attribute to store the previous Direction
	private WorldSpatial.Direction previousDirection;

	@Override
	public void behave(MyAIController controller, float delta) {
		if(previousDirection == null){
			previousDirection = controller.getOrientation();
		}
		
		// first stop
		if(controller.getOrientation().equals(previousDirection)){
			
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
