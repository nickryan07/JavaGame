package engine.renderers;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import engine.MasterRender;
import entities.StaticEntity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import utils.MathUtils;

public class RenderEntity {
	
	private StaticShader shader;
	
	
	public RenderEntity(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Map<TexturedModel, List<StaticEntity>> entities) {
		for(TexturedModel model:entities.keySet()) {
			loadTexturedModel(model);
			List<StaticEntity> batch = entities.get(model);
			for(StaticEntity entity:batch) {
				loadInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unloadTexturedModel();
		}
	}
	
	private void loadTexturedModel(TexturedModel texturedModel) {
		RawModel model = texturedModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoId());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = texturedModel.getTexture();
		if(texture.getHasAlpha()) {
			MasterRender.disableCulling();
		}
		shader.loadFixLighting(texture.getFixLighting());
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getId());
	}
	
	private void unloadTexturedModel() {
		MasterRender.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}
	
	private void loadInstance(StaticEntity entity) {
		Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(entity.getPosition(),
				entity.getrX(), entity.getrY(), entity.getrZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
	}
	
}