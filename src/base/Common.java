package base;

import org.json.JSONArray;
import org.json.JSONException;
import org.lwjgl.BufferUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Scanner;

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


	public static String[] getStringArrFromJSON(JSONArray ja) throws JSONException {
		String[] ret;
		ret = new String[ja.length()];
		for (int i = 0; i < ja.length(); i++)
			ret[i] = ja.getString(i);
		return ret;
	}

	public static float[] getFloatArrFromJSON(JSONArray ja) throws JSONException {
		float[] ret;
		ret = new float[ja.length()];
		for (int i = 0; i < ja.length(); i++)
			ret[i] = (float) ja.getDouble(i);

		return ret;
	}

	public static int[] getIntArrFromJSON(JSONArray ja) throws JSONException {
		int[] ret;
		ret = new int[ja.length()];
		for (int i = 0; i < ja.length(); i++)
			ret[i] = ja.getInt(i);

		return ret;
	}

}
