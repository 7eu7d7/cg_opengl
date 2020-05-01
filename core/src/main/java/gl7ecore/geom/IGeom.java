package gl7ecore.geom;

import com.jogamp.opengl.GL2;

public interface IGeom {
     /*
        首先需要创建一个几何体对象
        使用build构建顶点等数据
        update在绘制新一帧时调用
        使用draw绘制build的数据
    */
     void update(GL2 gl2);
     void draw(GL2 gl2);
     void build(GL2 gl2);

     void setDrawType(int type);
}
