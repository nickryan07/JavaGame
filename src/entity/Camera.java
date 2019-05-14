package entity;

import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import utils.Constants;

public class Camera {

	private Vector3f position = new Vector3f(0, 3, 0);
	private float pitch, yaw, roll;
	Player player = null;
	
	private float distance = 50;
	private float angle = 0;
	
	public Camera(Player player) {
		if(player != null) {
			if(!Constants.THIRD_PERSON) {
				Vector3f playerPosition = player.getPosition();
				position = new Vector3f(playerPosition.getX(), playerPosition.getY()+3, playerPosition.getZ());
				this.yaw = player.getrY();
			}
			this.player = player;
		}
	}
	
	public void move() {
		if(Constants.THIRD_PERSON) {
			zoom();
			pitch();
			angle();
			setThirdPersonPosition(horizontalDistance(), verticalDistance());
			this.yaw = 180 - (player.getrY() + angle);
		}
	}
	
	public void setThirdPersonPosition(float hDist, float vDist) {
		float theta = player.getrY() + angle;
		position.x = player.getPosition().x - (float) (hDist * Math.sin(Math.toRadians(theta)));
		position.y = player.getPosition().y + vDist;
		position.z = player.getPosition().z - (float) (hDist * Math.cos(Math.toRadians(theta)));
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
	
	private float horizontalDistance() {
		return (float) (distance * Math.cos(Math.toRadians(pitch)));
	}
	
	private float verticalDistance() {
		return (float) (distance * Math.sin(Math.toRadians(pitch)));
	}
	
	private void zoom() {
		float zoom = Mouse.getDWheel() * 0.07f;
		distance -= zoom;
	}
	
	private void pitch() {
		if(Mouse.isButtonDown(1)) {
			float delta = Mouse.getDY() * 0.07f;
			pitch -= delta;
		}
	}
	
	private void angle() {
		if(Mouse.isButtonDown(0)) {
			float delta = Mouse.getDX() * 0.5f;
			angle -= delta;
		}
	}
	
}
