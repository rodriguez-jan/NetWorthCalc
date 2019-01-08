package com.example.janit.networthcalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class ExampleDialog extends AppCompatDialogFragment {
    private EditText totalAssets;
    private EditText totalLiabilities;
    private ExampleDialogListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.calculator,null);

        builder.setView(view).setTitle("Input Income").setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String username = totalAssets.getText().toString();
                String password = totalLiabilities.getText().toString();
                listener.applyTexts(username,password);
            }
        });

        totalAssets = view.findViewById(R.id.edit_assets);
        totalLiabilities = view.findViewById(R.id.edit_liabilities);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener =(ExampleDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString()+"must implement ExampledialogListner");
        }
    }

    public interface ExampleDialogListener{
        void applyTexts(String assets,String liab);
    }
}
