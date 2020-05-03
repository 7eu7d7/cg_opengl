package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import gl7ecore.utils.GLHelper;
import gl7ecore.utils.ShaderLodaer;
import glm.vec._3.Vec3;

import java.util.LinkedList;
import java.util.List;

public class GeomGroup implements IGeom{
    List<IGeom> geom_group=new LinkedList<IGeom>();
    int draw_type=GL2.GL_NONE;

    Vec3 position=new Vec3();
    Vec3 rotation=new Vec3();
    Vec3 scale=new Vec3(1,1,1);

    @Override
    public void update(GL2 gl2) {
        gl2.glPushMatrix();
        gl2.glTranslatef(position.x,position.y,position.z);
        gl2.glRotatef(rotation.x,1,0,0);
        gl2.glRotatef(rotation.y,0,1,0);
        gl2.glRotatef(rotation.z,0,0,1);
        gl2.glScalef(scale.x, scale.y, scale.z);

        for(IGeom geom:geom_group){
            geom.update(gl2);
        }

        gl2.glPopMatrix();
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


    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public void setPosition(float x,float y,float z) {
        position.set(x,y,z);
    }

    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }

    public void setRotation(float x,float y,float z) {
        rotation.set(x,y,z);
    }

    public void setScale(Vec3 scale) {
        this.scale = scale;
    }
    public void setScale(float x,float y,float z) {
        scale.set(x,y,z);
    }
}
