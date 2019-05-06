package textures;

public class ModelTexture {
	
	private int textureId;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasAlpha = false;
	private boolean fixLighting = false;
	
	
	public ModelTexture(int id) {
		this.textureId = id;
	}
	
	public boolean getFixLighting() {
		return fixLighting;
	}

	public void setFixLighting(boolean fixLighting) {
		this.fixLighting = fixLighting;
	}
	
	public boolean getHasAlpha() {
		return hasAlpha;
	}

	public void setHasAlpha(boolean hasAlpha) {
		this.hasAlpha = hasAlpha;
	}

	public int getId() {
		return this.textureId;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

}
