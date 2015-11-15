package org.david.notebook;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_EXIT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(), NewActivity.class);
                startActivityForResult(newIntent, REQUEST_EXIT);
            }
        });

        ArrayList arrayList = new ArrayList<String>();
        ListView list = (ListView) findViewById(R.id.listView);
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.my_text_view, arrayList);

        list.setAdapter(adapter);

        final String[] files;
        files = getFiles();
        for (String file : files) {
            adapter.add(file);
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = (String) parent.getItemAtPosition(position);

                Intent editIntent = new Intent(getApplicationContext(), EditActivity.class);
                editIntent.putExtra("title", title);
                startActivityForResult(editIntent, REQUEST_EXIT);
            }
        });

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            int selected;

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                selected = position;

                // hides action bar
                getSupportActionBar().hide();
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // Respond to clicks on the actions in the CAB
                switch (item.getItemId()) {
                    case R.id.deleteNode:
                        deleteNote(files[selected]);
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    case R.id.shareNode:
                        shareNode(files[selected]);
                        mode.finish(); // Action picked, so close the CAB
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Inflate the menu for the CAB

                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // Here you can make any necessary updates to the activity when
                // the CAB is removed. By default, selected items are deselected/unchecked.

                // refresh Activity
                recreate();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an invalidate() request
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_EXIT) {
            if (resultCode == RESULT_OK) {
                // restart Activity when child Activity gets closed
                recreate();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void shareNode(String file) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");

        String shareBody;

        try {
            shareBody = file + "\n" + readFile(file);
        }
        catch (Exception e) {
            shareBody = file;
        }

        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, file);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    public String[] getFiles() {
        File f = new File(this.getFilesDir().getPath());
        File[] files = f.listFiles();
        String[] stringFiles = new String[files.length];

        for (int i = 0; i < files.length; i++) {
            stringFiles[i] = files[i].getName();
        }
        return stringFiles;
    }

    private void deleteNote(String file) {
        File dir = getFilesDir();
        File del_file = new File(dir, file);
        del_file.delete();
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
}
