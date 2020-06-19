package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.Constant;
import gl7ecore.geom.*;
import gl7ecore.light.Light;
import gl7ecore.light.Material;
import gl7ecore.utils.ShaderLodaer;
import gl7ecore.view.GeomView;
import glm.vec._3.Vec3;
import org.dom4j.DocumentException;

public class ViewTestBone extends GeomView {

    int rx,rz;
    Model7e m7e=new Model7e();

    DAEModelAss anim=new DAEModelAss();

    Light light=new Light(0);

    @Override
    public void init(GL2 gl2) {
        m7e=new Model7e();
        try {
            m7e.loadModel(gl2,getClass().getResource("/model_cube.xml"));
            anim.loadDAE(gl2,"./src/main/resources/nepmz/nep3.dae");
            //anim.loadDAE(gl2,"./src/main/resources/bob/bob.dae");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //addGeom(m7e);

        Material mat=new Material();
        //mat.setEmission(new Vec3(0.5f));
        mat.setAmbient(new Vec3(0.4f));
        mat.setSpecular(new Vec3(1f));
        mat.setShininess(1.5f);

        //anim.setRotation(-90,0,180);
        //anim.setScale(0.1f, 0.1f, 0.1f);
        anim.setScale(3, 3, 3);
        anim.setMaterial(mat);

        Constant.light_list[0]=light;

        /*anim.geom_group.forEach((x)->x.setVisible(false));
        anim.geom_group.get(1).setVisible(true);*/

        addGeom(anim);

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

        gl2.glClearColor(1,1,1,1);
    }

    @Override
    public void draw(GL2 gl2) {

        glu.gluLookAt(3, 5, 3, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);

        gl2.glPrimitiveRestartIndex(0xffff);
        gl2.glEnable(GL2.GL_PRIMITIVE_RESTART);

        rx++;rz++;
        rx%=360;rz%=360;

        //anim.setRotation(rx,0,rz);

        super.draw(gl2);

    }
}
