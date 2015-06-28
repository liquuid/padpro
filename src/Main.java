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
	
	/* Método principal */
	
	public static void main(String [] args){

		/* Indica que o jogo está em execução */
		boolean running = true;

		/* variáveis usadas no controle de tempo efetuado no main loop */
		
		long delta;
		long currentTime = System.currentTimeMillis();

		/* variáveis do player */
		
		int player_state = ACTIVE;								// estado
		double player_X = GameLib.WIDTH / 2;					// coordenada x
		double player_Y = GameLib.HEIGHT * 0.90;				// coordenada y
		double player_VX = 0.25;								// velocidade no eixo x
		double player_VY = 0.25;								// velocidade no eixo y
		double player_radius = 12.0;							// raio (tamanho aproximado do player)
		double player_explosion_start = 0;						// instante do início da explosão
		double player_explosion_end = 0;						// instante do final da explosão
		long player_nextShot = currentTime;						// instante a partir do qual pode haver um próximo tiro

		/* variáveis dos projéteis disparados pelo player */
		
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
		
		/* variáveis dos projéteis lançados pelos inimigos (tanto tipo 1, quanto tipo 2) */
		
		int [] e_projectile_states = new int[200];				// estados
		double [] e_projectile_X = new double[200];				// coordenadas x
		double [] e_projectile_Y = new double[200];				// coordenadas y
		double [] e_projectile_VX = new double[200];			// velocidade no eixo x
		double [] e_projectile_VY = new double[200];			// velocidade no eixo y
		double e_projectile_radius = 2.0;						// raio (tamanho dos projéteis inimigos)
		
		/* estrelas que formam o fundo */
				
		for(int i = 0; i != 50; i++){
			background.add(new StarDeep(0.045, Color.DARK_GRAY));
		}
		for(int i = 0; i != 20; i++){
			background.add(new StarFront(0.070, Color.GRAY));
		}
				
		/* inicializações */
		
		for(int i = 0; i < projectile_states.length; i++) projectile_states[i] = INACTIVE;
		for(int i = 0; i < e_projectile_states.length; i++) e_projectile_states[i] = INACTIVE;
		
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
						
			if(player_state == ACTIVE){
				
				/* colisões player - projeteis (inimigo) */
				
				for(int i = 0; i < e_projectile_states.length; i++){
					
					double dx = e_projectile_X[i] - player_X;
					double dy = e_projectile_Y[i] - player_Y;
					double dist = Math.sqrt(dx * dx + dy * dy);
					
					if(dist < (player_radius + e_projectile_radius) * 0.8){
						
						player_state = EXPLODING;
						player_explosion_start = currentTime;
						player_explosion_end = currentTime + 2000;
					}
				}
				
			    /* colisão do player com os inimigos */	
				
				for (Enemy enemy : enemies){
					double dx = enemy.posX() - player_X;
					double dy = enemy.posY() - player_Y;
					double dist = Math.sqrt(dx * dx + dy * dy);
									
					if(dist < (player_radius + enemy.radius()) * 0.8){
						player_state = EXPLODING;
						player_explosion_start = currentTime;
						player_explosion_end = currentTime + 2000;
					}
				}
			}
			
			/* colisões projeteis (player) - inimigos */
			
			for(int k = 0; k < projectile_states.length; k++){
				
				for(Enemy enemy : enemies){
										
					if(enemy.state() == ACTIVE){
					
						double dx = enemy.posX() - projectile_X[k];
						double dy = enemy.posY() - projectile_Y[k];
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
			
			for(int i = 0; i < projectile_states.length; i++){
				
				if(projectile_states[i] == ACTIVE){
					
					/* verificando se projétil saiu da tela */
					if(projectile_Y[i] < 0) {
						
						projectile_states[i] = INACTIVE;
					}
					else {
					
						projectile_X[i] += projectile_VX[i] * delta;
						projectile_Y[i] += projectile_VY[i] * delta;
					}
				}
			}
			
			/* projeteis (inimigos) */
			
			for(int i = 0; i < e_projectile_states.length; i++){
				
				if(e_projectile_states[i] == ACTIVE){
					
					/* verificando se projétil saiu da tela */
					if(e_projectile_Y[i] > GameLib.HEIGHT) {
						
						e_projectile_states[i] = INACTIVE;
					}
					else {
					
						e_projectile_X[i] += e_projectile_VX[i] * delta;
						e_projectile_Y[i] += e_projectile_VY[i] * delta;
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
				// Ele atirava daqui .......
			}
			
			/* verificando se novos inimigos (tipo 1) devem ser "lançados" */
			
			if (currentTime > nextEnemy1) {
				nextEnemy1 = currentTime + 500;
				enemies.add(new EnemyTipo1(ACTIVE, (currentTime + 500)));

			}
			System.out.println(enemies.size());
			
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
			if(player_state == EXPLODING){
				
				if(currentTime > player_explosion_end){
					
					player_state = ACTIVE;
				}
			}
			
			/********************************************/
			/* Verificando entrada do usuário (teclado) */
			/********************************************/
			
			if(player_state == ACTIVE){
				
				if(GameLib.iskeyPressed(GameLib.KEY_UP)) player_Y -= delta * player_VY;
				if(GameLib.iskeyPressed(GameLib.KEY_DOWN)) player_Y += delta * player_VY;
				if(GameLib.iskeyPressed(GameLib.KEY_LEFT)) player_X -= delta * player_VX;
				if(GameLib.iskeyPressed(GameLib.KEY_RIGHT)) player_X += delta * player_VY;
				if(GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
					
					if(currentTime > player_nextShot){
						
						int free = findFreeIndex(projectile_states);
												
						if(free < projectile_states.length){
							
							projectile_X[free] = player_X;
							projectile_Y[free] = player_Y - 2 * player_radius;
							projectile_VX[free] = 0.0;
							projectile_VY[free] = -1.0;
							projectile_states[free] = 1;
							player_nextShot = currentTime + 100;
						}
					}	
				}
			}
			
			if(GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) running = false;
			
			/* Verificando se coordenadas do player ainda estão dentro	*/
			/* da tela de jogo após processar entrada do usuário.       */
			
			if(player_X < 0.0) player_X = 0.0;
			if(player_X >= GameLib.WIDTH) player_X = GameLib.WIDTH - 1;
			if(player_Y < 25.0) player_Y = 25.0;
			if(player_Y >= GameLib.HEIGHT) player_Y = GameLib.HEIGHT - 1;

			/*******************/
			/* Desenho da cena */
			/*******************/
			
			/* desenhando plano fundo  */
			
			for(Star estrela : background){
				estrela.move(delta);
				estrela.draw();
			}
			
						
			/* desenhando player */
			
			if(player_state == EXPLODING){
				
				double alpha = (currentTime - player_explosion_start) / (player_explosion_end - player_explosion_start);
				GameLib.drawExplosion(player_X, player_Y, alpha);
			}
			else{
				
				GameLib.setColor(Color.BLUE);
				GameLib.drawPlayer(player_X, player_Y, player_radius);
			}
				
			/* deenhando projeteis (player) */
			
			for(int i = 0; i < projectile_states.length; i++){
				
				if(projectile_states[i] == ACTIVE){
					
					GameLib.setColor(Color.GREEN);
					GameLib.drawLine(projectile_X[i], projectile_Y[i] - 5, projectile_X[i], projectile_Y[i] + 5);
					GameLib.drawLine(projectile_X[i] - 1, projectile_Y[i] - 3, projectile_X[i] - 1, projectile_Y[i] + 3);
					GameLib.drawLine(projectile_X[i] + 1, projectile_Y[i] - 3, projectile_X[i] + 1, projectile_Y[i] + 3);
				}
			}
			
			/* desenhando projeteis (inimigos) */
		
			for(int i = 0; i < e_projectile_states.length; i++){
				
				if(e_projectile_states[i] == ACTIVE){
	
					GameLib.setColor(Color.RED);
					GameLib.drawCircle(e_projectile_X[i], e_projectile_Y[i], e_projectile_radius);
				}
			}
			
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
