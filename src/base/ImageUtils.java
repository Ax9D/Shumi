package base;

import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class ImageUtils {
    public static ByteBuffer toByteBuffer(BufferedImage image, int width, int height) {
        int[] pixels = new int[width * height];
        image.getRGB(0, 0, width, height, pixels, 0, width);

        ByteBuffer ret = BufferUtils.createByteBuffer(width * height * 4);

        byte r, g, b, a;
        int pixel;
        for (int i = 0; i < pixels.length; i++) {
            pixel = pixels[i];
            a = (byte) ((pixel >> 24) & 0xFF);
            r = (byte) ((pixel >> 16) & 0xFF);
            g = (byte) ((pixel >> 8) & 0xFF);
            b = (byte) ((pixel >> 0) & 0xFF);
            ret.put(r);
            ret.put(g);
            ret.put(b);
            ret.put(a);
        }
        ret.flip();
        return ret;
    }
}
