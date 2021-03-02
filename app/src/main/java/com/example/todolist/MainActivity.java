package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText itemET;
    private EditText editText;
    private Button btn;

    private String[] mTodos;

    ToDoListAdapter adapter;
    RecyclerView recyclerView;

    private static final String DIALOG_DETAILS = "dialogDetails";
    String[] dialogDetails = {"N", "0", ""};

    private String showDialog = "N";
    int lineNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // recovering the instance state
        /*
        if (savedInstanceState != null){
            dialogDetails = savedInstanceState.getStringArray(DIALOG_DETAILS);
        }
        */

        /*call the super class onCreate to complete the
            creation of activity with any state changes */
        super.onCreate(savedInstanceState);
        // set the user interface layout for this activity
        setContentView(R.layout.activity_main);

        mTodos = FileHelper.readFile(MainActivity.this);

        adapter = new ToDoListAdapter(mTodos);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        itemET = findViewById(R.id.item_edit_text);
        btn = findViewById(R.id.add_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemEntered = itemET.getText().toString();
                itemEntered = itemEntered.replace("\n", " ");
                itemET.setText("");
                FileHelper.writeData(itemEntered, MainActivity.this);

                mTodos = adapter.getData();

                List<String> list = new ArrayList<>();
                Collections.addAll(list, mTodos);
                list.add(itemEntered);

                mTodos = new String[list.size()];
                mTodos = list.toArray(mTodos);

                adapter.setData(mTodos);

                adapter.notifyItemInserted(list.size());

                Toast.makeText(MainActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
            }
        });
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
/*
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
*/
    /**
     * invoked when the activity may be temporarily destroyed, save the instance state here
     * @param savedInstanceState
     */
/*
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
*/
    /**
     * add item to list when clicked on and replace editText with ""
     * @param
     */

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        editText = new EditText(MainActivity.this);
        showDialog="Y";
        lineNo = position;
    }
}
