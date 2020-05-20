package gl7ecore.geom;

import com.jogamp.opengl.GL2;

public class Circle extends Mesh{
    float x,y,r;
    int split=20;

    public Circle(){
        setDrawType(GL2.GL_TRIANGLE_FAN);
    }

    public Circle(float x,float y,float r){
        this();
        this.x=x;this.y=y;this.r=r;
    }

    public void setSplit(int split) {
        this.split = split;
    }

    @Override
    public void build(GL2 gl2) {
        addVertex(x,y,0);

        for(int i=0;i<=split;i++){
            float ang=(float)(((float) i/split)*Math.PI*2);
            addVertex((float)(x+r*Math.cos(ang)),(float)(y+r*Math.sin(ang)),0);
        }

        super.build(gl2);
    }

    public void setColor(int cols){
        colors.clear();
        addColor(cols);
        for(int i=0;i<=split;i++){
            addColor(cols);
        }
    }

}
