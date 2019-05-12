package entity;

import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 3, 0);
	private float pitch, yaw, roll;
	Player player = null;
	
	public Camera(Player player) {
		if(player != null) {
			Vector3f playerPosition = player.getPosition();
			position = new Vector3f(playerPosition.getX(), playerPosition.getY()+3, playerPosition.getZ()); //+3, +4
			this.yaw = player.getrY();
			this.player = player;
		}
	}
	
	public void move() {
//		if(Keyboard.isKeyDown(Keyboard.KEY_W))
//			position.z -= 0.5f;
//		if(Keyboard.isKeyDown(Keyboard.KEY_D))
//			position.x += 0.35f;
//		if(Keyboard.isKeyDown(Keyboard.KEY_A))
//			position.x -= 0.35f;
//		if(Keyboard.isKeyDown(Keyboard.KEY_S))
//			position.z += 0.5f;
//		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
//			position.y += 0.15f;
//		if(Keyboard.isKeyDown(Keyboard.KEY_E))
//			position.y -= 0.15f;
	}
	
	
	public void setPosition(float turn) {
		if(player != null) {
			Vector3f playerPosition = player.getPosition();
			
			position = new Vector3f(playerPosition.getX(), playerPosition.getY()+3, playerPosition.getZ());
			//this.yaw = rX;
			this.yaw = -player.getrY()-180;
			//this.pitch = rZ;
		}
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
	
}
