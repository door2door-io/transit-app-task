package com.eutechpro.allytransitapp.utils;

import android.content.res.AssetManager;
import android.support.test.InstrumentationRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @Unused for now
 * Util for services mock.
 * Created by Kursulla on 07/11/15.
 */
public class AssetsUtil {
    public static String getStringFromFile(String filePath) {
        AssetManager am = InstrumentationRegistry.getContext().getAssets();
        String retString = "";
        try {
            InputStream is = am.open(filePath);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            retString = sb.toString();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retString;
    }
}
