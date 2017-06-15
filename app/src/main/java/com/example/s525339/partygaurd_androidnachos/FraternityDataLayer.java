package com.example.s525339.partygaurd_androidnachos;

import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * This is FraternityDataLayer class where file path to the json file is provided
 */

public class FraternityDataLayer {
    private String fraternityJSON;
    private String fileName = "C:\\Users\\Janeet Suneetha\\Documents\\Fourth Semester\\GDP\\Gandhapuneni_Fraternities_JSON.txt";


    public String getFraternityJSON() throws IOException {
        String fraternityJSON = readFile(fileName);
        return fraternityJSON;
    }

    private String readFile(String fileName) throws IOException {
        StringBuilder jsonBuilder = new StringBuilder();
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str;
        while ((str = in.readLine()) != null) {
            jsonBuilder.append(str);
        }
        in.close();
        return jsonBuilder.toString();
    }

//    private String getJSONFromURL(String fraternityJSON) {
//        StringBuilder jsonBuilder = new StringBuilder();
//
//        try {
//            URL url = new URL(urlstr);
//            BufferedReader jsonReader = new BufferedReader(new InputStreamReader(url.openStream()));
//
//            String line;
//
//            while((line = jsonReader.readLine()) != null)
//            {
//                jsonBuilder.append(line);
//            }
//        }catch (IOException e)
//        {
//            e.printStackTrace();
//        }
//
//        return jsonBuilder.toString();
//    }
}
