package engine;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import animation.Animation;
import animation.JointTransformation;
import animation.KeyFrame;
import animation.Quaternion;
import engine.collada.ColladaLoader;
import engine.collada.data.AnimationData;
import engine.collada.data.JointTransformData;
import engine.collada.data.KeyFrameData;

public class AnimationLoader {
	
	public static Animation loadAnimation(String colladaFile) {
		AnimationData animationData = ColladaLoader.loadColladaAnimation(colladaFile);
		KeyFrame[] frames = new KeyFrame[animationData.keyFrames.length];
		for (int i = 0; i < frames.length; i++) {
			frames[i] = createKeyFrame(animationData.keyFrames[i]);
		}
		return new Animation(animationData.lengthSeconds, frames);
	}

	private static KeyFrame createKeyFrame(KeyFrameData data) {
		Map<String, JointTransformation> map = new HashMap<String, JointTransformation>();
		for (JointTransformData jointData : data.jointTransforms) {
			JointTransformation jointTransform = createTransform(jointData);
			map.put(jointData.jointNameId, jointTransform);
		}
		return new KeyFrame(data.time, map);
	}

	private static JointTransformation createTransform(JointTransformData data) {
		Matrix4f mat = data.jointLocalTransform;
		Vector3f translation = new Vector3f(mat.m30, mat.m31, mat.m32);
		Quaternion rotation = Quaternion.fromMatrix(mat);
		return new JointTransformation(translation, rotation);
	}
	
}
