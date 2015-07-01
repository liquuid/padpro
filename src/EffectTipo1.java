import java.util.List;


public class EffectTipo1 extends BaseEffects{
	
	public EffectTipo1(ObjEffect pl){
		this.player = pl;
	}
	
	public void shot(List<Shot> listShoots){
		long nextShot = this.player.getNextShot();
		long currentTime = System.currentTimeMillis();
		
		if(currentTime > nextShot){
			nextShot = (long) (currentTime + 200 + Math.random() * 500);
			Shot shot = new ShotPlayer2(player);
			listShoots.add(shot);
		}	
	}
}
