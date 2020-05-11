package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import gl7ecore.utils.Expression;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;

import java.util.HashMap;

public class FunctionGeom extends Mesh{

    //参数t
    public String funcx="0";
    public String funcy="0";
    public String funcz="0";
    public Vec3 range=new Vec3(0,1,0.5);


    @Override
    public void build(GL2 gl2) {

        Expression exp=new Expression();
        try {
            HashMap<String,Double> map=new HashMap<String,Double>();
            map.put("t",(double)range.x);

            String cmpx=exp.doTrans(funcx);
            String cmpy=exp.doTrans(funcy);
            String cmpz=exp.doTrans(funcz);

            exp.calculate_check(cmpx,map);
            exp.calculate_check(cmpy,map);
            exp.calculate_check(cmpz,map);

            for(float i=range.x;i<=range.y;i+=range.z){
                map.clear();
                map.put("t",(double)i);
                addVertex(exp.calculate(cmpx,map),
                        exp.calculate(cmpy,map),
                        exp.calculate(cmpz,map));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.build(gl2);
    }

    public void setRange(float start,float end){
        range.set(start,end,1);
    }
    public void setRange(float start,float end,float step){
        range.set(start,end,step);
    }
}
