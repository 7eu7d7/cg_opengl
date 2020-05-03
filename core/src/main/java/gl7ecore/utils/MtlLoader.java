package gl7ecore.utils;

import com.jogamp.opengl.util.texture.Texture;
import gl7ecore.Utils;
import gl7ecore.geom.Mesh;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class MtlLoader {

    public static HashMap<String,Mtl> loadMtl(URL url){
        String mtl=Utils.readURL(url);
        String[] datas=mtl.split("\n");
        HashMap<String,Mtl> mtls=new HashMap<String, Mtl>();
        Mtl mtl_now=null;

        for(String line:datas){
            line=line.trim();
            String[] items=line.split(" +");
            switch (items[0]){
                case "newmtl":{
                    mtl_now=new Mtl();
                    mtls.put(items[1],mtl_now);
                }break;
                case "map_Kd":{
                    try {
                        Texture tex=Utils.loadTexture(new URL(Utils.getSubStr(url.toString(),"/")+items[1]));
                        mtl_now.setMap_KD(tex);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }break;
            }
        }
        return mtls;
    }
}
