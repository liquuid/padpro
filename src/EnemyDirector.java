import java.util.List;

public interface EnemyDirector {
	public void tick (long currentTime, List<Enemy> enemies);
}
