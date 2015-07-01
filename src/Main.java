import java.util.ArrayList;
import java.util.List;

public class Main {
	
	/* Constantes relacionadas aos estados que os elementos   */
	/* do jogo (player, projeteis ou inimigos) podem assumir. */
	
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;
		
	/* Lista com todos inimigos */
	
	private static List<Enemy> enemies = new ArrayList<Enemy>();
	
	/* Lista com todos projeteis */
	
	private static List<Shot> shots = new ArrayList<Shot>();
	
	/* Espera, sem fazer nada, até que o instante de tempo atual seja */
	/* maior ou igual ao instante especificado no parâmetro "time.    */
	
	public static void busyWait(long time){
		
		while(System.currentTimeMillis() < time) Thread.yield();
	}
		
	/* Método principal */
	
	public static void main(String [] args){

		/* Indica que o jogo está em execução */
		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */
		
		long delta;
		long currentTime = System.currentTimeMillis();

		/* variáveis do player */
		
		ObjEffect player = new Player(GameLib.WIDTH / 2,GameLib.HEIGHT * 0.90 );
		
		/* variáveis dos inimigos tipo 1 */
		
		long nextEnemy1 = currentTime + 2000;					// instante em que um novo inimigo 1 deve aparecer
		
		/* variáveis do power up */
		PowerUP pw = new PowerUP1(INACTIVE);
		long nextPowerUP = currentTime + 2000;
		
		/* variáveis dos inimigos tipo 2 */
		
		double enemy2_spawnX = GameLib.WIDTH * 0.20;			// coordenada x do próximo inimigo tipo 2 a aparecer
		int enemy2_count = 0;								// contagem de inimigos tipo 2 (usada na "formação de voo")
		int enemy3_count = 0;
		long nextEnemy2 = currentTime + 7000;					// instante em que um novo inimigo 2 deve aparecer
		long nextEnemy3 = currentTime + 12000;
		/* estrelas que formam o fundo */
				
		Background background = Background.getInstance();
		background.init();
				
		/* iniciado interface gráfica */
		
		GameLib.initGraphics();
		
		/*************************************************************************************************/
		/*                                                                                               */
		/* Main loop do jogo                                                                             */
		/*                                                                                               */
		/* O main loop do jogo possui executa as seguintes operações:                                    */
		/*                                                                                               */
		/* 1) Verifica se há colisões e atualiza estados dos elementos conforme a necessidade.           */
		/*                                                                                               */
		/* 2) Atualiza estados dos elementos baseados no tempo que correu desde a última atualização     */
		/*    e no timestamp atual: posição e orientação, execução de disparos de projéteis, etc.        */
		/*                                                                                               */
		/* 3) Processa entrada do usuário (teclado) e atualiza estados do player conforme a necessidade. */
		/*                                                                                               */
		/* 4) Desenha a cena, a partir dos estados dos elementos.                                        */
		/*                                                                                               */
		/* 5) Espera um período de tempo (de modo que delta seja aproximadamente sempre constante).      */
		/*                                                                                               */
		/*************************************************************************************************/
		
		while(running){
		
			/* Usada para atualizar o estado dos elementos do jogo    */
			/* (player, projéteis e inimigos) "delta" indica quantos  */
			/* ms se passaram desde a última atualização.             */
			
			delta = System.currentTimeMillis() - currentTime;
			
			/* Já a variável "currentTime" nos dá o timestamp atual.  */
			
			currentTime = System.currentTimeMillis();
			
			/***************************/
			/* Verificação de colisões */
			/***************************/
						
			for (Enemy enemy : enemies){
				enemy.colisionDetection(player);
			}
			for(Shot shot : shots){
				shot.colisionDetection(player);
				shot.colisionDetection(enemies);
			}

			player = pw.colisionDetection(player);

			
			/***************************/
			/* Atualizações de estados */
			/***************************/
			
			/* projeteis */
			
			for(Shot shot : shots){		
				if(shot.getState() == ACTIVE){
					/* verificando se projétil saiu da tela */
					if(shot.getY() > GameLib.HEIGHT || shot.getY() < 0 ) {
						shot.setState(INACTIVE);
					}
					else {
						shot.move(delta);
					}
				}
			}
			
			/* inimigos  */
			
			for(Enemy enemy : enemies){
				if(enemy.state() == EXPLODING){
					if(currentTime > enemy.getExplosionEnd()){
						enemy.setState(INACTIVE);
						enemies.remove(enemy);
						break;
					}
				}
				enemy.move(delta);
				enemy.shot(shots,player);
				enemy.setNextShot((long) (currentTime + 200 + Math.random() * 500));
				
			}
			
			/* power ups*/
			
			if(pw.state() == EXPLODING){
				if(currentTime > pw.getExplosionEnd()){
					pw.setState(INACTIVE);
				}
			}
			
			pw.move(delta);
			
			/* verificando se power ups devem ser lançados*/
			
			if (currentTime > nextPowerUP) {
				nextPowerUP = (long) (currentTime + 45000);
				pw = new PowerUP1(ACTIVE);
			}
			
			/* verificando se powe up deve ser terminado */
			
			if (currentTime > nextPowerUP - 25000) {
				player = player.removePowerUP(player);
			}
			
			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */
			
			if (currentTime > nextEnemy1) {
				nextEnemy1 = currentTime + 500;
				enemies.add(new EnemyTipo1(ACTIVE, (currentTime + 500)));
			}
			
			/* verificando se novos inimigos (tipo 2) devem ser "lançados" */
			
			if (currentTime > nextEnemy2) {

				enemy2_count++;
				if (enemy2_count < 10) {
					nextEnemy2 = currentTime + 120;
					enemies.add(new EnemyTipo2(ACTIVE, (currentTime + 500),enemy2_spawnX ));
				} else {
					enemy2_count = 0;
					enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
					nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000);
				}

			}
			
			/* verificando se novos inimigos (tipo 3) devem ser "lançados" */
			
			if (currentTime > nextEnemy3) {

				enemy3_count++;
				if (enemy3_count < 10) {
					nextEnemy3 = currentTime + 120;
					enemies.add(new EnemyTipo3(ACTIVE, (currentTime + 500)));
					System.out.println(enemy3_count);
				} else {
					enemy3_count = 0;
					nextEnemy3 = (long) (currentTime + 20000 + Math.random() * 3000);
				}

			}
			
			

			/* Verificando se a explosão do player já acabou.         */
			/* Ao final da explosão, o player volta a ser controlável */
			if(player.getState() == EXPLODING){
				player = player.removePowerUP(player);
				if(currentTime > player.getExplosion_end()){
					player.setState(ACTIVE);
				}
			}
			
			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/
			
			if(player.getState() == ACTIVE){
				
				if(GameLib.iskeyPressed(GameLib.KEY_UP)) player.setY(player.getY() - delta * player.getVy());
				if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) player.setY(player.getY() + delta * player.getVy());
				if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) player.setX(player.getX() - delta * player.getVx());
				if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) player.setX(player.getX() + delta * player.getVx());
				if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
					
					if(currentTime > player.getNextShot()){										
							player.shot(shots);
							player.setNextShot(currentTime + 100);
					}	
				}
			}
			
			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;
			
			/* Verificando se coordenadas do player ainda estão dentro	*/
			/* da tela de jogo após processar entrada do usuário.       */
			
			if(player.getX() < 0.0) player.setX(0.0);
			if(player.getX() >= GameLib.WIDTH) player.setX(GameLib.WIDTH - 1);
			if(player.getY() < 25.0) player.setY(25.0);
			if(player.getY() >= GameLib.HEIGHT - 25) player.setY(GameLib.HEIGHT - 25);

			/*******************/
			/* Desenho da cena */
			/*******************/
			
			/* desenhando plano fundo  */
			
			background.tick(delta);
					
			/* desenhando player */
			
			player.draw();
			
			/* desenhando projeteis */
			
			for (Shot shot : shots){
				shot.draw();
			}
			
			/* desenhando power ups*/
			
			pw.draw();
			
			/* desenhando inimigos */
			
			for(Enemy enemy : enemies){
				enemy.draw();
			}
			
			/* destruindo objetos inativos */
			
			for(Enemy enemy : enemies){
				if (enemy.state() == INACTIVE ){
					enemies.remove(enemy);
					break;
				}
			}
			
			for(Shot shot : shots){
				if (shot.getState() == INACTIVE ){
					shots.remove(shot);
					break;
				}
			}
			
			
			/* chamama a display() da classe GameLib atualiza o desenho exibido pela interface do jogo. */
			
			GameLib.display();
			
			/* faz uma pausa de modo que cada execução do laço do main loop demore aproximadamente 5 ms. */
			
			busyWait(currentTime + 5);
		}
		
		System.exit(0);
	}
}
