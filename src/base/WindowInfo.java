package base;

public class WindowInfo {
    public static long window;
    public static int WIDTH = 1920;
    public static int HEIGHT = 1080;


    public static void updateWindow(int WIDTH, int HEIGHT) {
        WindowInfo.WIDTH = WIDTH;
        WindowInfo.HEIGHT = HEIGHT;
    }
}
