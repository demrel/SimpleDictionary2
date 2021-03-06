package com.example.bv.simpledictionary.Fragments;


import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.GridLayoutManager;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bv.simpledictionary.Adapter.DirectoryAdapter;
import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.FolderDialogFragment;
import com.example.bv.simpledictionary.R;
import com.example.bv.simpledictionary.Activities.folderClickActivity;



final public class FolderFragment extends Fragment implements FolderDialogFragment.FolderDialogListener,LoaderManager.LoaderCallbacks<Cursor>,DirectoryAdapter.ListItemClickListener {
    private static final String TAG=FolderFragment.class.getName();
    public static final int TASK_LOADER_ID=1;
    private DirectoryAdapter mAdapter;
    private Uri mCurentDirectoryUri;
    public LoaderManager.LoaderCallbacks loaderCallbacks;
    RecyclerView recyclerView;
   private Uri rename;
    private boolean isClicked=false;

    public FolderFragment(){}
    private final BroadcastReceiver FragmentReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            rename=intent.getData();
            if(intent.getAction().equals("rename")){
                int id=Integer.parseInt(intent.getStringExtra("id"));
                showEditDialog(id);
                rename= ContentUris.withAppendedId(Contract.DirectoryEntry.CONTENT_URI,id);
                isClicked=true;
                Toast.makeText(context, "broadcast rename", Toast.LENGTH_SHORT).show();
            }
            if(intent.getAction().equals("pageupdate")){
                getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID, null,loaderCallbacks);
                Log.e("timer", "Broadcast received");
            }
        }};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view=inflater.inflate(R.layout.folderfragment,container,false);
       recyclerView=(RecyclerView) view.findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),calculateNoOfColumns(getContext(),R.layout.category_item)));
        mAdapter=new DirectoryAdapter(getContext(),this);
        recyclerView.setAdapter(mAdapter);


        FloatingActionButton actionButton=(FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog(-1);
            }
        });
        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID,null,this);
        Log.e("Folder F  life cickle"," onCreateView.");
        loaderCallbacks=this;
        return view;

    }
    public static int calculateNoOfColumns(Context context, int viewId) {
        View view = View.inflate(context, viewId, null);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int width = view.getMeasuredWidth();
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return  displayMetrics.widthPixels / width;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCurentDirectoryUri=getActivity().getIntent().getData();
        getActivity().registerReceiver(FragmentReceiver1,new IntentFilter("pageupdate"));
        getActivity().registerReceiver(FragmentReceiver1,new IntentFilter("rename"));
        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID,null,loaderCallbacks);

    }

    public void showEditDialog(int id){
        Uri uri=ContentUris.withAppendedId(Contract.DirectoryEntry.CONTENT_URI,id);
        String oldName="";
        if(id>=0) {

            Cursor cursor=getContext().getContentResolver().query(uri,null,null,null,null);
            if (cursor.moveToFirst()){
                oldName =cursor.getString(cursor.getColumnIndex(Contract.DirectoryEntry.COLUMN_DIRECTORY_NAME));
            }
            cursor.close();

        }
        FragmentManager fm=getActivity().getSupportFragmentManager();
        FolderDialogFragment dialogFragment=FolderDialogFragment.newInstance("Folder Name",oldName);
        dialogFragment.setTargetFragment(this,1);



        dialogFragment.show(fm,"FolderDialogFragment");
    }



    @Override
    public void onReturneValue(String value) {
        ContentValues contentValues=new ContentValues();
        contentValues.put(Contract.DirectoryEntry.COLUMN_DIRECTORY_NAME,value);
        if(isClicked){
            getContext().getContentResolver().update(rename,contentValues,null,null);
            isClicked=false;
        }
        else {
            getContext().getContentResolver().insert(Contract.DirectoryEntry.CONTENT_URI,contentValues);
        }
        getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Folder F  life cickle"," onResume.");
    }


    @Override
    public void onPause() {
        super.onPause();
        Log.e("Folder F  life cickle"," onPause.");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Folder F  life cickle"," onDestroy.");
        getActivity().unregisterReceiver(FragmentReceiver1);


    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getActivity()) {
            Cursor mTaskData=null;

            @Override
            protected void onStartLoading() {
                if(mTaskData!=null)deliverResult(mTaskData);
                else forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContext().getContentResolver().query(Contract.DirectoryEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            null);
                }
                catch (Exception e){
                    Log.e(TAG,"failed load data");
                    e.printStackTrace();
                    return null;
                }
            }
            public void deliverResult(Cursor data){
                mTaskData=data;
                super.deliverResult(data);
            }
        };
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onListItemClick(int clickedItemIndex,String name) {
        Toast.makeText(getContext(),"Index "+clickedItemIndex,Toast.LENGTH_SHORT).show();
        Intent indexData = new Intent(getContext(),folderClickActivity.class);
        indexData.putExtra("id",clickedItemIndex);
        indexData.putExtra("name",name);
        startActivity(indexData);

    }

}
