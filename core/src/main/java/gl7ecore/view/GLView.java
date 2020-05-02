package gl7ecore.view;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import gl7ecore.Screen;
import gl7ecore.utils.GLHelper;
import gl7ecore.utils.ShaderLodaer;

public abstract class GLView {

    public static GLU glu=new GLU();

    public abstract void init(GL2 gl2);

    public abstract void draw(GL2 gl2);

    public abstract void touch(GL2 gl2);

    public void sendTransform(GL2 gl2){
        gl2.glUniformMatrix4fv(ShaderLodaer.mv,1,false, GLHelper.getModelViewMatrix(gl2),0);
        gl2.glUniformMatrix4fv(ShaderLodaer.proj,1,false, GLHelper.getProjectionMatrix(gl2),0);
    }
}
