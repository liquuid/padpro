import java.awt.Color;


public class ShootPlayer implements Shoot {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;

	private double x;
	private double y;
	private double vx = 0.0;
	private double vy = -1.0 ;
	private int state = ACTIVE;
	private double radius = 2.0;
	
	public ShootPlayer(double x, double y) {
		this.x = x;
		this.y = y;
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
			
			GameLib.setColor(Color.GREEN);
			GameLib.drawLine(this.x, this.y - 5, this.x, this.y + 5);
			GameLib.drawLine(this.x - 1, this.y - 3, this.x - 1, this.y + 3);
			GameLib.drawLine(this.x + 1, this.y - 3, this.x + 1, this.y + 3);
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
