import java.awt.Color;
import java.util.ArrayList;

public class ShootTipo1 implements Shoot{
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int TAMANHO = 200;
	
	ArrayList<Integer> e_projectile_states = new ArrayList<Integer>(); // estados
	ArrayList<Double> e_projectile_X = new ArrayList<Double>(); // coordenadas x
	ArrayList<Double> e_projectile_Y = new ArrayList<Double>(); // coordenadas y
	ArrayList<Double> e_projectile_VX = new ArrayList<Double>(); // velocidade no eixo x
	ArrayList<Double> e_projectile_VY = new ArrayList<Double>(); // velocidade no eixo y		
	private double e_projectile_radius = 2.0;						// raio (tamanho dos projéteis inimigos)
	
	public ShootTipo1(){
		for(int i = 0; i < TAMANHO;i++){
			e_projectile_states.add(i, INACTIVE);
			e_projectile_X.add(i, 0.0);
			e_projectile_Y.add(i, 0.0);
			e_projectile_VX.add(i, 0.0);
			e_projectile_VY.add(i, 0.0);
		}
	}

	public void move(long delta){
		
		for(int i = 0; i < e_projectile_states.size(); i++){
			
			if(e_projectile_states.get(i) == ACTIVE){
				
				/* verificando se projétil saiu da tela */
				if(e_projectile_Y.get(i) > GameLib.HEIGHT) {
					
					e_projectile_states.set(i, INACTIVE);
				}
				else {
					e_projectile_X.set(i, (e_projectile_X.get(i) + e_projectile_VX.get(i) * delta));
					e_projectile_Y.set(i, (e_projectile_Y.get(i) + e_projectile_VY.get(i) * delta));
				}
			}
		}
	}
	
	public void atira(long delta, double x, double y, double vx, double vy, double angle){
		int free = e_projectile_states.indexOf(INACTIVE);
		
		if(free < e_projectile_states.size()){
			e_projectile_X.add(free, x);
			e_projectile_Y.add(free, y);
			e_projectile_VX.add(free, vx);
			e_projectile_VY.add(free, vy);
			e_projectile_states.add(free, ACTIVE);
		}
	}
		
	public void draw(){
		for(int i = 0; i < e_projectile_states.size(); i++){
			
			if(e_projectile_states.get(i) == ACTIVE){

				GameLib.setColor(Color.RED);
				GameLib.drawCircle(e_projectile_X.get(i), e_projectile_Y.get(i), e_projectile_radius);
			}
		}
	}
	public void atira(long delta, double x, double y, double[] angles){
		
	}
	
	public int getSize(){
		return TAMANHO;
	}
	
	public double getPosX(int i){
		return e_projectile_X.get(i);
	}
	
	public double getPosY(int i){
		return e_projectile_Y.get(i);
	}
	
	public double getRadius(){
		return e_projectile_radius;
	}
}
