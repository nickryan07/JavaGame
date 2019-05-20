package models.animated;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

public class Joint {
	
	public final int id;
	public final String name;
	public final List<Joint> children = new ArrayList<Joint>();
	
	private Matrix4f transform = new Matrix4f();
	private final Matrix4f localBindTransform;
	private Matrix4f inverseBindTransform = new Matrix4f();
	
	public Joint(int id, String name, Matrix4f localBindTransform) {
		this.id = id;
		this.name = name;
		this.localBindTransform = localBindTransform;
	}
	
	public void addChild(Joint child) {
		this.children.add(child);
	}

	public Matrix4f getTransform() {
		return transform;
	}
	
	public void setTransform(Matrix4f transformation) {
		this.transform = transformation;
	}
	
	public Matrix4f getInverseBindTransform() {
		return inverseBindTransform;
	}

	protected void calcInverseBindTransform(Matrix4f parentBindTransform) {
		Matrix4f bindTransform = Matrix4f.mul(parentBindTransform, localBindTransform, null);
		Matrix4f.invert(bindTransform, inverseBindTransform);
		for (Joint child : children) {
			child.calcInverseBindTransform(bindTransform);
		}
	}
	
}
