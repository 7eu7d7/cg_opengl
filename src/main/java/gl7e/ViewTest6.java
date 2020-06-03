package gl7e;

import assimp.AiScene;
import com.jogamp.opengl.GL2;
import gl7ecore.geom.DAEModel;
import gl7ecore.geom.FBXModel;
import gl7ecore.geom.Model7e;
import gl7ecore.light.Light;
import gl7ecore.light.Material;
import gl7ecore.utils.ShaderLodaer;
import gl7ecore.view.GeomView;
import glm.vec._3.Vec3;
import org.dom4j.DocumentException;

import java.net.URL;

public class ViewTest6 extends GeomView {

    int rx,rz;
    Model7e m7e=new Model7e();
    DAEModel fbx;

    Light light=new Light(0);

    @Override
    public void init(GL2 gl2) {
        m7e=new Model7e();
        try {
            m7e.loadModel(gl2,getClass().getResource("/model_cube.xml"));
            //m7e.setScale(0.5f,0.5f,0.5f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        //addGeom(m7e);

        fbx=new DAEModel(gl2, getClass().getResource("/nep/nep.DAE"));
        //fbx.setScale(0.05f,0.05f,0.05f);
        fbx.setScale(3f,3f,3f);

        Material mat=new Material();
        mat.setAmbient(new Vec3(0.2f));
        mat.setSpecular(new Vec3(1f));
        mat.setShininess(1.0f);
        fbx.setMaterial(mat);
        addGeom(fbx);

        super.init(gl2);
    }

    @Override
    public void setup(GL2 gl2) {
        super.setup(gl2);

        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        float ratio=1280f/720;
        //透视投影
        gl2.glFrustumf(-ratio,ratio,-1,1,1,142);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void draw(GL2 gl2) {

        glu.gluLookAt(3, 5, 3, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);

        gl2.glPrimitiveRestartIndex(0xffff);
        gl2.glEnable(GL2.GL_PRIMITIVE_RESTART);

        rx++;rz++;
        rx%=360;rz%=360;

        fbx.setRotation(rx,0,rz);

        sendTransform(gl2);
        light.send(gl2, ShaderLodaer.COLOR_TEX);

        super.draw(gl2);

    }
}
