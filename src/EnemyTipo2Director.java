import java.util.List;


public class EnemyTipo2Director implements EnemyDirector {
	private long interval = 120;
	private long nextLaunch;
	private int count;
	private double spawnX = GameLib.WIDTH * 0.20;;
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;

	public EnemyTipo2Director(long currentTime) {
		this.nextLaunch = currentTime;
	}

	@Override
	public void tick(long currentTime, List<Enemy> enemies) {
		/* verificando se novos inimigos (tipo 2) devem ser "lançados" */
		
		if (currentTime > this.nextLaunch) {

			count++;
			if (count < 10) {
				this.nextLaunch = currentTime + interval;
				enemies.add(new EnemyTipo2(ACTIVE, (currentTime + 500),spawnX ));
			} else {
				count = 0;
				spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
				nextLaunch = (long) (currentTime + 3000 + Math.random() * 3000);
			}

		}

	}

}
