package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import gl7ecore.Utils;
import gl7ecore.utils.Mtl;
import gl7ecore.utils.MtlLoader;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DAEModel extends GeomGroup{

    public DAEModel(){
        draw_type= GL2.GL_TRIANGLES;
    }

    public DAEModel(GL2 gl2, URL url){
        this();
        try {
            loadDAE(gl2,url);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    public void loadDAE(GL2 gl2,URL url) throws DocumentException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);

        Element rootElement = document.getRootElement();

        Iterator iterator = rootElement.elementIterator();
        while (iterator.hasNext()){
            Element item = (Element) iterator.next();

        }
    }

    public Mesh loadGeometry(Element node){
        Mesh mesh=new Mesh();

        Element node_mesh=node.element("mesh");
        node_mesh.elements("");

        return mesh;
    }

    public int[][] getFaceData(String[] datas,int start,int end){
        int[][] res=new int[end-start][];
        for (int i = start; i < end; i++) {
            res[i-start]=Utils.str2int(datas[i].split("/"));
        }
        return res;
    }
}
