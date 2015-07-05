import java.awt.Color;
import java.util.List;

public class EnemyTipo3 implements Enemy {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;

	private int state; // estado
	private double x; // coordenadas x
	private double y; // coordenadas y
	private double v; // velocidades
	private double angle; // ângulos (indicam direção do movimento)
	private double rv; // velocidades de rotação
	private double explosion_start; // instantes dos inícios das explosões
	private double explosion_end; // instantes dos finais da explosões
	private long nextShot; // instantes do próximo tiro
	private double radius = 24.0; // raio (tamanho do inimigo 3)
	private boolean shootNow = false;

	public EnemyTipo3(int state, long nextShot) {
		this.state = state;
		this.nextShot = nextShot;
		this.x = (GameLib.WIDTH / 2) + (Math.random() - 0.5) * (GameLib.WIDTH / 2);
		this.y = -10.0;
		this.v = 3;
		//this.angle = (3 * Math.PI) / 2;
		//this.rv = 0.0;
	}

	@Override
	public void draw() {
		long currentTime = System.currentTimeMillis();
		if (this.state == EXPLODING) {
			double alpha = (currentTime - this.explosion_start)
					/ (this.explosion_end - this.explosion_start);
			GameLib.drawExplosion(this.x, this.y, alpha);
		}

		if (this.state == ACTIVE) {
			GameLib.setColor(Color.WHITE);
			GameLib.drawDiamond(this.x, this.y, this.radius);
		}
	}

	@Override
	public void move(long delta) {
		long currentTime = System.currentTimeMillis();
		if (this.state == EXPLODING) {
			if (currentTime > this.explosion_end) {
				this.state = INACTIVE;
			}
		}

		if (this.state == ACTIVE) {
			/* verificando se inimigo saiu da tela */
			if (this.y < -50 || this.y > GameLib.HEIGHT + 10) {
				this.state = INACTIVE;
			} else {
				this.y += this.v;
				this.v -= 0.01;
				if ( this.v < 1.0 && this.v > 0.0 ){
					this.shootNow = true;
				}
			}
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
		this.state = state;
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
	public double getNextShot() {
		return this.nextShot;
	}

	@Override
	public void setNextShot(long time) {
		if ( this.nextShot == 0 ){
			this.nextShot = time;
		}
	}

	
	public void shot(List<Shot> listShots, ObjEffect player) {
		long currentTime = System.currentTimeMillis();
		if(currentTime > this.nextShot && shootNow ){
			this.nextShot = (long) (currentTime);
			double [] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
					
			Shot shot = new ShotEnemy2(this, angles[0] + Math.sin(currentTime/500));
			listShots.add(shot);

		}
	}

	@Override
	public void colisionDetection(ObjEffect player) {
		if (player.getState() == ACTIVE){
			double dx = this.x - player.getX();
			double dy = this.y - player.getY();
			double dist = Math.sqrt(dx * dx + dy * dy);
							
			if(dist < (player.getRadius() + this.radius) * 0.8){
				long currentTime = System.currentTimeMillis();
				player.setState(EXPLODING);
				player.setExplosion_start(currentTime);
				player.setExplosion_end(currentTime + 2000);
			}
		}
		
	}

}
