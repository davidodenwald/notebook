package org.david.notebook;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class EditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String titleString;
        String bodyString;

        try {
            Bundle bundle = getIntent().getExtras();
            titleString = bundle.getString("title");
        } catch (NullPointerException e) {
            titleString = "";
        }
        try {
            bodyString = readFile(titleString);
        }
        catch (Exception e) {
            bodyString = "";
        }

        final String fileName = titleString;

        final EditText editTitleEdit = (EditText) findViewById(R.id.editTitleEdit);
        editTitleEdit.setText(titleString, TextView.BufferType.EDITABLE);

        final EditText editBodyEdit = (EditText) findViewById(R.id.editBodyEdit);
        editBodyEdit.setText(bodyString, TextView.BufferType.EDITABLE);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleString = editTitleEdit.getText().toString();
                String bodyString = editBodyEdit.getText().toString();

                if(titleString.isEmpty()) {
                    deleteNote(fileName);
                }
                else {
                    if (titleString != fileName) {
                        deleteNote(fileName);
                    }
                    saveFile(titleString, bodyString);

                }
                setResult(RESULT_OK, null);
                finish();

           }
        });
    }

    private void saveFile(String title, String body) {
        try {
            FileOutputStream fos = openFileOutput(title, Context.MODE_PRIVATE);
            fos.write(body.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String readFile(String file) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(openFileInput(file)));
            String inputString;
            while ((inputString = inputReader.readLine()) != null) {
                stringBuffer.append(inputString + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        String str = stringBuffer.toString();
        return str.substring(0, str.length()-1);
    }

    private void deleteNote(String file) {
        File dir = getFilesDir();
        File del_file = new File(dir, file);
        del_file.delete();
    }
}
