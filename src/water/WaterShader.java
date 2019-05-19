package water;

import org.lwjgl.util.vector.Matrix4f;

import entity.Camera;
import entity.Light;
import shaders.Shader;
import utils.Constants;
import utils.MathUtils;

public class WaterShader extends Shader {

	private final static String VERTEX_FILE = Constants.SHADER_PREFIX + "water/waterVertex.txt";
	private final static String FRAGMENT_FILE = Constants.SHADER_PREFIX + "water/waterFragment.txt";

	private int locationModelMatrix;
	private int locationViewMatrix;
	private int locationProjectionMatrix;
	private int locationReflectionTexture;
	private int locationRefractionTexture;
	private int locationDudvMap;
	private int locationMoveFactor;
	private int locationCameraPosition;
	private int locationNormalMap;
	private int locationLightColor;
	private int locationLightPosition;
	private int locationDepthMap;

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
		locationCameraPosition = getUniformLocation("cameraPosition");
		locationNormalMap = getUniformLocation("normalMap");
		locationLightColor = getUniformLocation("lightColor");
		locationLightPosition = getUniformLocation("lightPosition");
		locationDepthMap = getUniformLocation("depthMap");
	}
	
	public void loadLight(Light light) {
		super.loadVector(locationLightColor, light.getColor());
		super.loadVector(locationLightPosition, light.getPosition());
	}
	
	public void loadMoveFactor(float factor) {
		super.loadFloat(locationMoveFactor, factor);
	}
	
	public void connectTextures() {
		super.loadInt(locationReflectionTexture, 0);
		super.loadInt(locationRefractionTexture, 1);
		super.loadInt(locationDudvMap, 2);
		super.loadInt(locationNormalMap, 3);
		super.loadInt(locationDepthMap, 4);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(locationProjectionMatrix, projection);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
		loadMatrix(locationViewMatrix, viewMatrix);
		super.loadVector(locationCameraPosition, camera.getPosition());
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(locationModelMatrix, modelMatrix);
	}
	
}
