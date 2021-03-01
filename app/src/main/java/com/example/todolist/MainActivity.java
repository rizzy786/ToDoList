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

    private EditText itemET;
    private EditText editText;
    private Button btn;
    private ListView itemsList;

    //returns a view for each object in a collection of data
    private ArrayAdapter<String> adapter;
    private ArrayList<String> mTodos;

    private static final String DIALOG_DETAILS = "dialogDetails";
    String[] dialogDetails = {"N", "0", ""};

    private String showDialog = "N";
    int lineNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // recovering the instance state
        if (savedInstanceState != null){
            dialogDetails = savedInstanceState.getStringArray(DIALOG_DETAILS);
        }

        /*call the super class onCreate to complete the
            creation of activity with any state changes */
        super.onCreate(savedInstanceState);
        // set the user interface layout for this activity
        setContentView(R.layout.activity_main);

        itemET = findViewById(R.id.item_edit_text);
        btn = findViewById(R.id.add_btn);
        itemsList= findViewById(R.id.items_list);

        mTodos = FileHelper.readFile(this);

        // provide views for an AdapterView
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mTodos);

        itemsList.setAdapter(adapter);

        btn.setOnClickListener(this);
        itemsList.setOnItemClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     *
     * @param savedInstanceState
     */
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        dialogDetails = savedInstanceState.getStringArray(DIALOG_DETAILS);

        if(dialogDetails[0].equals("Y")) {
            lineNo = Integer.valueOf(dialogDetails[1]);
            buildPopup();
            editText.setText(dialogDetails[2]);
            showDialog = "Y";
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * invoked when the activity may be temporarily destroyed, save the instance state here
     * @param savedInstanceState
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        dialogDetails[0]= showDialog;
        dialogDetails[1]= String.valueOf(lineNo);

        if (showDialog.equals("Y")) {
            dialogDetails[2] = editText.getText().toString();
        } else {
            dialogDetails[2] = "";
        }
        savedInstanceState.putStringArray(DIALOG_DETAILS, dialogDetails);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(savedInstanceState);
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
            //store whichever item in ArrayAdapter
            adapter.add(itemEntered);
            itemET.setText("");

            FileHelper.writeData(itemEntered, this);
            Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();
            break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        editText = new EditText(MainActivity.this);
        showDialog="Y";
        lineNo = position;
        buildPopup();
    }

    private void deleteItem(int lineNo){
        FileHelper.deleteData(lineNo, this);
        mTodos.remove(lineNo);
        //underlying data has been changed and any View reflecting the data set should refresh itself
        adapter.notifyDataSetChanged();
        showDialog="N";
        Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show();
    }

    private void updateItem(int lineNo, String newText){
        newText = newText.replace("\n", " ");
        FileHelper.updateData(lineNo, newText, this);
        mTodos.set(lineNo, newText);
        adapter.notifyDataSetChanged();
        showDialog="N";
        Toast.makeText(this, "Item Updated to " + newText, Toast.LENGTH_SHORT).show();
    }

    private void buildPopup(){
        //pop up when clicked on individual item
        editText = new EditText(MainActivity.this);
        AlertDialog.Builder editDeleteDialog = new AlertDialog.Builder(MainActivity.this);
        editDeleteDialog.setTitle("Update/Delete Task");

        String value = (String)itemsList.getItemAtPosition(lineNo);
        editText.setText(value);
        editDeleteDialog.setView(editText);

        editDeleteDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {

            //call updateItem method
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateItem(lineNo, editText.getText().toString());
            }
        });

        editDeleteDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            //call deleteItem method
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem(lineNo);
            }
        });

        editDeleteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showDialog="N";
            }
        });

        AlertDialog alertDialog = editDeleteDialog.create();
        alertDialog.show();
    }
}
