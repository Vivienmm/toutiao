package com.chinaso.toutiao.mvp.ui.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chinaso.toutiao.R;
import com.chinaso.toutiao.mvp.ui.activity.base.BaseActivity;
import com.chinaso.toutiao.view.BaseWebView;
import com.chinaso.toutiao.view.CustomActionBar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.BindView;
import butterknife.OnEditorAction;

public class SearchNewsActivity extends BaseActivity {
    @BindView(R.id.searchBar)
    CustomActionBar searchBar;
    @BindView(R.id.inputContentET)
    EditText inputContentET;
    @BindView(R.id.hotNewsWord)
    LinearLayout hotNewsWord;
    @BindView(R.id.searchNewsWeb)
    BaseWebView searchNewsWeb;

    private String searchURL = "http://m.chinaso.com/news/search.htm?keys=";
    @Override
    public int getLayoutId() {
        return R.layout.activity_search_news;
    }

    @Override
    public void initViews() {
        searchBar.setTitleView("搜索");
        searchBar.setLeftViewImg(R.mipmap.actionbar_back);
        searchBar.setOnClickListener(new CustomActionBar.ActionBarLeftClick() {
            @Override
            public void leftViewClick() {
                finish();
            }
        });

        searchNewsWeb.setWebChromeClient(new WebChromeClient());
        searchNewsWeb.setWebViewClient(new SearchNewsWebViewClient());

        initInput();

    }

    private void initInput() {
        inputContentET.setFocusable(true);
        inputContentET.setFocusableInTouchMode(true);
        inputContentET.requestFocus();
        InputMethodManager imm = (InputMethodManager)inputContentET.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(inputContentET, InputMethodManager.SHOW_FORCED);
    }

    @OnEditorAction(R.id.inputContentET)
    boolean onInputEditorAction(TextView view, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId== EditorInfo.IME_ACTION_UNSPECIFIED) {

            String content = null;
            try {
                content = URLEncoder.encode(inputContentET.getText().toString().trim(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(content)) {
                return false;
            }
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(SearchNewsActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            hotNewsWord.setVisibility(View.GONE);
            searchNewsWeb.setVisibility(View.VISIBLE);
            searchNewsWeb.loadUrl(searchURL + content);
            return true;
        }
        return false;
    }

    class SearchNewsWebViewClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url) {
            view.loadUrl("javascript:"+handleWebViewHeader(url));
            super.onPageFinished(view, url);
        }
    }

    private String handleWebViewHeader(String url) {
        String before = url;
        if (before.contains("http://m.chinaso.com/page/search.htm")) {  //startsWith
            before = removeHeader();
        } else if (before.contains("http://m.chinaso.com/news/search.htm")) {
            before = removeHeader();
        } else if (before.contains("http://m.chinaso.com/paper.jsp")) {
            before = removeHeader();
        } else if (before.contains("http://m.chinaso.com/paper/search.htm")) {
            before = removeHeader();
        } else if (before.contains("http://m.chinaso.com/newsindex.html")) {
            before = removeHeaderByClass();
        } else if (before.contains("http://m.123.chinaso.com")) {
            before = "document.getElementsByClassName(\"se-form\")[0].parentNode.removeChild(document.getElementsByClassName(\"se-form\")[0])";
        } else if (before.contains("http://m.chinaso.com/news.html")) {
            before = removeHeaderByClass();
        }
        return before;
    }

    private String removeHeader() {
        return "document.body.removeChild(document.getElementsByTagName(\"header\")[0])";
    }

    private String removeHeaderByClass() {
        return "document.body.removeChild(document.getElementsByClassName(\"header\")[0])";
    }
}
