package com.example.bv.simpledictionary.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.bv.simpledictionary.Adapter.DictionaryAdapter;

import com.example.bv.simpledictionary.Data.Contract;
import com.example.bv.simpledictionary.R;
import com.polyak.iconswitch.IconSwitch;

public class SearchFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>,IconSwitch.CheckedChangeListener {
    RecyclerView recyclerView;
    private DictionaryAdapter mAdapter;
    private final static int TASK_LOADER_ID=23;
    private String filterStr="";
    private boolean checked=false;

    public SearchFragment(){}
    private final BroadcastReceiver FragmentReceiver1 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
                getActivity().getSupportLoaderManager().restartLoader(0, null,SearchFragment.this);
        }};
    TextView.OnEditorActionListener actionListener=new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId== EditorInfo.IME_ACTION_SEARCH){
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view= inflater.inflate(R.layout.searchfragment,container,false);
        recyclerView= (RecyclerView) view.findViewById(R.id.allListRecyclerViewView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter=new DictionaryAdapter(getContext());
        recyclerView.setAdapter(mAdapter);
        IconSwitch mSwitch= (IconSwitch) view.findViewById(R.id.icon_switch);
        mSwitch.setCheckedChangeListener(this);

        EditText editText= (EditText) view.findViewById(R.id.EditTextSearch);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterStr=s.toString().toLowerCase();
                getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,SearchFragment.this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID,null,this);
        editText.setOnEditorActionListener(actionListener);
        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().registerReceiver(FragmentReceiver1,new IntentFilter("deleteWord"));
        getActivity().getSupportLoaderManager().initLoader(TASK_LOADER_ID,null,this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(TASK_LOADER_ID,null,this);
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
                    String select=Contract.DictionaryEntry.COLUMN_FOREIGN_LANGUAGE;

                if (checked){
                    select=Contract.DictionaryEntry.COLUMN_TRANSLATE;
                }
                try{
                    return getContext().getContentResolver().query(Contract.DictionaryEntry.CONTENT_URI,null,
                            select+" LIKE ?",
                            new String[]{"%"+filterStr+"%"},
                            Contract.DictionaryEntry.COLUMN_FOREIGN_LANGUAGE+" ASC");
                }
                catch (Exception e){
                    Log.e(SearchFragment.class.getName()," FAILED LOAD DATA ASYNCHRONOUSLY");
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
    public void onCheckChanged(IconSwitch.Checked current) {
        checked= current==IconSwitch.Checked.RIGHT;
    }
}
