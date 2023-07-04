package ru.netology.graphics.image;

public class ColorConverter implements TextColorSchema {
    char[] symbols = new char[]{'#', '$', '@', '%', '*', '+', '-', '\''};
    @Override
    public char convert(int color) {
        return symbols[(int) Math.floor(color / 256. * symbols.length)];
    }

}