package water;

import org.lwjgl.util.vector.Matrix4f;

import entity.Camera;
import shaders.Shader;
import utils.MathUtils;

public class WaterShader extends Shader {

	private final static String VERTEX_FILE = "src/water/waterVertex.txt";
	private final static String FRAGMENT_FILE = "src/water/waterFragment.txt";

	private int locationModelMatrix;
	private int locationViewMatrix;
	private int locationProjectionMatrix;
	private int locationReflectionTexture;
	private int locationRefractionTexture;
	private int locationDudvMap;
	private int locationMoveFactor;

	public WaterShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		locationProjectionMatrix = getUniformLocation("projectionMatrix");
		locationViewMatrix = getUniformLocation("viewMatrix");
		locationModelMatrix = getUniformLocation("modelMatrix");
		locationReflectionTexture = getUniformLocation("reflectionTexture");
		locationRefractionTexture = getUniformLocation("refractionTexture");
		locationDudvMap = getUniformLocation("dudvMap");
		locationMoveFactor = getUniformLocation("moveFactor");
	}
	
	public void loadMoveFactor(float factor) {
		super.loadFloat(locationMoveFactor, factor);
	}
	
	public void connectTextures() {
		super.loadInt(locationReflectionTexture, 0);
		super.loadInt(locationRefractionTexture, 1);
		super.loadInt(locationDudvMap, 2);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(locationProjectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
		loadMatrix(locationViewMatrix, viewMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(locationModelMatrix, modelMatrix);
	}
	
}
