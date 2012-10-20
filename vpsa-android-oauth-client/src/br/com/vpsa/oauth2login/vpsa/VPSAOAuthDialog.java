package br.com.vpsa.oauth2login.vpsa;

 import java.util.HashMap;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import br.com.vpsa.R;
import br.com.vpsa.oauth2login.config.AppContext;
import br.com.vpsa.oauth2login.config.OAuthInfo;
import br.com.vpsa.oauth2login.dialog.GenericDialogListener;
import br.com.vpsa.oauth2login.utils.LOGGING;
import br.com.vpsa.oauth2login.utils.Utils;
import br.com.vpsa.oauth2login.utils.WebService;

public class VPSAOAuthDialog extends Dialog {

	private static final String TAG = "VpsaOAuthDialog";
	
    public static final String OAUTHCALLBACK_URI = AppContext.VPSA_APP_OAUTH_CALLBACK;
	
    static final int BG_COLOR = Color.LTGRAY;//0xFF6D84B4;
    static final float[] DIMENSIONS_LANDSCAPE = {460, 260};
    static final float[] DIMENSIONS_PORTRAIT = {280, 420};
    static final FrameLayout.LayoutParams FILL = 
        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 
                         ViewGroup.LayoutParams.FILL_PARENT);
    static final int MARGIN = 4;
    static final int PADDING = 2;
    static final String DISPLAY_STRING = "touch";
    //static final String FB_ICON = "icon.png";
    
    private String mUrl;
    private GenericDialogListener mListener;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private LinearLayout mContent;
    private TextView mTitle;
    
    public VPSAOAuthDialog(Context context) {
        super(context);
        
        CookieSyncManager.createInstance(context);
        
        String authRequestRedirect = AppContext.VPSA_OAUTH_BASE_URL + AppContext.VPSA_OAUTH_AUTHORIZATION_URL
		        + "?app_id="+AppContext.APP_ID
		        + "&response_type=code" 
		        + "&redirect_uri="+AppContext.VPSA_APP_OAUTH_CALLBACK;
    	
		if(LOGGING.DEBUG)Log.d(TAG, "authRequestRedirect->"+authRequestRedirect);
        
        mUrl = authRequestRedirect;
        mListener = new VPSADialogListener(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");
        
        mContent = new LinearLayout(getContext());
        mContent.setOrientation(LinearLayout.VERTICAL);
        setUpTitle();
        setUpWebView();
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        final float scale = getContext().getResources().getDisplayMetrics().density;
        float[] dimensions = display.getWidth() < display.getHeight() ?
        		DIMENSIONS_PORTRAIT : DIMENSIONS_LANDSCAPE;
        addContentView(mContent, new FrameLayout.LayoutParams(
        		(int) (dimensions[0] * scale + 0.5f),
        		(int) (dimensions[1] * scale + 0.5f)));
    }

    private void setUpTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Drawable icon = getContext().getResources().getDrawable(
                R.drawable.img_icon_fsqlogin_header_small);
        mTitle = new TextView(getContext());
        mTitle.setText("VPSA Accounts");
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTypeface(Typeface.DEFAULT_BOLD);
        mTitle.setBackgroundColor(BG_COLOR);
        mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
        mTitle.setCompoundDrawablePadding(MARGIN + PADDING);
        mTitle.setCompoundDrawablesWithIntrinsicBounds(
                icon, null, null, null);
        mContent.addView(mTitle);
    }
    
    private void setUpWebView() {
        mWebView = new WebView(getContext());
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new VPSAOAuthDialog.OAuthWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSavePassword(false);
        mWebView.loadUrl(mUrl);
        mWebView.setLayoutParams(FILL);
        mContent.addView(mWebView);
    }

    private class OAuthWebViewClient extends WebViewClient {
    
        @Override
        public void onReceivedError(WebView view, int errorCode,
                String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            /*// TODO: pass error back to listener!
             * mListener.onError( 
                    new DialogError(description, errorCode, failingUrl));*/
            VPSAOAuthDialog.this.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (AppContext.DEBUG)Log.d(TAG, "onPageStarted->Webview loading URL: " + url);
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
        	if (AppContext.DEBUG)Log.d(TAG, "onPageFinished->Webview URL: " + url);
        	super.onPageFinished(view, url);
        	
            String title = mWebView.getTitle();
            if (title != null && title.length() > 0) {
                mTitle.setText(title);
            }
          
            try {// to avoid crashing the app add try-catch block, avoid this stupid crash!
	            if (mSpinner!=null && mSpinner.isShowing())// by YG
	            	mSpinner.dismiss();
            }
            catch (Exception ex) {
            	Log.w(TAG, "wtf exception onPageFinished! " + ex.toString());
            }
              
            if (url.startsWith(OAUTHCALLBACK_URI)) {
                Bundle values = Utils.parseUrl(url); 
 
               
                String error = values.containsKey("error")?values.getString("error"):null;
                if (error == null) {
                    error = values.containsKey("error_type")?values.getString("error_type"):null;
                }

                if (error == null) {
                    mListener.onComplete(values);
                } else if (error.equals("access_denied") ||
                           error.equals("OAuthAccessDeniedException")) {
                    mListener.onCancel();
                } 

                VPSAOAuthDialog.this.dismiss();
            }
        }   
        
    }
    
    private class VPSADialogListener extends GenericDialogListener{
    	private Context context;
    	
    	private WebService webService = new WebService();
    	
    	public VPSADialogListener(Context context)
    	{
			this.context = context;
    	}
    	
		public void onComplete(Bundle values) {
			if(LOGGING.DEBUG)Log.d(TAG, "onComplete->"+values);
			
            CookieSyncManager.getInstance().sync();
			
			String tokenResponse = "";
			try{
				webService.setWebServiceUrl(AppContext.VPSA_OAUTH_BASE_URL);
				
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("app_id", AppContext.APP_ID);
				params.put("app_secret", AppContext.APP_SECRET);
				params.put("grant_type", "authorization_code");
				params.put("redirect_uri", AppContext.VPSA_APP_OAUTH_CALLBACK);
				params.put("code", values.getString("code"));
				if(LOGGING.DEBUG)Log.d(TAG, "params->" + params);
				
				tokenResponse = webService.webInvokeWithJson(AppContext.VPSA_OAUTH_TOKEN_URL, webService.getJsonFromParams(params));
				
				if(LOGGING.DEBUG)Log.d(TAG, "tokenResponse->" + tokenResponse);
				
				broadcastLoginResult(tokenResponse);
			}
			catch (Exception ex1){
				Log.w(TAG, ex1.toString());
				tokenResponse = null;
			}
	    }
		public void onError(String e) {
			if(LOGGING.DEBUG)Log.d(TAG, "onError->"+e);
	    }
		public void onCancel() {
			if(LOGGING.DEBUG)Log.d(TAG, "onCancel()");
	    }
		
		private void broadcastLoginResult(String jsonAuthInfo) {
			
			try {
				
				String intentAction = AppContext.BCAST_USERLOGIN_VPSA;
				String intentExtra = AppContext.INTENT_EXTRA_USERLOGIN_VPSA;
				
				if(LOGGING.DEBUG)Log.d(TAG, "sending Broadcast! " 
						+ "|intentAction->"+intentAction
						+ "|intentExtra->"+intentExtra
						+ "|payload->"+jsonAuthInfo
						);
				
				Intent mIntent = new Intent();
	        	mIntent.setAction(intentAction);
	        	
	        	mIntent.putExtra(intentExtra, new OAuthInfo(jsonAuthInfo).getBundle());
	        	
	        	context.sendBroadcast(mIntent);
			}
	    	catch (Exception ex) {
	    		Log.w(TAG, ex.toString());
	    	}
	    	
		}
		
	}
    
    
    
    
}
