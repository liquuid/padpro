import java.awt.Color;


public class PowerUP1 implements PowerUP {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;


	private int state;           	// estado
	private double x;					// coordenadas x
	private double y;					// coordenadas y
	private double v;					// velocidades
	private double angle;				// ângulos (indicam direção do movimento)
	private double rv;					// velocidades de rotação
	private double explosion_start;		// instantes dos inícios das explosões
	private double explosion_end;		// instantes dos finais da explosões
	private double radius = 20.0;		    // raio (tamanho do inimigo 1)

	public PowerUP1(int state) {
		this.state = state;
		this.x = Math.random() * (GameLib.WIDTH - 20.0) + 20.0;
		this.y = -10.0;
		this.v = 0.1 + Math.random() * 0.15;
		this.angle = 3 * Math.PI / 2;
		this.rv = 0.0;

	}

	@Override
	public void draw() {
		long currentTime = System.currentTimeMillis();
		if(this.state == EXPLODING){
			double alpha = (currentTime - this.explosion_start) / (this.explosion_end - this.explosion_start);
			GameLib.drawExplosion(this.x, this.y, alpha);
		}
		
		if(this.state == ACTIVE){
	
			GameLib.setColor(Color.YELLOW);
			GameLib.drawCircle(this.x, this.y, this.radius);
		}
		
	}

	@Override
	public double posX() {
		return this.x;
	}

	@Override
	public double posY() {
		return this.y;
	}

	@Override
	public double radius() {
		return this.radius;
	}

	@Override
	public int state() {
		return this.state;
	}
	@Override
	public void setState(int state) {
		this.state = state ;
	}

	@Override
	public void setExplosionStart(long time) {
		this.explosion_start = time;		
	}

	@Override
	public void setExplosionEnd(long time) {
		this.explosion_end = time;
		
	}

	@Override
	public double getExplosionStart() {
		return this.explosion_start;
	}

	@Override
	public double getExplosionEnd() {
		return this.explosion_end;
	}

	@Override
	public void setPosX(double x) {
		this.x = x;
		
	}

	@Override
	public void setPosY(double y) {
		this.y = y;		
	}

	@Override
	public double getV() {
		return this.v;
	}

	@Override
	public void setV(double v) {
		this.v = v;
		
	}
	@Override
	public double getRV() {
		return this.rv;
	}

	@Override
	public void setRV(double rv) {
		this.rv = rv;
		
	}
	@Override
	public double getAngle() {
		return this.angle;
	}

	@Override
	public void setAngle(double angle) {
		this.angle = angle;
		
	}

	@Override
	public void move(long delta) {
		if(this.state == ACTIVE){
			/* verificando se o powerup saiu da tela */
			if(this.y > GameLib.HEIGHT + 10) {
				this.state = INACTIVE;
			}
			else {
				this.x += this.v * Math.cos(this.angle) * delta;
				this.y += this.v * Math.sin(this.angle) * delta * (-1.0);
				this.angle += this.rv * delta;

			}
		}
	}

	@Override
	public ObjEffect colisionDetection(ObjEffect player) {
		long currentTime = System.currentTimeMillis();
		if (player.getState() == ACTIVE){
			double dx = this.x - player.getX();
			double dy = this.y - player.getY();
			double dist = Math.sqrt(dx * dx + dy * dy);
							
			if(dist < (player.getRadius() + this.radius) * 0.8){
				this.state = EXPLODING;
				this.explosion_start = currentTime;
				this.explosion_end = currentTime + 1000;
				return player = new EffectTipo1(player);
			}
		}
		return player;
	}
	
}
