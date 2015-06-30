import java.util.List;


public interface Shot {
	public void draw();
	public void move(double delta);
	public void setRadius(double radius);
	public double getRadius();
	public void setState(int state);
	public int getState();
	public double getX();
	public void setX(double x);
	public double getY();
	public void setY(double y);
	public double getVx();
	public void setVx(double vx);
	public double getVy();
	public void setVy(double vy);
	public void colisionDetection(Player player);
	public void colisionDetection(List<Enemy> enemies);
}
