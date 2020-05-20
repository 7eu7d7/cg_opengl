package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;

public class Rect extends Mesh{
    public float x,y,w,h;

    public Rect(){
        setDrawType(GL2.GL_QUADS);
    }

    public Rect(float x,float y,float w,float h){
        this();
        set(x,y,w,h);
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

    public void setColors(int[] cols){
        colors.clear();
        addColor(cols[0]);
        addColor(cols[1]);
        addColor(cols[2]);
        addColor(cols[3]);
    }

    public void setTexture(GL2 gl2,Texture texture){
        texuv.clear();
        addTex(0,0);
        addTex(1,0);
        addTex(1,1);
        addTex(0,1);
        bindTexture(gl2,texture);
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
