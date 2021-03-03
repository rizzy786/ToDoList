package com.example.todolist;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {

    String[] data;
    EditText editText;
    int lineNo;

    private String showDialog = "N";

    public ToDoListAdapter(String[] data) {
        this.data = data;
    }

    public String getShowDialog() {
        return showDialog;
    }

    public void setShowDialog(String showDialog) {
        this.showDialog = showDialog;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo (int lineNo) {
        this.lineNo = lineNo;
    }

    public void setData(String [] data){
        this.data = data;
    }

    public String[] getData(){
        return data;
    }

    /**
     *  inflates a View item and returns a new ViewHolder that contains it
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.to_do_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    /**
     *  sets the contents of a View item at a given position in the RecyclerView
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String item = data[position];
        final int lineNo = position;
        holder.textView.setText(item);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //pop up when clicked on individual item
                final Context context = view.getContext();
                editText = new EditText(view.getContext());
                AlertDialog.Builder editDeleteDialog = new AlertDialog.Builder(view.getContext());
                editDeleteDialog.setTitle("Update/Delete Task");

                editText.setText(item);
                editDeleteDialog.setView(editText);

                editDeleteDialog.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    //call deleteItem method
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteItem(lineNo, context);
                    }
                });

                editDeleteDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {

                    //call updateItem method
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String newText = editText.getText().toString();
                        updateItem(lineNo, newText, context);
                    }
                });

                editDeleteDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        setShowDialog("N");
                    }
                });

                AlertDialog alertDialog = editDeleteDialog.create();
                alertDialog.show();
                setShowDialog("Y");
                setLineNo(lineNo);
            }
        });
    }

    /**
     *  returns the total number of items in the data set held by the adapter
     * @return
     */
    @Override
    public int getItemCount() {
        return data.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public LinearLayout linearLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.linearLayout);
        }
    }

    public void updateItem(int lineNo, String newText, Context context){
        newText = newText.replace("\n", " ");
        FileHelper.updateData(lineNo, newText, context);
        data[lineNo] = newText;
        notifyDataSetChanged();
        setShowDialog("N");
        Toast.makeText(context, "Item updated to " + newText, Toast.LENGTH_SHORT).show();
    }

    public void deleteItem(int lineNo, Context context){
        FileHelper.deleteData(lineNo, context);

        List<String> list = new ArrayList<>();
        Collections.addAll(list, data);
        list.remove(lineNo);
        data = new String[list.size()];
        data = list.toArray(data);

        notifyItemRemoved(lineNo);
        setShowDialog("N");
        Toast.makeText(context, "Item Deleted", Toast.LENGTH_SHORT).show();
    }
}
