package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import gl7ecore.light.Material;
import glm.vec._3.Vec3;

public abstract class IGeom {
    public int draw_type=GL2.GL_LINES;

    Vec3 position=new Vec3();
    Vec3 rotation=new Vec3();
    Vec3 scale=new Vec3(1,1,1);

    Material material;

     /*
        首先需要创建一个几何体对象
        使用build构建顶点等数据
        update在绘制新一帧时调用
        使用draw绘制build的数据
    */
     public void update(GL2 gl2){
         gl2.glPushMatrix();
         gl2.glTranslatef(position.x,position.y,position.z);
         gl2.glRotatef(rotation.x,1,0,0);
         gl2.glRotatef(rotation.y,0,1,0);
         gl2.glRotatef(rotation.z,0,0,1);
         gl2.glScalef(scale.x, scale.y, scale.z);

         draw(gl2);

         gl2.glPopMatrix();
     }
     public abstract void draw(GL2 gl2);
     public abstract void build(GL2 gl2);

     public void setDrawType(int type){
         draw_type=type;
     }

    public void setPosition(Vec3 position) {
        this.position = position;
    }

    public void setPosition(float x,float y,float z) {
        position.set(x,y,z);
    }

    public void setRotation(Vec3 rotation) {
        this.rotation = rotation;
    }

    public void setRotation(float x,float y,float z) {
        rotation.set(x,y,z);
    }

    public void setScale(Vec3 scale) {
        this.scale = scale;
    }
    public void setScale(float x,float y,float z) {
        scale.set(x,y,z);
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
