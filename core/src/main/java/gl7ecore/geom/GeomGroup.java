package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import gl7ecore.light.Material;
import gl7ecore.utils.GLHelper;
import gl7ecore.utils.ShaderLodaer;
import glm.vec._3.Vec3;

import java.util.LinkedList;
import java.util.List;

public class GeomGroup extends IGeom{
    public List<IGeom> geom_group=new LinkedList<IGeom>();

    public GeomGroup(){
        draw_type=GL2.GL_NONE;
    }

    @Override
    public void draw(GL2 gl2) {
        for(IGeom geom:geom_group){
            geom.update(gl2);
        }
    }

    @Override
    public void build(GL2 gl2) {
        for(IGeom geom:geom_group){
            if(draw_type!=GL2.GL_NONE)
                geom.setDrawType(draw_type);
            if(material!=null)
                geom.setMaterial(material);
            geom.build(gl2);
        }
    }

    public void addGeom(IGeom geom){
        geom_group.add(geom);
    }

    public void clear(){
        geom_group.clear();
    }

}
