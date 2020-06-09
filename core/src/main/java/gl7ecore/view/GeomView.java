package gl7ecore.view;

import com.jogamp.opengl.GL2;
import gl7ecore.geom.IGeom;
import gl7ecore.geom.GeomGroup;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class GeomView extends GLView {

    public GeomGroup geoms=new GeomGroup();

    @Override
    public void init(GL2 gl2) {
        geoms.build(gl2);
    }

    @Override
    public void setup(GL2 gl2) {

    }

    @Override
    public void draw(GL2 gl2) {
        geoms.update(gl2);
    }

    @Override
    public boolean onMouse(MouseEvent event) {
        return false;
    }

    @Override
    public boolean onKey(KeyEvent event) {
        return false;
    }


    public void addGeom(IGeom geom){
        geoms.addGeom(geom);
    }
}
