package animation;

import java.util.Map;

public class KeyFrame {
	private final float timeStamp;
	private final Map<String, JointTransformation> pose;

	public KeyFrame(float timeStamp, Map<String, JointTransformation> jointKeyFrames) {
		this.timeStamp = timeStamp;
		this.pose = jointKeyFrames;
	}

	protected float getTimeStamp() {
		return timeStamp;
	}
	protected Map<String, JointTransformation> getJointKeyFrames() {
		return pose;
	}

}
