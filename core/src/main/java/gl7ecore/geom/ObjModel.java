package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import gl7ecore.Utils;
import gl7ecore.utils.Mtl;
import gl7ecore.utils.MtlLoader;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ObjModel extends GeomGroup{

    public ObjModel(){
        draw_type= GL2.GL_TRIANGLES;
    }

    public ObjModel(GL2 gl2,URL url){
        this();
        loadOBJ(gl2,url);
    }

    public void loadOBJ(GL2 gl2,URL url){
        String model=Utils.readURL(url);
        String[] datas=model.split("\n");
        ArrayList<Vec3> vtx_list=new ArrayList<Vec3>();
        ArrayList<Vec2> tex_list=new ArrayList<Vec2>();

        clear();
        Mesh mesh_now = null;
        HashMap<String,Mtl> mtl_data=null;
        Mtl last_mtl=null;

        int idx=0;
        for(String line:datas){
            line=line.trim();
            String[] items=line.split(" +");
            switch (items[0]){
                case "v":{
                    vtx_list.add(new Vec3(Utils.str2float(items,1,items.length)));
                }break;
                case "vt":{
                    float[] fvt=Utils.str2float(items,1,items.length);
                    //System.out.println(fvt.length);
                    tex_list.add(new Vec2(Utils.check_pos(fvt[0]),Utils.check_pos(fvt[1])));
                }break;
                case "f":{
                    int[][] fdata=getFaceData(items,1,items.length);
                    if (fdata.length==3){
                        //v,vt
                        mesh_now.addVertex(vtx_list.get(fdata[0][0]-1)).addTex(tex_list.get(fdata[0][1]-1));
                        mesh_now.addVertex(vtx_list.get(fdata[1][0]-1)).addTex(tex_list.get(fdata[1][1]-1));
                        mesh_now.addVertex(vtx_list.get(fdata[2][0]-1)).addTex(tex_list.get(fdata[2][1]-1));

                    } else {
                        //v,vt
                        mesh_now.addVertex(vtx_list.get(fdata[0][0]-1)).addTex(tex_list.get(fdata[0][1]-1));
                        mesh_now.addVertex(vtx_list.get(fdata[1][0]-1)).addTex(tex_list.get(fdata[1][1]-1));
                        mesh_now.addVertex(vtx_list.get(fdata[2][0]-1)).addTex(tex_list.get(fdata[2][1]-1));

                        mesh_now.addVertex(vtx_list.get(fdata[0][0]-1)).addTex(tex_list.get(fdata[0][1]-1));
                        mesh_now.addVertex(vtx_list.get(fdata[2][0]-1)).addTex(tex_list.get(fdata[2][1]-1));
                        mesh_now.addVertex(vtx_list.get(fdata[3][0]-1)).addTex(tex_list.get(fdata[3][1]-1));
                    }
                }break;
                case "g":{
                    mesh_now=new Mesh();
                    if(last_mtl!=null) {
                        if(last_mtl.texture_kd!=null)
                            mesh_now.bindTexture(gl2,last_mtl.texture_kd);
                    }
                    addGeom(mesh_now);
                }break;
                case "mtllib":{
                    try {
                        mtl_data=MtlLoader.loadMtl(new URL(Utils.getSubStr(url.toString(),"/")+items[1]));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                }break;
                case "usemtl":{
                    last_mtl=mtl_data.get(items[1]);
                    if(last_mtl.texture_kd!=null)
                        mesh_now.bindTexture(gl2,last_mtl.texture_kd);
                }break;
            }
        }
    }

    public int[][] getFaceData(String[] datas,int start,int end){
        int[][] res=new int[end-start][];
        for (int i = start; i < end; i++) {
            res[i-start]=Utils.str2int(datas[i].split("/"));
        }
        return res;
    }
}
