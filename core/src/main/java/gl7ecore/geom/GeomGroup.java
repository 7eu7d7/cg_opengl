package gl7ecore.geom;

import com.jogamp.opengl.GL2;

import java.util.LinkedList;
import java.util.List;

public class GeomGroup implements IGeom{
    List<IGeom> geom_group=new LinkedList<IGeom>();
    int draw_type=GL2.GL_NONE;

    @Override
    public void update(GL2 gl2) {
        for(IGeom geom:geom_group){
            geom.update(gl2);
        }
    }

    @Override
    public void draw(GL2 gl2) {

    }

    @Override
    public void build(GL2 gl2) {
        for(IGeom geom:geom_group){
            if(draw_type!=GL2.GL_NONE)
                geom.setDrawType(draw_type);
            geom.build(gl2);
        }
    }

    @Override
    public void setDrawType(int type) {
        draw_type=type;
    }

    public void addGeom(IGeom geom){
        geom_group.add(geom);
    }

    public void clear(){
        geom_group.clear();
    }
}
