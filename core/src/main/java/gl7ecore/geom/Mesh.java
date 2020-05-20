package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;
import gl7ecore.Constant;
import gl7ecore.Screen;
import gl7ecore.Utils;
import gl7ecore.utils.GLHelper;
import gl7ecore.utils.ShaderLodaer;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;

import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.List;

public class Mesh implements IGeom {

    public int draw_type=GL2.GL_LINES;
    public int shader_mode=Constant.SELF_SHADER;
    public int render_type=0; //临时
    //public int ind_group;

    int vtx_len;
    Buffer vtx_buff,col_buff,ind_buff,texuv_buff; //使用自定义Buffer
    Texture texture;

    List<Float> vertexs=new LinkedList<Float>();
    List<Float> colors=new LinkedList<Float>();
    List<Integer> indices=new LinkedList<Integer>();
    List<Float> texuv=new LinkedList<Float>();

    Vec3 position=new Vec3();
    Vec3 rotation=new Vec3();
    Vec3 scale=new Vec3(1,1,1);

    public void build(GL2 gl2) {
        vtx_len=vertexs.size();
        if(!vertexs.isEmpty())
            vtx_buff=setVertexBuffer(gl2, Utils.list2arrf(vertexs));
        if(!colors.isEmpty())
            col_buff=setColorBuffer(gl2, Utils.list2arrf(colors));
        if(!indices.isEmpty())
            ind_buff=setIndicesBuffer(gl2,Utils.list2arri(indices));
        if(!texuv.isEmpty())
            texuv_buff=setTextureBuffer(gl2,Utils.list2arrf(texuv));

        //gl2.glTexCoordPointer(2, GL2.GL_FLOAT, 0, textureCoords);
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
        gl2.glScalef(scale.x, scale.y, scale.z);

        gl2.glUniformMatrix4fv(ShaderLodaer.mv,1,false, GLHelper.getModelViewMatrix(gl2),0);
        gl2.glUniformMatrix4fv(ShaderLodaer.proj,1,false, GLHelper.getProjectionMatrix(gl2),0);
        gl2.glUniform1i(ShaderLodaer.type, render_type);
        draw(gl2);

        gl2.glPopMatrix();
    }

    /*public int selectProg(){
        if(!vertexs.isEmpty()){
            if(!colors.isEmpty() && !texuv.isEmpty()){
                return ShaderLodaer.COLOR_TEX;
            }else if(!colors.isEmpty()){
                return ShaderLodaer.COLOR;
            }else if(!texuv.isEmpty()){
                return ShaderLodaer.TEX;
            }else {
                return ShaderLodaer.RAW;
            }
        }
        return 0;
    }*/

    public int selectProg(GL2 gl2){
        if(!vertexs.isEmpty()){
            gl2.glEnableVertexAttribArray(Constant.vPosition);
            if(!colors.isEmpty() && !texuv.isEmpty()){
                gl2.glEnableVertexAttribArray(Constant.tex_coord);
                gl2.glEnableVertexAttribArray(Constant.aColor);
            }else if(!colors.isEmpty()){
                gl2.glDisableVertexAttribArray(Constant.tex_coord);
                gl2.glEnableVertexAttribArray(Constant.aColor);
            }else if(!texuv.isEmpty()){
                gl2.glDisableVertexAttribArray(Constant.aColor);
                gl2.glEnableVertexAttribArray(Constant.tex_coord);
            }else {
                gl2.glDisableVertexAttribArray(Constant.aColor);
                gl2.glDisableVertexAttribArray(Constant.tex_coord);
            }
        }
        return ShaderLodaer.COLOR_TEX;
    }

    @Override
    public void draw(GL2 gl2){
        //gl2.glBindVertexArray(vtx_id);

        if (shader_mode==Constant.RAW_SHADER){
            gl2.glUseProgram(0);
            gl2.glDisableVertexAttribArray(Constant.aColor);
            gl2.glDisableVertexAttribArray(Constant.tex_coord);
            gl2.glDisableVertexAttribArray(Constant.vPosition);

            if (!vertexs.isEmpty()) {
                gl2.glEnableClientState(GL2.GL_VERTEX_ARRAY);
                gl2.glVertexPointer( 4, GL2.GL_FLOAT, 0, vtx_buff.rewind());
            }

            if (!colors.isEmpty()) {
                gl2.glEnableClientState(GL2.GL_COLOR_ARRAY);
                gl2.glColorPointer(4, GL2.GL_FLOAT, 0, col_buff.rewind());
            }

            if (!texuv.isEmpty()) {
                gl2.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
                texture.enable(gl2);
                texture.bind(gl2);
                gl2.glTexCoordPointer(2, GL2.GL_FLOAT, 0, texuv_buff.rewind());
            }

        }else if(shader_mode==Constant.SELF_SHADER) {
            gl2.glUseProgram(selectProg(gl2));

            if (!vertexs.isEmpty())
                gl2.glVertexAttribPointer(Constant.vPosition, 4, GL2.GL_FLOAT, false, 0, vtx_buff.rewind());

            if (!colors.isEmpty())
                gl2.glVertexAttribPointer(Constant.aColor, 4, GL2.GL_FLOAT, false, 0, col_buff.rewind());
            else
                gl2.glVertexAttrib4f(Constant.aColor,1,1,1,1);  //默认颜色为白色

            if (!texuv.isEmpty()) {
                gl2.glActiveTexture(GL2.GL_TEXTURE0);
                texture.enable(gl2);
                texture.bind(gl2);
                gl2.glUniform1i(ShaderLodaer.tex, 0);
                gl2.glVertexAttribPointer(Constant.tex_coord, 2, GL2.GL_FLOAT, false, 0, texuv_buff.rewind());
            }
        }

        if(!indices.isEmpty()){
            gl2.glDrawElements(draw_type, indices.size(),gl2.GL_UNSIGNED_INT,ind_buff.rewind());
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

    //texture
    public void bindTexture(GL2 gl2,Texture tex){
        gl2.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        tex.enable(gl2);
        texture=tex;
    }

    public Mesh addTex(float u,float v){
        texuv.add(u);
        texuv.add(v);
        return this;
    }

    public Mesh addTex(Vec2 uv){
        texuv.add(uv.x);
        texuv.add(uv.y);
        return this;
    }

    public Mesh addIndices(int... inds){
        for (int ind : inds) {
            indices.add(ind);
        }
        return this;
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

    public void setScale(Vec3 scale) {
        this.scale = scale;
    }
    public void setScale(float x,float y,float z) {
        scale.set(x,y,z);
    }

    public Buffer setVertexBuffer(GL2 gl2, float[] vertexData){
        //绑定顶点
        gl2.glEnableVertexAttribArray(Constant.vPosition);
        return GLBuffers.newDirectFloatBuffer(vertexData);
    }

    public Buffer setColorBuffer(GL2 gl2, float[] colorData){
        //绑定颜色
        gl2.glEnableVertexAttribArray(Constant.aColor);
        return GLBuffers.newDirectFloatBuffer(colorData);
    }

    public Buffer setIndicesBuffer(GL2 gl2,int[] indicesData){
        return GLBuffers.newDirectIntBuffer(indicesData);
    }

    public Buffer setTextureBuffer(GL2 gl2,float[] texData){
        gl2.glEnableVertexAttribArray(Constant.tex_coord);
        return GLBuffers.newDirectFloatBuffer(texData);
    }
}
