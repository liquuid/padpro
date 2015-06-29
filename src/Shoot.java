
public interface Shoot {
	public void move(long delta);
	public void atira(long delta, double x, double y, double vx, double vy, double angle);
	public void atira(long delta, double x, double y, double[] angles);
	public void draw();
	public double getRadius();
	public int getSize();
	public double getPosX(int i);
	public double getPosY(int i);
}
