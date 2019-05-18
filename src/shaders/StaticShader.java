package shaders;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import entity.Camera;
import entity.Light;
import utils.Constants;
import utils.MathUtils;

public class StaticShader extends Shader {
	
	private static final String VERTEX_FILE = Constants.SHADER_PREFIX + "shaders/vertexShader.txt";
	private static final String FRAGMENT_FILE = Constants.SHADER_PREFIX + "shaders/fragmentShader.txt";
	
	private static final int MAX_LIGHT_SOURCES = 6;
	
	private int locationTransformationMatrix;
	private int locationProjectionMatrix;
	private int locationViewMatrix;
	private int locationLightPosition[];
	private int locationLightColor[];
	private int locationAttenuation[];
	private int locationShineDamper;
	private int locationReflectivity;
	private int locationFixLighting;
	private int locationSkyColor;
	private int locationPlane;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
		locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
		locationViewMatrix = super.getUniformLocation("viewMatrix");
		locationShineDamper = super.getUniformLocation("shineDamper");
		locationReflectivity = super.getUniformLocation("reflectivity");
		locationFixLighting = super.getUniformLocation("fixLighting");
		locationSkyColor = super.getUniformLocation("skyColor");
		locationLightPosition = new int[MAX_LIGHT_SOURCES];
		locationLightColor = new int[MAX_LIGHT_SOURCES];
		locationAttenuation = new int[MAX_LIGHT_SOURCES];
		for(int i = 0; i < MAX_LIGHT_SOURCES; i++) {
			locationLightPosition[i] = super.getUniformLocation("lightPosition["+i+"]");
			locationLightColor[i] = super.getUniformLocation("lightColor["+i+"]");
			locationAttenuation[i] = super.getUniformLocation("attenuation["+i+"]");;
		}
		locationPlane = super.getUniformLocation("plane");
	}
	
	public void loadPlane(Vector4f plane) {
		super.loadVector4f(locationPlane, plane);
	}
	
	public void loadSkyColor(float red, float green, float blue) {
		super.loadVector(locationSkyColor, new Vector3f(red, green, blue));
	}
	
	public void loadFixLighting(boolean fixLighting) {
		super.loadBoolean(locationFixLighting, fixLighting);
	}
	
	public void loadLights(List<Light> lights) {
		for(int i = 0; i < MAX_LIGHT_SOURCES; i++) {
			if(i < lights.size()) {
				super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
				super.loadVector(locationLightColor[i], lights.get(i).getColor());
				super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
			} else {
				super.loadVector(locationLightPosition[i], new Vector3f(0, 0, 0));
				super.loadVector(locationLightColor[i], new Vector3f(0, 0, 0));
				super.loadVector(locationAttenuation[i], new Vector3f(1, 0, 0));
			}
		}
	}
	
	public void loadShineVariables(float damper, float reflectivity) {
		super.loadFloat(locationShineDamper, damper);
		super.loadFloat(locationReflectivity, reflectivity);
	}

	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(locationTransformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(locationProjectionMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = MathUtils.createViewMatrix(camera);
		super.loadMatrix(locationViewMatrix, viewMatrix);
	}
	
}
