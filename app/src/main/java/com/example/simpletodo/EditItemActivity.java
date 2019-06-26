package com.example.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.simpletodo.MainActivity.ITEM_POSITION;
import static com.example.simpletodo.MainActivity.ITEM_TEXT;


public class EditItemActivity extends AppCompatActivity {


    //track edit
    EditText etTextItem;
    //pos of edited item
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        //resolve edit text from the layout

        etTextItem = findViewById(R.id.etItemEdit);

        //set edit text value from intent extra
        etTextItem.setText(getIntent().getStringExtra(ITEM_TEXT));

        //update pos from intent extra
        position = getIntent().getIntExtra(ITEM_POSITION,0);
        getSupportActionBar().setTitle("Edit Item");


    }

    public void onSaveItem(View v){
        //prepare new intent for result
        Intent intent = new Intent();
        intent.putExtra(ITEM_TEXT,etTextItem.getText().toString());
        intent.putExtra(ITEM_POSITION,position);
        setResult(RESULT_OK,intent);
        finish();
    }
}
