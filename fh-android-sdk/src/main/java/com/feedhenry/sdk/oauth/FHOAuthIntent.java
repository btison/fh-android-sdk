/**
 * Copyright (c) 2015 FeedHenry Ltd, All Rights Reserved.
 *
 * Please refer to your contract with FeedHenry for the software license agreement.
 * If you do not have a contract, you do not have a license to use this software.
 */
package com.feedhenry.sdk.oauth;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

/**
 * An intent wrapper for the WebView.
 */
public class FHOAuthIntent extends Activity {

    private FHOAuthWebView mWebview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        mWebview = new FHOAuthWebView(this, getIntent().getBundleExtra("settings"));
        mWebview.onCreate();
        setContentView(mWebview.getView());
    }

    public boolean onKeyDown(int pkeyCode, KeyEvent pEvent) {
        if (pkeyCode == KeyEvent.KEYCODE_BACK) {
            mWebview.close();
            return true;
        } else {
            return super.onKeyDown(pkeyCode, pEvent);
        }
    }
}
