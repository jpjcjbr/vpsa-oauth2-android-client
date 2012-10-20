package br.com.vpsa.oauth2login.example;


import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import br.com.vpsa.oauth2login.config.AppContext;
import br.com.vpsa.oauth2login.config.OAuthInfo;
import br.com.vpsa.oauth2login.utils.WebService;
import br.com.vpsa.oauth2login.vpsa.VPSAOAuthDialog;

public class AppMainExample extends Activity {
    
	static
	{
		AppContext.APP_ID = "50679703d93a4b347f000017";  
		AppContext.APP_SECRET = "493a98a5509088aa6ce6a5cf1cad9e8052f102d2e3c58cbc1502f0d531f0c608";
		AppContext.VPSA_APP_OAUTH_CALLBACK = "http://localhost:3000/auth/vpsa/callback";
	}
	
	public Context mContext;
	public Activity mActivity;
	public WebService webService = new WebService();
	public Bundle bundle = new Bundle();
	
	private TextView texto;
	private TextView entidades;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = this;
        mActivity = this;
        setContentView(R.layout.appmainexample);
        
        texto = (TextView) findViewById(R.id.texto);
        entidades = (TextView) findViewById(R.id.entidades);
        
        IntentFilter filter = new IntentFilter(AppContext.BCAST_USERLOGIN_VPSA);
        this.registerReceiver(new MyReceiver(this), filter); 
    }
    
    public void onClick_vpsaLogin(View v)
    {
		new VPSAOAuthDialog(mContext).show();
    }
    
    public void onClick_getEntidades(View v)
    {
    	webService.setWebServiceUrl(AppContext.VPSA_OAUTH_BASE_URL);
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("token", bundle.getString(OAuthInfo.ACCESS_TOKEN));
    	
		String jsonEntidades = webService.webGet("/apps/api/entidades", map);
		
		entidades.setText(jsonEntidades);
    }
    
    private class MyReceiver extends BroadcastReceiver {
    	
    	private AppMainExample appMainExample;

		public MyReceiver(AppMainExample appMainExample)
    	{
			this.appMainExample = appMainExample;
    	}
    	
		@Override
		public void onReceive(Context context, Intent intent) {
			Bundle bundle = intent.getExtras().getBundle(AppContext.INTENT_EXTRA_USERLOGIN_VPSA);
			appMainExample.bundle = bundle;
			
			appMainExample.texto.setText(bundle.getString(OAuthInfo.ACCESS_TOKEN));
		}
    	
    }
	
}