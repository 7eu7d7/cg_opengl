package gl7ecore.geom;

import com.jogamp.opengl.GL2;

public class Rect extends Mesh{
    public float x,y,w,h;

    public Rect(float x,float y,float w,float h){
        set(x,y,w,h);
        setDrawType(GL2.GL_QUADS);
    }

    @Override
    public void build(GL2 gl2) {
        addVertex(left(),top(),0);
        addVertex(right(),top(),0);
        addVertex(right(),bottom(),0);
        addVertex(left(),bottom(),0);

        super.build(gl2);
    }

    public void set(float x, float y, float w, float h){
        this.x=x;this.y=y;
        this.w=w;this.h=h;
    }

    public float left(){
        return x;
    }
    public float top(){
        return y;
    }
    public float right(){
        return x+w;
    }
    public float bottom(){
        return y+h;
    }
}