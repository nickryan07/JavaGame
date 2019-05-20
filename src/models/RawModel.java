package models;

public class RawModel {

	private int vaoId;
	private int vertexCount;
	
	public RawModel(int vaoId, int vertexCount) {
		this.vaoId = vaoId;
		this.vertexCount = vertexCount;
	}
	
//	public RawModel(int vaoId, int vertexCount, int indexCount) {
//		this.vaoId = vaoId;
//		this.vertexCount = vertexCount;
//		this.indexCount = indexCount;
//	}

	public int getVaoId() {
		return vaoId;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
}
