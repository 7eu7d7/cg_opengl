package gl7e;

import com.jogamp.opengl.GL2;
import gl7ecore.geom.Ball;
import gl7ecore.geom.Mesh;
import gl7ecore.view.GLView;
import gl7ecore.view.GeomView;

public class ViewTest1 extends GeomView{



    @Override
    public void init(GL2 gl2) {
        //点
        gl2.glPointSize(10f);
        Mesh point=new Mesh();
        point.draw_type=GL2.GL_POINTS;
        point.addVertex(0,0,0);
        point.addColor(0xff66ccff);
        point.setPosition(-8,0,0);
        addGeom(point);

        //线
        gl2.glLineWidth(5f);
        Mesh line=new Mesh();
        line.draw_type=GL2.GL_LINES;
        line.addVertex(0,-3,0);
        line.addVertex(0,3,0);
        line.setPosition(-7,0,0);
        addGeom(line);

        //条带线
        Mesh lines=new Mesh();
        lines.draw_type=GL2.GL_LINE_STRIP;
        lines.addVertex(-1,-3,0);
        lines.addVertex(1,-1,0);
        lines.addVertex(-1,1,0);
        lines.addVertex(1,3,0);
        lines.setPosition(-5,0,0);
        addGeom(lines);

        //循环线
        Mesh linel=new Mesh();
        linel.draw_type=GL2.GL_LINE_LOOP;
        linel.addVertex(0,-1,0);
        linel.addVertex(-1,0,0);
        linel.addVertex(0,1,0);
        linel.addVertex(1,0,0);
        linel.setPosition(-3,0,0);
        addGeom(linel);

        //三角形
        Mesh tri=new Mesh();
        tri.draw_type=GL2.GL_TRIANGLES;
        tri.addVertex(-0.5f,-1,0).addColor(0xffff0000);
        tri.addVertex(0.5f,-1,0).addColor(0xff00ff00);
        tri.addVertex(0,1,0).addColor(0xff0000ff);
        tri.setPosition(-1,0,0);
        addGeom(tri);

        //三角形条带
        Mesh tris=new Mesh();
        tris.draw_type=GL2.GL_TRIANGLE_STRIP;
        tris.addVertex(-0.5f,-1,0).addColor(0xffff0000);
        tris.addVertex(0.5f,-1,0).addColor(0xff00ff00);
        tris.addVertex(0,0,0).addColor(0xff0000ff);
        tris.addVertex(1,1,0).addColor(0xff66ccff);
        tris.addVertex(0,2,0).addColor(0xffff66cc);
        tris.setPosition(1,0,0);
        addGeom(tris);

        //三角形扇面
        Mesh trif=new Mesh();
        trif.draw_type=GL2.GL_TRIANGLE_FAN;
        trif.addVertex(0,0,0).addColor(0xffff0000);
        trif.addVertex(-1,-1,0).addColor(0xff00ff00);
        trif.addVertex(0,-1,0).addColor(0xff0000ff);
        trif.addVertex(1,-1,0).addColor(0xff66ccff);
        trif.addVertex(0.5f,1,0).addColor(0xffff66cc);
        trif.addVertex(-0.5f,0.5f,0).addColor(0xffffb000);
        trif.setPosition(4,0,0);
        addGeom(trif);

        Ball ball=new Ball(2,20,40){
            @Override
            public void draw(GL2 gl2) {
                gl2.glLineWidth(1f);
                super.draw(gl2);
                gl2.glLineWidth(5f);
            }
        };
        ball.setDrawType(GL2.GL_LINE_STRIP);
        ball.setPosition(7,0,0);
        addGeom(ball);

        super.init(gl2);
    }

    @Override
    public void draw(GL2 gl2) {
        glu.gluLookAt(0, 0, 5.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0);
        sendTransform(gl2);
        super.draw(gl2);
    }

    @Override
    public void touch(GL2 gl2) {
        super.touch(gl2);
    }
}
