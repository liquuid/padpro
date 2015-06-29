import java.awt.Color;
import java.util.ArrayList;


public class ShootTipo2 implements Shoot {
	public static final int INACTIVE = 0;
	public static final int ACTIVE = 1;
	public static final int TAMANHO = 200;
	
	ArrayList<Integer> projectile_states = new ArrayList<Integer>(); // estados
	ArrayList<Double> projectile_X = new ArrayList<Double>(); // coordenadas x
	ArrayList<Double> projectile_Y = new ArrayList<Double>(); // coordenadas y
	ArrayList<Double> projectile_VX = new ArrayList<Double>(); // velocidade no eixo x
	ArrayList<Double> projectile_VY = new ArrayList<Double>(); // velocidade no eixo y
	
	public ShootTipo2(){
		for(int i = 0; i < TAMANHO;i++){
			projectile_states.add(i, INACTIVE);
			projectile_X.add(i, 0.0);
			projectile_Y.add(i, 0.0);
			projectile_VX.add(i, 0.0);
			projectile_VY.add(i, 0.0);
		}
	}
	
	public void move(long delta){
		for(int i = 0; i < projectile_states.size(); i++){
			
			if(projectile_states.get(i) == ACTIVE){
				
				/* verificando se projétil saiu da tela */
				if(projectile_Y.get(i) < 0) {
					
					projectile_states.set(i, INACTIVE);
				}
				else {
				
					projectile_X.set(i, (projectile_X.get(i) + projectile_VX.get(i) * delta));
					projectile_Y.set(i, (projectile_Y.get(i) + projectile_VY.get(i) * delta));
				}
			}
		}
	}
	
	public void atira(long delta, double x, double y, double vx, double vy, double angle){
		int free = projectile_states.indexOf(INACTIVE);
		
		if(free < projectile_states.size()){
			projectile_X.add(free, x);
			projectile_Y.add(free, y);
			projectile_VX.add(free, vx);
			projectile_VY.add(free, vy);
			projectile_states.add(free, ACTIVE);
		}
	}
		
	public void draw(){
		for(int i = 0; i < projectile_states.size(); i++){
			
			if(projectile_states.get(i) == ACTIVE){
				
				GameLib.setColor(Color.GREEN);
				GameLib.drawLine(projectile_X.get(i), projectile_Y.get(i) - 5, projectile_X.get(i), projectile_Y.get(i) + 5);
				GameLib.drawLine(projectile_X.get(i) - 1, projectile_Y.get(i) - 3, projectile_X.get(i) - 1, projectile_Y.get(i) + 3);
				GameLib.drawLine(projectile_X.get(i) + 1, projectile_Y.get(i) - 3, projectile_X.get(i) + 1, projectile_Y.get(i) + 3);
			}
		}
	}
}
