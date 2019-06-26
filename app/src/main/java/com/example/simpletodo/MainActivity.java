package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    //numeric code to indentify the edit activity
    public final static int EDIT_REQUEST_CODE = 20;
    // keys used for passing data between activities
    public final static String ITEM_TEXT= "itemText";
    public final static String ITEM_POSITION = "itemPosition";

    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems = (ListView) findViewById(R.id.lvitems);
        lvItems.setAdapter(itemsAdapter);



        setupListViewListener();


    }

    public void onAddItem(View v){
        EditText etNewItem = (EditText) findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        itemsAdapter.add(itemText);
        etNewItem.setText("");
        writeItems();
        Toast.makeText(getApplicationContext(),"Item added to list!", Toast.LENGTH_SHORT).show();
    }

    private void setupListViewListener(){
        Log.i("MainActivity","Listener on list view set up.");
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i("MainActivity","Item at position " + i + " removed.");
                items.remove(i);
                itemsAdapter.notifyDataSetChanged();
                writeItems();
                return true;
            }
        });

        //listener for edits (short click)
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //create new activity
                Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
                //pass data
                intent.putExtra(ITEM_TEXT, items.get(i));
                intent.putExtra(ITEM_POSITION, i);
                //display activity
                startActivityForResult(intent,EDIT_REQUEST_CODE);

            }
        });
    }

    //handle results from the edit activity


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the edit activity completed is ok
        if (resultCode == RESULT_OK && requestCode == EDIT_REQUEST_CODE){
            String updatedItem = data.getExtras().getString(ITEM_TEXT);
            int position = data.getExtras().getInt(ITEM_POSITION);
            items.set(position,updatedItem);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
            Toast.makeText(this, "Item updated!", Toast.LENGTH_SHORT).show();


        }

    }

    private File getDataFile() {
        return new File(getFilesDir(), "todo.txt");
    }

    private void readItems(){
        //try to retrieve data, if failed log error and print stack trace
        try {
            items = new ArrayList<>(FileUtils.readLines(getDataFile(), Charset.defaultCharset()));
        } catch (IOException e) {
            Log.e("MainActivity","Error reading file",e);
            items = new ArrayList<>();
        }
    }

    private void writeItems(){

        //try to write data, if failed log error and print stack trace
        try {
            FileUtils.writeLines(getDataFile(), items);
        } catch (IOException e) {
            Log.e("MainActivity","Error writing file",e);
        }
    }



}
