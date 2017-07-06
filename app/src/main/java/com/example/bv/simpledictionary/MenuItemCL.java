package com.example.bv.simpledictionary;

import android.app.Activity;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.app.LoaderManager;
import com.example.bv.simpledictionary.Activities.AddWord;
import com.example.bv.simpledictionary.Activities.WordListActivity;
import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.Fragments.FolderFragment;



public  class MenuItemCL extends Service implements PopupMenu.OnMenuItemClickListener {

        private int mPosition;
        private Context mContext;
        private Uri mCurrentUri;





        public MenuItemCL(int position, Context context) {
            this.mPosition=position;
            this.mContext=context;

        }


        @Override
        public boolean onMenuItemClick(MenuItem item)  {
            switch (item.getItemId()){
                case R.id.edit:
                    Intent intent=new Intent(mContext,AddWord.class);
                    mCurrentUri= ContentUris.withAppendedId(Contract.DictionaryEntry.CONTENT_URI,mPosition);
                    intent.setData(mCurrentUri);
                    mContext.startActivity(intent);
                    return true;

                case R.id.deleteWord:
                    mCurrentUri=ContentUris.withAppendedId(Contract.DictionaryEntry.CONTENT_URI,mPosition);
                    mContext.getContentResolver().delete(mCurrentUri,null,null);
                    Intent intentDeleteWord=new Intent("deleteWord");
                    mContext.sendBroadcast(intentDeleteWord);
                    Toast.makeText(mContext, "deleteword", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.delete:
                    mCurrentUri=ContentUris.withAppendedId(Contract.DirectoryEntry.CONTENT_URI,mPosition);
                   mContext.getContentResolver().delete(mCurrentUri,null,null);
                    Intent deleteIntent = new Intent("pageupdate");
                    mContext.sendBroadcast(deleteIntent);
                    Toast.makeText(mContext, "deleted", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.rename:
                    mCurrentUri= ContentUris.withAppendedId(Contract.DirectoryEntry.CONTENT_URI,mPosition);
                    Intent renameIntent=new Intent("rename");
                    renameIntent.putExtra("id",String.valueOf(mPosition));
                    mContext.sendBroadcast(renameIntent);
                    return true;
            }
            return false;
        }





    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}

