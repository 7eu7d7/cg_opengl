package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.Constant;
import gl7ecore.Utils;
import gl7ecore.geom.Ball;
import gl7ecore.geom.Model7e;
import gl7ecore.geom.SkyBox;
import gl7ecore.light.Light;
import gl7ecore.light.Material;
import gl7ecore.view.GeomView;
import glm.vec._3.Vec3;
import org.dom4j.DocumentException;

import java.io.IOException;

public class ViewTest9 extends GeomView {

    int rx,rz;
    GouraudLightBall glball=new GouraudLightBall(4,20,20);
    Ball ball=new Ball(4,20,20);

    Light light=new Light(0);

    @Override
    public void init(GL2 gl2) {

        Material mat=new Material();
        mat.setAmbient(new Vec3(0.4f));
        mat.setSpecular(new Vec3(0.6f));
        mat.setShininess(1f);

        glball.setMaterial(mat);
        glball.normalON();
        glball.setColor(0xff66ccff);
        //glball.globalAmbient

        Constant.light_list[0]=light;

        ball.setMaterial(mat);
        ball.normalON();
        ball.setColor(0xff66ccff);
        try {
            ball.setTexture(gl2, Utils.loadTexture(getClass().getResource("/nep.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //addGeom(glball);
        addGeom(ball);

        super.init(gl2);
    }

    @Override
    public void draw(GL2 gl2) {
        glu.gluLookAt(-5, -5, -3, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);
        //glu.gluLookAt(5, 5, 3, 5+Math.cos(Math.toRadians(rx)), 5+Math.sin(Math.toRadians(rx)), 3.0, 0.0, 0.0, 1.0);

        gl2.glPrimitiveRestartIndex(0xffff);
        gl2.glEnable(GL2.GL_PRIMITIVE_RESTART);

        rx++;rz++;
        rx%=360;rz%=360;

        ball.setRotation(rx,0,rz);

        super.draw(gl2);
    }
}
