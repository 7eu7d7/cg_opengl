package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLBuffers;
import gl7ecore.Screen;
import gl7ecore.Utils;
import gl7ecore.utils.GLHelper;
import glm.vec._3.Vec3;

import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

public class Mesh implements IGeom {

    final int vPosition=0;
    final int aColor=1;

    public int draw_type=GL2.GL_LINES;
    //public int ind_group;

    int vtx_id;
    int vtx_len;
    int vtx_buff,ind_buff;


    List<Float> vertexs=new LinkedList<Float>();
    List<Float> colors=new LinkedList<Float>();
    List<Integer> indices=new LinkedList<Integer>();

    Vec3 position=new Vec3();
    Vec3 rotation=new Vec3();

    public void build(GL2 gl2) {
        vtx_id=newVertex(gl2);
        vtx_len=vertexs.size();
        vtx_buff=setVertexBuffer(gl2, Utils.list2arrf(vertexs), Utils.list2arrf(colors));
        if(!indices.isEmpty())
            ind_buff=setIndicesBuffer(gl2,Utils.list2arri(indices));
    }

    @Override
    public void setDrawType(int type) {
        draw_type=type;
    }

    @Override
    public void update(GL2 gl2) {
        gl2.glPushMatrix();
        gl2.glTranslatef(position.x,position.y,position.z);
        gl2.glRotatef(rotation.x,1,0,0);
        gl2.glRotatef(rotation.y,0,1,0);
        gl2.glRotatef(rotation.z,0,0,1);

        gl2.glUniformMatrix4fv(Screen.mv,1,false, GLHelper.getModelViewMatrix(gl2),0);
        gl2.glUniformMatrix4fv(Screen.proj,1,false, GLHelper.getProjectionMatrix(gl2),0);
        draw(gl2);

        gl2.glPopMatrix();
    }

    @Override
    public void draw(GL2 gl2){
        gl2.glBindVertexArray(vtx_id);
        if(!indices.isEmpty()){
            gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, ind_buff);
            gl2.glDrawElements(draw_type, indices.size(),gl2.GL_UNSIGNED_INT,0);
        }else
            gl2.glDrawArrays(draw_type, 0, vtx_len/4) ;
    }

    //vertex builder
    public Mesh addVertex(float x,float y,float z,float w){
        vertexs.add(x);
        vertexs.add(y);
        vertexs.add(z);
        vertexs.add(w);
        return this;
    }

    public Mesh addVertex(float x,float y,float z){
        return addVertex(x,y,z,1);
    }

    public Mesh addVertex(Vec3 pos){
        return addVertex(pos.x,pos.y,pos.z);
    }

    public void clear(){
        vertexs.clear();
    }

    //color builder
    public Mesh addColor(int col){
        return addColor(((col >> 16) & 0xFF)/255f, ((col >> 8) & 0xFF)/255f, (col & 0xFF)/255f, (col >> 24 & 0xFF)/255f);
    }

    public Mesh addColor(float r,float g,float b,float a){
        colors.add(r);
        colors.add(g);
        colors.add(b);
        colors.add(a);
        return this;
    }


    public int newVertex(GL2 gl2){
        IntBuffer id= GLBuffers.newDirectIntBuffer(1);
        gl2.glGenVertexArrays(1, id);
        gl2.glBindVertexArray(id.get(0));
        return id.get(0);
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public void setPosition(float x,float y,float z) {
        position.set(x,y,z);
    }

    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }

    public void setRotation(float x,float y,float z) {
        rotation.set(x,y,z);
    }

    /*public int setVertexBuffer(GL2 gl2,float[] vertexData,float[] colorData){
        IntBuffer id=GLBuffers.newDirectIntBuffer(1);//gl7ecore.BufferUtil.getIntBuffer(1);
        gl2.glGenBuffers(1, id);
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, id.get(0));
        if (colorData.length>0){
            float[] vc=Utils.mix_arr(vertexData,4,colorData,4);
            gl2.glBufferData(GL2.GL_ARRAY_BUFFER, vc.length*4, GLBuffers.newDirectFloatBuffer(vc), GL2.GL_STATIC_DRAW);

            //绑定顶点
            gl2.glEnableVertexAttribArray(vPosition);
            gl2.glVertexAttribPointer(vPosition, 4, GL2.GL_FLOAT, false, 8*4, 0);

            //绑定颜色
            gl2.glEnableVertexAttribArray(aColor);
            gl2.glVertexAttribPointer(aColor, 4, GL2.GL_FLOAT, false, 8*4, 4*4);
        }else {
            gl2.glBufferData(GL2.GL_ARRAY_BUFFER, vertexData.length * 4, GLBuffers.newDirectFloatBuffer(vertexData), GL2.GL_STATIC_DRAW);

            //绑定顶点
            gl2.glEnableVertexAttribArray(vPosition);
            gl2.glVertexAttribPointer(vPosition, 4, GL2.GL_FLOAT, false, 0, 0);
        }


        return id.get(0);
    }*/

    public int setVertexBuffer(GL2 gl2,float[] vertexData,float[] colorData){
        IntBuffer id=GLBuffers.newDirectIntBuffer(1);//gl7ecore.BufferUtil.getIntBuffer(1);
        gl2.glGenBuffers(1, id);
        gl2.glBindBuffer(GL2.GL_ARRAY_BUFFER, id.get(0));
        if (colorData.length>0){
            gl2.glBufferData(GL2.GL_ARRAY_BUFFER, vertexData.length*4+colorData.length*4, null, GL2.GL_STATIC_DRAW);
            gl2.glBufferSubData(GL2.GL_ARRAY_BUFFER,0,vertexData.length*4,GLBuffers.newDirectFloatBuffer(vertexData));
            gl2.glBufferSubData(GL2.GL_ARRAY_BUFFER,vertexData.length*4,colorData.length*4,GLBuffers.newDirectFloatBuffer(colorData));
        }else
            gl2.glBufferData(GL2.GL_ARRAY_BUFFER, vertexData.length*4, GLBuffers.newDirectFloatBuffer(vertexData), GL2.GL_STATIC_DRAW);

        //绑定顶点
        gl2.glEnableVertexAttribArray(vPosition);
        gl2.glVertexAttribPointer(vPosition, 4, GL2.GL_FLOAT, false, 0, 0);


        if(colorData.length>0){
            //绑定颜色
            gl2.glEnableVertexAttribArray(aColor);
            gl2.glVertexAttribPointer(aColor, 4, GL2.GL_FLOAT, false, 0, vertexData.length*4);
        }

        return id.get(0);
    }

    public int setIndicesBuffer(GL2 gl2,int[] indicesData){
        IntBuffer id=GLBuffers.newDirectIntBuffer(1);//gl7ecore.BufferUtil.getIntBuffer(1);
        gl2.glGenBuffers(1, id);
        gl2.glBindBuffer(GL2.GL_ELEMENT_ARRAY_BUFFER, id.get(0));
        gl2.glBufferData(GL2.GL_ELEMENT_ARRAY_BUFFER, indicesData.length*4, GLBuffers.newDirectIntBuffer(indicesData), GL2.GL_STATIC_DRAW);

        return id.get(0);
    }
}
