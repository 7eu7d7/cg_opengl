package gl7ecore.geom;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;
import gl7ecore.Constant;
import gl7ecore.Utils;
import gl7ecore.light.Light;
import gl7ecore.light.Material;
import gl7ecore.utils.GLHelper;
import gl7ecore.utils.ShaderLodaer;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;

import java.nio.Buffer;
import java.util.LinkedList;
import java.util.List;

public class Mesh extends IGeom {

    public int shader_mode=Constant.SELF_SHADER;
    public int render_type=0; //渲染方式
    //public int ind_group;

    public int prog_id;
    public int prog_id_force=-1;
    public ShaderDataSender sdser;

    int vtx_len;
    Buffer vtx_buff,col_buff,nor_buff,ind_buff,texuv_buff; //使用自定义Buffer
    Texture texture;

    List<Float> vertexs=new LinkedList<Float>();
    List<Float> normals=new LinkedList<Float>();
    List<Float> colors=new LinkedList<Float>();
    List<Integer> indices=new LinkedList<Integer>();
    List<Float> texuv=new LinkedList<Float>();

    public void build(GL2 gl2) {
        vtx_len=vertexs.size();
        if(!vertexs.isEmpty())
            vtx_buff=setVertexBuffer(gl2, Utils.list2arrf(vertexs));
        if(!normals.isEmpty())
            nor_buff=setNormalBuffer(gl2, Utils.list2arrf(normals));
        if(!colors.isEmpty())
            col_buff=setColorBuffer(gl2, Utils.list2arrf(colors));
        if(!indices.isEmpty())
            ind_buff=setIndicesBuffer(gl2,Utils.list2arri(indices));
        if(!texuv.isEmpty())
            texuv_buff=setTextureBuffer(gl2,Utils.list2arrf(texuv));

        //gl2.glTexCoordPointer(2, GL2.GL_FLOAT, 0, textureCoords);
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
                gl2.glEnableVertexAttribArray(Constant.vColor);
            }else if(!colors.isEmpty()){
                gl2.glDisableVertexAttribArray(Constant.tex_coord);
                gl2.glEnableVertexAttribArray(Constant.vColor);
            }else if(!texuv.isEmpty()){
                gl2.glDisableVertexAttribArray(Constant.vColor);
                gl2.glEnableVertexAttribArray(Constant.tex_coord);
            }else {
                gl2.glDisableVertexAttribArray(Constant.vColor);
                gl2.glDisableVertexAttribArray(Constant.tex_coord);
            }
        }
        return prog_id_force==-1?ShaderLodaer.COLOR_TEX:prog_id_force;
    }

    @Override
    public void draw(GL2 gl2){
        //gl2.glBindVertexArray(vtx_id);

        if (shader_mode==Constant.RAW_SHADER){
            gl2.glUseProgram(0);
            gl2.glDisableVertexAttribArray(Constant.vColor);
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
            prog_id = selectProg(gl2);
            gl2.glUseProgram(prog_id);

            gl2.glUniformMatrix4fv(gl2.glGetUniformLocation(prog_id,"mv"),1,false, GLHelper.getModelViewMatrix(gl2),0);
            gl2.glUniformMatrix4fv(gl2.glGetUniformLocation(prog_id,"proj"),1,false, GLHelper.getProjectionMatrix(gl2),0);
            gl2.glUniform1i(gl2.glGetUniformLocation(prog_id,"type"), render_type);

            if (!vertexs.isEmpty())
                gl2.glVertexAttribPointer(Constant.vPosition, 4, GL2.GL_FLOAT, false, 0, vtx_buff.rewind());

            if (!colors.isEmpty())
                gl2.glVertexAttribPointer(Constant.vColor, 4, GL2.GL_FLOAT, false, 0, col_buff.rewind());
            else
                gl2.glVertexAttrib4f(Constant.vColor,1,1,1,1);  //默认颜色为白色

            if (!normals.isEmpty()) {
                gl2.glVertexAttribPointer(Constant.vNormal, 3, GL2.GL_FLOAT, false, 0, nor_buff.rewind());
            }

            if (material!=null && (prog_id==ShaderLodaer.COLOR_TEX || prog_id==ShaderLodaer.COLOR_TEX_BONE)) {
                material.send(gl2, prog_id);
                for (Light light : Constant.light_list) {
                    if(light!=null){
                        light.send(gl2,prog_id);
                    }
                }
            }

            if (!texuv.isEmpty()) {
                gl2.glActiveTexture(GL2.GL_TEXTURE0);
                texture.enable(gl2);
                texture.bind(gl2);
                gl2.glUniform1i(gl2.glGetUniformLocation(prog_id,"tex"), 0);
                gl2.glVertexAttribPointer(Constant.tex_coord, 2, GL2.GL_FLOAT, false, 0, texuv_buff.rewind());
            }

            sendShaderDatas(gl2); //发送额外的数据

        }

        if(!indices.isEmpty()){
            gl2.glDrawElements(draw_type, indices.size(),gl2.GL_UNSIGNED_INT,ind_buff.rewind());
        }else
            gl2.glDrawArrays(draw_type, 0, vtx_len/4) ;

    }

    public void sendShaderDatas(GL2 gl2){
        if(sdser!=null)
            sdser.onDataSend(prog_id);
    }

    public void setShaderDataSender(ShaderDataSender sdser) {
        this.sdser = sdser;
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

    public Mesh addNormal(float x,float y,float z){
        normals.add(x);
        normals.add(y);
        normals.add(z);
        return this;
    }

    public Mesh addNormal(Vec3 vec){
        return addNormal(vec.x,vec.y,vec.z);
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

    public void setMaterial(Material material) {
        super.setMaterial(material);
        render_type=1;
    }

    public Buffer setVertexBuffer(GL2 gl2, float[] vertexData){
        //绑定顶点
        gl2.glEnableVertexAttribArray(Constant.vPosition);
        return GLBuffers.newDirectFloatBuffer(vertexData);
    }

    public Buffer setNormalBuffer(GL2 gl2, float[] normalData){
        //绑定法线
        gl2.glEnableVertexAttribArray(Constant.vNormal);
        return GLBuffers.newDirectFloatBuffer(normalData);
    }

    public Buffer setColorBuffer(GL2 gl2, float[] colorData){
        //绑定颜色
        gl2.glEnableVertexAttribArray(Constant.vColor);
        return GLBuffers.newDirectFloatBuffer(colorData);
    }

    public Buffer setIndicesBuffer(GL2 gl2,int[] indicesData){
        return GLBuffers.newDirectIntBuffer(indicesData);
    }

    public Buffer setTextureBuffer(GL2 gl2,float[] texData){
        gl2.glEnableVertexAttribArray(Constant.tex_coord);
        return GLBuffers.newDirectFloatBuffer(texData);
    }

    public interface ShaderDataSender{
        void onDataSend(int prog_id);
    }
}
