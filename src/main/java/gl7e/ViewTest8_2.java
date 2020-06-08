package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.Constant;
import gl7ecore.geom.Model7e;
import gl7ecore.geom.SkyBox;
import gl7ecore.light.Light;
import gl7ecore.light.Material;
import gl7ecore.utils.ShaderLodaer;
import gl7ecore.view.GeomView;
import glm.vec._3.Vec3;
import org.dom4j.DocumentException;

import java.io.IOException;

public class ViewTest8_2 extends GeomView {

    int rx,rz;
    Model7e m7e=new Model7e();
    SkyBox skybox=new SkyBox();

    Light light=new Light(0);

    @Override
    public void init(GL2 gl2) {

        //gl2.glClearColor(1,1,1,1);

        try {
            m7e.loadModel(gl2,getClass().getResource("/model_cube.xml"));

            skybox.loadTextureBack(gl2,getClass().getResource("/skybox/sp3back.jpg"));
            skybox.loadTextureFront(gl2,getClass().getResource("/skybox/sp3front.jpg"));
            skybox.loadTextureLeft(gl2,getClass().getResource("/skybox/sp3left.jpg"));
            skybox.loadTextureRight(gl2,getClass().getResource("/skybox/sp3right.jpg"));
            skybox.loadTextureTop(gl2,getClass().getResource("/skybox/sp3top.jpg"));
            skybox.loadTextureBottom(gl2,getClass().getResource("/skybox/sp3bot.jpg"));
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
        Material mat=new Material();
        mat.setAmbient(new Vec3(0.4f));
        mat.setSpecular(new Vec3(1f));
        mat.setShininess(1.0f);

        m7e.setMaterial(mat);

        Constant.light_list[0]=light;

        addGeom(m7e);
        addGeom(skybox);

        super.init(gl2);
    }

    @Override
    public void draw(GL2 gl2) {
        glu.gluLookAt(-5*Math.cos(Math.toRadians(rx)), -5*Math.sin(Math.toRadians(rx)), -3, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);
        //glu.gluLookAt(5, 5, 3, 5+Math.cos(Math.toRadians(rx)), 5+Math.sin(Math.toRadians(rx)), 3.0, 0.0, 0.0, 1.0);

        gl2.glPrimitiveRestartIndex(0xffff);
        gl2.glEnable(GL2.GL_PRIMITIVE_RESTART);

        rx++;rz++;
        rx%=360;rz%=360;

        m7e.setRotation(rx,0,rz);

        super.draw(gl2);
    }
}
