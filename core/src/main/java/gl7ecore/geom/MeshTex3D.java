package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import gl7ecore.Constant;
import gl7ecore.Utils;
import gl7ecore.utils.ShaderLodaer;
import gl7ecore.utils.Texture3D;

import java.nio.Buffer;
import java.util.LinkedList;
import java.util.List;

public class MeshTex3D extends Mesh{
    Texture3D tex3d;
    List<Float> tex3d_uvr=new LinkedList<Float>();
    Buffer tex3d_buff;

    @Override
    public void build(GL2 gl2) {
        super.build(gl2);

        render_type=2;

        if(!tex3d_uvr.isEmpty())
            tex3d_buff=setTextureBuffer(gl2, Utils.list2arrf(tex3d_uvr));
    }

    @Override
    public int selectProg(GL2 gl2) {
        super.selectProg(gl2);
        gl2.glEnableVertexAttribArray(Constant.tex3d_coord);
        return ShaderLodaer.COLOR_TEX3D;
    }

    @Override
    public void sendShaderDatas(GL2 gl2) {
        super.sendShaderDatas(gl2);

        tex3d.bind(gl2);
        gl2.glUniform1i(gl2.glGetUniformLocation(prog_id,"tex3d"), 0);
        gl2.glVertexAttribPointer(Constant.tex3d_coord, 3, GL2.GL_FLOAT, false, 0, tex3d_buff.rewind());
    }

    public void bindTex3D(GL2 gl2, Texture3D tex){
        gl2.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        tex3d=tex;
    }

    public MeshTex3D addTex3D(float u,float v,float r){
        tex3d_uvr.add(u);
        tex3d_uvr.add(v);
        tex3d_uvr.add(r);
        return this;
    }

}
