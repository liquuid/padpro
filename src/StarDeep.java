import java.awt.Color;

public class StarDeep implements Star {
	private double velocidade;
	private double x;
	private double y;
	private Color color = null;
	
	public StarDeep(double velocidade, Color color){
		this.velocidade = velocidade;
		this.x = Math.random() * GameLib.WIDTH;
		this.y = Math.random() * GameLib.HEIGHT;
		this.color = color;
	}
	@Override
	public void draw(){
		GameLib.setColor(this.color);
		GameLib.fillRect(this.posX(), this.posY() % GameLib.HEIGHT, 2, 2);
	}
	@Override
	public void move(long delta){
		this.y += this.velocidade * delta ;
	}
	@Override
	public double posX(){
		return this.x;
	}
	@Override
	public double posY(){
		return this.y;
	}
}
