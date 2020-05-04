package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.Constant;
import gl7ecore.Screen;
import gl7ecore.Utils;
import gl7ecore.geom.*;
import gl7ecore.utils.GLHelper;
import gl7ecore.view.GLView;
import gl7ecore.view.GeomView;
import org.dom4j.DocumentException;

import java.io.IOException;

public class MainView extends GeomView{

    Rect rect=new Rect(-1,-1,5,5);
    Cube cube=new Cube(-1,-1,-1,2,2,2);
    Ball ball=new Ball(2,40,20);
    ObjModel objModel=new ObjModel();
    Model7e m7e=new Model7e();

    int angle=0;

    @Override
    public void init(GL2 gl2) {

        rect.addColor(0xff66ccff);
        rect.addColor(0xff00ff00);
        rect.addColor(0xffff0000);
        rect.addColor(0xff0000ff);

        try {
            rect.bindTexture(gl2, Utils.loadTexture("/hinata.png",this.getClass()));
            rect.addTex(0,0);
            rect.addTex(1,0);
            rect.addTex(1,1);
            rect.addTex(0,1);
            ball.setTexture(gl2,Utils.loadTexture("/hinata.png",this.getClass()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            m7e.loadModel(gl2,getClass().getResource("/model1.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        /*objModel.loadOBJ(gl2,getClass().getResource("/miku/miku.obj"));
        objModel.setScale(0.05f,0.05f,0.05f);
        objModel.setRotation(90,180,0);*/

        //addGeom(rect);
        //gv.addGeom(cube);
        //addGeom(ball);
        //addGeom(objModel);
        addGeom(m7e);

        super.init(gl2);
    }

    @Override
    public void draw(GL2 gl2) {
        glu.gluLookAt(3, 5, 3, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);
        //glu.gluLookAt(0, 0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

        gl2.glRotatef(angle, 1, 0, 0);
        gl2.glRotatef(angle,0,0,1);
        angle++;
        angle %= 360;

        sendTransform(gl2);
        super.draw(gl2);
    }

    @Override
    public void touch(GL2 gl2) {
        super.touch(gl2);
    }
}
