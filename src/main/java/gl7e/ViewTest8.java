package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.geom.MeshTex3D;
import gl7ecore.geom.Model7e;
import gl7ecore.light.Light;
import gl7ecore.light.Material;
import gl7ecore.utils.ShaderLodaer;
import gl7ecore.utils.Texture3D;
import gl7ecore.view.GeomView;
import glm.vec._3.Vec3;
import org.dom4j.DocumentException;

public class ViewTest8 extends GeomView {

    int rx,rz;
    MeshTex3D mt3d=new MeshTex3D();

    @Override
    public void init(GL2 gl2) {
        Texture3D t3d=new Texture3D(getClass().getResource("/test.tex3d"));

        mt3d.bindTex3D(gl2,t3d);
        mt3d.setDrawType(GL2.GL_QUADS);

        mt3d.addTex3D(0,0,0).addVertex(0,0,0);
        mt3d.addTex3D(1,0,0).addVertex(1,0,0);
        mt3d.addTex3D(1,0,1).addVertex(1,0,1);
        mt3d.addTex3D(0,0,1).addVertex(0,0,1);

        mt3d.addTex3D(0,0,0).addVertex(0,0,0);
        mt3d.addTex3D(0,1,0).addVertex(0,1,0);
        mt3d.addTex3D(0,1,1).addVertex(0,1,1);
        mt3d.addTex3D(0,0,1).addVertex(0,0,1);

        mt3d.addTex3D(0,0,0).addVertex(0,0,0);
        mt3d.addTex3D(1,0,0).addVertex(1,0,0);
        mt3d.addTex3D(1,1,0).addVertex(1,1,0);
        mt3d.addTex3D(0,1,0).addVertex(0,1,0);

        mt3d.setScale(3,3,3);

        addGeom(mt3d);

        super.init(gl2);
    }

    @Override
    public void setup(GL2 gl2) {
        super.setup(gl2);

        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();
        float ratio=1280f/720;
        //正交投影
        //gl2.glOrthof(-10,10,-10/ratio,10/ratio,1,142);
        //透视投影
        gl2.glFrustumf(-ratio,ratio,-1,1,1,142);
        gl2.glMatrixMode(GL2.GL_MODELVIEW);
    }

    @Override
    public void draw(GL2 gl2) {
        glu.gluLookAt(-3, -5, -3, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0);

        gl2.glPrimitiveRestartIndex(0xffff);
        gl2.glEnable(GL2.GL_PRIMITIVE_RESTART);

        rx++;rz++;
        rx%=360;rz%=360;

        mt3d.setRotation(rx,0,rz);
        super.draw(gl2);
    }
}
