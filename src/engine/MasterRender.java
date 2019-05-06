package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

import entity.Camera;
import entity.Entity;
import entity.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

public class MasterRender {

	private static final float FOV = 70; //70
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private RenderEntity render;
	
	private RenderTerrain renderTerrain;
	private TerrainShader terrainShader = new TerrainShader();
	
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	public MasterRender() {
		enableCulling();
		createProjectionMatrix();
		render = new RenderEntity(shader, projectionMatrix);
		renderTerrain = new RenderTerrain(terrainShader, projectionMatrix);
	}
	
	public void render(Light light, Camera camera) {
		init();
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(camera);
		render.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadLight(light);
		terrainShader.loadViewMatrix(camera);
		renderTerrain.render(terrains);
		terrainShader.stop();
		entities.clear();
		terrains.clear();
	}
	
	public void init() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(0.5f, 0.98f, 0.99f, 1);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if(batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
		
	}
	
	public void unload() {
		shader.unload();
		terrainShader.unload();
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) (1f/ Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio;
		float x_scale = y_scale / aspectRatio;
		float frustumLength = FAR_PLANE - NEAR_PLANE;
		
		projectionMatrix = new Matrix4f();
		projectionMatrix.m00 = x_scale;
		projectionMatrix.m11 = y_scale;
		projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) /frustumLength);
		projectionMatrix.m23 = -1;
		projectionMatrix.m32 = -((2* FAR_PLANE * NEAR_PLANE) /frustumLength);
		projectionMatrix.m33 = 0;
		
	}
	
}
