package animationEditor.main;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import org.lwjgl.opengl.Display;

public class Main {
	
	public static boolean closeRequested = false;

	public static void main(String[] args){
		Canvas canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(1000, 500));
		canvas.setSize(1000, 500);
		
		JFrame frame = new JFrame();
		frame.setPreferredSize(new Dimension(1000, 500));
		frame.setSize(1000, 500);
		frame.addWindowListener(new WindowAdapter()
		{
		    public void windowClosing(WindowEvent e)
		    {
		        closeRequested = true;
		    }
		});
		
		frame.add(canvas);
		
		canvas.setVisible(true);
		frame.setVisible(true);
		Window.create("Animator", canvas);
		
		long lastTime = System.currentTimeMillis();
		while(!(Display.isCloseRequested() || closeRequested)){

			long time = System.currentTimeMillis();
			render((int)(time - lastTime));
			lastTime = time;
			
			try {
				Thread.sleep(17 - (int)(System.currentTimeMillis() - time));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Display.destroy();
		System.exit(0);
	}
	
	public static void render(int delta){
		System.out.println(delta);
	}
	
}
