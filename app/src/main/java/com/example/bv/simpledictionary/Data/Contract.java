package com.example.bv.simpledictionary.Data;

import android.net.Uri;
import android.provider.BaseColumns;



public class Contract {
    static final String AUTHORITY = "com.example.bv.simpledictionary";
    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    static final String PATH_DIRECTORY = "directory";
    static final String PATH_DICTIONARY="dictionary";
    static final String PATH_QUIZ ="quiz" ;

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
        public static final String COLUMN_TRANSCRIPTION="transcription";
        public static final String COLUMN_TRANSLATE="translatelanguage";
        public static final String DIRECTORY_ID="directory_id";
        public static final String COLUMN_DEGREE="degreelevels";
        public static final String COLUMN_DESCRIPTION="descriptions";
    }
    public static final class QuizEntry implements BaseColumns{

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_QUIZ).build();
        public static final String TABLE_NAME="quiz";
        public static final String COLUMN_ALLQUESTION="allwords";
        public static final String COLUMN_CORRECT="correctwords";
        public static final String COLUMN_WRONG="wrongwords";
        public static final String COLUMN_EXPECTED="expectedword";
        public static final String COLUMN_TIME="quiztime";
        public static final String DIRECTORY_ID="directory_id";
    }
}
