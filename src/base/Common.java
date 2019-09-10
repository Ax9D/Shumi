package base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;

import org.lwjgl.BufferUtils;

public class Common {

	public static FloatBuffer toFloatBuffer(float[] x) {
		FloatBuffer f = BufferUtils.createFloatBuffer(x.length);
		f.put(x);
		f.flip();
		return f;
	}

	public static IntBuffer toIntBuffer(int[] x) {
		IntBuffer i = BufferUtils.createIntBuffer(x.length);
		i.put(x);
		i.flip();
		return i;
	}

	public static String getText(String path) {
		String txt = "";
		try {
			FileInputStream fs = new FileInputStream(path);
			Scanner sc = new Scanner(fs);
			while (sc.hasNextLine())
				txt += sc.nextLine() + "\n";
			sc.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return txt;

	}
}
