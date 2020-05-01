package gl7ecore;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Utils {

    public static String readResources(String file){
        try {
            InputStream is = Utils.class.getClass().getResourceAsStream(file);
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

}
