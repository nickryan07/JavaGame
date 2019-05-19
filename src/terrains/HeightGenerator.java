package terrains;

import java.util.Random;

public class HeightGenerator {
	
	private static final float AMPLITUDE = 55f;
//	private static final int OCTAVES = 3;
//	private static final float ROUGHNESS = 0.4f;
//	
	
	private Random random = new Random();
	private int seed;
	//(int) (System.currentTimeMillis() / 1000L)
	public HeightGenerator() {
		this.seed = 1000000000;//random.nextInt(1000000000);
	}
	
	public float generateHeight(int x, int z) {
		float totalNoise = interpolateNoise(x/8f, z/8f) * AMPLITUDE;
		totalNoise += interpolateNoise(x/4f, z/4f) * AMPLITUDE/4f;
		totalNoise += interpolateNoise(x, z) * AMPLITUDE/16f;
		return totalNoise;
	}
	
	private float averageNoise(int x, int z) {
		float corners = (generateNoise(x-1, z-1) + generateNoise(x+1, z-1) + generateNoise(x+1, z+1) + generateNoise(x-1, z+1)) /16f;
		float adjacent = (generateNoise(x-1, z) + generateNoise(x, z-1) + generateNoise(x+1, z) + generateNoise(x, z+1)) / 8f;
		return corners + adjacent + (generateNoise(x, z)/4f);
	}
	
	private float interpolateNoise(float x, float z) {
		int intX = (int) x;
		int intZ = (int) z;
		float fracX = x - intX;
		float fracZ = z - intZ;
		
		float v1 = averageNoise(intX, intZ);
		float v2 = averageNoise(intX + 1, intZ);
		float v3 = averageNoise(intX, intZ + 1);
		float v4 = averageNoise(intX + 1, intZ + 1);
		float i1 = interpolate(v1, v2, fracX);
		float i2 = interpolate(v3, v4, fracX);
		return interpolate(i1, i2, fracZ);
	}
	
	
	private float interpolate(float a, float b, float mix) {
		double theta = mix * Math.PI;
		float f = (float) (1f - Math.cos(theta)) * 0.5f;
		return a * (1f - f) + b * f;
	}
	
	private float generateNoise(int x, int z) {
		random.setSeed(313 * x + z * 59267 + seed);
		return random.nextFloat() * 2f - 1f;
	}

}
