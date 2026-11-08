package com.macromate.tracker;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.view.View;
import android.graphics.Color;
public class MainActivity extends Activity {
 private WebView web;
 @Override public void onCreate(Bundle b){super.onCreate(b);getWindow().setStatusBarColor(Color.rgb(12,11,23));getWindow().setNavigationBarColor(Color.rgb(12,11,23));web=new WebView(this);setContentView(web);web.setBackgroundColor(Color.rgb(12,11,23));WebSettings s=web.getSettings();s.setJavaScriptEnabled(true);s.setDomStorageEnabled(true);s.setAllowFileAccess(false);s.setAllowContentAccess(false);web.setWebViewClient(new WebViewClient());web.setWebChromeClient(new WebChromeClient());web.loadUrl("https://macromate-sable.vercel.app/");}
 @Override public void onBackPressed(){if(web.canGoBack())web.goBack();else super.onBackPressed();}
 @Override protected void onDestroy(){web.destroy();super.onDestroy();}
}