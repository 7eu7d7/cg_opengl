package gl7ecore;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import gl7ecore.geom.Ball;
import gl7ecore.geom.Cube;
import gl7ecore.geom.Rect;
import gl7ecore.utils.GLHelper;
import gl7ecore.utils.ShaderLodaer;
import gl7ecore.view.GLView;

public class Screen {

    public GLView view;

    int prog;

    public void init(GL2 gl2){
        gl2.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl2.glEnable(GL2.GL_DEPTH_TEST);
        gl2.glDepthMask(true);
        //gl2.glShadeModel(GL2.GL_SMOOTH);

        ShaderLodaer.loadAllShader(gl2);

        view.init(gl2);
    }

    public void setup(GL2 gl2, int width, int height ) {
        gl2.glViewport(0, 0, width, height);
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        float ratio=(float)width/height;
        gl2.glOrthof(-10,10,-10/ratio,10/ratio,1,142);
        //gl2.glFrustumf(-ratio,ratio,-1,1,1,142);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void render( GL2 gl2, int width, int height ) {
        gl2.glClear( GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl2.glLoadIdentity();
        gl2.glPushMatrix();

        view.draw(gl2);

        gl2.glPopMatrix();
    }

    /*public void draw(GL2 gl2){
        gl2.glRotatef(angle, 1, 0, 0);
        gl2.glRotatef(angle,0,0,1);
        angle++;
        angle %= 360;

        gl2.glUniformMatrix4fv(mv,1,false, GLHelper.getModelViewMatrix(gl2),0);
        gl2.glUniformMatrix4fv(proj,1,false, GLHelper.getProjectionMatrix(gl2),0);

        rect.update(gl2);
        ball.update(gl2);
    }*/

}
