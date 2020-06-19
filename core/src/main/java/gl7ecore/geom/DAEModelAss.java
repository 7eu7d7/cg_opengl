package gl7ecore.geom;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;
import gl7ecore.Utils;
import gl7ecore.anim.Animation;
import gl7ecore.bones.AnimatedBoneLoader;
import gl7ecore.bones.Bone;
import gl7ecore.bones.BoneVertex;
import gl7ecore.bones.Node;
import glm.vec._3.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

import static org.lwjgl.assimp.Assimp.*;

public class DAEModelAss extends GeomGroup{

    private URL url_load;

    private String path_model;

    public DAEModelAss(){
        draw_type= GL2.GL_TRIANGLES;
    }

    /*public DAEModelAss(GL2 gl2, URL url){
        this();
        try {
            loadDAE(gl2,url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    public void loadDAE(GL2 gl2,String path) throws Exception {
        /*url_load=url;
        InputStream is=url.openStream();
        byte[] bys=new byte[is.available()];

        AIScene scene=aiImportFile(GLBuffers.newDirectByteBuffer(bys),aiProcess_GenSmoothNormals | aiProcess_Triangulate);*/

        path_model=path;
        AIScene scene=aiImportFile(path,aiProcess_GenSmoothNormals | aiProcess_Triangulate);
        System.out.println(scene);
        if(scene==null){
            throw new Exception("load error");
        }

        ArrayList<AIMaterial> materials=getMaterials(scene.mNumMaterials(), scene.mMaterials());
        ArrayList<AIAnimation> animations=getAnimations(scene.mNumAnimations(),scene.mAnimations());

        for (AIMesh aiMesh : getMeshs(scene.mNumMeshes(),scene.mMeshes())) {
            Mesh now_mesh = aiMesh.mNumBones()>0 ? new BoneMesh() : new Mesh();
            //Mesh now_mesh = new Mesh();

            //vertex
            aiMesh.mVertices().forEach((vtx)->{
                now_mesh.addVertex(vtx.x(),vtx.y(),vtx.z());
            });

            //color
            if(aiMesh.mColors(0)!=null){
                aiMesh.mColors(0).forEach((color)->{
                    now_mesh.addColor(color.r(),color.g(),color.b(),color.a());
                });
            }

            //normal
            if(aiMesh.mNormals()!=null) {
                aiMesh.mNormals().forEach((nor) -> {
                    now_mesh.addNormal(nor.x(), nor.y(), nor.z());
                });
            }

            //Texture UV
            if (aiMesh.mTextureCoords(0) != null) {
                aiMesh.mTextureCoords(0).forEach((tex)->{
                    now_mesh.addTex(tex.x(),tex.y());
                });
            }

            //Bone
            if (aiMesh.mNumBones()>0) {
                System.out.println("load bone");
                List<AIBone> bones = getBones(aiMesh.mNumBones(),aiMesh.mBones());
                HashMap<Integer,BoneVertex> weight_map=new HashMap<Integer,BoneVertex>();
                HashMap<String,Bone> bone_map=AnimatedBoneLoader.processBones(bones,weight_map);

                AINode aiRootNode = scene.mRootNode();
                Matrix4f rootTransfromation = AnimatedBoneLoader.toMatrix(aiRootNode.mTransformation());
                Node rootNode = AnimatedBoneLoader.processNodesHierarchy(aiRootNode, null);

                List<Bone> bone_list=new ArrayList<Bone>(bone_map.values());
                bone_list.sort(Comparator.comparingInt(x -> x.boneId));

                HashMap<String, Animation> anim_map=AnimatedBoneLoader.processAnimations(scene,bone_list,rootNode,rootTransfromation);

                ((BoneMesh) now_mesh).setAnimations(anim_map);

                List<BoneVertex> bv_list=new LinkedList<BoneVertex>();
                for(int i=0;i<weight_map.size();i++){
                    bv_list.add(weight_map.get(i));
                }

                ((BoneMesh) now_mesh).setVtxWeights(bv_list);

                ((BoneMesh) now_mesh).selectAnimation(0);

                /*((BoneMesh) now_mesh).setAnimations(animations);
                ((BoneMesh) now_mesh).addBones(bones);*/
            }

            //材质
            //System.out.println(aiMesh.getName() + "," + aiMesh.getMaterialIndex());
            if (aiMesh.mMaterialIndex()>=0) {
                Texture texture=loadMaterialTextures(materials.get(aiMesh.mMaterialIndex()), aiTextureType_DIFFUSE);
                //Texture texture = Utils.loadTexture(new URL(Utils.getSubStr(url_load.toString(), "/") + tex_map.get(aiMesh.getName())));
                System.out.println(texture);
                now_mesh.bindTexture(gl2, texture);
            }
            addGeom(now_mesh);
        }

    }

    public ArrayList<AIMesh> getMeshs(int count, PointerBuffer buffer){
        ArrayList<AIMesh> meshes = new ArrayList<AIMesh>();
        for (int i = 0; i < count; ++i) {
            meshes.add(AIMesh.create(buffer.get(i)));
        }
        return meshes;
    }

    public ArrayList<AIMaterial> getMaterials(int count, PointerBuffer buffer){
        ArrayList<AIMaterial> meshes = new ArrayList<AIMaterial>();
        for (int i = 0; i < count; ++i) {
            meshes.add(AIMaterial.create(buffer.get(i)));
        }
        return meshes;
    }

    public ArrayList<AIAnimation> getAnimations(int count, PointerBuffer buffer){
        ArrayList<AIAnimation> meshes = new ArrayList<AIAnimation>();
        for (int i = 0; i < count; ++i) {
            meshes.add(AIAnimation.create(buffer.get(i)));
        }
        return meshes;
    }

    public ArrayList<AIBone> getBones(int count, PointerBuffer buffer){
        ArrayList<AIBone> meshes = new ArrayList<AIBone>();
        for (int i = 0; i < count; ++i) {
            meshes.add(AIBone.create(buffer.get(i)));
        }
        return meshes;
    }

    public Texture loadMaterialTextures(AIMaterial material, int type){
        AIString aistr=AIString.create();
        aiGetMaterialTexture(material,type,0,aistr,(int[]) null,null,null,null,null,null);
        System.out.println(aistr.dataString());

        try {
            return Utils.loadTexture(new File(new File(path_model).getParent(),aistr.dataString()).toURL());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //System.out.println(material.getName());
        /*for (AIMaterial.Texture texture : material.getTextures()) {
            System.out.println(texture.getType());
            if(texture.getType()== type){
                try {
                    return Utils.loadTexture(new URL(Utils.getSubStr(url_load.toString(),"/")+texture.getFile()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        return null;
    }


}
