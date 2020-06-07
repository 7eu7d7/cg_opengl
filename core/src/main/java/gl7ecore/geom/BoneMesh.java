package gl7ecore.geom;

import assimp.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLBuffers;
import gl7ecore.Constant;
import gl7ecore.Utils;
import gl7ecore.utils.ShaderLodaer;
import glm_.mat4x4.Mat4;
import glm_.quat.Quat;
import glm_.vec3.Vec3;

import java.nio.Buffer;
import java.util.*;

public class BoneMesh extends Mesh{

    HashMap<String,Bone> bone_map=new HashMap<String,Bone>();
    LinkedList<String> bone_name_list=new LinkedList<String>();
    HashMap<Integer,BoneVtx> bone_data=new HashMap<Integer,BoneVtx>();
    List<AiAnimation> animations;

    AiNode rootNode;
    Mat4 ginv_trans;

    double time;

    public BoneMesh(AiNode rootNode){
        this.rootNode=rootNode;
        ginv_trans=rootNode.getTransformation().inverse();
        System.out.println(Mat2Str(ginv_trans));
    }

    @Override
    public void update(GL2 gl2) {
        time+= Constant.TIME_PER_TICK;
        super.update(gl2);
    }

    @Override
    public int selectProg(GL2 gl2) {
        super.selectProg(gl2);
        return ShaderLodaer.COLOR_TEX_BONE;
    }

    @Override
    public void sendShaderDatas(GL2 gl2) {
        if(animations.size()<=0)
            return;

        gl2.glEnableVertexAttribArray(Constant.bones);
        gl2.glEnableVertexAttribArray(Constant.weights);

        LinkedList<Bone> bones=getBoneTransform(time);
        for (Bone bone : bones) {
            /*for (float v : bone.transform.getArray()) {
                System.out.print(v+" ");
            }
            System.out.println();*/
            //System.out.println(bone.bid);
            gl2.glUniformMatrix4fv(gl2.glGetUniformLocation(prog_id,"gBones["+bone.bid+"]"),1,true, bone.transform.toFloatBuffer());
        }

        LinkedList<Float> bone_ids=new LinkedList<Float>();
        LinkedList<Float> weights=new LinkedList<Float>();

        //System.out.println(vertexs.size()+","+bone_data.size());
        for (int i = 0; i < bone_data.size(); i++) {
            BoneVtx bv=bone_data.get(i);

            for (String s : bv.bname) {
                bone_ids.offer((float) bone_map.get(s).bid);
            }

            weights.addAll(bv.weight);

            for(int u=0;u<4-bv.bname.size();u++) {
                bone_ids.add(0f);
                weights.add(0f);
            }

        }
        //System.out.println(bone_ids.size()+","+weights.size()+","+vertexs.size()+","+bone_data.size());

        Buffer bone_buffer=setBonesBuffer(gl2, Utils.list2arrf(bone_ids));
        Buffer weights_buffer=setWeightsBuffer(gl2, Utils.list2arrf(weights));

        gl2.glVertexAttribPointer(Constant.bones, 4, GL2.GL_FLOAT, false, 0, bone_buffer.rewind());
        gl2.glVertexAttribPointer(Constant.weights, 4, GL2.GL_FLOAT, false, 0, weights_buffer.rewind());
    }

    public Buffer setBonesBuffer(GL2 gl2, float[] idsData){
        //绑定顶点
        gl2.glEnableVertexAttribArray(Constant.bones);
        return GLBuffers.newDirectFloatBuffer(idsData);
    }

    public Buffer setWeightsBuffer(GL2 gl2, float[] wData){
        //绑定顶点
        gl2.glEnableVertexAttribArray(Constant.weights);
        return GLBuffers.newDirectFloatBuffer(wData);
    }

    public void setAnimations(List<AiAnimation> animations) {
        this.animations = animations;
    }

    public void addBones(List<AiBone> bones){
        bone_map.clear();
        bone_data.clear();

        for (AiBone bone : bones) {
            String bname=bone.getName();

            if(!bone_map.containsKey(bname)){
                System.out.println(Mat2Str(bone.getOffsetMatrix()));
                bone_map.put(bname,new Bone(bone_map.size(),bone.getOffsetMatrix().inverse()));
                bone_name_list.offer(bname);
            }

            bone.getWeights().forEach((aiVertexWeight -> {
                int vid=aiVertexWeight.getVertexId();
                if(!bone_data.containsKey(vid))
                    bone_data.put(vid,new BoneVtx());
                bone_data.get(vid).addBoneData(bname,aiVertexWeight.getWeight());
            }));
        }

    }

    //Animation
    public LinkedList<Bone> getBoneTransform(double TimeInSeconds){
        AiAnimation animation=animations.get(0);
        double TicksPerSecond = animation.getTicksPerSecond() != 0 ? animation.getTicksPerSecond() : 25;
        double TimeInTicks = TimeInSeconds * TicksPerSecond;
        double atime = TimeInTicks % animation.getDuration(); //TODO fmod是否可以与%代替
        //System.out.println(animation.getDuration());

        Mat4 mat=new Mat4();
        mat.identity();
        readNodeHeirarchy(atime,rootNode,mat);

        LinkedList<Bone> res=new LinkedList<Bone>();
        /*bone_name_list.forEach((name)->{
            res.offer(bone_map.get(name));
        });*/
        bone_map.forEach((k,v)->{ res.offer(v); });

        res.sort(Comparator.comparingInt(x -> x.bid));

        return res;
    }

    public void readNodeHeirarchy(double atime, AiNode node,Mat4 parentTransform){
        String nodeName=node.getName();
        AiAnimation animation=animations.get(0);
        Mat4 n_trans=node.getTransformation();
        AiNodeAnim nodeAnim = findNodeAnim(animation, nodeName);

        if(nodeAnim!=null){
            Mat4 mat_scaling=new Mat4();
            mat_scaling.identity();
            mat_scaling.scaleAssign(calcInterpolatedScaling(atime,nodeAnim));

            Mat4 mat_rotate=calcInterpolatedRotation(atime,nodeAnim).toMat4();
            /*Mat4 mat_rotate=new Mat4();
            mat_rotate.identity();
            /*Vec3 angs=calcInterpolatedRotation(atime,nodeAnim).eulerAngles();

            System.out.println(arr2Str(angs.getArray()));
            mat_rotate.rotateXYZassign(angs);*/
            //System.out.println(nodeAnim.getNodeName()+" -> "+angs.getX()+","+angs.getY()+","+angs.getZ());
            /*if(nodeName.equals("Bip001_R_Thigh") || nodeName.equals("Bip001_R_Calf")){
                mat_rotate.rotateXassign((float) (Math.PI/4));
            }*/

            Mat4 mat_pos=new Mat4();
            mat_pos.identity();
            mat_pos.translateAssign(calcInterpolatedPosition(atime,nodeAnim));

            //System.out.println(Mat2Str(mat_scaling));

            mat_pos.identity();
            //mat_scaling.identity();
            //System.out.println(Mat2Str(mat_rotate));

            n_trans = matMul(matMul(mat_pos,mat_rotate),mat_scaling);
            //n_trans = matMul(mat_scaling,matMul(mat_rotate,mat_pos));
        }

        //Mat4 g_trans=parentTransform.times(n_trans);
        System.out.println(nodeName+" -> "+Mat2Str(parentTransform));
        Mat4 g_trans=matMul(parentTransform,n_trans);
        if(bone_map.containsKey(nodeName)){
            //g_trans.identity();
            bone_map.get(nodeName).transform=matMul(ginv_trans,g_trans);//matMul(matMul(ginv_trans,g_trans),bone_map.get(nodeName).boneOffset);
                    //ginv_trans.times(g_trans).times(bone_map.get(nodeName).boneOffset);
        }

        node.getChildren().forEach((cnode)->{
            readNodeHeirarchy(atime,cnode,g_trans);
        });
    }

    public Mat4 matMul(Mat4 a,Mat4 b){
        return b.times(a);
    }

    public String Mat2Str(Mat4 mat){
        return arr2Str(mat.getArray());
    }

    public String arr2Str(float[] arr){
        String str="";
        for (float v : arr) {
            str+=(v+" ");
        }
        return str;
    }

    public Vec3 calcInterpolatedScaling(double atime, AiNodeAnim nodeAnim){
        if (nodeAnim.getNumScalingKeys() == 1) {
            return nodeAnim.getScalingKeys().get(0).getValue();
        }

        ArrayList<AiVectorKey> scalingKeys = nodeAnim.getScalingKeys();

        for (int i = 0 ; i < nodeAnim.getNumScalingKeys() - 1 ; i++) {
            if (atime < scalingKeys.get(i+1).getTime()) {
                double deltaTime = scalingKeys.get(i+1).getTime() - scalingKeys.get(i).getTime();
                double factor = (atime - scalingKeys.get(i).getTime()) / deltaTime;
                assert(factor >= 0.0f && factor <= 1.0f);
                Vec3 start = scalingKeys.get(i).getValue();
                Vec3 end   = scalingKeys.get(i+1).getValue();
                Vec3 delta = end.minus(start);
                return start.plus(delta.times(factor));
            }
        }

        return null;
    }

    public Vec3 calcInterpolatedPosition(double atime, AiNodeAnim nodeAnim){
        if (nodeAnim.getNumPositionKeys() == 1) {
            return nodeAnim.getPositionKeys().get(0).getValue();
        }

        ArrayList<AiVectorKey> posKeys = nodeAnim.getPositionKeys();

        for (int i = 0 ; i < nodeAnim.getNumPositionKeys() - 1 ; i++) {
            if (atime < posKeys.get(i+1).getTime()) {
                double deltaTime = posKeys.get(i+1).getTime() - posKeys.get(i).getTime();
                double factor = (atime - posKeys.get(i).getTime()) / deltaTime;
                assert(factor >= 0.0f && factor <= 1.0f);
                Vec3 start = posKeys.get(i).getValue();
                Vec3 end   = posKeys.get(i+1).getValue();
                Vec3 delta = end.minus(start);
                //System.out.println(arr2Str(delta.times(factor).getArray()));
                return start.plus(delta.times(factor)).times(0.0254);
            }
        }

        return null;
    }

    public Quat calcInterpolatedRotation(double atime, AiNodeAnim nodeAnim){
        if (nodeAnim.getNumRotationKeys() == 1) {
            return nodeAnim.getRotationKeys().get(0).getValue();
        }

        ArrayList<AiQuatKey> ai_keys = nodeAnim.getRotationKeys();

        for (int i = 0 ; i < nodeAnim.getNumRotationKeys() - 1 ; i++) {
            if (atime < ai_keys.get(i+1).getTime()) {
                double deltaTime = ai_keys.get(i+1).getTime() - ai_keys.get(i).getTime();
                double factor = (atime - ai_keys.get(i).getTime()) / deltaTime;
                assert(factor >= 0.0f && factor <= 1.0f);
                Quat start = ai_keys.get(i).getValue();
                Quat end   = ai_keys.get(i+1).getValue();
                Quat delta = end.minus(start);
                //System.out.println(delta);
                return delta;
                //return start.plus(delta.times((float) factor)).normalize();
            }
        }

        return null;
    }

    private AiNodeAnim findNodeAnim(AiAnimation animation, String nodeName)
    {
        for (AiNodeAnim aiNodeAnim : animation.getChannels()) {
            if(aiNodeAnim.getNodeName().equals(nodeName))
                return aiNodeAnim;
        }
        return null;
    }

    class Bone{
        int bid;
        Mat4 boneOffset,transform;
        public Bone(int bid,Mat4 boneOffset){
            this.bid=bid;
            this.boneOffset=boneOffset;
        }

    }

    class BoneVtx{
        //一个顶点所受的骨骼影响集合
        LinkedList<String> bname=new LinkedList<String>();
        LinkedList<Float> weight=new LinkedList<Float>();

        public void addBoneData(String bname, float weight)
        {
            if(this.bname.size()>=4)
                return;
            this.bname.offer(bname);
            this.weight.offer(weight);
        }

    }

}
