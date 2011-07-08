package com.cabelo.droidOpenDataBr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.util.Log;

public class listValue {
	InputStream is;
	String result;
	String URLBase = "http://www.paraondefoiomeudinheiro.com.br/data";
	String sNode;
    
    public void getJsonContent() {
        result = "";

        try {
            HttpClient httpclient = new DefaultHttpClient();
            //HttpPost httppost = new HttpPost("http://www.paraondefoiomeudinheiro.com.br/data/node/1");
            //HttpResponse response = httpclient.execute(httppost);
            HttpGet httpGet = new HttpGet(URLBase + sNode);	//"http://www.paraondefoiomeudinheiro.com.br/data/node/1");
			HttpResponse response = httpclient.execute(httpGet);
			HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch(Exception e) {
            Log.e("JSON CABELO 0:", "Error in http connection "+e.toString());
        }

        try {
           //BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
        	BufferedReader reader = new BufferedReader(new InputStreamReader(is),8);
           StringBuilder sb = new StringBuilder();
           String line = null;
           while ((line = reader.readLine()) != null) {
               sb.append(line);
           }
           is.close();
           result=sb.toString();
        } catch(Exception e) {
            Log.e("JSON CABELO 1:", "Error converting result "+e.toString());
        }

    }    



}
