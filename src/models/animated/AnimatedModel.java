package models.animated;

import org.lwjgl.util.vector.Matrix4f;
import org.newdawn.slick.opengl.Texture;

import animation.Animation;
import animation.Animator;
import models.RawModel;

public class AnimatedModel {

	private final RawModel model;
	private final Texture texture;
	private final Joint root;
	private final int jointNumber;
	private final Animator animator;
	
	public AnimatedModel(RawModel model, Texture texture, Joint rootJoint, int jointCount) {
		this.model = model;
		this.texture = texture;
		this.root = rootJoint;
		this.jointNumber = jointCount;
		this.animator = new Animator(this);
		rootJoint.calcInverseBindTransform(new Matrix4f());
	}

	public RawModel getModel() {
		return model;
	}

	public Texture getTexture() {
		return texture;
	}

	public Joint getRoot() {
		return root;
	}

	public void delete() {
		//model.unload();
	}

	public void doAnimation(Animation animation) {
		animator.doAnimation(animation);
	}

	public void update() {
		animator.update();
	}

	public Matrix4f[] getJointTransforms() {
		Matrix4f[] jointMatrices = new Matrix4f[jointNumber];
		addJointsToArray(root, jointMatrices);
		return jointMatrices;
	}

	private void addJointsToArray(Joint headJoint, Matrix4f[] jointMatrices) {
		jointMatrices[headJoint.id] = headJoint.getTransform();
		for (Joint childJoint : headJoint.children) {
			addJointsToArray(childJoint, jointMatrices);
		}
	}
	
}
