package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import gl7ecore.Constant;
import gl7ecore.Utils;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class Model7e extends GeomGroup{
    URL url;

    public void loadModel(GL2 gl2, URL url) throws DocumentException {
        this.url=url;
        SAXReader reader = new SAXReader();
        Document document = reader.read(url);

        Element rootElement = document.getRootElement();

        Iterator iterator = rootElement.elementIterator();
        while (iterator.hasNext()){
            Element item = (Element) iterator.next();
            switch (item.getName()){
                case "Mesh":{
                    createMesh(gl2,item);
                }break;
            }

            /*List<Attribute> attributes = stu.attributes();
            System.out.println("======获取属性值======");
            for (Attribute attribute : attributes) {
                System.out.println(attribute.getValue());
            }*/
        }
    }

    public void createMesh(GL2 gl2, Element elem){
        Mesh mesh=new Mesh();

        Iterator iterator = elem.elementIterator();
        while (iterator.hasNext()) {
            Element data = (Element) iterator.next();
            switch (data.getName()){
                case "Vertex":{
                    createVertex(data,mesh);
                }break;
                case "Texture":{
                    try {
                        URL texurl=new URL(Utils.getSubStr(url.toString(),"/")+data.getStringValue());
                        mesh.bindTexture(gl2, Utils.loadTexture(texurl));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }break;
                case "DrawType":{
                    mesh.draw_type= Constant.drawTypes.get(data.getStringValue());
                }break;
                case "Position":{
                    mesh.setPosition(new Vec3(Utils.str2float(data.getStringValue().split(","))));
                }break;
                case "Rotation":{
                    mesh.setRotation(new Vec3(Utils.str2float(data.getStringValue().split(","))));
                }break;
                case "Scale":{
                    mesh.setScale(new Vec3(Utils.str2float(data.getStringValue().split(","))));
                }break;
            }
        }

        addGeom(mesh);
    }

    public void createVertex(Element elem,Mesh mesh){
        Iterator iterator = elem.elementIterator();
        while (iterator.hasNext()) {
            Element data = (Element) iterator.next();
            String value=data.getStringValue();
            switch (data.getName()){
                case "pos":{
                    mesh.addVertex(new Vec3(Utils.str2float(value.split(","))));
                }break;
                case "color":{
                    mesh.addColor(Integer.parseUnsignedInt(value,16));
                }break;
                case "tex":{
                    float[] tex=Utils.str2float(value.split(","));
                    mesh.addTex(tex[0],tex[1]);
                }break;
            }
        }
    }
}
