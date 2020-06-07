package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.geom.Circle;
import gl7ecore.geom.Mesh;
import gl7ecore.geom.Model7e;
import gl7ecore.geom.ObjModel;
import gl7ecore.view.GeomView;
import org.dom4j.DocumentException;

public class ViewTest3_2 extends GeomView {

    @Override
    public void init(GL2 gl2) {
        Model7e md1=new Model7e();
        try {
            md1.loadModel(gl2,getClass().getResource("/model2.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        md1.setScale(0.5f,0.5f,0.5f);
        addGeom(md1);

        super.init(gl2);
    }

    @Override
    public void draw(GL2 gl2) {
        glu.gluLookAt(0, 0, 5, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);

        gl2.glMultMatrixf(new float[]{
                1,0,0,0,
                0,-1,0,0,
                0,0,1,0,
                0,0,0,1
        },0);

        super.draw(gl2);
    }
}
