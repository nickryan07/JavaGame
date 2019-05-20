package engineTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import org.newdawn.slick.SlickException;

import animation.Animation;
import engine.AnimatedModelLoader;
import engine.AnimationLoader;
import engine.DisplayManager;
import engine.MasterRender;
import engine.ModelData;
import engine.ModelLoader;
import engine.ObjLoader;
import engine.RenderWater;
import entities.AnimatedEntity;
import entities.Camera;
import entities.StaticEntity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import models.animated.AnimatedModel;
import terrains.Terrain;
import textures.BlendedTerrainTexture;
import textures.ModelTexture;
import textures.TerrainTexture;
import water.WaterFrameBuffer;
import water.WaterShader;
import water.WaterTile;

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

    	
    	//Music openingMenuMusic;
		//openingMenuMusic = new Music("res/music.ogg");
        //openingMenuMusic.loop();
        //openingMenuMusic.setVolume(0.08f);
//        Music natureSounds;
//    	natureSounds = new Music("res/nature.wav");
//    	natureSounds.loop();
//    	natureSounds.setVolume(0.005f);
		
		ModelLoader loader = new ModelLoader();
		
		
		
//		ModelData charData = ObjLoader.loadObjModel("char2decent");
//		RawModel model = loader.loadInVAO(charData.getVertices(), charData.getTextureCoords(),
//				charData.getNormals(), charData.getIndices());
//		ModelTexture texture = new ModelTexture(loader.loadTexture("Text"));
//		TexturedModel texturedModel = new TexturedModel(model, texture);
//		texture.setShineDamper(20);
//		texture.setReflectivity(0.1f);
		
		//Player player = new Player(texturedModel, new Vector3f(-5, 0, -30), 0, 180, 0, 0.015f);
		//Entity stall = new Entity(texturedModel, new Vector3f(0, 0, -30), 0, 0, 0, 0.015f);
		
		
		List<StaticEntity> entities = new ArrayList<StaticEntity>();
		List<AnimatedEntity> animEntities = new ArrayList<AnimatedEntity>();
		//entities.add(player);
		
		List<Light> lights = new ArrayList<Light>();
		Light sun = new Light(new Vector3f(10000, 10000, 1000), new Vector3f(0.9f,0.9f,0.9f));
		lights.add(sun);
		
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
		 //null
		ModelData grassData = ObjLoader.loadObjModel("grassModel");
		RawModel grassModel = loader.loadInVAO(grassData.getVertices(), grassData.getTextureCoords(),
				grassData.getNormals(), grassData.getIndices());//grassModel
		ModelData treeData = ObjLoader.loadObjModel("tree");
		RawModel treeModel = loader.loadInVAO(treeData.getVertices(), treeData.getTextureCoords(),
				treeData.getNormals(), treeData.getIndices());//grassModel
		
		TexturedModel grassStaticModel = new TexturedModel(grassModel,new ModelTexture(loader.loadTexture("grass2")));
		grassStaticModel.getTexture().setHasAlpha(true);
		grassStaticModel.getTexture().setFixLighting(true);Random random = new Random();
		TexturedModel treeStaticModel = new TexturedModel(treeModel,new ModelTexture(loader.loadTexture("tree")));
		treeStaticModel.getTexture().setHasAlpha(true);
		treeStaticModel.getTexture().setFixLighting(true);
		for(int i = 0; i < 500; i++) {
			float x = random.nextFloat()*800 - 400;
			float z = random.nextFloat() * -600;
			if(i % 2 == 0)
				entities.add(new StaticEntity(grassStaticModel, new Vector3f(x, terrain2.getTerrainHeight(x, z), z),0,0,0,1));
			else
				entities.add(new StaticEntity(treeStaticModel, new Vector3f(x, terrain2.getTerrainHeight(x, z), z),0,0,0,3));
			
				
		}
		ModelData lampData = ObjLoader.loadObjModel("lamp");
		RawModel lampModel = loader.loadInVAO(lampData.getVertices(), lampData.getTextureCoords(),
				lampData.getNormals(), lampData.getIndices());
		
		TexturedModel lampStaticModel = new TexturedModel(lampModel,new ModelTexture(loader.loadTexture("lamp")));
		lampStaticModel.getTexture().setHasAlpha(true);
		lampStaticModel.getTexture().setFixLighting(true);
		entities.add(new StaticEntity(lampStaticModel, new Vector3f(-25, terrain2.getTerrainHeight(-25, -40), -40),0,0,0,0.5f));
		lights.add(new Light(new Vector3f(-25, 20+terrain2.getTerrainHeight(-25, -40), -40), new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f)));
		
		
		// Animation testing
		final String MODEL_FILE = "res/" + "model.dae";
		final String ANIM_FILE =  "res/" + "model.dae";
		final String DIFFUSE_FILE =  "res/" + "diffuse.png";
		AnimatedModelLoader animModel = new AnimatedModelLoader();
		AnimatedModel entity = animModel.loadEntity(MODEL_FILE, DIFFUSE_FILE);
		Animation animation = AnimationLoader.loadAnimation(ANIM_FILE);
		//entity.doAnimation(null);
		Player player = new Player(entity, animation, new Vector3f(-5, 0, -30), 0, 180, 0, 0.4f);//
		animEntities.add(player);
		Camera camera = new Camera(player);
		MasterRender render = new MasterRender();

		
		WaterFrameBuffer fbos = new WaterFrameBuffer();
		WaterShader waterShader = new WaterShader();
		RenderWater water = new RenderWater(loader, waterShader, render.getProjectionMatrix(), fbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile waterTile = new WaterTile(-125, -75, 0);
		waters.add(waterTile);
		
		final Vector4f reflectionClipPlane = new Vector4f(0, 1, 0, -waterTile.getHeight());
		final Vector4f refractionClipPlane = new Vector4f(0, -1, 0, waterTile.getHeight()+0.25f);
		final Vector4f finalClipPlane = new Vector4f(0, 1, 0, 10000);
		
		
		
		while(!Display.isCloseRequested()) {
			camera.move();
			player.move(camera, terrain2);
			entity.update();
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			fbos.bindReflectionFrameBuffer();
			float originalYPosition = camera.getPosition().y;
			float distance = 2 *(camera.getPosition().y - waterTile.getHeight());
			camera.getPosition().y -= distance;
			camera.invertPitch();
			render.updateScene(entities, animEntities, terrains, lights, camera, reflectionClipPlane);
			camera.getPosition().y = originalYPosition;
			camera.invertPitch();
			
			fbos.bindRefractionFrameBuffer();
			render.updateScene(entities, animEntities, terrains, lights, camera, refractionClipPlane);
			
			fbos.unbindCurrentFrameBuffer();
			render.updateScene(entities, animEntities, terrains, lights, camera, finalClipPlane);
			water.render(waters, camera, sun);
			DisplayManager.updateDisplay();
			updateFPS();
		}

		//openingMenuMusic.stop();
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
