import java.util.List;


public interface Enemy {
	public void draw();
	public void move(long delta);
	public double posX();
	public double posY();
	public void setPosX(double x);
	public void setPosY(double y);
	public double radius();
	public int state();
	public void setState(int state);
	public void setExplosionStart(long time);
	public void setExplosionEnd(long time);
	public double getExplosionStart();
	public double getExplosionEnd();
	public double getV();
	public void setV(double v);
	public double getRV();
	public void setRV(double rv);
	public double getAngle();
	public void setAngle(double angle);
	public double getNextShoot();
	public void setNextShoot(long time);
	public void shoot(List<Shoot> listShoots, Player player);
	public void colisionDetection(Player player);
}
