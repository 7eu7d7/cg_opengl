package gl7ecore.geom;

import assimp.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import gl7ecore.Utils;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class FBXModel extends GeomGroup{

    private URL url_load;

    public FBXModel(){
        draw_type= GL2.GL_TRIANGLES;
    }

    public FBXModel(GL2 gl2,URL url){
        this();
        try {
            loadFBX(gl2,url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadFBX(GL2 gl2,URL url) throws Exception {
        url_load=url;
        AiScene scene=new Importer().readFile(url);
        if(scene==null || scene.rootNode==null){
            throw new Exception("load error");
        }

        List<AiMaterial> materials=scene.getMaterials();

        for (AiMesh aiMesh : scene.getMeshes()) {
            Mesh now_mesh=new Mesh();

            boolean has_norm=aiMesh.getHasNormals();

            List<Vec3> vertices=aiMesh.getVertices();
            List<List<Vec4>> colors=aiMesh.getColors();
            List<List<float[]>> coords=aiMesh.getTextureCoords();
            List<Vec3> normals=aiMesh.getNormals();


            float[] tmp;
            for (int i = 0; i < vertices.size(); i++) {
                now_mesh.addVertex(vertices.get(i));

                //color
                if(aiMesh.hasVertexColors(i)) {
                    now_mesh.addColor(colors.get(0).get(i));
                } else {
                    now_mesh.addColor(1,1,1,1);
                }

                //normal
                if(has_norm){
                    now_mesh.addNormal(normals.get(i));
                }

                //Texture
                /*if((tmp=coords.get(0).get(i))!=null){
                    //tmp=coords.get(i).get(0);
                    now_mesh.addTex(Utils.check_pos(tmp[0]),Utils.check_pos(tmp[1]));
                } else {
                    now_mesh.addTex(0,0);
                }*/
            }

            /*aiMesh.getVertices().forEach(now_mesh::addVertex);
            if(aiMesh.hasVertexColors(0))
                aiMesh.getColors().forEach((x)->now_mesh.addColor(x.get(0)));
            if(aiMesh.getHasNormals())
                aiMesh.getNormals().forEach(now_mesh::addNormal);
            aiMesh.getTextureCoords().forEach((x)->now_mesh.addTex(x.get(0)[0],x.get(0)[1]));*/

            //材质
            if(aiMesh.getMaterialIndex()>0){
                Texture texture=loadMaterialTextures(materials.get(aiMesh.getMaterialIndex()), AiTexture.Type.diffuse);
                System.out.println(texture);
                if(texture!=null)
                    now_mesh.bindTexture(gl2, texture);
            }
            addGeom(now_mesh);
        }
    }

    public Texture loadMaterialTextures(AiMaterial material, AiTexture.Type type){
        System.out.println(material.getTextures().size());
        System.out.println(material.getName());
        for (AiMaterial.Texture texture : material.getTextures()) {
            System.out.println(texture.getType());
            if(texture.getType()== type){
                try {
                    return Utils.loadTexture(new URL(Utils.getSubStr(url_load.toString(),"/")+texture.getFile()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
