package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.geom.Model7e;
import gl7ecore.view.GeomView;
import org.dom4j.DocumentException;

public class ViewTest4 extends GeomView {

    int rx,rz;
    Model7e md1;

    @Override
    public void init(GL2 gl2) {
        md1=new Model7e();
        try {
            md1.loadModel(gl2,getClass().getResource("/model3.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        addGeom(md1);

        super.init(gl2);
    }

    @Override
    public void draw(GL2 gl2) {
        glu.gluLookAt(3, 5, 3, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);
        gl2.glLineWidth(2);

        rx++;rz++;
        rx%=360;rz%=360;

        md1.setRotation(rx,0,rz);

        super.draw(gl2);
    }
}
