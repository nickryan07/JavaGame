package entity;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

	private Vector3f position = new Vector3f(0, 3, 0);
	private float pitch, yaw, roll;
	
	public void move() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W))
			position.z -= 0.5f;
		if(Keyboard.isKeyDown(Keyboard.KEY_D))
			position.x += 0.35f;
		if(Keyboard.isKeyDown(Keyboard.KEY_A))
			position.x -= 0.35f;
		if(Keyboard.isKeyDown(Keyboard.KEY_S))
			position.z += 0.5f;
		if(Keyboard.isKeyDown(Keyboard.KEY_Q))
			position.y += 0.15f;
		if(Keyboard.isKeyDown(Keyboard.KEY_E))
			position.y -= 0.15f;
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
