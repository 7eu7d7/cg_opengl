package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import gl7ecore.Utils;

public class Ball extends Mesh{

    int splith=10,splitv=10;
    float r;

    public Ball(float r){
        this(r,10,10);
    }

    public Ball(float r,int splith,int splitv){
        this.splith=splith;
        this.splitv=splitv;
        this.r=r;
        setDrawType(GL2.GL_QUAD_STRIP);
    }

    @Override
    public void build(GL2 gl2) {
        float steph=360f/splith;
        float stepv=360f/splitv;

        //addVertex(0,0,r);
        //addVertex(0,0,r);

        for(float h=0;h<180;h+=steph){
            for(float v=0;v<360;v+=stepv){
                float rxy=r*Utils.sind(v);
                addVertex(rxy * Utils.cosd(h),
                        rxy * Utils.sind(h),
                        r * Utils.cosd(v));
                addVertex(rxy * Utils.cosd(h+steph),
                        rxy * Utils.sind(h+steph),
                        r * Utils.cosd(v));
            }
        }


        super.build(gl2);
    }


}
