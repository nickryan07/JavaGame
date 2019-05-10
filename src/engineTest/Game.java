package engineTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import engine.DisplayManager;
import engine.MasterRender;
import engine.ModelLoader;
import engine.ObjLoader;
import entity.Camera;
import entity.Entity;
import entity.Light;
import entity.Player;
import models.RawModel;
import models.TexturedModel;
import terrains.Terrain;
import textures.BlendedTerrainTexture;
import textures.ModelTexture;
import textures.TerrainTexture;

public class Game {
	
	 /** time at last frame */
    long lastFrame;
     
    /** frames per second */
    int fps;
    /** last fps time */
    long lastFPS = getTime();
 
    
    /**
     * Get the accurate system time
     * 
     * @return The system time in milliseconds
     */
    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    
    /**
     * Calculate the FPS and set it in the title bar
     */
    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("Java Game - FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
    
    public void initialize() throws SlickException {
    	Music openingMenuMusic;
		openingMenuMusic = new Music("res/music.ogg");
        //openingMenuMusic.loop();
        openingMenuMusic.setVolume(0.08f);
    	
    	DisplayManager.createDisplay();
		
		ModelLoader loader = new ModelLoader();
		
		
		
		RawModel model = ObjLoader.loadObjModel("char2", loader);//Stall
		ModelTexture texture = new ModelTexture(loader.loadTexture("Text"));//StallTexture
		TexturedModel texturedModel = new TexturedModel(model, texture);
		//ModelTexture tex = texturedModel.getTexture();
		texture.setShineDamper(20);
		texture.setReflectivity(0.1f);
		
		Player player = new Player(texturedModel, new Vector3f(0, 0, -30), 0, 180, 0, 0.015f);
		//Entity stall = new Entity(texturedModel, new Vector3f(0, 0, -30), 0, 0, 0, 0.015f);
		RawModel grassModel = ObjLoader.loadObjModel("grassModel", loader);//grassModel
		
		TexturedModel grassStaticModel = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("grass2")));
		grassStaticModel.getTexture().setHasAlpha(true);
		grassStaticModel.getTexture().setFixLighting(true);
		List<Entity> entities = new ArrayList<Entity>();
		entities.add(player);
		Random random = new Random();
		for(int i=0;i<500;i++){
			entities.add(new Entity(grassStaticModel, new Vector3f(random.nextFloat()*800 - 400,0,random.nextFloat() * -600),0,0,0,1));
		}
		Light light = new Light(new Vector3f(10000, 10000, 1000), new Vector3f(1,1,1));
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("flowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		BlendedTerrainTexture blendedTexture = new BlendedTerrainTexture(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("texmap2"));
		Terrain terrain = new Terrain(0, -1,loader,blendedTexture, blendMap);
		Terrain terrain2 = new Terrain(-1,-1,loader,blendedTexture, blendMap);
		
		Camera camera = new Camera(player); //null
		
		
		
		MasterRender render = new MasterRender();
		while(!Display.isCloseRequested()) {
			camera.move();
			player.move(camera);
			render.processTerrain(terrain);
			render.processTerrain(terrain2);
			for(Entity entity:entities){
				render.processEntity(entity);
			}
			render.render(light, camera);
			DisplayManager.updateDisplay();
			updateFPS();
		}

		openingMenuMusic.stop();
		render.unload();
		loader.unload();
		DisplayManager.closeDisplay();
    }
    
	/**
	 * Main
	 */
	public static void main(String[] args) {
		Game game = new Game();
		try {
			game.initialize();
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
