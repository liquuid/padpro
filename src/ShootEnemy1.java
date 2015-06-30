import java.awt.Color;


public class ShootEnemy1 implements Shoot {

	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;

	private double x;
	private double y;
	private double vx;
	private double vy;
	private int state = ACTIVE;
	private double radius = 2.0;
	

	public ShootEnemy1(Enemy enemy) {
		this.x = enemy.posX();
		this.y = enemy.posY();
		this.vx = Math.cos(enemy.getAngle()) * 0.45;
		this.vy = Math.sin(enemy.getAngle()) * 0.45 * (-1.0);
				
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


	@Override
	public void draw() {
		if(this.state == ACTIVE){
			GameLib.setColor(Color.RED);
			GameLib.drawCircle(this.x, this.y, this.radius);
		}
		
	}


	@Override
	public void move(double delta) {
		this.x += this.vx * delta;
		this.y += this.vy * delta;
	}


	public int getState() {
		return state;
	}


	public void setState(int state) {
		this.state = state;
	}


	public double getRadius() {
		return radius;
	}


	public void setRadius(double radius) {
		this.radius = radius;
	}
}
