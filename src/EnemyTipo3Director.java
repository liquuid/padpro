import java.util.List;

public class EnemyTipo3Director implements EnemyDirector {
	private long interval = 120;
	private long nextLaunch;
	private int count;
	private double spawnX = GameLib.WIDTH * 0.20;;
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;

	public EnemyTipo3Director(long currentTime) {
		this.nextLaunch = currentTime;
	}

	@Override
	public void tick(long currentTime, List<Enemy> enemies) {
		/* verificando se novos inimigos (tipo 3) devem ser "lançados" */
		
		if (currentTime > nextLaunch) {

			count++;
			if (count < 10) {
				nextLaunch = currentTime + interval;
				enemies.add(new EnemyTipo3(ACTIVE, (currentTime + 500)));
			} else {
				count = 0;
				nextLaunch = (long) (currentTime + 20000 + Math.random() * 3000);
			}

		}
		

	}

}
