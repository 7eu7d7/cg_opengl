package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLBuffers;
import gl7ecore.Constant;
import gl7ecore.Utils;
import gl7ecore.anim.AnimatedFrame;
import gl7ecore.anim.Animation;
import gl7ecore.bones.Bone;
import gl7ecore.bones.BoneVertex;
import gl7ecore.utils.ShaderLodaer;
import glm.mat._4.Mat4;
import glm.quat.Quat;
import glm.vec._3.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.nio.Buffer;
import java.util.*;

public class BoneMesh extends Mesh{

    List<BoneVertex> vtx_weights;
    HashMap<String, Animation> anim_map;
    Animation anim_now;

    double time;

    Buffer bone_buffer,weights_buffer;

    public BoneMesh(){
        //System.out.println(Mat2Str(ginv_trans));
    }

    @Override
    public void build(GL2 gl2) {
        super.build(gl2);

        long st=System.currentTimeMillis();

        LinkedList<Float> bone_ids=new LinkedList<Float>();
        LinkedList<Float> weights=new LinkedList<Float>();

        //System.out.println(vertexs.size()+","+bone_data.size());
        for (int i = 0; i < vtx_weights.size(); i++) {
            BoneVertex bv=vtx_weights.get(i);

            bv.bid.forEach((bid)->{
                bone_ids.offer((float)bid);
            });

            weights.addAll(bv.weight);

            for(int u=0;u<4-bv.bid.size();u++) {
                bone_ids.add(0f);
                weights.add(0f);
            }

        }

        System.out.println(System.currentTimeMillis()-st);

        bone_buffer=setBonesBuffer(gl2, Utils.list2arrf(bone_ids));
        weights_buffer=setWeightsBuffer(gl2, Utils.list2arrf(weights));
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
        if(anim_now==null)
            return;

        gl2.glEnableVertexAttribArray(Constant.bones);
        gl2.glEnableVertexAttribArray(Constant.weights);

        AnimatedFrame frame=anim_now.getCurrentFrame();
        anim_now.nextFrame();

        int count=0;
        for (Matrix4f mat_bone : frame.getJointMatrices()) {
            gl2.glUniformMatrix4fv(gl2.glGetUniformLocation(prog_id,"gBones["+count+"]"),1,false, mat_bone.get(GLBuffers.newDirectFloatBuffer(new float[16])));
            count++;
        }


        //System.out.println(bone_ids.size()+","+weights.size()+","+vertexs.size()+","+bone_data.size());

        gl2.glVertexAttribPointer(Constant.bones, 4, GL2.GL_FLOAT, false, 0, bone_buffer.rewind());
        gl2.glVertexAttribPointer(Constant.weights, 4, GL2.GL_FLOAT, false, 0, weights_buffer.rewind());
        //System.out.println(time);
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

    public void setAnimations(HashMap<String, Animation> anim_map) {
        this.anim_map = anim_map;
    }

    public void selectAnimation(String name){
        anim_now=anim_map.get(name);
    }

    public void selectAnimation(int which){
        anim_now=new ArrayList<Animation>(anim_map.values()).get(which);
    }

    public void setVtxWeights(List<BoneVertex> vtx_weights) {
        this.vtx_weights = vtx_weights;
    }
}
