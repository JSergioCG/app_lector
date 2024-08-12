package com.example.appservicesce;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

public class Files {

    public static void writeToInternal(Context context, Serializable object, String filename){
        try {
            //File root1 = new File(context.getFilesDir(), "MyApp");
            //or
            File root = new File("/data/user/0/com.example.myapplication/files/MyApp/");
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, filename);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutput out = new ObjectOutputStream(fos);
            out.writeObject(object);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Object read(String filename) {
        try {
            File file = new File("/storage/emulated/0/MyApp/" + filename);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream input = new ObjectInputStream(fis);
            Object data = (Object) input.readObject();
            input.close();
            return data;
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
