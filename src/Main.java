import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Main {
	
	/* Constantes relacionadas aos estados que os elementos   */
	/* do jogo (player, projeteis ou inimigos) podem assumir. */
	
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int EXPLODING = 2;

	
	/* Lista com todas as estrelinhas */
	
	private static List<Star> background = new ArrayList<Star>();
	
	/* Lista com todos inimigos */
	
	private static List<Enemy> enemies = new ArrayList<Enemy>();
	
	/* Lista com todos projeteis */
	
	private static List<Shoot> shoots = new ArrayList<Shoot>();
	
	
	/* Espera, sem fazer nada, até que o instante de tempo atual seja */
	/* maior ou igual ao instante especificado no parâmetro "time.    */
	
	public static void busyWait(long time){
		
		while(System.currentTimeMillis() < time) Thread.yield();
	}
	
	/* Encontra e devolve o primeiro índice do  */
	/* array referente a uma posição "inativa". */
	
	public static int findFreeIndex(int [] stateArray){
		
		int i;
		
		for(i = 0; i < stateArray.length; i++){
			
			if(stateArray[i] == INACTIVE) break;
		}
		
		return i;
	}
	
	/* Encontra e devolve o conjunto de índices (a quantidade */
	/* de índices é defnida através do parâmetro "amount") do */
	/* array, referentes a posições "inativas".               */ 

	public static int [] findFreeIndex(int [] stateArray, int amount){

		int i, k;
		int [] freeArray = { stateArray.length, stateArray.length, stateArray.length };
		
		for(i = 0, k = 0; i < stateArray.length && k < amount; i++){
				
			if(stateArray[i] == INACTIVE) { 
				
				freeArray[k] = i; 
				k++;
			}
		}
		
		return freeArray;
	}
	
	/* Método principal */
	
	public static void main(String [] args){

		/* Indica que o jogo está em execução */
		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */
		
		long delta;
		long currentTime = System.currentTimeMillis();

		/* variáveis do player */
		
		Player player = new Player(GameLib.WIDTH / 2,GameLib.HEIGHT * 0.90 );
		
		/* variáveis dos inimigos tipo 1 */
		
		long nextEnemy1 = currentTime + 2000;					// instante em que um novo inimigo 1 deve aparecer
		
		/* variáveis dos inimigos tipo 2 */
		
		double enemy2_spawnX = GameLib.WIDTH * 0.20;			// coordenada x do próximo inimigo tipo 2 a aparecer
		int enemy2_count = 0;									// contagem de inimigos tipo 2 (usada na "formação de voo")
		long nextEnemy2 = currentTime + 7000;					// instante em que um novo inimigo 2 deve aparecer
		
		/* estrelas que formam o fundo */
				
		for(int i = 0; i != 50; i++){
			background.add(new StarDeep(0.045, Color.DARK_GRAY));
		}
		for(int i = 0; i != 20; i++){
			background.add(new StarFront(0.070, Color.GRAY));
		}
									
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
						
			if(player.getState() == ACTIVE){
				
				/* colisões player - projeteis (inimigo) */
				
				for(Shoot shoot : shoots){
					
					double dx = shoot.getX() - player.getX();
					double dy = shoot.getY() - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player.getRadius() + shoot.getRadius()) * 0.8){
						player.setState(EXPLODING);
						player.setExplosion_start(currentTime);
						player.setExplosion_end(currentTime + 2000);
					}
				}
				
			    /* colisão do player com os inimigos */	
				
				for (Enemy enemy : enemies){
					double dx = enemy.posX() - player.getX();
					double dy = enemy.posY() - player.getY();
					double dist = Math.sqrt(dx * dx + dy * dy);
									
					if(dist < (player.getRadius() + enemy.radius()) * 0.8){
						player.setState(EXPLODING);
						player.setExplosion_start(currentTime);
						player.setExplosion_end(currentTime + 2000);
					}
				}
			}
			
			/* colisões projeteis (player) - inimigos */
			
			for(Shoot shoot : shoots){
				for(Enemy enemy : enemies){
										
					if(enemy.state() == ACTIVE){
					
						double dx = enemy.posX() - shoot.getX();
						double dy = enemy.posY() - shoot.getY();
						double dist = Math.sqrt(dx * dx + dy * dy);
						
						if(dist < enemy.radius()){
							
							enemy.setState(EXPLODING);
							enemy.setExplosionStart(currentTime);
							enemy.setExplosionEnd(currentTime + 500);
						}
					}
				}
			}
				
			/***************************/
			/* Atualizações de estados */
			/***************************/
			
			/* projeteis (player) */
			
			for(Shoot shoot : shoots){
				
				if(shoot.getState() == ACTIVE){
					
					/* verificando se projétil saiu da tela */
					if(shoot.getY() < 0) {
						
						shoot.setState(INACTIVE);
					}
					else {
					
						shoot.setX(shoot.getX() + shoot.getVx() * delta);
						shoot.setY(shoot.getY() + shoot.getVy() * delta);
						//projectile_Y[i] += projectile_VY[i] * delta;
					}
				}
			}
			
			/* projeteis (inimigos) */
			
			for(Shoot shoot : shoots){		
				if(shoot.getState() == ACTIVE){
					/* verificando se projétil saiu da tela */
					if(shoot.getY() > GameLib.HEIGHT) {
						shoot.setState(INACTIVE);
					}
					else {
						shoot.move(delta);
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
				enemy.shoot(shoots,player);
				enemy.setNextShoot((long) (currentTime + 200 + Math.random() * 500));
				
			}
			
			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */
			
			if (currentTime > nextEnemy1) {
				nextEnemy1 = currentTime + 500;
				enemies.add(new EnemyTipo1(ACTIVE, (currentTime + 500)));

			}
			System.out.println(shoots.size());
			
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

			/* Verificando se a explosão do player já acabou.         */
			/* Ao final da explosão, o player volta a ser controlável */
			if(player.getState() == EXPLODING){
				
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
													
							shoots.add(new ShootPlayer(player.getX() ,player.getY() - 2 * player.getRadius() ));
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
			if(player.getY() >= GameLib.HEIGHT) player.setY(GameLib.HEIGHT - 1);

			/*******************/
			/* Desenho da cena */
			/*******************/
			
			/* desenhando plano fundo  */
			
			for(Star estrela : background){
				estrela.move(delta);
				estrela.draw();
			}
			
						
			/* desenhando player */
			
			player.draw();
			
			/* desenhando projeteis */
			
			for (Shoot shoot : shoots){
				shoot.draw();
			}
			
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
			
			for(Shoot shoot : shoots){
				if (shoot.getState() == INACTIVE ){
					shoots.remove(shoot);
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
