package engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

public class DisplayManager {
	
	private static final int width = 1600, height = 900;
	private static final int FPS = 144;
	
	
    private static long lastFrame;
    private static float delta;
 
    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

	public static void createDisplay() {
		
		ContextAttribs attribs = new ContextAttribs(3,3)
			.withForwardCompatible(true)
			.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat(), attribs);
			//Display.setTitle("Java Game");
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GL11.glViewport(0, 0, width, height);
		lastFrame = getTime();
	}
	
	public static void updateDisplay() {
		Display.sync(FPS);
		Display.update();
		long currentFrameTime = getTime();
		delta = (currentFrameTime - lastFrame)/1000f;
		lastFrame = currentFrameTime;
	}
	
	public static void closeDisplay() {
		Display.destroy();
	}
	
	public static float getFrameTimeSeconds() {
		return delta;
	}
	
}
