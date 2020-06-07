package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.geom.Model7e;
import gl7ecore.light.Light;
import gl7ecore.light.Material;
import gl7ecore.utils.ShaderLodaer;
import gl7ecore.view.GeomView;
import glm.vec._3.Vec3;
import org.dom4j.DocumentException;

public class ViewTest6 extends GeomView {

    int rx,rz;
    Model7e m7e=new Model7e();

    int[] blend_funcs={
            GL2.GL_ZERO,GL2.GL_ONE,GL2.GL_SRC_COLOR,GL2.GL_ONE_MINUS_SRC_COLOR,GL2.GL_DST_COLOR,GL2.GL_ONE_MINUS_DST_COLOR,
            GL2.GL_SRC_ALPHA,GL2.GL_ONE_MINUS_SRC_ALPHA,GL2.GL_DST_ALPHA,GL2.GL_ONE_MINUS_DST_ALPHA,GL2.GL_CONSTANT_COLOR,
            GL2.GL_ONE_MINUS_CONSTANT_COLOR,GL2.GL_CONSTANT_ALPHA,GL2.GL_ONE_MINUS_CONSTANT_ALPHA,GL2.GL_SRC_ALPHA_SATURATE
    };

    @Override
    public void init(GL2 gl2) {
        m7e=new Model7e();
        try {
            m7e.loadModel(gl2,getClass().getResource("/model_cube.xml"));
            //m7e.setScale(0.5f,0.5f,0.5f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        m7e.setScale(0.2f,0.2f,0.2f);
        m7e.build(gl2);
        System.out.println(blend_funcs.length);

        gl2.glClearColor(0.4f,0.8f,1f,1f);

        super.init(gl2);
    }

    @Override
    public void setup(GL2 gl2) {
        super.setup(gl2);

        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        float ratio=1280f/1000;
        //正交投影
        gl2.glOrthof(-10,10,-10/ratio,10/ratio,1,142);
        //透视投影
        //gl2.glFrustumf(-ratio,ratio,-1,1,1,142);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void draw(GL2 gl2) {
        //glu.gluLookAt(3, 5, 3, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);
        glu.gluLookAt(0, 0, 8, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

        gl2.glPrimitiveRestartIndex(0xffff);
        gl2.glEnable(GL2.GL_PRIMITIVE_RESTART);

        rx++;rz++;
        rx%=360;rz%=360;

        m7e.setRotation(rx,0,rz);

        gl2.glBlendEquation(GL2.GL_MAX);

        for (int i = 0; i < blend_funcs.length; i++) {
            for (int u = 0; u < blend_funcs.length; u++) {
                gl2.glBlendFunc(blend_funcs[i],blend_funcs[u]);
                m7e.setPosition(-8+1*u, -7 + 1*i, 0);
                m7e.update(gl2);
            }
        }

    }
}
