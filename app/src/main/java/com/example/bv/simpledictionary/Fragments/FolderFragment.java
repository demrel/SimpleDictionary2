package com.example.bv.simpledictionary.Fragments;

import android.content.ContentValues;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bv.simpledictionary.Adapter.DirectoryAdapter;
import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.FolderDialogFragment;
import com.example.bv.simpledictionary.R;

/**
 * Created by bv on 3/21/2017.
 */


public class FolderFragment extends Fragment implements FolderDialogFragment.FolderDialogListener,LoaderManager.LoaderCallbacks<Cursor>,DirectoryAdapter.ListItemClickListener {
    private static final String TAG=FolderFragment.class.getName();
    private static final int TASK_LOADER_ID=1;
    private DirectoryAdapter mAdapter;

    RecyclerView recyclerView;

    public FolderFragment(){}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.folderfragment,container,false);
       recyclerView=(RecyclerView) view.findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        mAdapter=new DirectoryAdapter(getContext(),this);
        recyclerView.setAdapter(mAdapter);

        FloatingActionButton actionButton=(FloatingActionButton) view.findViewById(R.id.floatingActionButton);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditDialog();
            }
        });
        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID,null,this);
        return view;
    }
    private void showEditDialog(){
        FragmentManager fm=getActivity().getSupportFragmentManager();
        FolderDialogFragment dialogFragment=FolderDialogFragment.newInstance("Folder Name");
        dialogFragment.setTargetFragment(this,1);
        dialogFragment.show(fm,"FolderDialogFragment");

    }

    @Override
    public void onReturneValue(String value) {
        ContentValues contentValues=new ContentValues();
        contentValues.put(Contract.DirectoryEntry.COLUMN_DIRECTORY_NAME,value);
        getContext().getContentResolver().insert(Contract.DirectoryEntry.CONTENT_URI,contentValues);
        getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getActivity()) {
            Cursor mTaskData;

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
    public void onListItemClick(int clickedItemIndex) {
        Toast.makeText(getContext(),"Index "+clickedItemIndex,Toast.LENGTH_SHORT).show();
        //TODO-1: write there  intent and send folderClickActivity data to folderClickActivity
    }
}
