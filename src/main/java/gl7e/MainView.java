package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.Screen;
import gl7ecore.geom.Ball;
import gl7ecore.geom.Cube;
import gl7ecore.geom.Rect;
import gl7ecore.utils.GLHelper;
import gl7ecore.view.GLView;
import gl7ecore.view.GeomView;

public class MainView extends GeomView{

    Rect rect=new Rect(-1,-1,5,5);
    Cube cube=new Cube(-1,-1,-1,2,2,2);
    Ball ball=new Ball(2,40,80);

    int angle=0;

    @Override
    public void init(GL2 gl2) {

        rect.addColor(0xff66ccff);
        rect.addColor(0xff00ff00);
        rect.addColor(0xffff0000);
        rect.addColor(0xff0000ff);

        addGeom(rect);
        //gv.addGeom(cube);
        addGeom(ball);

        super.init(gl2);
    }

    @Override
    public void draw(GL2 gl2) {
        //glu.gluLookAt(5.0, 5.0, 5.0, 0.0, 0.0, 0.0, 1.0, 1.0, 0.0);
        glu.gluLookAt(0, 0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

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