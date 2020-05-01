package gl7ecore.geom;

import com.jogamp.opengl.GL2;

public class Cube extends GeomGroup {
    public float x,y,z;
    public float w,h,d;

    public Cube(){
        setDrawType(GL2.GL_QUAD_STRIP);
    }

    public Cube(float x,float y,float z,float w,float h,float d){
        this();
        setPosition(x,y,z);
        setSize(w,h,d);
    }

    public void setPosition(float x,float y,float z){
        this.x=x;this.y=y;this.z=z;
    }
    public void setSize(float w,float h,float d){
        this.w=w;this.h=h;this.d=d;
    }

    @Override
    public void build(GL2 gl2) {
        Mesh part1=new Mesh();
        part1.addVertex(x,y,z);
        part1.addVertex(x+w,y,z);

        part1.addVertex(x,y,z+d);
        part1.addVertex(x+w,y,z+d);

        part1.addVertex(x,y+h,z+d);
        part1.addVertex(x+w,y+h,z+d);

        part1.addVertex(x,y+h,z);
        part1.addVertex(x+w,y+h,z);
        addGeom(part1);

        Mesh part2=new Mesh();
        part2.addVertex(x,y,z+d);
        part2.addVertex(x,y+h,z+d);

        part2.addVertex(x,y,z);
        part2.addVertex(x,y+h,z);

        part2.addVertex(x+w,y,z);
        part2.addVertex(x+w,y+h,z);

        part2.addVertex(x+w,y,z+d);
        part2.addVertex(x+w,y+h,z+d);
        addGeom(part2);

        super.build(gl2);
    }
}
