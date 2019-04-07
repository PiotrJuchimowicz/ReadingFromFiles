package com.company.reader;

public abstract class MyFileReader {
    private static final String CHARSET = "UTF-8";

    protected static String getCHARSET() {
        return CHARSET;
    }

    public abstract void saveFromFile(String filePath);
}
