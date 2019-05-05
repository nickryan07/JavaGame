package engine;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import models.RawModel;
import shaders.TerrainShader;
import terrains.Terrain;
import textures.ModelTexture;
import utils.MathUtils;

public class RenderTerrain {
	
	private TerrainShader shader;
	
	public RenderTerrain(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(List<Terrain> terrains) {
		for(Terrain terrain: terrains) {
			loadTerrain(terrain);
			loadModelMatrix(terrain);
			GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getMesh().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			unloadTexturedModel();
		}
	}
	
	private void loadTerrain(Terrain terrain) {
		RawModel model = terrain.getMesh();
		GL30.glBindVertexArray(model.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = terrain.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getId());
	}
	
	private void unloadTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(new Vector3f(terrain.getX(), 0, terrain.getZ()),
				0, 0, 0, 1);
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
}
