package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.geom.Circle;
import gl7ecore.geom.Mesh;
import gl7ecore.geom.Model7e;
import gl7ecore.geom.ObjModel;
import gl7ecore.view.GeomView;
import org.dom4j.DocumentException;

public class ViewTest3 extends GeomView {
    ObjModel objModel;

    int rx,rz;
    int angle;

    @Override
    public void init(GL2 gl2) {

        objModel=new ObjModel();
        objModel.loadOBJ(gl2,getClass().getResource("/miku/miku.obj"));
        objModel.setScale(0.05f,0.05f,0.05f);
        objModel.setRotation(90,180,0);
        addGeom(objModel);

        Circle cir=new Circle(0,0,2);
        cir.draw_type=GL2.GL_LINE_LOOP;
        addGeom(cir);

        super.init(gl2);
    }

    @Override
    public void draw(GL2 gl2) {
        glu.gluLookAt(3, 5, 3, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);

        rx++;rz++;
        rx%=360;rz%=360;
        angle++;
        angle%=360;

        objModel.setRotation(rx,0,rz);
        objModel.setPosition((float) (2*Math.cos(Math.toRadians(angle))),(float)(2*Math.sin(Math.toRadians(angle))),0);
        objModel.setScale(0.05f, 0.03f*(float) (Math.cos(Math.toRadians(angle)))+0.05f,0.05f);

        sendTransform(gl2);
        super.draw(gl2);
    }
}
