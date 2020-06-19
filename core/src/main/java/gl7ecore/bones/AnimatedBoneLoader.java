package gl7ecore.bones;

import gl7ecore.Utils;
import gl7ecore.anim.AnimatedFrame;
import gl7ecore.anim.Animation;
import org.joml.AxisAngle4f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnimatedBoneLoader {

    private static void buildTransFormationMatrices(AINodeAnim aiNodeAnim, Node node) {
        int numFrames = aiNodeAnim.mNumPositionKeys();
        AIVectorKey.Buffer positionKeys = aiNodeAnim.mPositionKeys();
        AIVectorKey.Buffer scalingKeys = aiNodeAnim.mScalingKeys();
        AIQuatKey.Buffer rotationKeys = aiNodeAnim.mRotationKeys();

        for (int i = 0; i < numFrames; i++) {
            AIVectorKey aiVecKey = positionKeys.get(i);
            AIVector3D vec = aiVecKey.mValue();

            Matrix4f transfMat = new Matrix4f().translate(vec.x(), vec.y(), vec.z());

            AIQuatKey quatKey = rotationKeys.get(i);
            AIQuaternion aiQuat = quatKey.mValue();
            Quaternionf quat = new Quaternionf(aiQuat.x(), aiQuat.y(), aiQuat.z(), aiQuat.w());
            transfMat.rotate(quat);

            if (i < aiNodeAnim.mNumScalingKeys()) {
                aiVecKey = scalingKeys.get(i);
                vec = aiVecKey.mValue();
                transfMat.scale(vec.x(), vec.y(), vec.z());
            }

            node.addTransformation(transfMat);
        }
    }

    private static List<AnimatedFrame> buildAnimationFrames(List<Bone> boneList, Node rootNode, Matrix4f rootTransformation) {

        int numFrames = rootNode.getAnimationFrames();
        List<AnimatedFrame> frameList = new ArrayList<>();
        for (int i = 0; i < numFrames; i++) {
            AnimatedFrame frame = new AnimatedFrame();
            frameList.add(frame);

            int numBones = boneList.size();
            for (int j = 0; j < numBones; j++) {
                Bone bone = boneList.get(j);
                Node node = rootNode.findByName(bone.getBoneName());
                Matrix4f boneMatrix = Node.getParentTransforms(node, i);
                boneMatrix.mul(bone.getOffsetMatrix());
                //System.out.println(bone.getOffsetMatrix().toString());
                boneMatrix = new Matrix4f(rootTransformation).mul(boneMatrix);

                frame.setMatrix(j, boneMatrix);
            }
        }

        return frameList;
    }

    public static HashMap<String, Animation> processAnimations(AIScene aiScene, List<Bone> boneList, Node rootNode, Matrix4f rootTransformation) {
        HashMap<String, Animation> animations = new HashMap<>();

        // Process all animations
        int numAnimations = aiScene.mNumAnimations();
        PointerBuffer aiAnimations = aiScene.mAnimations();
        for (int i = 0; i < numAnimations; i++) {
            AIAnimation aiAnimation = AIAnimation.create(aiAnimations.get(i));

            // Calculate transformation matrices for each node
            int numChanels = aiAnimation.mNumChannels();
            PointerBuffer aiChannels = aiAnimation.mChannels();
            for (int j = 0; j < numChanels; j++) {
                AINodeAnim aiNodeAnim = AINodeAnim.create(aiChannels.get(j));
                String nodeName = aiNodeAnim.mNodeName().dataString();
                Node node = rootNode.findByName(nodeName);
                buildTransFormationMatrices(aiNodeAnim, node);
            }

            List<AnimatedFrame> frames = buildAnimationFrames(boneList, rootNode, rootTransformation);
            Animation animation = new Animation(aiAnimation.mName().dataString(), frames, aiAnimation.mDuration());
            animations.put(animation.getName(), animation);
        }
        return animations;
    }

    public static HashMap<String,Bone> processBones(List<AIBone> bones,HashMap<Integer,BoneVertex> weight_map) {
        HashMap<String,Bone> bone_map = new HashMap<String,Bone>();

        for (AIBone bone : bones) {
            String bname=bone.mName().dataString();
            if(!bone_map.containsKey(bname)){
                //System.out.println(Mat2Str(Utils.Aimat2Mat(bone.mOffsetMatrix())));
                Matrix4f off_mat=toMatrix(bone.mOffsetMatrix());
                //off_mat=Change(bname,off_mat);
                //off_mat=off_mat.rotateXYZ((float) -Math.PI,(float) -Math.PI,(float) -Math.PI);
                System.out.println(bname+" -> \n"+off_mat);

                bone_map.put(bname,new Bone(bone_map.size(),bname, off_mat));
            }

            bone.mWeights().forEach((aiVertexWeight -> {
                int vid=aiVertexWeight.mVertexId();
                if(!weight_map.containsKey(vid))
                    weight_map.put(vid,new BoneVertex());
                weight_map.get(vid).addBoneData(bone_map.get(bname).boneId,aiVertexWeight.mWeight());
            }));
        }

        return bone_map;
    }

    public static Node processNodesHierarchy(AINode aiNode, Node parentNode) {
        String nodeName = aiNode.mName().dataString();
        Node node = new Node(nodeName, parentNode);

        int numChildren = aiNode.mNumChildren();
        PointerBuffer aiChildren = aiNode.mChildren();
        for (int i = 0; i < numChildren; i++) {
            AINode aiChildNode = AINode.create(aiChildren.get(i));
            Node childNode = processNodesHierarchy(aiChildNode, node);
            node.addChild(childNode);
        }

        return node;
    }

    public static Matrix4f toMatrix(AIMatrix4x4 aiMatrix4x4) {
        Matrix4f result = new Matrix4f();
        result.m00(aiMatrix4x4.a1());
        result.m10(aiMatrix4x4.a2());
        result.m20(aiMatrix4x4.a3());
        result.m30(aiMatrix4x4.a4());
        result.m01(aiMatrix4x4.b1());
        result.m11(aiMatrix4x4.b2());
        result.m21(aiMatrix4x4.b3());
        result.m31(aiMatrix4x4.b4());
        result.m02(aiMatrix4x4.c1());
        result.m12(aiMatrix4x4.c2());
        result.m22(aiMatrix4x4.c3());
        result.m32(aiMatrix4x4.c4());
        result.m03(aiMatrix4x4.d1());
        result.m13(aiMatrix4x4.d2());
        result.m23(aiMatrix4x4.d3());
        result.m33(aiMatrix4x4.d4());

        return result;
    }

    public static Matrix4f Transpose(Matrix4f mat) {
        Matrix4f result = new Matrix4f();
        result.m00(mat.m00());
        result.m10(mat.m01());
        result.m20(mat.m02());
        //result.m30(mat.m03());
        result.m01(mat.m10());
        result.m11(mat.m11());
        result.m21(mat.m12());
        //result.m31(mat.m13());
        result.m02(mat.m20());
        result.m12(mat.m21());
        result.m22(mat.m22());
        //result.m32(mat.m23());
        /*result.m03(mat.m30());
        result.m13(mat.m31());
        result.m23(mat.m32());
        result.m33(mat.m33());*/

        return result;
    }

    public static Matrix4f Change(String name,Matrix4f mat) {
        Matrix4f result = new Matrix4f();
        /*result.m21(mat.m22());
        result.m22(mat.m21());

        result.m12(mat.m11());
        result.m11(mat.m12());*/
        //result.rotateY(180);

        /*if(name.equals("R_elbow")){
            result.m00(-mat.m00());
        }*/

        Vector3f pos = mat.getTranslation(new Vector3f());
        AxisAngle4f rot = mat.getRotation(new AxisAngle4f());

        System.out.println(pos.toString());
        System.out.println(rot.toString());

        result.translation(pos);
        result.rotate(rot);

        /*if(name.equals("L_foot_01")) {

            //pos.z=-pos.z;
            //pos.x=-pos.x;
            pos.y=0;

            result.translation(pos);
            result.rotate(rot);
        } else {
            return mat;
        }*/

        return result;
    }

}
