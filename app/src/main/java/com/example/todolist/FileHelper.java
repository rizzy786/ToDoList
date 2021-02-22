package com.example.todolist;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import androidx.appcompat.app.AppCompatActivity;

public class FileHelper extends AppCompatActivity {

    public static final String FILENAME = "to_do_list.txt";

    public static ArrayList<String> readFile(Context context) {
        ArrayList<String> mTodos = new ArrayList<String>();
        //auto-generate file if not found
        try {
            File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + FILENAME);
            if (!file.exists()) {
                file.createNewFile();
            }
            //read text file line by line
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                mTodos.add(line);
            }
            br.close();
        } catch (IOException e) {
        }
        return mTodos;
    }

    /**
     * add data to the textfile when user adds item
     * @param itemToAdd
     * @param context
     */
    public static void writeData(String itemToAdd, Context context) {
        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + FILENAME);

        try {
            FileWriter fw = new FileWriter(file, true);
            fw.append(itemToAdd);
            fw.append("\n");
            fw.close();
        } catch (IOException e) {
        }
    }

    public static void updateData(int lineNo, String newText, Context context) {
        rewriteFile(lineNo, newText, true, context);
    }

    public static void deleteData(int lineNo, Context context) {
        rewriteFile(lineNo, null, false, context);
    }

    private static void rewriteFile(int lineNo, String newText, boolean updateData, Context context){
        File file = new File(context.getFilesDir().getAbsolutePath() + File.separator + FILENAME);
        File tmpFile = new File(context.getFilesDir().getAbsolutePath() + File.separator + "tmp_" + FILENAME);

        try {
            if (!tmpFile.exists()) {
                tmpFile.createNewFile();
            }

            BufferedReader br = new BufferedReader(new FileReader(file));
            FileWriter fw = new FileWriter(tmpFile, true);

            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                if (i != lineNo) {
                    fw.append(line);
                    fw.append("\n");
                } else {
                    if (updateData){
                        fw.append(newText);
                        fw.append("\n");
                    }
                }
                i++;
            }
            br.close();
            fw.close();

            file.delete();
            tmpFile.renameTo(file);
        } catch (IOException e) {
        }
    }
}