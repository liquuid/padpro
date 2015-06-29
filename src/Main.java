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
	
	public static void colisao(Shoot tiro, long currentTime, Player p1){
		for(int i = 0; i < tiro.getSize(); i++){
			
			double dx = tiro.getPosX(i) - p1.getPlayer_X();
			double dy = tiro.getPosY(i) - p1.getPlayer_Y();
			double dist = Math.sqrt(dx * dx + dy * dy);
			
			if(dist < (p1.getPlayer_radius() + tiro.getRadius()) * 0.8){
				
				p1.setPlayer_state(EXPLODING);
				p1.setPlayer_explosion_start(currentTime);
				p1.setPlayer_explosion_end(currentTime + 2000);
			}
		}
	}
	/* Método principal */
	
	public static void main(String [] args){

		/* Indica que o jogo está em execução */
		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */
		
		long delta;
		long currentTime = System.currentTimeMillis();
		
		Shoot tiro1 = new ShootTipo1();
		Shoot tiro2 = new ShootTipo2();
		Shoot tiro3 = new ShootTipo3();
		
		/* variáveis do player 
		
		int player_state = ACTIVE;								// estado
		double player_X = GameLib.WIDTH / 2;					// coordenada x
		double player_Y = GameLib.HEIGHT * 0.90;				// coordenada y
		double player_VX = 0.25;								// velocidade no eixo x
		double player_VY = 0.25;								// velocidade no eixo y
		double player_radius = 12.0;							// raio (tamanho aproximado do player)
		double player_explosion_start = 0;						// instante do início da explosão
		double player_explosion_end = 0;						// instante do final da explosão
		long player_nextShot = currentTime;						// instante a partir do qual pode haver um próximo tiro
		*/
		Player p1 = new Player(currentTime, tiro2);
		/* variáveis dos projéteis disparados pelo player 
		
		int [] projectile_states = new int[10];					// estados
		double [] projectile_X = new double[10];				// coordenadas x
		double [] projectile_Y = new double[10];				// coordenadas y
		double [] projectile_VX = new double[10];				// velocidades no eixo x
		double [] projectile_VY = new double[10];				// velocidades no eixo y

		/* variáveis dos inimigos tipo 1 */
		
		long nextEnemy1 = currentTime + 2000;					// instante em que um novo inimigo 1 deve aparecer
		
		/* variáveis dos inimigos tipo 2 */
		
		double enemy2_spawnX = GameLib.WIDTH * 0.20;			// coordenada x do próximo inimigo tipo 2 a aparecer
		int enemy2_count = 0;									// contagem de inimigos tipo 2 (usada na "formação de voo")
		long nextEnemy2 = currentTime + 7000;					// instante em que um novo inimigo 2 deve aparecer
		
		/* variáveis dos projéteis lançados pelos inimigos (tanto tipo 1, quanto tipo 2) 
		
		int [] e_projectile_states = new int[200];				// estados
		double [] e_projectile_X = new double[200];				// coordenadas x
		double [] e_projectile_Y = new double[200];				// coordenadas y
		double [] e_projectile_VX = new double[200];			// velocidade no eixo x
		double [] e_projectile_VY = new double[200];			// velocidade no eixo y
		double e_projectile_radius = 2.0;						// raio (tamanho dos projéteis inimigos)
		*/

		
		/* estrelas que formam o fundo */
		for(int i = 0; i != 50; i++){
			background.add(new StarDeep(0.045, Color.DARK_GRAY));
		}
		for(int i = 0; i != 20; i++){
			background.add(new StarFront(0.070, Color.GRAY));
		}
				
		/* inicializações */
		
		//for(int i = 0; i < projectile_states.length; i++) projectile_states[i] = INACTIVE;
		//for(int i = 0; i < e_projectile_states.length; i++) e_projectile_states[i] = INACTIVE;
		
		/*for(int i = 0; i < 10; i++) {
			enemies.add(new EnemyTipo1( INACTIVE, (currentTime + 500)));
		}
		for(int i = 0; i < 10; i++) {
			enemies.add(new EnemyTipo2( INACTIVE, (currentTime + 500),enemy2_spawnX));
		}*/
		
		/* for(int i = 0; i < enemy2_states.length; i++) enemy2_states[i] = INACTIVE; */
								
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
						
			if(p1.getPlayer_state() == ACTIVE){
				
				/* colisões player - projeteis (inimigo) */
				
				colisao(tiro1, currentTime, p1);
				colisao(tiro3, currentTime, p1);
				
			    /* colisão do player com os inimigos */	
				
				for (Enemy enemy : enemies){
					double dx = enemy.posX() - p1.getPlayer_X();
					double dy = enemy.posY() - p1.getPlayer_Y();
					double dist = Math.sqrt(dx * dx + dy * dy);
									
					if(dist < (p1.getPlayer_radius() + enemy.radius()) * 0.8){
						p1.setPlayer_state(EXPLODING);
						p1.setPlayer_explosion_start(currentTime);
						p1.setPlayer_explosion_end(currentTime + 2000);
					}
				}
			}
			
			/* colisões projeteis (player) - inimigos */
			
			for(int k = 0; k < tiro2.getSize(); k++){
				
				for(Enemy enemy : enemies){
										
					if(enemy.state() == ACTIVE){
					
						double dx = enemy.posX() - tiro2.getPosX(k);
						double dy = enemy.posY() - tiro2.getPosY(k);
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
			
			tiro2.move(delta);
			
			/* projeteis (inimigos) */
			
			tiro1.move(delta);
			tiro3.move(delta);
			
			/* inimigos  */
			
			for(Enemy enemy : enemies){
				if(enemy.state() == EXPLODING){
					if(currentTime > enemy.getExplosionEnd()){
						enemy.setState(INACTIVE);
						enemies.remove(enemy);
						break;
					}
				}
				enemy.move(delta, p1);
				// Ele atirava daqui .......
			}
			
			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */
			
			if (currentTime > nextEnemy1) {
				nextEnemy1 = currentTime + 500;
				enemies.add(new EnemyTipo1(ACTIVE, (currentTime + 500), tiro1));

			}
			System.out.println(enemies.size());
			
			/* verificando se novos inimigos (tipo 2) devem ser "lançados" */
			
			if (currentTime > nextEnemy2) {

				enemy2_count++;
				if (enemy2_count < 10) {
					nextEnemy2 = currentTime + 120;
					enemies.add(new EnemyTipo2(ACTIVE, (currentTime + 500),enemy2_spawnX, tiro3));
				} else {
					enemy2_count = 0;
					enemy2_spawnX = Math.random() > 0.5 ? GameLib.WIDTH * 0.2 : GameLib.WIDTH * 0.8;
					nextEnemy2 = (long) (currentTime + 3000 + Math.random() * 3000);
				}

			}

			/* Verificando se a explosão do player já acabou.         */
			/* Ao final da explosão, o player volta a ser controlável */
			if(p1.getPlayer_state() == EXPLODING){
				
				if(currentTime > p1.getPlayer_explosion_end()){
					
					p1.setPlayer_state(ACTIVE);
				}
			}
			
			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/
			running = p1.tecla(delta);
			

			/*******************/
			/* Desenho da cena */
			/*******************/
			
			/* desenhando plano fundo  */
			
			for(Star estrela : background){
				estrela.move(delta);
				estrela.draw();
			}
			
						
			/* desenhando player */
			
			p1.draw();
				
			/* deenhando projeteis (player) */
			
			tiro2.draw();
			
			/* desenhando projeteis (inimigos) */
		
			tiro1.draw();
			tiro3.draw();
			/* desenhando inimigos (tipo 1) */
			
			for(Enemy enemy : enemies){
				enemy.draw();
			}
			
			/* destruindo objetos inativos */
			
			for(Enemy enemy : enemies){
				if (enemy.state() == INACTIVE ){
					System.out.println("removendo");
					enemies.remove(enemy);
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
