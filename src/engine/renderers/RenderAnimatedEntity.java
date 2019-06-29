package engine.renderers;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

import engine.MasterRender;
import entities.AnimatedEntity;
import entities.Camera;
import models.RawModel;
import models.animated.AnimatedModel;
import shaders.AnimatedModelShader;
import utils.MathUtils;

public class RenderAnimatedEntity {

	private AnimatedModelShader shader;

	public RenderAnimatedEntity(AnimatedModelShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(List<AnimatedEntity> entities, Camera camera, Vector3f lightDir) {
		for(AnimatedEntity entity:entities ) {
			AnimatedModel animModel = entity.getModel();
			RawModel model = animModel.getModel();
			GL30.glBindVertexArray(model.getVaoId());
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL20.glEnableVertexAttribArray(2);
			GL20.glEnableVertexAttribArray(3);
			GL20.glEnableVertexAttribArray(4);
			
			Texture tex = animModel.getTexture();
			if(tex.hasAlpha()) {
				MasterRender.disableCulling();
			}
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());
			shader.loadJointTransforms(animModel.getJointTransforms());
			//shader.loadLightDir(lightDir);
			Matrix4f transformationMatrix = MathUtils.createTransformationMatrix(entity.getPosition(),
					entity.getrX(), entity.getrY(), entity.getrZ(), entity.getScale());
			shader.loadTransformationMatrix(transformationMatrix);
			GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			finish();
		}
	}

	public void unload() {
		shader.unload();
	}

//	private void prepare(Camera camera, Vector3f lightDir, Matrix4f[] mats) {
//		shader.start();
//		OpenGlUtils.antialias(true);
//		OpenGlUtils.disableBlending();
//		OpenGlUtils.enableDepthTesting(true);
//		shader.stop();
//	}

	private void finish() {
		MasterRender.enableCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL30.glBindVertexArray(0);
	}
	
}
