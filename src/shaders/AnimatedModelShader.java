package shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import utils.Constants;
import utils.MathUtils;

public class AnimatedModelShader extends Shader {
	
	private static final int MAX_JOINTS = 50;// max number of joints in a skeleton
	private static final int DIFFUSE_TEX_UNIT = 0;

	private static final String VERTEX_SHADER = Constants.SHADER_PREFIX + "shaders/animatedEntityVertex.txt";
	private static final String FRAGMENT_SHADER = Constants.SHADER_PREFIX + "shaders/animatedEntityFragment.txt";
	
	private int locationProjectionViewMatrix;
	private int locationLightDirection;
	private int locationJointTransforms[];
	private int locationDiffuseMap;
	private int locationTransformationMatrix;
	private int locationViewMatrix;

	
	public AnimatedModelShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}

	public void loadTextureUnits() {
		super.loadInt(locationDiffuseMap, DIFFUSE_TEX_UNIT);
	}

	@Override
	protected void getAllUniformLocations() {
		locationProjectionViewMatrix = super.getUniformLocation("projectionViewMatrix");
		locationLightDirection = super.getUniformLocation("lightDirection");
		locationJointTransforms = new int[MAX_JOINTS];
		for(int i = 0; i < MAX_JOINTS; i++) {
			locationJointTransforms[i] = super.getUniformLocation("jointTransforms["+i+"]");
		}
		locationDiffuseMap = super.getUniformLocation("diffuseMap");
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
		super.bindAttribute(1, "in_textureCoords");
		super.bindAttribute(2, "in_normal");
		super.bindAttribute(3, "in_jointIndices");
		super.bindAttribute(4, "in_weights");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(locationProjectionViewMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
		super.loadMatrix(locationViewMatrix, viewMatrix);
	}
	
	public void loadLightDir(Vector3f lightDir) {
		super.loadVector(locationLightDirection, lightDir);
	}
	
	public void loadJointTransforms(Matrix4f[] mats) {
		for(int i = 0; i < MAX_JOINTS; i++) {
			if(i < mats.length) {
				super.loadMatrix(locationJointTransforms[i], mats[i]);
			} else {
				super.loadMatrix(locationJointTransforms[i], new Matrix4f());
			}
		}
	}
	
}
