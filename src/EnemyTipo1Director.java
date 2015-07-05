import java.util.List;

public class EnemyTipo1Director implements EnemyDirector{
	private long interval = 500;
	private long nextLaunch;
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	
	public EnemyTipo1Director(long currentTime) {
		this.nextLaunch = currentTime;
	}
	
	@Override
	public void tick (long currentTime, List<Enemy> enemies){
		if (currentTime > this.nextLaunch) {
			this.nextLaunch = currentTime + interval;
			enemies.add(new EnemyTipo1(ACTIVE, (currentTime + 500)));
		}
		/* destruindo objetos inativos */
		
		/*for(Enemy enemy : enemies){
			if (enemy.state() == INACTIVE ){
				enemies.remove(enemy);
				break;
			}
		}*/
	}
}
