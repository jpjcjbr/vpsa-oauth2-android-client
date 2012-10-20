package br.com.vpsa.oauth2login.config;

import br.com.vpsa.oauth2login.utils.LOGGING;


public class AppContext {

	public static final boolean DEBUG = LOGGING.DEBUG;// enable/disable logging

	public static final String INTENT_EXTRA_USERLOGIN_VPSA = "USERLOGIN_FSQ";
    public static final String BCAST_USERLOGIN_VPSA = "com.wareninja.android.opensource.oauth2login.BCAST_USERLOGIN_FSQ";
	
	public static final String APP_ID = "505db9e470a7df43aa000039";  
	public static final String APP_SECRET = "f307f700ca530040e5fab3af8b4353c06a747ff608807420e6893bf6dfe3ca69";
	public static final String VPSA_APP_OAUTH_CALLBACK = "http://notificador-pagamentos.herokuapp.com/auth/vpsa/callback";// YOURAPP_REDIRECT_URI
	public static final String VPSA_OAUTH_BASE_URL = "https://www.vpsa.com.br";
	public static final String VPSA_OAUTH_AUTHORIZATION_URL = "/apps/oauth/authorization";
	public static final String VPSA_OAUTH_TOKEN_URL = "/apps/oauth/token";
	
	public static final int FACEBOOK_SSO_ACTIVITY_CODE = 8844;
}
