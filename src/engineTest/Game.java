package engineTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import engine.DisplayManager;
import engine.MasterRender;
import engine.ModelLoader;
import engine.ObjLoader;
import entity.Camera;
import entity.Entity;
import entity.Light;
import models.RawModel;
import models.TexturedModel;
import terrains.Terrain;
import textures.ModelTexture;

public class Game {
	
	/**
	 * Main
	 */
	public static void main(String[] args) {
		DisplayManager.createDisplay();
		
		ModelLoader loader = new ModelLoader();
		
		
		
		RawModel model = ObjLoader.loadObjModel("Stall", loader);
		ModelTexture texture = new ModelTexture(loader.loadTexture("stallTexture"));
		TexturedModel texturedModel = new TexturedModel(model, texture);
		//ModelTexture tex = texturedModel.getTexture();
		texture.setShineDamper(20);
		texture.setReflectivity(0.5f);
		
		Entity entity = new Entity(texturedModel, new Vector3f(0, 0, -30), 0, 180, 0, 1);
//		RawModel model = ObjLoader.loadObjModel("stall", loader);
//		
//		TexturedModel staticModel = new TexturedModel(model,new ModelTexture(loader.loadTexture("stallTexture")));
//		
//		List<Entity> entities = new ArrayList<Entity>();
//		Random random = new Random();
//		for(int i=0;i<50;i++){
//			entities.add(new Entity(staticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,3));
//		}
		Light light = new Light(new Vector3f(0, 0, -20), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0, -1,loader,new ModelTexture(loader.loadTexture("10")));
		Terrain terrain2 = new Terrain(-1,-1,loader,new ModelTexture(loader.loadTexture("grass")));
		
		Camera camera = new Camera();
		
		MasterRender render = new MasterRender();
		while(!Display.isCloseRequested()) {
			camera.move();
			render.processTerrain(terrain);
			render.processTerrain(terrain2);
			//for(Entity entity:entities){
				render.processEntity(entity);
			//}
			render.render(light, camera);
			DisplayManager.updateDisplay();
		}
		
		render.unload();
		loader.unload();
		DisplayManager.closeDisplay();
	}
	

}
