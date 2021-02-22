package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private EditText itemET, editText;
    private Button btn;
    private ListView itemsList;

    //returns a view for each object in a collection of data
    private ArrayAdapter<String> adapter;
    private ArrayList<String> mTodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*call the super class onCreate to complete the
            creation of activity with any state changes */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemET = findViewById(R.id.item_edit_text);
        btn = findViewById(R.id.add_btn);
        itemsList= findViewById(R.id.items_list);

        mTodos = FileHelper.readFile(this);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTodos);
        //display items in a list
        itemsList.setAdapter(adapter);
        btn.setOnClickListener(this);
        itemsList.setOnItemClickListener(this);
    }

    /**
     * add item to list when clicked on and replace editText with ""
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.add_btn:
            String itemEntered = itemET.getText().toString();
            itemEntered = itemEntered.replace("\n", " ");
            adapter.add(itemEntered);
            itemET.setText("");

            FileHelper.writeData(itemEntered, this);
            Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        editText = new EditText(MainActivity.this);

        //pop up when clicked on individual item
        AlertDialog.Builder editDeleteDialog = new AlertDialog.Builder(MainActivity.this);
        editDeleteDialog.setTitle("Update/Delete Task");

        String value = (String)itemsList.getItemAtPosition( position );
        editText.setText(value);
        editDeleteDialog.setView(editText);

        editDeleteDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {

            //call updateItem method
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateItem(position, editText.getText().toString());
            }
        });

        editDeleteDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            //call deleteItem method
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem(position);
            }
        });

        AlertDialog alertDialog = editDeleteDialog.create();
        alertDialog.show();
    }

    private void deleteItem(int lineNo){
        FileHelper.deleteData(lineNo, this);
        mTodos.remove(lineNo);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show();
    }

    private void updateItem(int lineNo, String newText){
        newText = newText.replace("\n", " ");
        FileHelper.updateData(lineNo, newText, this);
        mTodos.set(lineNo, newText);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Item Updated to " + newText, Toast.LENGTH_SHORT).show();
    }
}
