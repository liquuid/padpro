abstract class BaseEffects implements ObjEffect{
	
	ObjEffect player;
	
	public ObjEffect removePowerUP(ObjEffect toRemove) {
		  if (player == null) {
		    return null;
		  } else
		    return player.getObj();
	}

	public void draw(){
		this.player.draw();
	}
	
	public int getState(){
		return this.player.getState();
	}
	
	public void setState(int state){
		this.player.setState(state);
	}
	
	public double getX(){
		return this.player.getX();
	}
	
	public void setX(double x){
		this.player.setX(x);
	}
	
	public double getY(){
		return this.player.getY();
	}
	
	public void setY(double y){
		this.player.setY(y);
	}
	
	public double getVx(){
		return this.player.getVx();
	}
	
	public void setVx(double vx){
		this.player.setVx(vx);
	}
	
	public double getVy(){
		return this.player.getVy();
	}
	
	public void setVy(double vy){
		this.setVy(vy);
	}
	
	public double getRadius(){
		return this.player.getRadius();
	}
	
	public void setRadius(double radius){
		this.player.setRadius(radius);
	}
	
	public double getExplosion_start(){
		return this.player.getExplosion_start();
	}
	
	public void setExplosion_start(double explosion_start){
		this.player.setExplosion_start(explosion_start);
	}
	
	public double getExplosion_end(){
		return this.player.getExplosion_end();
	}
	
	public void setExplosion_end(double explosion_end){
		this.player.setExplosion_end(explosion_end);
	}
	
	public long getNextShot(){
		return this.player.getNextShot();
	}
	
	public void setNextShot(long nextShot){
		this.player.setNextShot(nextShot);
	}
	public ObjEffect getObj(){
		return this.player;
	}
}
