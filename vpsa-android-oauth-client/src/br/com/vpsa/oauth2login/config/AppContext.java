package br.com.vpsa.oauth2login.config;

import br.com.vpsa.oauth2login.utils.LOGGING;


public class AppContext {

	public static final boolean DEBUG = LOGGING.DEBUG;

	public static final String INTENT_EXTRA_USERLOGIN_VPSA = "VPSA_LOGIN";
    public static final String BCAST_USERLOGIN_VPSA = "BROADCAST_VPSA_LOGIN";
	
	public static String APP_ID = "id da aplicacao na vpsa store";  
	public static String APP_SECRET = "secret da aplicacao na vpsa store";
	public static String VPSA_APP_OAUTH_CALLBACK = "http://aplicacao.exemplo.com/auth/vpsa/callback";
	public static final String VPSA_OAUTH_BASE_URL = "https://www.vpsa.com.br";
	public static final String VPSA_OAUTH_AUTHORIZATION_URL = "/apps/oauth/authorization";
	public static final String VPSA_OAUTH_TOKEN_URL = "/apps/oauth/token";
}
