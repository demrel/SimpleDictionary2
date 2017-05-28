package com.example.bv.simpledictionary.Data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by bv on 3/22/2017.
 */

public class Contract {
    static final String AUTHORITY = "com.example.bv.simpledictionary";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static final String PATH_DIRECTORY = "directory";
    static final String PATH_DICTIONARY="dictionary";

    public static final class DirectoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_DIRECTORY).build();
        public static final String TABLE_NAME="directory";
        public static final String COLUMN_DIRECTORY_NAME="directory";
        public static final String COLUMN_CREATE_DATE="date";
    }
    public static final class DictionaryEntry implements BaseColumns{
        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_DICTIONARY).build();
        public static final String TABLE_NAME="dictionary";
        public static final String COLUMN_FOREIGN_LANGUAGE="foreignlanguage";
        public static final String COLUMN_TRANSLATE="translatelanguage";
        public static final String DIRECTORY_ID="directory_id";
    }
}
