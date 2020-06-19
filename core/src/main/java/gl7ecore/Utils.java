package gl7ecore;

import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.spi.DDSImage;
import gl7ecore.utils.DDSReader;
import glm.mat._4.Mat4;
import glm.quat.Quat;
import glm.vec._3.Vec3;
import org.joml.Matrix4f;
import org.lwjgl.assimp.AIMatrix4x4;
import org.lwjgl.assimp.AIQuaternion;
import org.lwjgl.assimp.AIVector3D;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class Utils {
    public static HashMap<URL,Texture> tex_pool=new HashMap<URL,Texture>();

    public static Texture loadTexture(URL url) throws GLException, IOException
    {
        if(tex_pool.containsKey(url))
            return tex_pool.get(url);

        //String font=getSubStr_start(url.toString(),".").toLowerCase();
        BufferedImage bim=null;
        if(url.toString().endsWith(".dds")) {
            bim = DDSReader.read(url.openStream());
        }else {
            bim = ImageIO.read(url.openStream());
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bim, "png", os);
        InputStream fis = new ByteArrayInputStream(os.toByteArray());
        Texture texture=TextureIO.newTexture(fis, true, TextureIO.PNG);
        tex_pool.put(url,texture);
        return texture;
    }

    public static TextureData loadTextureData(URL url) throws GLException, IOException
    {
        BufferedImage bim=null;
        if(url.toString().endsWith(".dds")) {
            bim = DDSReader.read(url.openStream());
        }else {
            bim = ImageIO.read(url.openStream());
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(bim, "png", os);
        InputStream fis = new ByteArrayInputStream(os.toByteArray());
        TextureData texture=TextureIO.newTextureData(GLContext.getCurrentGL().getGLProfile(), fis, true, TextureIO.PNG);
        return texture;
    }

    public static Texture loadTexture(String file,Class cls) throws GLException, IOException
    {
        return loadTexture(cls.getResource(file));
    }

    public static String readResources(String file,Class cls){
        return readURL(cls.getResource(file));
    }

    public static String readURL(URL url){
        try {
            InputStream is = url.openStream();
            byte[] bys=new byte[is.available()];
            is.read(bys);
            return new String(bys);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readFile(String file){
        try {
            InputStream is = new FileInputStream(file);
            byte[] bys=new byte[is.available()];
            is.read(bys);
            return new String(bys);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static float[] list2arrf(List<Float> list){
        float[] arr=new float[list.size()];
        int idx=0;
        for(float f:list)
            arr[idx++]=f;
        return arr;
    }

    public static int[] list2arri(List<Integer> list){
        int[] arr=new int[list.size()];
        int idx=0;
        for(int f:list)
            arr[idx++]=f;
        return arr;
    }

    public static float[] mix_arr(float[] a,int unita,float[] b,int unitb){
        float[] res=new float[a.length+b.length];
        int idxa=0,idxb=0;
        int idx=0;

        while (idxa<a.length && idxb<b.length){
            for(int i=0;i<unita;i++){
                res[idx++]=a[idxa++];
            }

            for(int i=0;i<unitb;i++){
                res[idx++]=b[idxb++];
            }
        }

        return res;
    }

    public static float cosd(float deg){
        return (float) Math.cos(Math.toRadians(deg));
    }

    public static float sind(float deg){
        return (float) Math.sin(Math.toRadians(deg));
    }

    public static float[] str2float(String[] strs,int start,int end){
        float[] farr=new float[end-start];
        for (int i = start; i < end; i++) {
            farr[i-start]=Float.parseFloat(strs[i]);
        }
        return farr;
    }

    public static int[] str2int(String[] strs,int start,int end){
        int[] farr=new int[end-start];
        for (int i = start; i < end; i++) {
            farr[i-start]=Integer.parseInt(strs[i]);
        }
        return farr;
    }

    public static String getSubStr(String str,String end){
        return str.substring(0,str.lastIndexOf(end)+end.length());
    }

    public static String getSubStr_start(String str,String start){
        return str.substring(str.lastIndexOf(start)+start.length());
    }

    public static float[] str2float(String[] strs){
        return str2float(strs,0,strs.length);
    }
    public static int[] str2int(String[] strs){
        return str2int(strs,0,strs.length);
    }

    public static float check_pos(float a){
        return a>=0?a:1+a;
    }

    public static String getEncoding(String str) {
        String encode = "UTF-8";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是UTF-8
                String s2 = encode;
                return s2;
            }
        } catch (Exception exception2) {
        }
        encode = "GB2312";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GB2312
                String s = encode;
                return s; //是的话，返回“GB2312“，以下代码同理
            }
        } catch (Exception exception) {
        }
        encode = "ISO-8859-1";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是ISO-8859-1
                String s1 = encode;
                return s1;
            }
        } catch (Exception exception1) {
        }

        encode = "GBK";
        try {
            if (str.equals(new String(str.getBytes(encode), encode))) { //判断是不是GBK
                String s3 = encode;
                return s3;
            }
        } catch (Exception exception3) {
        }
        return "";
    }

    public static Mat4 Aimat2Mat(AIMatrix4x4 aimat){
        Mat4 mat=new Mat4(aimat.a1(),aimat.a2(),aimat.a3(),aimat.a4(),
                          aimat.b1(),aimat.b2(),aimat.b3(),aimat.b4(),
                          aimat.c1(),aimat.c2(),aimat.c3(),aimat.c4(),
                          aimat.d1(),aimat.d2(),aimat.d3(),aimat.d4());
        return mat;
    }

    public static Matrix4f Aimat2Matf(AIMatrix4x4 aimat){
        Matrix4f mat=new Matrix4f(aimat.a1(),aimat.a2(),aimat.a3(),aimat.a4(),
                aimat.b1(),aimat.b2(),aimat.b3(),aimat.b4(),
                aimat.c1(),aimat.c2(),aimat.c3(),aimat.c4(),
                aimat.d1(),aimat.d2(),aimat.d3(),aimat.d4());
        return mat;
    }

    public static Vec3 Aiv2Vec(AIVector3D v3d){
        return new Vec3(v3d.x(),v3d.y(),v3d.z());
    }

    public static Quat Aiq2Quat(AIQuaternion quat){
        return new Quat(quat.x(),quat.y(),quat.z(),quat.w());
    }

}
