package mycontroller;

public abstract class Driver {
	
	public Driver changeBehavior(MyAIController controller){
		
	}
	
	public abstract void behave(MyAIController controller, float delta);
	public abstract boolean isDone(MyAIController controller);

}
