package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.geom.Model7e;
import gl7ecore.light.Light;
import gl7ecore.light.Material;
import gl7ecore.utils.ShaderLodaer;
import gl7ecore.view.GeomView;
import glm.vec._3.Vec3;
import org.dom4j.DocumentException;

public class ViewTest7 extends GeomView {

    int rx,rz;
    Model7e m7e=new Model7e();

    Light light=new Light(0);

    @Override
    public void init(GL2 gl2) {
        m7e=new Model7e();
        try {
            m7e.loadModel(gl2,getClass().getResource("/model7.xml"));
            //gl2.glGenerateMipmap(GL2.GL_TEXTURE_2D);
            //m7e.setScale(0.5f,0.5f,0.5f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Material mat=new Material();
        mat.setAmbient(new Vec3(0.4f));
        mat.setSpecular(new Vec3(1f));
        mat.setShininess(1.0f);

        m7e.setMaterial(mat);
        addGeom(m7e);

        super.init(gl2);
    }

    @Override
    public void setup(GL2 gl2) {
        super.setup(gl2);

        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        float ratio=1280f/720;
        //正交投影
        //gl2.glOrthof(-10,10,-10/ratio,10/ratio,1,142);
        //透视投影
        gl2.glFrustumf(-ratio,ratio,-1,1,1,142);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void draw(GL2 gl2) {
        glu.gluLookAt(-4, 0, 3, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);

        gl2.glPrimitiveRestartIndex(0xffff);
        gl2.glEnable(GL2.GL_PRIMITIVE_RESTART);

        rx++;rz++;
        rx%=360;rz%=360;

        //m7e.setRotation(rx,0,rz);
        //各向异性过滤
        float[] max_TexAni=new float[1];
        gl2.glGetFloatv(GL2.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT,max_TexAni,0);
        gl2.glTexParameterf(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAX_ANISOTROPY_EXT, max_TexAni[0]);

        gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, GL2.GL_REPEAT);    //横坐标
        gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, GL2.GL_REPEAT);    //纵坐标

        //gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,GL2.GL_LINEAR_MIPMAP_LINEAR);

        gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);  //缩小时的过滤方式
        gl2.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);  //放大时的过滤方式

        light.send(gl2, ShaderLodaer.COLOR_TEX);
        super.draw(gl2);
    }
}
