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
	String URL = "http://www.paraondefoiomeudinheiro.com.br";
	// String URLBase = "http://www.paraondefoiomeudinheiro.com.br/data";
	String sNode;
	String BaseURL;
	int    Ano;
	String BaseWork;
	
    public void getJsonContent() {
        result = "";
        String tmpStr;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            
            //HttpPost httppost = new HttpPost("http://www.paraondefoiomeudinheiro.com.br/data/node/1");
            //HttpResponse response = httpclient.execute(httppost);
            
            int i1;
            i1 = BaseWork.indexOf("/dataset/");
            
            tmpStr =  BaseWork.substring(i1+9,BaseWork.indexOf("/", i1+9));
            if(BaseWork.charAt(BaseWork.length()-1) == '/')
            {
            	BaseWork = BaseWork.substring(0,BaseWork.length()-1);
            }	
            	

            sNode = BaseWork.replace(tmpStr,tmpStr + "/data"); 
            
			
            HttpGet httpGet = new HttpGet(URL +sNode);	//"http://www.paraondefoiomeudinheiro.com.br/data/node/1");
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
