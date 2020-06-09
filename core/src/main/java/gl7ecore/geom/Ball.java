package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import gl7ecore.Utils;

public class Ball extends Mesh{

    int splith=10,splitv=10;
    float r;
    int color=0;
    boolean normal_on=false;

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
        float stepv=180f/splitv;

        for(float h=0;h<180;h+=steph){
            for(float v=0;v<=360;v+=stepv){
                float rxy=r*Utils.sind(v);
                addVertex(rxy * Utils.cosd(h),
                        rxy * Utils.sind(h),
                        r * Utils.cosd(v));
                addVertex(rxy * Utils.cosd(h+steph),
                        rxy * Utils.sind(h+steph),
                        r * Utils.cosd(v));
                if(color!=0){
                    addColor(color);
                    addColor(color);
                }
                if(normal_on){
                    addNormal(rxy * Utils.cosd(h),
                            rxy * Utils.sind(h),
                            r * Utils.cosd(v));
                    addNormal(rxy * Utils.cosd(h+steph),
                            rxy * Utils.sind(h+steph),
                            r * Utils.cosd(v));
                }
            }
        }

        super.build(gl2);
    }

    public void setColor(int col){
        color=col;
    }

    public void normalON(){
        normal_on=true;
    }

    public void normalOFF(){
        normal_on=false;
    }

    public void setTexture(GL2 gl2,Texture texture){
        texuv.clear();

        /*for(float h=0;h<splith/2;h++){
            for(float v=0;v<splitv;v++){
                addTex(h/splith,v/splitv);
                addTex((h+1)/splith,v/splitv);
            }
            for(float v=0;v<=splitv;v++){
                addTex(0.5f+h/splith,1-v/splitv);
                addTex(0.5f+(h+1)/splith,1-v/splitv);
            }
        }*/
        bindTexture(gl2,texture);
    }
}
