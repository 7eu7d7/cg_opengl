package gl7ecore.geom;

import assimp.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.texture.Texture;
import gl7ecore.Utils;
import glm_.mat4x4.Mat4;
import glm_.vec3.Vec3;
import glm_.vec4.Vec4;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class DAEModel extends GeomGroup{

    private URL url_load;

    public DAEModel(){
        draw_type= GL2.GL_TRIANGLES;
    }

    public DAEModel(GL2 gl2, URL url){
        this();
        try {
            loadDAE(gl2,url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadDAE(GL2 gl2,URL url) throws Exception {
        url_load=url;
        AiScene scene=new Importer().readFile(url);
        if(scene==null || scene.rootNode==null){
            throw new Exception("load error");
        }

        HashMap<String,String> tex_map=loadTexFile(new URL(url_load.toString()+".tex"));
        List<AiMaterial> materials=scene.getMaterials();
        List<AiAnimation> animations=scene.getAnimations();

        for (AiMesh aiMesh : scene.getMeshes()) {
            Mesh now_mesh=aiMesh.getHasBones()?new BoneMesh(scene.getRootNode()):new Mesh();

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
                if((tmp=coords.get(0).get(i))!=null){
                    now_mesh.addTex(Utils.check_pos(tmp[0]),Utils.check_pos(tmp[1]));
                } else {
                    now_mesh.addTex(0,0);
                }

            }

            //Bone
            if(aiMesh.getHasBones()){
                System.out.println("load bone");
                List<AiBone> bones=aiMesh.getBones();
                ((BoneMesh)now_mesh).setAnimations(animations);
                ((BoneMesh)now_mesh).addBones(bones);
            }

            //材质
            if(tex_map.containsKey(aiMesh.getName())){
                //Texture texture=loadMaterialTextures(materials.get(aiMesh.getMaterialIndex()), AiTexture.Type.diffuse);
                Texture texture=Utils.loadTexture(new URL(Utils.getSubStr(url_load.toString(),"/")+tex_map.get(aiMesh.getName())));
                System.out.println(texture);
                now_mesh.bindTexture(gl2, texture);
            }
            addGeom(now_mesh);
        }
    }


    public HashMap<String,String> loadTexFile(URL tex_url){
        HashMap<String,String> tex_map=new HashMap<String,String>();
        String[] texs=Utils.readURL(tex_url).split("\n");
        for (String tex : texs) {
            String[] strs=tex.split(" ");
            tex_map.put(strs[0],strs[1]);
        }
        return tex_map;
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
