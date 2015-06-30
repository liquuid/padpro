import java.awt.Color;
import java.util.List;

public class EnemyTipo2 implements Enemy {
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
	private long nextShoot; // instantes do próximo tiro
	private double radius = 12.0; // raio (tamanho do inimigo 1)
	private boolean shootNow = false;

	public EnemyTipo2(int state, long nextShoot, double enemy2_spawnX) {
		this.state = state;
		this.nextShoot = nextShoot;
		this.x = enemy2_spawnX;
		this.y = -10.0;
		this.v = 0.42;
		this.angle = (3 * Math.PI) / 2;
		this.rv = 0.0;
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
			GameLib.setColor(Color.MAGENTA);
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
			if (this.x < -10 || this.x > GameLib.WIDTH + 10) {

				this.state = INACTIVE;
			} else {
				shootNow = false;
				double previousY = this.y;

				this.x += this.v * Math.cos(this.angle) * delta;
				this.y += this.v * Math.sin(this.angle) * delta * (-1.0);
				this.angle += this.rv * delta;

				double threshold = GameLib.HEIGHT * 0.30;

				if (previousY < threshold && this.y >= threshold) {

					if (this.x < GameLib.WIDTH / 2)
						this.rv = 0.003;
					else
						this.rv = -0.003;
				}

				if (this.rv > 0 && Math.abs(this.angle - 3 * Math.PI) < 0.05) {

					this.rv = 0.0;
					this.angle = 3 * Math.PI;
					shootNow = true;
				}

				if (this.rv < 0 && Math.abs(this.angle) < 0.05) {

					this.rv = 0.0;
					this.angle = 0.0;
					shootNow = true;
					
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
	public double getNextShoot() {
		return this.nextShoot;
	}

	@Override
	public void setNextShoot(long time) {
		if ( this.nextShoot == 0 ){
			System.out.println(this.nextShoot);
			this.nextShoot = time;
		}
	}

	
	public void shoot(List<Shoot> listShoots, Player player) {
		long currentTime = System.currentTimeMillis();
		if(currentTime > this.nextShoot && shootNow ){
			this.nextShoot = (long) (currentTime + 200 + Math.random() * 500);
			double [] angles = { Math.PI/2 + Math.PI/8, Math.PI/2, Math.PI/2 - Math.PI/8 };
					
			Shoot shoot1 = new ShootEnemy2(this, angles[0] + Math.random() * Math.PI/6 - Math.PI/12);
			Shoot shoot2 = new ShootEnemy2(this, angles[1] + Math.random() * Math.PI/6 - Math.PI/12);
			Shoot shoot3 = new ShootEnemy2(this, angles[2] + Math.random() * Math.PI/6 - Math.PI/12);
			listShoots.add(shoot1);
			listShoots.add(shoot2);
			listShoots.add(shoot3);
		}
	}

}
