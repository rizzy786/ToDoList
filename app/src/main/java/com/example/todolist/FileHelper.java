package com.example.todolist;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.Resources;

public class FileHelper extends AppCompatActivity {

    public static final String FILENAME = "to_do_list.txt";

    public static ArrayList<String> readFile(Context context){
        ArrayList<String> mTodos = new ArrayList<String>();
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + File.separator  + FILENAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                mTodos.add(line);
            }
        } catch (IOException e) {
        }
        return mTodos;
    }

    public static void writeData(String itemToAdd, Context context){
        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator  + FILENAME);

        try {
            FileWriter fw = new FileWriter(file, true);
            fw.append(itemToAdd);
            fw.append("\n");
            fw.flush();
            fw.close();
        } catch (IOException e){
        }
    }

    public static void updateData(String newText, Context context) {
        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator  + FILENAME);
    }

    public static void deleteData(Context context) {
        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator  + FILENAME);
    }

    }
