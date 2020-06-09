package gl7e;

import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import gl7ecore.Screen;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

/**
 * A minimal program that draws with JOGL in a Swing JFrame using the AWT GLCanvas.
 *
 * @author Wade Walker
 */
public class Main {
    public static JFrame jframe;
    public static JPanel panel;
    public static Screen screen=new Screen();

    public static int w=1280,h=720;

    public static void main( String [] args ) {

        screen.view=new ViewTest9();

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

        //key event
        KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventPostProcessor(event -> screen.onKey(event));

        //mouse event
        long eventMask = AWTEvent.MOUSE_MOTION_EVENT_MASK + AWTEvent.MOUSE_EVENT_MASK;
        Toolkit.getDefaultToolkit().addAWTEventListener(e -> {
            if (e instanceof MouseEvent){
                screen.onMouse((MouseEvent) e);
            }
        }, eventMask);

        panel=new JPanel();
        //panel.setSize(w,h);
        glcanvas.setSize(w,h);
        panel.setLayout(null);
        panel.add(glcanvas);


        jframe = new JFrame( "Opengl CG" );
        jframe.addWindowListener( new WindowAdapter() {
            public void windowClosing( WindowEvent windowevent ) {
                jframe.dispose();
                System.exit( 0 );
            }
        });

        jframe.getContentPane().add( panel, BorderLayout.CENTER );
        jframe.setSize( w, h );
        jframe.setVisible( true );

        //updater
        new Thread(){
            @Override
            public void run() {
                while (true) {
                    long start = System.currentTimeMillis();
                    glcanvas.repaint();
                    try {
                        Thread.sleep(Math.max(0,20 - (System.currentTimeMillis() - start)));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}