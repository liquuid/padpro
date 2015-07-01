import java.awt.Color;
import java.util.List;


public class ShotPlayer2 implements Shot {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;

	private double x;
	private double x2;
	private double y;
	private double y2;
	private double vx = 0.0;
	private double vx2 = 0.0;
	private double vy = -1.0 ;
	private double vy2 = -1.0 ;
	private int state = ACTIVE;
	private double radius = 2.0;
	
	public ShotPlayer2(Player player) {
		this.x = player.getX();
		this.y = player.getY() - 2 * player.getRadius() ;
		
		this.x2 = player.getX();
		this.y2 = player.getY() - 2 * player.getRadius() ;
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
			
			GameLib.drawLine(this.x - 5, this.y - 3, this.x - 1, this.y + 3);
			GameLib.drawLine(this.x - 6, this.y - 3, this.x - 2, this.y + 3);
			GameLib.drawLine(this.x - 6, this.y - 3, this.x - 2, this.y + 3);
			
			GameLib.drawLine(this.x2 + 5, this.y2 - 3, this.x2 + 1, this.y2 + 3);
			GameLib.drawLine(this.x2 + 6, this.y2 - 3, this.x2 + 2, this.y2 + 3);
			GameLib.drawLine(this.x2 + 6, this.y2 - 3, this.x2 + 2, this.y2 + 3);
		}
	}


	@Override
	public void move(double delta) {
		this.x += this.vx * delta;
		this.y += this.vy * delta;
		this.vx -= 0.015;
		
		this.x2 += this.vx2 * delta;
		this.y2 += this.vy2 * delta;
		this.vx2 += 0.015;
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
				
				double dx = enemy.posX() - this.x;
				double dy = enemy.posY() - this.y;
				double dist = Math.sqrt(dx * dx + dy * dy);
				
				double dx2 = enemy.posX() - this.x2;
				double dy2 = enemy.posY() - this.y2;
				double dist2 = Math.sqrt(dx2 * dx2 + dy2 * dy2);

				if (dist < enemy.radius() || dist2 < enemy.radius()) {
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
	public void colisionDetection(Player player) {
		// TODO Auto-generated method stub
		
	}
}
