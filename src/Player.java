import java.awt.Color;


public class Player {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
	
	private int player_state = ACTIVE;								// estado
	private double player_X = GameLib.WIDTH / 2;					// coordenada x
	private double player_Y = GameLib.HEIGHT * 0.90;				// coordenada y
	private double player_VX = 0.25;								// velocidade no eixo x
	private double player_VY = 0.25;								// velocidade no eixo y
	private double player_radius = 12.0;							// raio (tamanho aproximado do player)
	private double player_explosion_start = 0;						// instante do início da explosão
	private double player_explosion_end = 0;						// instante do final da explosão
	private long player_nextShot;						// instante a partir do qual pode haver um próximo tiro
	private Shoot tiro;
	
	public Player(long time, Shoot tiro){
		this.player_nextShot = time;
		this.tiro = tiro;
	}
	
	public void draw(){
		long currentTime = System.currentTimeMillis();
		if(player_state == EXPLODING){
			
			double alpha = (currentTime - player_explosion_start) / (player_explosion_end - player_explosion_start);
			GameLib.drawExplosion(player_X, player_Y, alpha);
		}
		else{
			
			GameLib.setColor(Color.BLUE);
			GameLib.drawPlayer(player_X, player_Y, player_radius);
		}
	}
	
	public boolean tecla(long delta){
		long currentTime = System.currentTimeMillis();
		if(player_state == ACTIVE){
			if(GameLib.iskeyPressed(GameLib.KEY_UP)) player_Y -= delta * player_VY;
			if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) player_Y += delta * player_VY;
			if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) player_X -= delta * player_VX;
			if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) player_X += delta * player_VY;
			if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
				if(currentTime > player_nextShot){
					tiro.atira(delta, player_X, (player_Y - 2 * player_radius), 0.0, -1.0, player_radius);
					player_nextShot = currentTime + 100;
				}
			}
		}
		
		if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) return false;
		
		/* Verificando se coordenadas do player ainda estão dentro	*/
		/* da tela de jogo após processar entrada do usuário.       */
		
		if(player_X < 0.0) player_X = 0.0;
		if(player_X >= GameLib.WIDTH) player_X = GameLib.WIDTH - 1;
		if(player_Y < 25.0) player_Y = 25.0;
		if(player_Y >= GameLib.HEIGHT) player_Y = GameLib.HEIGHT - 1;
		return true;
	}
	
	public int getPlayer_state() {
		return player_state;
	}

	public void setPlayer_state(int player_state) {
		this.player_state = player_state;
	}

	public double getPlayer_X() {
		return player_X;
	}

	public void setPlayer_X(double player_X) {
		this.player_X = player_X;
	}

	public double getPlayer_Y() {
		return player_Y;
	}

	public void setPlayer_Y(double player_Y) {
		this.player_Y = player_Y;
	}

	public double getPlayer_VX() {
		return player_VX;
	}

	public void setPlayer_VX(double player_VX) {
		this.player_VX = player_VX;
	}

	public double getPlayer_VY() {
		return player_VY;
	}

	public void setPlayer_VY(double player_VY) {
		this.player_VY = player_VY;
	}

	public double getPlayer_radius() {
		return player_radius;
	}

	public void setPlayer_radius(double player_radius) {
		this.player_radius = player_radius;
	}

	public double getPlayer_explosion_start() {
		return player_explosion_start;
	}

	public void setPlayer_explosion_start(double player_explosion_start) {
		this.player_explosion_start = player_explosion_start;
	}

	public double getPlayer_explosion_end() {
		return player_explosion_end;
	}

	public void setPlayer_explosion_end(double player_explosion_end) {
		this.player_explosion_end = player_explosion_end;
	}

	public long getPlayer_nextShot() {
		return player_nextShot;
	}

	public void setPlayer_nextShot(long player_nextShot) {
		this.player_nextShot = player_nextShot;
	}
}
