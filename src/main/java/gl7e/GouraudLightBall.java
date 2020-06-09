package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.Constant;
import gl7ecore.geom.Ball;
import gl7ecore.geom.Mesh;
import gl7ecore.utils.ShaderLodaer;
import glm.vec._3.Vec3;

public class GouraudLightBall extends Ball{

    public static int prog_gouraud;
    Vec3 globalAmbient=new Vec3(1);

    public GouraudLightBall(float r) {
        super(r);
    }

    public GouraudLightBall(float r,int splith,int splitv){
        super(r,splith,splitv);
    }

    @Override
    public void build(GL2 gl2) {
        prog_gouraud=ShaderLodaer.loadShader(gl2,"/vs_gouraud.glsl","/fs_gouraud.glsl");
        super.build(gl2);
    }

    @Override
    public int selectProg(GL2 gl2) {
        super.selectProg(gl2);
        return prog_gouraud;
    }

    @Override
    public void sendShaderDatas(GL2 gl2) {
        if(material!=null){
            //send material
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"material.ambient"),1,material.ambient.toDfb_());
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"material.diffuse"),1,material.diffuse.toDfb_());
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"material.specular"),1,material.specular.toDfb_());
            gl2.glUniform1f(gl2.glGetUniformLocation(prog_gouraud,"material.shininess"),material.shininess);

            //send light
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"light.ambient"),1, Constant.light_list[0].ambient.toDfb_());
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"light.position"),1, Constant.light_list[0].position.toDfb_());
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"globalAmbient"),1, globalAmbient.toDfb_());
        }
    }
}
