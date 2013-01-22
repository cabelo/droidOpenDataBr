package com.cabelo.droidOpenDataBr;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
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
	StringBuffer result;
	String URL = "http://www.paraondefoiomeudinheiro.com.br";
	String sNode;
	String BaseURL;
	int    Ano;
	String BaseWork;
	String File;
	boolean FileExist;
	
    public void getJsonContent() {
        int i1;
        result = new StringBuffer("");
        String tmpStr;

        try {
            HttpClient httpclient = new DefaultHttpClient();
            i1 = BaseWork.indexOf("/dataset/");
            tmpStr =  BaseWork.substring(i1+9,BaseWork.indexOf("/", i1+9));
            if(BaseWork.charAt(BaseWork.length()-1) == '/')
            {
            	BaseWork = BaseWork.substring(0,BaseWork.length()-1);
            }	
            	

            sNode = BaseWork.replace(tmpStr,tmpStr + "/data"); 
            File = URL.substring(7,URL.length()-7) +sNode;
            //File = "/sdcard/cabelo/"+File.replace("/", ".");
            File = "/data/data/com.cabelo.droidOpenDataBr/"+File.replace("/", ".");
            Log.v("droidOpenDataBr [cache_json]:",  File);
            
            FileExist = new java.io.File(File).exists();
            if(!FileExist) 
            {
               HttpGet httpGet = new HttpGet(URL +sNode);
               HttpResponse response = httpclient.execute(httpGet);
			   HttpEntity entity = response.getEntity();
               is = entity.getContent();
            }
            
        } catch(Exception e) {
            Log.e("droidOpenDataBr [erro_http]", "Error in http connection "+e.toString());
        }

        try {
            //BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            if(!FileExist)
            {
        	   BufferedReader reader = new BufferedReader(new InputStreamReader(is),8);
               StringBuilder sb = new StringBuilder();
               String line = null;
               while ((line = reader.readLine()) != null) 
               {
                  sb.append(line);
               }
               is.close();

               BufferedWriter out = new BufferedWriter(new FileWriter(File));  
               out.write(sb.toString());  
               out.flush();  
               out.close();                
            }
        } catch(Exception e) {
            Log.e("droidOpenDataBr [error_BufferReader]:", "Error converting result "+e.toString());
        }

    }    



}
