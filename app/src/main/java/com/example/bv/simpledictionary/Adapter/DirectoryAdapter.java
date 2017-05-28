package com.example.bv.simpledictionary.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.R;



public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    private final ListItemClickListener mOnclickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }


    public DirectoryAdapter(Context context,ListItemClickListener listener){
        this.mContext=context;
        mOnclickListener=listener;
    }
    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item,parent,false);
        return new DirectoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DirectoryViewHolder holder, int position) {
        int indexID =mCursor.getColumnIndex(Contract.DirectoryEntry._ID);
        int nameIndex=mCursor.getColumnIndex(Contract.DirectoryEntry.COLUMN_DIRECTORY_NAME);
        mCursor.moveToPosition(position);
        final int id=mCursor.getInt(indexID);
        String name=mCursor.getString(nameIndex);
        if(name.length()>16){
            name=name.substring(0,16)+"...";
        }
        holder.itemView.setTag(id);
        holder.nameTextView.setText(name);
    }

    @Override
    public int getItemCount() {
        if(mCursor==null) return 0;
        return  mCursor.getCount();
    }
    public Cursor swapCursor(Cursor c){
        if(mCursor==c){
            return null;
        }
        Cursor temp=mCursor;
        this.mCursor=c;
        if(c!=null){
            this.notifyDataSetChanged();
        }
        return temp;
    }

    public class DirectoryViewHolder extends RecyclerView.ViewHolder  implements OnClickListener {
       TextView nameTextView;

        public DirectoryViewHolder(View itemView) {
            super(itemView);
            nameTextView=(TextView) itemView.findViewById(R.id.directoryTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clcikedPositionID= (int) itemView.getTag();
            mOnclickListener.onListItemClick(clcikedPositionID);
        }
    }

}
