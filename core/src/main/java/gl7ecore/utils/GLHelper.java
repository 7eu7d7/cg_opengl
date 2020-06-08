package gl7ecore.utils;

import com.jogamp.opengl.GL2;
import glm.mat._4.Mat4;

public class GLHelper {

    public static Mat4 model=new Mat4();



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

    public static void setProjectionMatrix(GL2 gl2,float[] mat){
        
    }

}
