package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import gl7ecore.Constant;
import gl7ecore.Utils;
import gl7ecore.utils.ShaderLodaer;

import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.util.LinkedList;
import java.util.List;

public class SkyBox extends Mesh{

    Texture sky_tex=new Texture(GL2.GL_TEXTURE_CUBE_MAP);
    List<Float> tex3d_uvr=new LinkedList<Float>();
    Buffer tex3d_buff;

    @Override
    public void build(GL2 gl2) {
        setDrawType(GL2.GL_QUADS);

        //bottom
        /*addTex3D(-1,-1,-1).addVertex(-1,-1,-1);
        addTex3D(1,-1,-1).addVertex(1,-1,-1);
        addTex3D(1,1,-1).addVertex(1,1,-1);
        addTex3D(-1,1,-1).addVertex(-1,1,-1);*/
        addTex3D(1,1,-1).addVertex(-1,-1,-1);
        addTex3D(-1,1,-1).addVertex(1,-1,-1);
        addTex3D(-1,-1,-1).addVertex(1,1,-1);
        addTex3D(1,-1,-1).addVertex(-1,1,-1);

        //up
        addTex3D(-1,-1,1).addVertex(-1,-1,1);
        addTex3D(-1,1,1).addVertex(-1,1,1);
        addTex3D(1,1,1).addVertex(1,1,1);
        addTex3D(1,-1,1).addVertex(1,-1,1);

        //left
        /*addTex3D(-1,-1,-1).addVertex(-1,-1,-1);
        addTex3D(-1,1,-1).addVertex(-1,1,-1);
        addTex3D(-1,1,1).addVertex(-1,1,1);
        addTex3D(-1,-1,1).addVertex(-1,-1,1);*/
        addTex3D(-1,1,-1).addVertex(-1,-1,-1);
        addTex3D(-1,1,1).addVertex(-1,1,-1);
        addTex3D(-1,-1,1).addVertex(-1,1,1);
        addTex3D(-1,-1,-1).addVertex(-1,-1,1);

        //right
        /*addTex3D(1,-1,-1).addVertex(1,-1,-1);
        addTex3D(1,-1,1).addVertex(1,-1,1);
        addTex3D(1,1,1).addVertex(1,1,1);
        addTex3D(1,1,-1).addVertex(1,1,-1);*/
        addTex3D(1,1,-1).addVertex(1,-1,-1);
        addTex3D(1,-1,-1).addVertex(1,-1,1);
        addTex3D(1,-1,1).addVertex(1,1,1);
        addTex3D(1,1,1).addVertex(1,1,-1);

        //back
        addTex3D(-1,1,-1).addVertex(-1,1,-1);
        addTex3D(1,1,-1).addVertex(1,1,-1);
        addTex3D(1,1,1).addVertex(1,1,1);
        addTex3D(-1,1,1).addVertex(-1,1,1);

        //front
        /*addTex3D(-1,-1,-1).addVertex(-1,-1,-1);
        addTex3D(-1,-1,1).addVertex(-1,-1,1);
        addTex3D(1,-1,1).addVertex(1,-1,1);
        addTex3D(1,-1,-1).addVertex(1,-1,-1);*/
        addTex3D(1,-1,1).addVertex(-1,-1,-1);
        addTex3D(1,-1,-1).addVertex(-1,-1,1);
        addTex3D(-1,-1,-1).addVertex(1,-1,1);
        addTex3D(-1,-1,1).addVertex(1,-1,-1);

        super.build(gl2);

        if(!tex3d_uvr.isEmpty())
            tex3d_buff=setTextureBuffer(gl2, Utils.list2arrf(tex3d_uvr));

        /*gl2.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_S, GL2.GL_CLAMP_TO_EDGE);
        gl2.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_T, GL2.GL_CLAMP_TO_EDGE);
        gl2.glTexParameteri(GL2.GL_TEXTURE_CUBE_MAP, GL2.GL_TEXTURE_WRAP_R, GL2.GL_CLAMP_TO_EDGE);*/
    }

    @Override
    public int selectProg(GL2 gl2) {
        gl2.glEnableVertexAttribArray(Constant.vPosition);
        gl2.glEnableVertexAttribArray(Constant.tex3d_coord);
        return ShaderLodaer.SKY_BOX;
    }

    @Override
    public void sendShaderDatas(GL2 gl2) {
        gl2.glActiveTexture(GL2.GL_TEXTURE0);
        sky_tex.enable(gl2);
        sky_tex.bind(gl2);
        gl2.glUniform1i(gl2.glGetUniformLocation(prog_id,"tex"), 0);
        gl2.glVertexAttribPointer(Constant.tex3d_coord, 3, GL2.GL_FLOAT, false, 0, tex3d_buff.rewind());
    }

    @Override
    public void draw(GL2 gl2) {
        gl2.glEnable(GL2.GL_CULL_FACE);
        gl2.glDepthFunc(GL2.GL_LEQUAL);

        gl2.glCullFace(GL2.GL_BACK);

        super.draw(gl2);

        gl2.glDepthFunc(GL2.GL_LESS);
        gl2.glDisable(GL2.GL_CULL_FACE);
        gl2.glDisableVertexAttribArray(Constant.tex3d_coord);
    }

    public SkyBox addTex3D(float u,float v,float r){
        tex3d_uvr.add(u);
        tex3d_uvr.add(v);
        tex3d_uvr.add(r);
        return this;
    }

    public void loadTextureTop(GL2 gl2, URL url) throws IOException {
        sky_tex.updateImage(gl2, Utils.loadTextureData(url),GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Z);
    }

    public void loadTextureBottom(GL2 gl2,URL url) throws IOException {
        sky_tex.updateImage(gl2, Utils.loadTextureData(url),GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z);
    }

    public void loadTextureLeft(GL2 gl2,URL url) throws IOException {
        sky_tex.updateImage(gl2, Utils.loadTextureData(url),GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_X);
    }

    public void loadTextureRight(GL2 gl2,URL url) throws IOException {
        sky_tex.updateImage(gl2, Utils.loadTextureData(url),GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_X);
    }

    public void loadTextureFront(GL2 gl2,URL url) throws IOException {
        sky_tex.updateImage(gl2, Utils.loadTextureData(url),GL2.GL_TEXTURE_CUBE_MAP_POSITIVE_Y);
    }

    public void loadTextureBack(GL2 gl2,URL url) throws IOException {
        sky_tex.updateImage(gl2, Utils.loadTextureData(url),GL2.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y);
    }

}
