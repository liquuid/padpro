import java.util.List;


interface ObjEffect {
	public void draw();
	public void shot(List<Shot> listShoots);
	
	public int getState();
	public void setState(int state);
	public double getX();
	public void setX(double x);
	public double getY();
	public void setY(double y);
	public double getVx();
	public void setVx(double vx);
	public double getVy();
	public void setVy(double vy);
	public double getRadius();
	public void setRadius(double radius);
	public double getExplosion_start();
	public void setExplosion_start(double explosion_start);
	public double getExplosion_end();
	public void setExplosion_end(double explosion_end);
	public long getNextShot();
	public void setNextShot(long nextShot);
	public ObjEffect getObj();
	public ObjEffect removePowerUP(ObjEffect toRemove); 
}
