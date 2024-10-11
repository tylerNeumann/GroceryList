package fvtc.edu.grocerylist;

import android.content.Context;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class FileIO {
    public static final String TAG = "FileIO";

    public static void writeFile(String filename, AppCompatActivity activity, String[] items) /*throws FileNotFoundException*/ {
        try {
            OutputStreamWriter writer = new OutputStreamWriter(activity.openFileOutput(filename, Context.MODE_PRIVATE));
            String line = "";
            for(int counter = 0; counter < items.length; counter++){
                line = items[counter];
                if(counter < items.length - 1)
                    line += "\r\n";
                writer.write(line);
                Log.d(TAG, "writeFile: " + line);

            }writer.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "WriteFile: FileNotFoundException" + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "WriteFile: IOException" + e.getMessage());
            e.printStackTrace();
        }
        catch (Exception e){
            Log.i(TAG, "WriteFile: " + e.getMessage());
        }
    }

    public ArrayList<String> readFile(String filename, AppCompatActivity activity){
        ArrayList<String> items = new ArrayList<String>();
        try{
            InputStream inputStream = activity.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null){
                items.add(line);
            }
            bufferedReader = null;
            inputStreamReader.close();
            inputStream.close();

        }catch (Exception e){
            Log.d(TAG, "readFile: " + e.getMessage());
        }
        return items;
    }
}
