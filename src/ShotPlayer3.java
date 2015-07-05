import java.awt.Color;
import java.util.List;


public class ShotPlayer3 implements Shot {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;

	private double x;
	private double y;
	private double vx = 0.0;
	private double vy = -0.5 ;

	private int state = ACTIVE;
	private double radius = 30.0;
	
	public ShotPlayer3(ObjEffect player) {
		this.x = player.getX();
		this.y = player.getY() - 2 * player.getRadius() ;
		
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
			
			GameLib.setColor(Color.YELLOW);
						
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
	
	public void colisionDetection(List<Enemy> listEnemies) {
		for (Enemy enemy : listEnemies) {
			if (enemy.state() == ACTIVE) {
				
				double dx = enemy.posX() - this.x ;
				double dy = enemy.posY() - this.y;
				double dist = Math.sqrt(dx * dx + dy * dy);
				

				if (dist - this.radius < enemy.radius()) {
					long currentTime = System.currentTimeMillis();

					enemy.setState(EXPLODING);
					enemy.setExplosionStart(currentTime);
					enemy.setExplosionEnd(currentTime + 500);
				}
			}
		}
	}

	public void colisionDetection(Object obj) {
		// TODO Auto-generated method stub
	}

	@Override
	public void colisionDetection(ObjEffect player) {
		// TODO Auto-generated method stub
	}
}
