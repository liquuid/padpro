import java.awt.Color;
import java.util.List;


public class Player {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	
	private int state;
	private double x;
	private double y;
	private double vx;
	private double vy;
	private double radius;
	private double explosion_start;
	private double explosion_end;
	private long nextShot;
	public Player(double x, double y){
		this.setState(ACTIVE);
		this.setX(x);
		this.setY(y);
		this.setVx(0.25);
		this.setVy(0.25);
		this.setRadius(12);		
	}
		
	public void draw(){
		long currentTime = System.currentTimeMillis();
		
		if(this.state == EXPLODING){
			double alpha = (currentTime - this.explosion_start) / (this.explosion_end - this.explosion_start);
			GameLib.drawExplosion(this.x, this.y, alpha);
		}
		else{
			GameLib.setColor(Color.BLUE);
			GameLib.drawPlayer(this.x, this.y, this.radius);
		}
	}
	public void shot(List<Shot> listShoots){
		long currentTime = System.currentTimeMillis();
		
		if(currentTime > this.nextShot){
			this.nextShot = (long) (currentTime + 200 + Math.random() * 500);
			Shot shot = new ShotPlayer(this);
			listShoots.add(shot);
		}		
	}
	
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getVx() {
		return vx;
	}
	public void setVx(double vx) {
		this.vx = vx;
	}
	public double getVy() {
		return vy;
	}
	public void setVy(double vy) {
		this.vy = vy;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public double getExplosion_start() {
		return explosion_start;
	}
	public void setExplosion_start(double explosion_start) {
		this.explosion_start = explosion_start;
	}
	public double getExplosion_end() {
		return explosion_end;
	}
	public void setExplosion_end(double explosion_end) {
		this.explosion_end = explosion_end;
	}
	public long getNextShot() {
		return nextShot;
	}
	public void setNextShot(long nextShot) {
		this.nextShot = nextShot;
	}

}
