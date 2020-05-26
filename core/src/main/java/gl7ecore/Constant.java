package gl7ecore;

import com.jogamp.opengl.GL2;

import java.util.HashMap;

public class Constant {
    public static final int vPosition=0;
    public static final int vColor =1;
    public static final int vNormal=2;
    public static final int tex_coord=3;

    public static final int SELF_SHADER=1;
    public static final int RAW_SHADER=0;

    public final static String[] FUNCTION_NAME="sin,cos,tan,arccos,arcsin,arctan,sinh,cosh,tanh,log,lg,ln,abs,max,min".split(",");

    public static final HashMap<String,Integer> drawTypes=new HashMap<String, Integer>();
    static {
        drawTypes.put("lines", GL2.GL_LINES);
        drawTypes.put("line_strip", GL2.GL_LINE_STRIP);
        drawTypes.put("line_loop", GL2.GL_LINE_LOOP);

        drawTypes.put("tri", GL2.GL_TRIANGLES);
        drawTypes.put("tri_strip", GL2.GL_TRIANGLE_STRIP);
        drawTypes.put("tri_fan", GL2.GL_TRIANGLE_FAN);

        drawTypes.put("quads", GL2.GL_QUADS);
        drawTypes.put("quad_strip", GL2.GL_QUAD_STRIP);
    }
}
