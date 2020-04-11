package com.prigus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    public final static String urlPage = "https://prigus.com";
    public final static String varSharedPage = "prigus.com te ha compartido el link!";
    public final static String varCategory = "category";
    public final static String varPage = "prigus";
    public final static String varHttp = "http://";
    public final static String varHttps = "https://";
    public final static String varMailto = "mailto:";
    public final static String varShared = "shared";
    public final static String varTypeMessage = "message/rfc822";
    public final static String varTypeShared = "text/plain";
    public final static String varEmailContact = "contacto@prigus.com";
    public final static String varSubjectEmail = "Sugerencias|eCommerce";
    public final static String varBodytEmail = "Muchas gracias por ponerte en contacto con nosotros," +
            " puedes dejarnos tus comentarios m&aacute;s abajo.";
    public final static String varRegex = "\\#";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView web = (WebView) findViewById(R.id.webViewPrigus);
        web.setWebViewClient(new PWebViewClient());
        /*For enabled js in WebView*/
        //web.setWebChromeClient(new WebChromeClient());
        WebSettings settings = web.getSettings();
        settings.setJavaScriptEnabled(true);
        setViewMobile(settings);
        clearDataWebView(web);
        web.loadUrl(urlPage);
    }
    private class PWebViewClient extends WebViewClient{
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            if (url.startsWith(varMailto)) {
                sendEmail();
                //view.reload();
                return true;
            } else if (!url.contains(varCategory)  && !url.contains(varPage) &&  (url.startsWith(varHttp)
                    || url.startsWith(varHttps))) {
                openBrowser(url);
                return true;
            } else if(url.contains(varShared)){
                shareProduct(url);
                return true;
            }
            view.loadUrl(url);
            return true;
        }
    }
    /*For return back page*/
    @Override
    public void onBackPressed() {
        WebView web = (WebView) findViewById(R.id.webViewPrigus);
        if (web.canGoBack()) {
            web.goBack();
        } else {
            super.onBackPressed();
        }
    }

    /*For clear data navigation*/
    private void clearDataWebView(WebView web){
        web.clearHistory();
        web.clearCache(true);
    }

    /*For mode view mobile*/
    private void setViewMobile( WebSettings settings){
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
    }

    /*For shared url product*/
    private void shareProduct(String product) {
        String url = product.split(varRegex)[1];
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(varTypeShared);
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        share.putExtra(Intent.EXTRA_SUBJECT, varSharedPage);
        share.putExtra(Intent.EXTRA_TEXT, url);
        share.putExtra(Intent.EXTRA_TITLE, varSharedPage);
        startActivity(Intent.createChooser(share, varSharedPage));
    }

    /*For send email in view*/
    private void sendEmail(){
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType(varTypeMessage);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{varEmailContact});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, varSubjectEmail);
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(varBodytEmail));
        startActivity(Intent.createChooser(emailIntent, varSubjectEmail));
    }

    /*For open browser of product*/
    private void openBrowser(String url){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }
}
