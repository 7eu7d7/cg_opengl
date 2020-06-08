package gl7ecore.utils;

import com.jogamp.opengl.GL2;
import gl7ecore.Utils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class ShaderLodaer {
    public static int COLOR_TEX;
    public static int COLOR_TEX_BONE;
    public static int COLOR_TEX3D;
    public static int SKY_BOX;

    //public static int mv,proj,tex,type;

    public static void loadAllShader(GL2 gl2){
        COLOR_TEX = ShaderLodaer.loadShader(gl2, "/vs_color_tex.glsl","/fs_color_tex.glsl");
        COLOR_TEX_BONE = ShaderLodaer.loadShader(gl2, "/vs_color_tex_bone.glsl","/fs_color_tex.glsl");
        COLOR_TEX3D = ShaderLodaer.loadShader(gl2, "/vs_color_tex3d.glsl","/fs_color_tex3d.glsl");
        SKY_BOX = ShaderLodaer.loadShader(gl2, "/vs_skybox.glsl","/fs_skybox.glsl");

        /*mv = gl2.glGetUniformLocation(COLOR_TEX,"mv");
        proj = gl2.glGetUniformLocation(COLOR_TEX,"proj");
        tex = gl2.glGetUniformLocation(COLOR_TEX,"tex");
        type = gl2.glGetUniformLocation(COLOR_TEX,"type");*/
    }

    public static int loadShader(GL2 gl2, String vtx, String frag){
        int v = gl2.glCreateShader(GL2.GL_VERTEX_SHADER);
        int f = gl2.glCreateShader(GL2.GL_FRAGMENT_SHADER);

        String[] vsrc=new String[]{Utils.readResources(vtx,ShaderLodaer.class.getClass())};
        gl2.glShaderSource(v, 1, vsrc, null);
        gl2.glCompileShader(v);
        checkerr(gl2,v);

        String[] fsrc=new String[]{Utils.readResources(frag,ShaderLodaer.class.getClass())};
        gl2.glShaderSource(f, 1, fsrc, null);
        gl2.glCompileShader(f);
        checkerr(gl2,f);


        int shaderprogram = gl2.glCreateProgram();
        gl2.glAttachShader(shaderprogram, v);
        gl2.glAttachShader(shaderprogram, f);
        gl2.glLinkProgram(shaderprogram);
        checkerr(gl2,shaderprogram);

        gl2.glValidateProgram(shaderprogram);
        checkerr(gl2,shaderprogram);

        return shaderprogram;
    }

    public static void checkerr(GL2 gl2,int programId){
        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl2.glGetProgramiv(programId, GL2.GL_LINK_STATUS, intBuffer);

        if (intBuffer.get(0) != 1) {
            gl2.glGetProgramiv(programId, GL2.GL_INFO_LOG_LENGTH, intBuffer);
            int size = intBuffer.get(0);
            if (size > 0) {
                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                gl2.glGetProgramInfoLog(programId, size, intBuffer, byteBuffer);
                System.out.println(new String(byteBuffer.array()));
            }
        }
    }
}
