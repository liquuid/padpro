import java.awt.Color;


public class ShootTipo1 {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	
	private int [] e_projectile_states = new int[200];				// estados
	private double [] e_projectile_X = new double[200];				// coordenadas x
	private double [] e_projectile_Y = new double[200];				// coordenadas y
	private double [] e_projectile_VX = new double[200];			// velocidade no eixo x
	private double [] e_projectile_VY = new double[200];			// velocidade no eixo y
	private double e_projectile_radius = 2.0;						// raio (tamanho dos projéteis inimigos)
	
	public ShootTipo1(){
		for(int i = 0; i < e_projectile_states.length; i++) e_projectile_states[i] = INACTIVE;
	}
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
	public void move(long delta){
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
	}
	
	public void atira(long delta, double x, double y, double angle){
		int free = findFreeIndex(e_projectile_states);
		
		if(free < e_projectile_states.length){
			e_projectile_X[free] = x;
			e_projectile_Y[free] = y;
			e_projectile_VX[free] = Math.cos(angle) * 0.45;
			e_projectile_VY[free] = Math.sin(angle) * 0.45 * (-1.0);
			e_projectile_states[free] = 1;
		}
	}
		
	public void draw(long delta){
		for(int i = 0; i < e_projectile_states.length; i++){
			
			if(e_projectile_states[i] == ACTIVE){

				GameLib.setColor(Color.RED);
				GameLib.drawCircle(e_projectile_X[i], e_projectile_Y[i], e_projectile_radius);
			}
		}
	}
	public int[] getE_projectile_states() {
		return e_projectile_states;
	}
	public void setE_projectile_states(int[] e_projectile_states) {
		this.e_projectile_states = e_projectile_states;
	}
	public double[] getE_projectile_X() {
		return e_projectile_X;
	}
	public void setE_projectile_X(double[] e_projectile_X) {
		this.e_projectile_X = e_projectile_X;
	}
	public double[] getE_projectile_Y() {
		return e_projectile_Y;
	}
	public void setE_projectile_Y(double[] e_projectile_Y) {
		this.e_projectile_Y = e_projectile_Y;
	}
	public double[] getE_projectile_VX() {
		return e_projectile_VX;
	}
	public void setE_projectile_VX(double[] e_projectile_VX) {
		this.e_projectile_VX = e_projectile_VX;
	}
	public double[] getE_projectile_VY() {
		return e_projectile_VY;
	}
	public void setE_projectile_VY(double[] e_projectile_VY) {
		this.e_projectile_VY = e_projectile_VY;
	}
	public double getE_projectile_radius() {
		return e_projectile_radius;
	}
	public void setE_projectile_radius(double e_projectile_radius) {
		this.e_projectile_radius = e_projectile_radius;
	}
}
