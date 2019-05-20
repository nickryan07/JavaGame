package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import animation.Animation;
import engine.DisplayManager;
import models.animated.AnimatedModel;
import terrains.Terrain;
import utils.Constants;

public class Player extends AnimatedEntity {
	
	private static final float RUN = 10;
	private static final float TURN = 160;
	private static final float JUMP = 15;
	private static final float GRAVITY = -60;
	
	private float currentSpeed = 0;
	private float currentTurn = 0;
	private float currentJump = 0;
	
	Animation moveAnimation;

	public Player(AnimatedModel model, Animation anim, Vector3f position, float rX, float rY, float rZ, float scale) {
		super(model, position, rX, rY, rZ, scale);
		moveAnimation = anim;
	}
	
//	public Player(AnimatedModel model, Vector3f position, float rX, float rY, float rZ, float scale) {
//		super(model, position, rX, rY, rZ, scale);
//		
//	}
	
	public void move(Camera camera, /*ArrayList<Terrain> terrains*/ Terrain terrain) {
		checkInputs();
		super.increaseRotation(0, currentTurn * DisplayManager.getFrameTimeSeconds(), 0);
		float distance = currentSpeed * DisplayManager.getFrameTimeSeconds();
		float x_dist = (float) (distance * Math.sin(Math.toRadians(super.getrY())));
		float z_dist = (float) (distance * Math.cos(Math.toRadians(super.getrY())));
		super.increasePosition(x_dist, 0, z_dist);
		currentJump += GRAVITY * DisplayManager.getFrameTimeSeconds();
		super.increasePosition(0, currentJump * DisplayManager.getFrameTimeSeconds(), 0);
		float terrainHeight = terrain.getTerrainHeight(super.getPosition().x, super.getPosition().z);
		if(super.getPosition().y < terrainHeight) {
			currentJump = 0;
			super.getPosition().y = terrainHeight;
		}
		if(!Constants.THIRD_PERSON) {
			camera.setPosition(currentTurn * DisplayManager.getFrameTimeSeconds());
		}
	}
	
	private void jump() {
		if(currentJump == 0)
			this.currentJump = JUMP;
	}
	
	private void checkInputs() {
		if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if(!super.getModel().isAnimating()) {
					super.getModel().doAnimation(moveAnimation);
				}
				this.currentSpeed = RUN*2;
			} else {
				if(!super.getModel().isAnimating()) {
					super.getModel().doAnimation(moveAnimation);
				} 
				this.currentSpeed = RUN;
			}
		} else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
			if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
				if(!super.getModel().isAnimating()) {
					super.getModel().doAnimation(moveAnimation);
				}
				this.currentSpeed = -RUN*2;
			} else {
				if(!super.getModel().isAnimating()) {
					super.getModel().doAnimation(moveAnimation);
				}
				this.currentSpeed =- RUN;
			}
		} else {
			this.currentSpeed = 0;
			super.getModel().reset();
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurn = -TURN;
		} else if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurn = TURN;
		} else {
			this.currentTurn = 0;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}

}
