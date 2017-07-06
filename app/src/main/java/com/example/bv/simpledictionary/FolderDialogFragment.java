package com.example.bv.simpledictionary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;



public class FolderDialogFragment extends DialogFragment {

    public FolderDialogFragment(){

    }
    public interface FolderDialogListener{
        void onReturneValue(String value);
            }
            FolderDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mListener=(FolderDialogListener) getTargetFragment();
        }catch (ClassCastException e){
            throw new ClassCastException(getActivity().toString()+" must implement notice dialog listener");
        }
    }
public static FolderDialogFragment newInstance(String title,String name){
    FolderDialogFragment dialogFragment=new FolderDialogFragment();
    Bundle args=new Bundle();
    args.putString("title",title);
    args.putString("name",name);
    dialogFragment.setArguments(args);
    return  dialogFragment;
}

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title=getArguments().getString("title");
        String oldName=getArguments().getString("name");
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        final LinearLayout view=(LinearLayout) inflater.inflate(R.layout.dialog_fragment,null);
        final EditText mEditText=(EditText) view.findViewById(R.id.nameEditText);
        mEditText.setText(oldName);
        alertDialog.setTitle(title)
                .setView(view)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nameText=mEditText.getText().toString().toLowerCase();
                        mListener.onReturneValue(nameText);
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
                final AlertDialog dialog=alertDialog.create();
                mEditText.addTextChangedListener(new TextWatcher() {
                   private void handleText(){
                        final Button okButtom=dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                       if(mEditText.getText().length()==0){
                           okButtom.setEnabled(false);
                       }
                       else okButtom.setEnabled(true);
                    }
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    handleText();
                    }
                });
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        return dialog;
    }
}
