package com.hunt.example.util;

import org.apache.commons.io.IOUtils;

import java.io.FileInputStream;

public class Util {
    public static byte[] readFileToBytes(String filenName) {
      //  IOUtils.toByteArray()
        try {
            FileInputStream in = new FileInputStream(filenName);
            byte [] data = IOUtils.toByteArray(in);
            in.close();
            return data;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
