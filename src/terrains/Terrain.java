package terrains;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import engine.ModelLoader;
import models.RawModel;
import textures.BlendedTerrainTexture;
import textures.TerrainTexture;
import utils.MathUtils;

public class Terrain {

	private static final float SIZE = 800;
	//private static final int VERTEX_COUNT = 128;
	private static final float MAX_HEIGHT = 30;
	private static final float MAX_COLOR = (float) Math.pow(256, 3);
	
	private float x;
	private float z;
	private RawModel mesh;
	private BlendedTerrainTexture textures;
	private TerrainTexture blendMap;
	
	private float[][] heights;
	
	public Terrain(int x, int z, ModelLoader loader, BlendedTerrainTexture textures,
			TerrainTexture blendMap, String heightMap) {
		this.textures = textures;
		this.blendMap = blendMap;
		this.x = x * SIZE;
		this.z = z * SIZE;
		this.mesh = generateTerrain(loader, heightMap);
	}
	
	
	
	private RawModel generateTerrain(ModelLoader loader, String heightMap){
		
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("res/" + heightMap + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int VERTEX_COUNT = img.getHeight();
		heights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count*2];
		int[] indices = new int[6*(VERTEX_COUNT-1)*(VERTEX_COUNT-1)];
		int vertexPointer = 0;
		for(int i=0;i<VERTEX_COUNT;i++){
			for(int j=0;j<VERTEX_COUNT;j++){
				vertices[vertexPointer*3] = (float)j/((float)VERTEX_COUNT - 1) * SIZE;
				float height = getHeight(j, i, img);
				heights[j][i] = height;
				vertices[vertexPointer*3+1] = height;
				vertices[vertexPointer*3+2] = (float)i/((float)VERTEX_COUNT - 1) * SIZE;
				Vector3f norm = normalize(j, i, img);
				normals[vertexPointer*3] = norm.x;
				normals[vertexPointer*3+1] = norm.y;
				normals[vertexPointer*3+2] = norm.z;
				textureCoords[vertexPointer*2] = (float)j/((float)VERTEX_COUNT - 1);
				textureCoords[vertexPointer*2+1] = (float)i/((float)VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for(int gz=0;gz<VERTEX_COUNT-1;gz++){
			for(int gx=0;gx<VERTEX_COUNT-1;gx++){
				int topLeft = (gz*VERTEX_COUNT)+gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz+1)*VERTEX_COUNT)+gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadInVAO(vertices, textureCoords, normals, indices);
	}

	public float getTerrainHeight(float x, float z) {
		float t_x = x - this.x;
		float t_z = z - this.z;
		float sqSize = SIZE / ((float) heights.length - 1);
		int gridX = (int) Math.floor(t_x / sqSize);
		int gridZ = (int) Math.floor(t_z / sqSize);
		if (gridX+1 >= heights.length || gridZ+1 >= heights.length || gridX < 0 || gridZ < 0) {
			return 0;
		}
		if(gridX >= heights.length - 1 || gridZ >= heights.length - 1 || gridX < 0 || gridZ < 0) {
			System.out.println("0 - Height");
			return 0;
		}
		float xCoord = (t_x % sqSize) / sqSize;
		float zCoord = (t_z % sqSize) / sqSize;
		float location;
		if(xCoord <= (1-zCoord)) {
			location = MathUtils
					.barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ], 0), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		} else {
			location = MathUtils
					.barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
							heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
							heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
		}
		return location;
	}
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getMesh() {
		return mesh;
	}

	public BlendedTerrainTexture getTextures() {
		return textures;
	}

	public TerrainTexture getBlendMap() {
		return blendMap;
	}
	
	private Vector3f normalize(int x, int z, BufferedImage img) {
		float heightL = getHeight(x - 1, z, img);
		float heightR = getHeight(x + 1, z, img);
		float heightD = getHeight(x, z - 1, img);
		float heightU = getHeight(x, z + 1, img);
		Vector3f norm = new Vector3f(heightL-heightR, 2f, heightD-heightU);
		norm.normalise();
		return norm;
	}
	
	private float getHeight(int x, int z, BufferedImage img) {
		if( x < 0 || x >= img.getHeight() || z < 0 || z >= img.getHeight()) {
			return 0;
		}
		float height = img.getRGB(x, z);
		height += MAX_COLOR/2f;
		height /= MAX_COLOR/2f;
		height *= MAX_HEIGHT;
		return height;
	}

}
