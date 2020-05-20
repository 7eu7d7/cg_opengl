package gl7e;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import gl7ecore.Screen;

import javax.swing.JFrame;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

/**
 * A minimal program that draws with JOGL in a Swing JFrame using the AWT GLCanvas.
 *
 * @author Wade Walker
 */
public class Main {
    public static Screen screen=new Screen();

    public static void main( String [] args ) {

        screen.view=new ViewTest4();

        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities( glprofile );
        final GLCanvas glcanvas = new GLCanvas( glcapabilities );

        glcanvas.addGLEventListener( new GLEventListener() {

            @Override
            public void reshape( GLAutoDrawable glautodrawable, int x, int y, int width, int height ) {
                screen.setup(glautodrawable.getGL().getGL2(),width,height);
            }

            @Override
            public void init( GLAutoDrawable glautodrawable ) {
                screen.init(glautodrawable.getGL().getGL2());
            }

            @Override
            public void dispose( GLAutoDrawable glautodrawable ) {
            }

            @Override
            public void display( GLAutoDrawable glautodrawable ) {
                screen.render(glautodrawable.getGL().getGL2(), glautodrawable.getSurfaceWidth(), glautodrawable.getSurfaceHeight());
            }
        });

        final JFrame jframe = new JFrame( "Opengl CG" );
        jframe.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent windowevent ) {
                jframe.dispose();
                System.exit( 0 );
            }
        });

        jframe.getContentPane().add( glcanvas, BorderLayout.CENTER );
        jframe.setSize( 1280, 720 );
        jframe.setVisible( true );

        //updater
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    long start = System.currentTimeMillis();
                    glcanvas.repaint();
                    try {
                        Thread.sleep(20 - (System.currentTimeMillis() - start));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}