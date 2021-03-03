package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText itemET;
    private EditText editText;
    private Button btn;

    private String[] mTodos;

    ToDoListAdapter adapter;
    RecyclerView recyclerView;

    int lineNo;

    private static final String DIALOG_DETAILS = "dialogDetails";
    String[] dialogDetails = {"N", "", ""};

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

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        dialogDetails = savedInstanceState.getStringArray(DIALOG_DETAILS);

        if(dialogDetails[0].equals("Y")) {
            lineNo = Integer.valueOf(dialogDetails[2]);
            editText = new EditText(this);
            AlertDialog.Builder editDeleteDialog = new AlertDialog.Builder(this);
            editDeleteDialog.setTitle("Update/Delete Task");

            editText.setText(dialogDetails[1]);
            editDeleteDialog.setView(editText);

            editDeleteDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                //call deleteItem method
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.deleteItem(lineNo, MainActivity.this);
                }
            });

            editDeleteDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                //call updateItem method
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String newText = editText.getText().toString();
                    adapter.updateItem(lineNo, newText, MainActivity.this);
                }
            });

            editDeleteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    adapter.setShowDialog("N");
                }
            });

            AlertDialog alertDialog = editDeleteDialog.create();
            alertDialog.show();
            adapter.setEditText(editText);
            adapter.setShowDialog("Y");
            adapter.setLineNo(lineNo);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * invoked when the activity may be temporarily destroyed, save the instance state here
     * @param savedInstanceState
     */

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        dialogDetails[0]= adapter.getShowDialog();

        if (adapter.getShowDialog().equals("Y")) {
            dialogDetails[1] = adapter.getEditText().getText().toString();
        } else {
            dialogDetails[1] = "";
        }

        dialogDetails[2]= String.valueOf(adapter.getLineNo());

        savedInstanceState.putStringArray(DIALOG_DETAILS, dialogDetails);

        // call superclass to save any view hierarchy
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * add item to list when clicked on and replace editText with ""
     * @param
     */
}
