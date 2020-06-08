package gl7ecore.utils;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

public class Texture3D {
    public int texHeight= 200;
    public int texWidth = 200;
    public int texDepth = 200;

    public int texid;

    public Texture3D(URL url){
        try {
            load3DTexture(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] loadData(InputStream in) throws IOException {
        DataInputStream is=new DataInputStream(in);
        texWidth=is.readInt();
        texHeight=is.readInt();
        texDepth=is.readInt();

        byte[] bys=new byte[texWidth*texHeight*texDepth*4];
        is.read(bys);

        return bys;
    }

    public int load3DTexture(URL url) throws IOException {
        GL2 gl = (GL2) GLContext.getCurrentGL();
        gl.glEnable(GL2.GL_TEXTURE_3D);

        byte[ ] data = loadData(url.openStream());
        ByteBuffer bb = Buffers.newDirectByteBuffer(data);
        int[ ] textureIDs = new int[1];
        gl.glGenTextures(1, textureIDs, 0);
        texid = textureIDs[0];
        gl.glBindTexture(GL2.GL_TEXTURE_3D, texid);
        gl.glTexStorage3D(GL2.GL_TEXTURE_3D, 1, GL2.GL_RGBA8, texWidth, texHeight,
                texDepth);
        gl.glTexSubImage3D(GL2.GL_TEXTURE_3D, 0, 0, 0, 0,
                texWidth, texHeight, texDepth, GL2.GL_RGBA,
                GL2.GL_UNSIGNED_INT_8_8_8_8_REV, bb);
        gl.glTexParameteri(GL2.GL_TEXTURE_3D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);
        return texid;
    }

    public void bind(GL2 gl2){
        gl2.glActiveTexture(GL2.GL_TEXTURE0);
        gl2.glBindTexture(GL2.GL_TEXTURE_3D, texid);
    }
}
