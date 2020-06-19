package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.Constant;
import gl7ecore.Utils;
import gl7ecore.geom.Ball;
import gl7ecore.geom.Mesh;
import gl7ecore.geom.Model7e;
import gl7ecore.geom.SkyBox;
import gl7ecore.light.Light;
import gl7ecore.light.Material;
import gl7ecore.utils.ShaderLodaer;
import gl7ecore.view.GeomView;
import glm.vec._3.Vec3;
import org.dom4j.DocumentException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ViewTest9 extends GeomView {

    int rx,rz;
    GouraudLightBall glball=new GouraudLightBall(4,20,20);
    Ball ball=new Ball(4,40,40);

    Model7e m7e_gou=new Model7e();
    Model7e m7e=new Model7e();

    Light light=new Light(0);
    JLabel label;


    @Override
    public void init(GL2 gl2) {
        label=new JLabel("phong");
        label.setLocation(100,100);
        label.setSize(50,20);

        Main.panel.add(label,0);

        int prog_gouraud= ShaderLodaer.loadShader(gl2,"/vs_gouraud.glsl","/fs_gouraud.glsl");

        light.pointLight();
        light.setPosition(0,0,5);
        light.quadraticAttenuation=0.02f;

        Material mat=new Material();
        mat.setAmbient(new Vec3(0.4f));
        mat.setSpecular(new Vec3(1f));
        mat.setShininess(1);

        glball.setMaterial(mat);
        glball.normalON();
        glball.setColor(0xff66ccff);
        //glball.globalAmbient

        Constant.light_list[0]=light;

        ball.setMaterial(mat);
        ball.normalON();
        ball.setColor(0xff66ccff);
        try {
            ball.setTexture(gl2, Utils.loadTexture(getClass().getResource("/nep.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //addGeom(glball);
        addGeom(ball);

        try {
            m7e_gou.loadModel(gl2,getClass().getResource("/model_cube.xml"));
            //gl2.glGenerateMipmap(GL2.GL_TEXTURE_2D);
            //m7e.setScale(0.5f,0.5f,0.5f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        Vec3 globalAmbient=new Vec3(1);

        ((Mesh)m7e_gou.geom_group.get(0)).prog_id_force=prog_gouraud;
        ((Mesh)m7e_gou.geom_group.get(0)).setShaderDataSender((prog_id -> {
            //send material
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"material.ambient"),1,mat.ambient.toDfb_());
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"material.diffuse"),1,mat.diffuse.toDfb_());
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"material.specular"),1,mat.specular.toDfb_());
            gl2.glUniform1f(gl2.glGetUniformLocation(prog_gouraud,"material.shininess"),mat.shininess);

            //send light
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"light.ambient"),1, Constant.light_list[0].ambient.toDfb_());
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"light.position"),1, Constant.light_list[0].position.toDfb_());
            gl2.glUniform3fv(gl2.glGetUniformLocation(prog_gouraud,"globalAmbient"),1, globalAmbient.toDfb_());
        }));

        //addGeom(m7e_gou);

        try {
            m7e.loadModel(gl2,getClass().getResource("/model_cube.xml"));
            //gl2.glGenerateMipmap(GL2.GL_TEXTURE_2D);
            //m7e.setScale(0.5f,0.5f,0.5f);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        m7e.setMaterial(mat);
        //addGeom(m7e);

        m7e_gou.setVisible(false);

        super.init(gl2);
    }

    @Override
    public void draw(GL2 gl2) {
        glu.gluLookAt(-5, -5, -3, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);
        //glu.gluLookAt(5, 5, 3, 5+Math.cos(Math.toRadians(rx)), 5+Math.sin(Math.toRadians(rx)), 3.0, 0.0, 0.0, 1.0);

        gl2.glPrimitiveRestartIndex(0xffff);
        gl2.glEnable(GL2.GL_PRIMITIVE_RESTART);

        rx++;rz++;
        rx%=360;rz%=360;

        //ball.setRotation(rx,0,rz);
        m7e_gou.setRotation(rx,0,rz);
        m7e.setRotation(rx,0,rz);
        light.setPosition(0,0,1+4*(rx/360f));

        super.draw(gl2);
    }

    @Override
    public boolean onKey(KeyEvent event) {

        if(event.getKeyCode()==KeyEvent.VK_A){
            m7e.setVisible(true);
            m7e_gou.setVisible(false);
            label.setText("phong");
        }

        if(event.getKeyCode()==KeyEvent.VK_S){
            m7e.setVisible(false);
            m7e_gou.setVisible(true);
            label.setText("gouraud");
        }

        return super.onKey(event);
    }
}
