package org.david.notebook;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import java.io.FileOutputStream;


public class NewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText editTitleNew = (EditText) findViewById(R.id.editTitleNew);

        final EditText editBodyNew = (EditText) findViewById(R.id.editBodyNew);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleString = editTitleNew.getText().toString();
                String bodyString = editBodyNew.getText().toString();

                saveFile(titleString, bodyString);
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

}
