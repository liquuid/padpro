import java.awt.Color;
import java.util.List;


/*Singleton*/
public class Player implements ObjEffect {
	private static Player instance;
	
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
	
	
	/* Construtor Privado da Classe.*/
	private Player() {
	} 
	
	
	/*Inplementado getInstance para criar o player único seguindo padrão Singleton*/
	public static Player getInstance(double x, double y) {
		if(instance == null) {
			instance = new  Player();
			instance.setState(ACTIVE);
			instance.setX(x);
			instance.setY(y);
			instance.setVx(0.25);
			instance.setVy(0.25);
			instance.setRadius(12);	
		}
		return instance;
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
	public ObjEffect getObj(){
		return this;
	}
	public ObjEffect removePowerUP(ObjEffect toRemove) {
		return this;
	}
}
