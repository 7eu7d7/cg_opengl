package gl7ecore.light;

import com.jogamp.opengl.GL2;
import glm.vec._3.Vec3;

public class Material {

    public Vec3 emission=new Vec3(0.0f); // 自发光颜色
    public Vec3 ambient=new Vec3(0.2f); // 环境光颜色
    public Vec3 diffuse=new Vec3(0.8f); // 漫反射颜色
    public Vec3 specular=new Vec3(0); // 镜面反射颜色
    public float shininess=0; // 镜面指数

    public void setEmission(Vec3 emission) {
        this.emission = emission;
    }
    public void setEmission(int color) {
        setEmission(new Vec3(((color >> 16) & 0xFF)/255f, ((color >> 8) & 0xFF)/255f, (color & 0xFF)/255f));
    }

    public void setAmbient(Vec3 ambient) {
        this.ambient = ambient;
    }
    public void setAmbient(int color) {
        setAmbient(new Vec3(((color >> 16) & 0xFF)/255f, ((color >> 8) & 0xFF)/255f, (color & 0xFF)/255f));
    }

    public void setDiffuse(Vec3 diffuse) {
        this.diffuse = diffuse;
    }
    public void setDiffuse(int color) {
        setDiffuse(new Vec3(((color >> 16) & 0xFF)/255f, ((color >> 8) & 0xFF)/255f, (color & 0xFF)/255f));
    }

    public void setSpecular(Vec3 specular) {
        this.specular = specular;
    }
    public void setSpecular(int color) {
        setSpecular(new Vec3(((color >> 16) & 0xFF)/255f, ((color >> 8) & 0xFF)/255f, (color & 0xFF)/255f));
    }

    public void setShininess(float shininess) {
        this.shininess = shininess;
    }

    public void send(GL2 gl2, int prog) {
        gl2.glUniform3fv(gl2.glGetUniformLocation(prog,"material.emission"),1,emission.toDfb_());
        gl2.glUniform3fv(gl2.glGetUniformLocation(prog,"material.ambient"),1,ambient.toDfb_());
        gl2.glUniform3fv(gl2.glGetUniformLocation(prog,"material.diffuse"),1,diffuse.toDfb_());
        gl2.glUniform3fv(gl2.glGetUniformLocation(prog,"material.specular"),1,specular.toDfb_());
        gl2.glUniform1f(gl2.glGetUniformLocation(prog,"material.shininess"),shininess);
    }
}
