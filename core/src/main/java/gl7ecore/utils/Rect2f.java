package gl7ecore.utils;

public class Rect2f {
    public float x,y,w,h;

    public Rect2f(){

    }

    public Rect2f(float x,float y,float w,float h){
        set(x,y,w,h);
    }

    public void set(float x,float y,float w,float h){
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
