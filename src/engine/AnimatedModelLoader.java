package engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import engine.collada.ColladaLoader;
import engine.collada.data.AnimatedModelData;
import engine.collada.data.JointData;
import engine.collada.data.MeshData;
import engine.collada.data.SkeletonData;
import models.RawModel;
import models.animated.AnimatedModel;
import models.animated.Joint;
import utils.Constants;

public class AnimatedModelLoader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	public AnimatedModel loadEntity(String modelFile, String textureFile) {
		AnimatedModelData entityData = ColladaLoader.loadColladaModel(modelFile, Constants.MAX_WEIGHTS);
		RawModel model = createVao(entityData.getMeshData());
		Texture texture = loadTexture(textureFile);
		SkeletonData skeletonData = entityData.getJointsData();
		Joint headJoint = createJoints(skeletonData.headJoint);
		return new AnimatedModel(model, texture, headJoint, skeletonData.jointCount);
	}

	private Texture loadTexture(String textureFile) {
		Texture diffuseTexture = null;
		try {
			diffuseTexture = TextureLoader.getTexture("PNG", new FileInputStream(textureFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int textureId = diffuseTexture.getTextureID();
		textures.add(textureId);
		return diffuseTexture;
	}

	private static Joint createJoints(JointData data) {
		Joint joint = new Joint(data.index, data.nameId, data.bindLocalTransform);
		for (JointData child : data.children) {
			joint.addChild(createJoints(child));
		}
		return joint;
	}
	
	private int createVAO() {
		int vaoId = GL30.glGenVertexArrays();
		vaos.add(vaoId);
		GL30.glBindVertexArray(vaoId);
		return vaoId;
	}
	
	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private RawModel createVao(MeshData data) {
		int vaoId = createVAO();
		bindIndiciesBuffer(data.getIndices());
		storeDataInAttributeList(0, 3, data.getVertices());
		storeDataInAttributeList(1, 2, data.getTextureCoords());
		storeDataInAttributeList(2, 3, data.getNormals());
		storeDataInIntAttributeList(3, 3, data.getJointIds());
		storeDataInAttributeList(4, 3, data.getVertexWeights());
		unbindVAO();
		return new RawModel(vaoId, data.getIndices().length);
	}
	
	private void storeDataInAttributeList(int attributeNumber, int coordSize, float[] data) {
		int vboId = GL15.glGenBuffers();
		vbos.add(vboId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordSize, GL11.GL_FLOAT,
				false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	private void storeDataInIntAttributeList(int attributeNumber, int coordSize, int[] data) {
		int vboId = GL15.glGenBuffers();
		vbos.add(vboId);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
		IntBuffer buffer = storeDataInIntBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL30.glVertexAttribIPointer(attributeNumber, coordSize, GL11.GL_INT, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void bindIndiciesBuffer(int[] indicies) {
		int vboId = GL15.glGenBuffers();
		vbos.add(vboId);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboId);
		IntBuffer buffer = storeDataInIntBuffer(indicies);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
}
