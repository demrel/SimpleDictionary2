package com.example.bv.simpledictionary.Adapter;


import android.content.Context;

import android.database.Cursor;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.MenuItemCL;
import com.example.bv.simpledictionary.R;




public class DictionaryAdapter extends RecyclerView.Adapter<DictionaryAdapter.DictionaryAdapterViewHolder> {


    private Cursor mCursor;
    final private Context mContext;
    private String name;
    public DictionaryAdapter(Context context){
        this.mContext=context;
    }

    @Override
    public DictionaryAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        name = mContext.getClass().getSimpleName();
        if (name.equals("DailyWord")){
            view = LayoutInflater.from(mContext).inflate(R.layout.dailyword_item,parent,false);
        }else {
             view = LayoutInflater.from(mContext).inflate(R.layout.dictionary_item,parent,false);
        }

        return new DictionaryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DictionaryAdapter.DictionaryAdapterViewHolder holder, final int position) {
        int idIndex=mCursor.getColumnIndex(Contract.DictionaryEntry._ID);
        int wordIndex=mCursor.getColumnIndex(Contract.DictionaryEntry.COLUMN_FOREIGN_LANGUAGE);
        int transcriptionIndex=mCursor.getColumnIndex(Contract.DictionaryEntry.COLUMN_TRANSCRIPTION);
        int translateIndex=mCursor.getColumnIndex(Contract.DictionaryEntry.COLUMN_TRANSLATE);
        int descriptionIndex=mCursor.getColumnIndex(Contract.DictionaryEntry.COLUMN_DESCRIPTION);
        mCursor.moveToPosition(position);

        final int id=mCursor.getInt(idIndex);
        String word=mCursor.getString(wordIndex);
        String transcription="[ "+mCursor.getString(transcriptionIndex)+" ]";
        String translate=mCursor.getString(translateIndex);
        String descripitonText=mCursor.getString(descriptionIndex);

        holder.itemView.setTag(id);
        holder.wordTextView.setText(word);
        holder.translateTextView.setText(translate);
        holder.transcriptionTextView.setText(transcription);
        holder.description.setText(descripitonText);
        if (descripitonText.equals("")){
            holder.showDescription.setVisibility(View.GONE);
        }



        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.imageButton, (Integer) holder.itemView.getTag(),mContext);
            }
        });

        //come here after create textView

    }

    private void showPopupMenu(View view, int position,Context Context) {
        PopupMenu popup=new PopupMenu(view.getContext(),view);
        popup.getMenuInflater().inflate(R.menu.words_menu,popup.getMenu());
        popup.setOnMenuItemClickListener (new MenuItemCL(position,Context));
        popup.show();

    }

    @Override
    public int getItemCount() {
        if(mCursor==null){
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c){
        if(mCursor==c){
            return null;
        }
        Cursor temnp=mCursor;
        this.mCursor=c;
        if(c!=null){
            this.notifyDataSetChanged();
        }
        return temnp;
    }

    public class DictionaryAdapterViewHolder extends RecyclerView.ViewHolder {
        boolean isShowed=false;
        TextView wordTextView;
        TextView translateTextView;
        TextView transcriptionTextView;
        ImageButton imageButton;
        ImageView showDescription;
        TextView description;


        private DictionaryAdapterViewHolder(View itemView) {
            super(itemView);

                wordTextView= (TextView) itemView.findViewById(R.id.word);
                transcriptionTextView= (TextView) itemView.findViewById(R.id.transcription);
                translateTextView= (TextView) itemView.findViewById(R.id.translate);
                imageButton=(ImageButton) itemView.findViewById(R.id.imageButton);
                showDescription=(ImageView) itemView.findViewById(R.id.showdesciptionButton);
                description=(TextView) itemView.findViewById(R.id.descriptionTextView);

            if (name.equals("DailyWord")){
                imageButton.setVisibility(View.GONE);
            }



                itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (!description.getText().toString().equals("")&&description.getText()!=null)
                        if (!isShowed){
                            description.setVisibility(View.VISIBLE);
                            isShowed=true;
                        }
                        else {
                            description.setVisibility(View.GONE);
                            isShowed=false;
                        }
                    }
                });

            }


    }



}
