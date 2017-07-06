package com.example.bv.simpledictionary.Adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;
import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.MenuItemCL;
import com.example.bv.simpledictionary.R;



public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    private final ListItemClickListener mOnclickListener;


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex,String name);
    }


    public DirectoryAdapter(Context context, ListItemClickListener listener){
        this.mContext=context;
        this.mOnclickListener=listener;

    }
    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.category_item,parent,false);
        return new DirectoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DirectoryViewHolder holder, int position) {
        int indexID =mCursor.getColumnIndex(Contract.DirectoryEntry._ID);
        int nameIndex=mCursor.getColumnIndex(Contract.DirectoryEntry.COLUMN_DIRECTORY_NAME);
        mCursor.moveToPosition(position);
        final int id=mCursor.getInt(indexID);
        String name=mCursor.getString(nameIndex);
        if(name.length()>16){
            name=name.substring(0,18)+"...";
        }
        holder.itemView.setTag(id);
        holder.nameTextView.setText(name);
        holder.popUpImageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.popUpImageButton, (Integer) holder.itemView.getTag(),mContext);
            }
        });
    }
    private void showPopupMenu(View view, int position,Context Context) {
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        popup.getMenuInflater().inflate(R.menu.directory_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MenuItemCL(position,Context));
        popup.show();
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
        ImageButton popUpImageButton;
        private DirectoryViewHolder(View itemView) {
            super(itemView);
            nameTextView=(TextView) itemView.findViewById(R.id.directoryTextView);
            itemView.setOnClickListener(this);
            popUpImageButton = (ImageButton) itemView.findViewById(R.id.pop_up_image);
        }

        @Override
        public void onClick(View v) {
            int clcikedPositionID= (int) itemView.getTag();
            String folderName= (String) nameTextView.getText();
            mOnclickListener.onListItemClick(clcikedPositionID,folderName);
        }
    }


}
