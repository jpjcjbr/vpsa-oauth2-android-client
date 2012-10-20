package br.com.vpsa.oauth2login.config;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;

public class OAuthInfo {

	public static final String ACCESS_TOKEN = "access_token";
	public static final String EXPIRES_IN = "expires_in";
	public static final String REFRESH_TOKEN = "refresh_token";
	public static final String CNPJ_EMPRESA = "cnpj_empresa";
	public static final String ID_TERCEIRO = "id_terceiro";
	public static final String NOME_TERCEIRO = "nome_terceiro";
	
	private Bundle bundle;
	
	public OAuthInfo(String jsonAuthInfo) {
		try {
			JSONObject jsonObject = new JSONObject(jsonAuthInfo);
			
			Bundle bundle = new Bundle();
			bundle.putString(ACCESS_TOKEN, jsonObject.getString(ACCESS_TOKEN));
			bundle.putString(EXPIRES_IN, jsonObject.getString(EXPIRES_IN));
			bundle.putString(REFRESH_TOKEN, jsonObject.getString(REFRESH_TOKEN));
			bundle.putString(CNPJ_EMPRESA, jsonObject.getString(CNPJ_EMPRESA));
			bundle.putString(ID_TERCEIRO, jsonObject.getString(ID_TERCEIRO));
			bundle.putString(NOME_TERCEIRO, jsonObject.getString(NOME_TERCEIRO));
		
			this.bundle = bundle;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public Bundle getBundle() {		
		return bundle;
	}

}
