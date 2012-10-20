package br.com.vpsa.oauth2login.example;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import br.com.vpsa.oauth2login.config.AppContext;
import br.com.vpsa.oauth2login.utils.WebService;
import br.com.vpsa.oauth2login.vpsa.VPSAOAuthDialog;

public class AppMainExample extends Activity {
    
	static
	{
		AppContext.APP_ID = "505db9e470a7df43aa000039";  
		AppContext.APP_SECRET = "f307f700ca530040e5fab3af8b4353c06a747ff608807420e6893bf6dfe3ca69";
		AppContext.VPSA_APP_OAUTH_CALLBACK = "http://notificador-pagamentos.herokuapp.com/auth/vpsa/callback";
	}
	
	protected static final String TAG = "AppMainExample";
	
	public Context mContext;
	public Activity mActivity;
	public WebService webService;
	
	private TextView texto;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        mContext = this;
        mActivity = this;
        setContentView(R.layout.appmainexample);
        
        texto = (TextView) findViewById(R.id.texto);
        
        IntentFilter filter = new IntentFilter(AppContext.BCAST_USERLOGIN_VPSA);
        this.registerReceiver(new MyReceiver(this), filter); 
    }
    
    public void onClick_vpsaLogin(View v)
    {
		new VPSAOAuthDialog(mContext).show();
    }
    
    private class MyReceiver extends BroadcastReceiver {
    	
    	private AppMainExample appMainExample;

		public MyReceiver(AppMainExample appMainExample)
    	{
			this.appMainExample = appMainExample;
    	}
    	
		@Override
		public void onReceive(Context context, Intent intent) {
			appMainExample.texto.setText(intent.getExtras().getString(AppContext.INTENT_EXTRA_USERLOGIN_VPSA));
		}
    	
    }
	
}