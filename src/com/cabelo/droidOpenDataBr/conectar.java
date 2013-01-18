package com.cabelo.droidOpenDataBr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;

class Dados

{
    public String titulo, valor, valorh, link, porcentagem;
}

public class conectar extends Activity {
	listValue dados1;
	int qtde;
	ProgressDialog dialog;
    String BaseNode;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        setContentView(R.layout.main);
        dados1 = new listValue(); 
        dados1.BaseURL = "/dataset/estado-sao-paulo-";	
        dados1.Ano = 2012;
        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.base_array, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner1.setEnabled(false);
        spinner1.setAdapter(adapter1);
        
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.periodo_array, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner2.setEnabled(false);
        spinner2.setAdapter(adapter2);
        
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	@Override 
        	public void onItemSelected(AdapterView<?> parent, View v, int posicao, long  id){
        		if(posicao ==0){
        			// Estado de Sao Paulo
        			dados1.BaseURL = "/dataset/estado-sao-paulo-";	
        		}
        		if(posicao ==1){
        			// Governo Federal
        			dados1.BaseURL = "/dataset/federal-direto-";
        			
        		}
        		if(posicao ==2){
        		    			// Municipio de Sao Paulo
        			dados1.BaseURL = "/dataset/municipio-sao-paulo-";
    			    
    		}

        	}
        	
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});


        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        	@Override 
        	public void onItemSelected(AdapterView<?> parent, View v, int posicao, long  id){
                    dados1.Ano = 2012-posicao;
        			
        		
        	}
        	
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
		});     
        
        final Button  capture = (Button) findViewById(R.id.button1);
        
        
        capture.setOnClickListener(new OnClickListener(){
        	public void onClick(View v){

         	    dados1.BaseWork = dados1.BaseURL+ Integer.toString(dados1.Ano)+"/";
         	    dados1.sNode = "(vazio)";
        		//Log.e("JSON CABELO 4:",dados1.sNode);

        		dialog =  ProgressDialog.show(conectar.this,"Aguarde...","Obtendo dados...",true);
        		getJson();
        	}
        });
    }
    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.BLACK);
    }
    public void getJson()
    {
		Thread authJSON = new Thread() {
			public void run() {
        		dados1.getJsonContent();
        		handler.sendEmptyMessage(0);
        		startList();

			}
		};
		authJSON.start();
    }
    
    public void startList(){
    	Intent it = new Intent(this, listCloud.class);
    	it.putExtra("json",dados1.result);
    	Log.e("JSON CABELO 4:",dados1.result);
    	startActivity(it);
    }
   
    private Handler handler = new Handler() {
		public void handleMessage(Message msg) {			
			dialog.dismiss();
		}
	};
}