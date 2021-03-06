package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import engine.renderers.RenderAnimatedEntity;
import engine.renderers.RenderEntity;
import engine.renderers.RenderSkybox;
import engine.renderers.RenderTerrain;
import entities.AnimatedEntity;
import entities.Camera;
import entities.StaticEntity;
import entities.Light;
import models.TexturedModel;
import shaders.AnimatedModelShader;
import shaders.StaticShader;
import shaders.TerrainShader;
import terrains.Terrain;

public class MasterRender {

	private static final float FOV = 70; //70
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000;
	
	private static final float RED = 0.5f, GREEN = 0.98f, BLUE = 0.99f;
	
	private Matrix4f projectionMatrix;
	
	private StaticShader shader = new StaticShader();
	private RenderEntity render;
	
	private RenderTerrain renderTerrain;
	private TerrainShader terrainShader = new TerrainShader();

	private RenderAnimatedEntity animatedRenderer;
	private AnimatedModelShader animatedShader = new AnimatedModelShader();
	
	private RenderSkybox renderSkybox;
	
	
	
	private Map<TexturedModel, List<StaticEntity>> entities = new HashMap<TexturedModel, List<StaticEntity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	private List<AnimatedEntity> animEntities = new ArrayList<AnimatedEntity>();
	
	public MasterRender(ModelLoader loader) {
		enableCulling();
		createProjectionMatrix();
		render = new RenderEntity(shader, projectionMatrix);
		renderTerrain = new RenderTerrain(terrainShader, projectionMatrix);
		animatedRenderer = new RenderAnimatedEntity(animatedShader, projectionMatrix);
		renderSkybox = new RenderSkybox(loader, projectionMatrix);
	}
	
	public void updateScene(List<StaticEntity> entitiesList, List<AnimatedEntity> animatedEntities, List<Terrain> terrains, List<Light> lights, Camera camera, Vector4f clippingPlane) {
		for(Terrain terr:terrains) {
			processTerrain(terr);
		}
		for(StaticEntity entity:entitiesList) {
			processEntity(entity);
		}
		for(AnimatedEntity anim:animatedEntities) {
			processAnimatedEntity(anim);
		}
		render(lights, camera, clippingPlane);
	}
	
	public void render(List<Light> lights, Camera camera, Vector4f clippingPlane) {
		init();
		shader.start();
		shader.loadPlane(clippingPlane);
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		render.render(entities);
		shader.stop();
		terrainShader.start();
		terrainShader.loadPlane(clippingPlane);
		terrainShader.loadSkyColor(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		renderTerrain.render(terrains);
		terrainShader.stop();
		animatedShader.start();
		animatedShader.loadViewMatrix(camera);
		//animatedShader.loadJointTransforms(am.getJointTransforms());
		//animatedShader.loadLightDir(lights.get(0).getPosition());
		animatedRenderer.render(animEntities, camera, lights.get(0).getPosition());
		animatedShader.stop();
		renderSkybox.render(camera);
		entities.clear();
		terrains.clear();
		animEntities.clear();
	}
	
	public void init() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
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
	
	public void processAnimatedEntity(AnimatedEntity entity) {
		animEntities.add(entity);
	}
	
	public void processEntity(StaticEntity entity) {
		TexturedModel entityModel = entity.getModel();
		List<StaticEntity> batch = entities.get(entityModel);
		if(batch != null) {
			batch.add(entity);
		} else {
			List<StaticEntity> newBatch = new ArrayList<StaticEntity>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
		
	}
	
	public void unload() {
		shader.unload();
		terrainShader.unload();
		animatedShader.unload();
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
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
}
