package gl7ecore.bones;

import org.joml.Matrix4f;

public class Bone {
    public final int boneId;
    public final String boneName;
    public Matrix4f offsetMatrix;
    public Matrix4f transformMatrix;


    public Bone(int boneId, String boneName, Matrix4f offsetMatrix) {
        this.boneId = boneId;
        this.boneName = boneName;
        this.offsetMatrix = offsetMatrix;
    }

    public int getBoneId() {
        return boneId;
    }

    public String getBoneName() {
        return boneName;
    }

    public Matrix4f getOffsetMatrix() {
        return offsetMatrix;
    }

    public void setTransformMatrix(Matrix4f transformMatrix) {
        this.transformMatrix = transformMatrix;
    }
}
