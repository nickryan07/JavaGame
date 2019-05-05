package engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static final int width = 1600, height = 900;
	private static final int FPS = 144;

	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3,3)
			.withForwardCompatible(true)
			.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Java Game");
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, width, height);
		
	}
	
	public static void updateDisplay() {
		Display.sync(FPS);
		Display.update();
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
}
