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
import engine.ModelData;
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
    	
    	DisplayManager.createDisplay();

    	
    	Music openingMenuMusic;
		openingMenuMusic = new Music("res/music.ogg");
        openingMenuMusic.loop();
        openingMenuMusic.setVolume(0.08f);
        Music natureSounds;
    	natureSounds = new Music("res/nature.wav");
    	natureSounds.loop();
    	natureSounds.setVolume(0.005f);
		
		ModelLoader loader = new ModelLoader();
		
		
		
		ModelData charData = ObjLoader.loadObjModel("char2");
		RawModel model = loader.loadInVAO(charData.getVertices(), charData.getTextureCoords(),
				charData.getNormals(), charData.getIndices());//Stall
		ModelTexture texture = new ModelTexture(loader.loadTexture("Text"));//StallTexture
		TexturedModel texturedModel = new TexturedModel(model, texture);
		//ModelTexture tex = texturedModel.getTexture();
		texture.setShineDamper(20);
		texture.setReflectivity(0.1f);
		
		Player player = new Player(texturedModel, new Vector3f(0, 0, -30), 0, 180, 0, 0.015f);
		//Entity stall = new Entity(texturedModel, new Vector3f(0, 0, -30), 0, 0, 0, 0.015f);
		
		
		List<Entity> entities = new ArrayList<Entity>();
		entities.add(player);
		
		List<Light> lights = new ArrayList<Light>();
		lights.add(new Light(new Vector3f(10000, 10000, 1000), new Vector3f(0.4f,0.4f,0.4f)));
		//lights.add(new Light(new Vector3f(-200, 40, -200), new Vector3f(100,0,0)));
		//lights.add(new Light(new Vector3f(-100, 10, -100), new Vector3f(0,0,100)));
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grass"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("mud"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("flowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		BlendedTerrainTexture blendedTexture = new BlendedTerrainTexture(backgroundTexture, rTexture, gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("texmap2"));
		Terrain terrain = new Terrain(0, -1,loader,blendedTexture, blendMap, "heightmap");
		Terrain terrain2 = new Terrain(-1,-1,loader,blendedTexture, blendMap, "heightmap");
		List<Terrain> terrains = new ArrayList<Terrain>();
		terrains.add(terrain);
		terrains.add(terrain2);
		Camera camera = new Camera(player); //null
		ModelData grassData = ObjLoader.loadObjModel("grassModel");
		RawModel grassModel = loader.loadInVAO(grassData.getVertices(), grassData.getTextureCoords(),
				grassData.getNormals(), grassData.getIndices());//grassModel
		
		TexturedModel grassStaticModel = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("grass2")));
		grassStaticModel.getTexture().setHasAlpha(true);
		grassStaticModel.getTexture().setFixLighting(true);Random random = new Random();
		for(int i = 0; i < 500; i++) {
			float x = random.nextFloat()*800 - 400;
			float z = random.nextFloat() * -600;
			entities.add(new Entity(grassStaticModel, new Vector3f(x, terrain2.getTerrainHeight(x, z), z),0,0,0,1));
		}
		ModelData lampData = ObjLoader.loadObjModel("lamp");
		RawModel lampModel = loader.loadInVAO(lampData.getVertices(), lampData.getTextureCoords(),
				lampData.getNormals(), lampData.getIndices());;//grassModel
		
		TexturedModel lampStaticModel = new TexturedModel(lampModel,new ModelTexture(loader.loadTexture("lamp")));
		lampStaticModel.getTexture().setHasAlpha(true);
		lampStaticModel.getTexture().setFixLighting(true);
		entities.add(new Entity(lampStaticModel, new Vector3f(-25, terrain2.getTerrainHeight(0, -40), -40),0,0,0,0.5f));
		lights.add(new Light(new Vector3f(-25, 20+terrain2.getTerrainHeight(0, -40), -40), new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f)));
		
		MasterRender render = new MasterRender();
		while(!Display.isCloseRequested()) {
			camera.move();
			player.move(camera, terrain2);
			render.processTerrain(terrain);
			render.processTerrain(terrain2);
			for(Entity entity:entities){
				render.processEntity(entity);
			}
			render.render(lights, camera);
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
			
			e.printStackTrace();
		}
		
	}
	

}
