package gl7ecore.utils;

import com.jogamp.opengl.GL2;

public class GLHelper {

    public static float[] getModelViewMatrix(GL2 gl2){
        float[] res=new float[16];
        gl2.glGetFloatv(GL2.GL_MODELVIEW_MATRIX,res,0);
        return res;
    }

    public static float[] getProjectionMatrix(GL2 gl2){
        float[] res=new float[16];
        gl2.glGetFloatv(GL2.GL_PROJECTION_MATRIX,res,0);
        return res;
    }

}
