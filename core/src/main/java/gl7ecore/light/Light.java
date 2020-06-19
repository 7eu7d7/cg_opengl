package gl7ecore.light;

import com.jogamp.opengl.GL2;
import glm.vec._3.Vec3;

public class Light {
    int id;

    boolean isEnabled=true; //是否开启
    boolean isLocal=false; //是否为点光源或聚光灯
    boolean isSpot=false; //是否为聚光灯
    public Vec3 ambient=new Vec3(1); //光照强度
    public Vec3 color=new Vec3(1,1,1); //颜色
    public Vec3 position=new Vec3(0,0,1); //光源位置
    public Vec3 halfVector=new Vec3(0,0,1); //平行光的高光方向
    public Vec3 coneDirection=new Vec3(0,0,-1); //聚光灯方向
    float spotCosCutoff=(float) Math.cos(Math.toRadians(10)); //椎体夹角
    float spotExponent=1;
    //衰减系数
    public float constantAttenuation=1;
    public float linearAttenuation=0;
    public float quadraticAttenuation=0;

    public static float Strength=0.9f;
    public static Vec3 EyeDirection=new Vec3(0,0,-1);

    public Light(int id){
        this.id=id;
    }

    public void enable(){
        isEnabled=true;
    }
    public void disable(){
        isEnabled=false;
    }

    public void directionLight(){
        isLocal=false;
        isSpot=false;
    }

    public void pointLight(){
        isLocal=true;
        isSpot=false;
    }

    public void spotLight(){
        isLocal=true;
        isSpot=true;
    }

    public void setAmbient(Vec3 ambient) {
        this.ambient = ambient;
    }
    public void setAmbient(float ambient) {
        setAmbient(new Vec3(ambient));
    }

    public void setColor(Vec3 color) {
        this.color = color;
    }
    public void setColor(float r,float g,float b) {
        setColor(new Vec3(r,g,b));
    }
    public void setColor(int color) {
        setColor(((color >> 16) & 0xFF)/255f, ((color >> 8) & 0xFF)/255f, (color & 0xFF)/255f);
    }

    public void setPosition(Vec3 position) {
        this.position = position;
    }
    public void setPosition(float x,float y,float z) {
        setPosition(new Vec3(x,y,z));
    }

    public void setHalfVector(Vec3 halfVector) {
        this.halfVector = halfVector;
    }

    public void setConeDirection(Vec3 coneDirection) {
        this.coneDirection = coneDirection;
    }

    public void setConeDirection(float x,float y,float z) {
        setConeDirection(new Vec3(x,y,z));
    }

    public void setSpotCosCutoff(float spotCosCutoff) {
        this.spotCosCutoff = (float) Math.cos(Math.toRadians(spotCosCutoff));
    }

    public void setSpotExponent(float spotExponent) {
        this.spotExponent = spotExponent;
    }

    public void setAttenuation(float constant,float linear,float quadratic){
        this.constantAttenuation=constant;
        this.linearAttenuation=linear;
        this.quadraticAttenuation=quadratic;
    }

    public void send(GL2 gl2,int prog){
        gl2.glUniform1i(gl2.glGetUniformLocation(prog,"Lights["+id+"].isEnabled"),isEnabled?1:0);
        gl2.glUniform1i(gl2.glGetUniformLocation(prog,"Lights["+id+"].isLocal"),isLocal?1:0);
        gl2.glUniform1i(gl2.glGetUniformLocation(prog,"Lights["+id+"].isSpot"),isSpot?1:0);

        gl2.glUniform3fv(gl2.glGetUniformLocation(prog,"Lights["+id+"].ambient"),1,ambient.toDfb_());
        gl2.glUniform3fv(gl2.glGetUniformLocation(prog,"Lights["+id+"].color"),1,color.toDfb_());
        gl2.glUniform3fv(gl2.glGetUniformLocation(prog,"Lights["+id+"].position"),1,position.toDfb_());
        gl2.glUniform3fv(gl2.glGetUniformLocation(prog,"Lights["+id+"].halfVector"),1,halfVector.toDfb_());
        gl2.glUniform3fv(gl2.glGetUniformLocation(prog,"Lights["+id+"].coneDirection"),1,coneDirection.toDfb_());

        gl2.glUniform1f(gl2.glGetUniformLocation(prog,"Lights["+id+"].spotCosCutoff"),spotCosCutoff);
        gl2.glUniform1f(gl2.glGetUniformLocation(prog,"Lights["+id+"].spotExponent"),spotExponent);
        gl2.glUniform1f(gl2.glGetUniformLocation(prog,"Lights["+id+"].constantAttenuation"),constantAttenuation);
        gl2.glUniform1f(gl2.glGetUniformLocation(prog,"Lights["+id+"].linearAttenuation"),linearAttenuation);
        gl2.glUniform1f(gl2.glGetUniformLocation(prog,"Lights["+id+"].quadraticAttenuation"),quadraticAttenuation);

        //static
        gl2.glUniform1f(gl2.glGetUniformLocation(prog,"Strength"),Strength);
        gl2.glUniform3fv(gl2.glGetUniformLocation(prog,"EyeDirection"),1,EyeDirection.toDfb_());
    }
}
