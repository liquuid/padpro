import java.awt.Color;
import java.util.ArrayList;
import java.util.List;


public class Background {
	private static Background instance;

	private List<Star> background = new ArrayList<Star>();
	private Background(){
		
	}
	public static Background getInstance(){
		if(instance == null){
			instance = new Background();
		}
		return instance;
			
	}
	public void init(){
		for(int i = 0; i != 50; i++){
			this.background.add(new StarDeep(0.045, Color.DARK_GRAY));
		}
		for(int i = 0; i != 20; i++){
			this.background.add(new StarFront(0.070, Color.GRAY));
		}
	}
	public void draw(){
		
	}
	public void tick(long delta){
				for (Star star : this.background){
					star.move(delta);
					star.draw();
				}
	}

}
