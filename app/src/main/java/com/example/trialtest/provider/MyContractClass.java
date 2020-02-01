package com.example.trialtest.provider;

import android.net.Uri;

public class MyContractClass {

    private static final String WEBSITE_TABLE = "websiteName";
    public final static String AUTHORITY =
            "com.example.trialtest.provider.UrlContentProvider";
    public final static Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + WEBSITE_TABLE);
    public final static String CONTENT_URI_STRING =
            "content://" + AUTHORITY + "/" + WEBSITE_TABLE ;

}

