package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import gl7ecore.Constant;

import java.util.ArrayList;

public class AnimGeom extends IGeom {
    ArrayList<IGeom> frames=new ArrayList<IGeom>();
    double time=0;
    double duration=0;

    public AnimGeom(){
        draw_type=GL2.GL_NONE;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void addFrame(IGeom geom){
        frames.add(geom);
    }

    public void clear(){
        frames.clear();
    }

    @Override
    public void draw(GL2 gl2) {
        //System.out.println(time%duration);
        frames.get((int)(frames.size()*((time%duration)/duration))).update(gl2);
        timeInc();
    }

    public void timeInc(){
        time+= Constant.TIME_PER_TICK;
    }

    @Override
    public void build(GL2 gl2) {
        for(IGeom geom:frames){
            if(draw_type!=GL2.GL_NONE)
                geom.setDrawType(draw_type);
            if(material!=null)
                geom.setMaterial(material);
            geom.build(gl2);
        }
    }

}
