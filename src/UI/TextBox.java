package UI;

import base.Color;
import base.GSystem;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;


public class TextBox{
    Vector2f topLeft;
    Vector2f size;
    String str;
    float textSize;
    GFont font;

    String[] words;
    int[] wordWidths;

    Vector4f textColor;
    public Frame parent;

    public TextBox(String str, Vector2f topLeft, float width, float height, float textSize) {
        this.str = str;
        this.topLeft = topLeft;
        size = new Vector2f(width, height);
        this.textSize = textSize / GFont.loadSize;
        getWords();
        setFont("");
        computeWords();
        setColor(Color.BLACK);
        this.parent = GSystem.ui.windowFrame;
    }

    public void setParent(Frame p) {
        this.parent = p;
    }

    private void getWords() {
        words = str.split(" ");
        wordWidths = new int[words.length];
    }

    public void setColor(Vector3f textColor) {
        this.textColor = new Vector4f(textColor,1);
    }
    public void setColor(Vector4f textColor) {
        this.textColor = textColor;
    }

    public void setString(String str) {
        this.str = str;
        getWords();
        computeWords();
    }
    private void computeWords() {
        if (words.length > 1) {
            wordWidths[0] = font.getWidth(words[0]);
            for (int i = 1; i < words.length - 1; i++)
                wordWidths[i] = font.getWidth(words[i]) + font.spaceWidth;
            wordWidths[words.length - 1] = font.getWidth(words[words.length - 1]);
        } else
            wordWidths[0] = words[0].length();
    }

    public void setFont(String fontName) {
        this.font = GSystem.rsmanager.getFont(fontName);
        computeWords();
    }
}
