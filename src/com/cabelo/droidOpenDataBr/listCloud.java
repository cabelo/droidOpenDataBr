package com.cabelo.droidOpenDataBr;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class listCloud extends ListActivity {
	Dados[] Informacoes;
	int qtde;
	StringBuffer result;
	String File;
	ProgressDialog dialog;
    String[] idS;
    String[] indiceS;
    listValue dados2;
    boolean errorFound;
    boolean foundFilter;
    int myPosition;
    String tituloLocal;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	getWindow().getDecorView().setBackgroundColor(Color.BLACK);
	   Intent it = getIntent();
	   foundFilter = false;
	   if(it != null){
	      Bundle params = it.getExtras();
	      if(params != null){
			  File = params.getString("json");
			  Log.v("droidOpenDataBr [reference] :",File);
			  result = new StringBuffer("");
              try {		  
		          BufferedReader in = new BufferedReader(new FileReader(File));  
		          int c = 0;  
 				   while ((c = in.read()) != -1) {  
				      result.append((char)c);  
				   }
				   in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
	      }
	   }
	   dados2 = new listValue();
	   dialog =  ProgressDialog.show(listCloud.this,"Aguarde...","Processando dados ...",true);
       ListView lv = getListView();
       LayoutInflater infalter = getLayoutInflater();
       ViewGroup header = (ViewGroup) infalter.inflate(R.layout.head, lv, false);
       lv.addHeaderView(header,null,false);	   
	   
	   
	   runOnUiThread(new Runnable() {
	        public void run() {
	            errorFound = false; 	
				processJSON();
				if(!errorFound)
				{
				   drawListMany();
				   //setSelection(myPosition);
				}
	        	handler.sendEmptyMessage(0);
	        }
	    });
	}
   
	public void processJSON(){
        try {
        	JSONObject jsonResponse = new JSONObject(result.toString());
      	    String query = jsonResponse.getString("total_tree");
            JSONArray jArray = jsonResponse.getJSONArray("children");
            qtde =  jArray.length();
            
   	        TextView label10=(TextView)findViewById(R.id.text10);
            TextView label11=(TextView)findViewById(R.id.text11);
            label10.setText("R$ " + query );
            label11.setText( "Itens: " + qtde );
           
            Informacoes = new Dados[ qtde];
            idS = new String[qtde];
            indiceS = new String[qtde];
			for (int i = 0; i < qtde ; i++) {
				JSONObject jsonObject = jArray.getJSONObject(i);
				JSONObject jsonResponse2  = jsonObject.getJSONObject("data");
				Informacoes[i] = new Dados();
				Informacoes[i].titulo      = jsonResponse2.getString("title");
				Informacoes[i].valor       = jsonResponse2.getString("printable_value");
				Informacoes[i].valorh      = jsonResponse2.getString("valor_tabela");
				Informacoes[i].link        = jsonResponse2.getString("link");
				Informacoes[i].porcentagem = jsonResponse2.getString("porcentagem");
				idS[i] = jsonResponse2.getString("link");
				indiceS[i] = String.format("%9.5f:",Float.parseFloat(jsonResponse2.getString("porcentagem")))+String.format("%03d",i);
				tituloLocal = Informacoes[i].titulo;
				if(tituloLocal.indexOf("PREF") >=0 )
				{
			       foundFilter = true;
				}
			}
			java.util.Arrays.sort(indiceS,Collections.reverseOrder());
			
        }catch(JSONException e) {
        	 Log.e("JSON CABELO 5:", "Error parsing data "+e.toString());
 			 Toast.makeText(this, "Dados não processados.", Toast.LENGTH_LONG).show();
			 errorFound = true;
        }
	}
	
    public void process()
    {
		Thread authJSON = new Thread() {
			public void run() {
				processJSON();
        		handler.sendEmptyMessage(0);
			}
		};
		authJSON.start();
    }
    
	public void drawListMany(){
		setListAdapter(new MyCustomAdapter(this, R.layout.data, idS));
	}	
	
	   public class MyCustomAdapter extends ArrayAdapter<String> {
           public MyCustomAdapter(Context context, int textViewResourceId,
                           String[] objects) {
                   super(context, textViewResourceId, objects);
           }
	   
       @Override
	   public View getView(int position, View convertView, ViewGroup parent) {
       	   LayoutInflater inflater=getLayoutInflater();
 	       View row;
 	       int mIndice = Integer.parseInt(indiceS[position].substring(10,13));
 	       row = inflater.inflate(R.layout.data , parent, false);
 	       TextView label1=(TextView)row.findViewById(R.id.text5);
           TextView label2=(TextView)row.findViewById(R.id.text6);
           TextView label3=(TextView)row.findViewById(R.id.text7);
           ProgressBar progressBar1=(ProgressBar)row.findViewById(R.id.progressbar);
 	       label1.setText(Informacoes[mIndice].titulo );
 	       if(foundFilter)
 	       {	 
 	          tituloLocal = Informacoes[mIndice].titulo;
 	          if(tituloLocal.indexOf("PREF") >=0 )
 	          {	   
 	             label1.setTextColor(Color.rgb(000, 000, 255));
 	             myPosition = position;
 	          }
 	          else
 	          {
  	             label1.setTextColor(Color.rgb(128, 128, 128));
  	          }
 	       } 	       
 	       label2.setText("R$ "+Informacoes[mIndice].valorh+" - "+Informacoes[mIndice].valor);
 	       label3.setText(" %"+Informacoes[mIndice].porcentagem);
           progressBar1.setProgress(Float.valueOf(Informacoes[mIndice].porcentagem.trim()).intValue());
 	       return row;
       }
   }
	   
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent intent) {
   }
   
   public void startList(){
   	Intent it = new Intent(this, listCloud.class);
   	it.putExtra("json",dados2.File);
   	startActivity(it);

   }
   
   public void getJson()
   {
		Thread authJSON = new Thread() {
			public void run() {
       		dados2.getJsonContent();
       		handler.sendEmptyMessage(0);
       		startList();

			}
		};
		authJSON.start();
   }
   
   @Override
   protected void onListItemClick(ListView l, View v, int position, long id) {
	   String tipoDados;
	   
	   int mIndice = Integer.parseInt(indiceS[position-1].substring(10,13));
	   String selection =  idS[mIndice]; //l.getItemAtPosition(mIndice-1).toString();
	   Toast.makeText(this, "Aguarde...", Toast.LENGTH_LONG).show();
       dados2.sNode = selection;
       dados2.BaseWork = selection;

       tipoDados = dados2.sNode.substring(0,7);
       if( tipoDados.equals("/credor") )
       { 
    	  // Log.e("JSON CABELO X0:","Passei aqui credor");
    	   startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.paraondefoiomeudinheiro.com.br"+dados2.sNode)));
       }
       else
       {   
    	  // Log.e("JSON CABELO X0:","Passei aqui lista");
          dialog =  ProgressDialog.show(listCloud.this,"Aguarde...","Obtendo dados...",true);
	 	  getJson();
       }
   }
   
   private Handler handler = new Handler() {
		public void handleMessage(Message msg) {	
			dialog.dismiss();
		}
	};
	
}
