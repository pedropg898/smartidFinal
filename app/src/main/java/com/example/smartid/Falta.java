package com.example.smartid;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.smartid.adapters.CustomArrayAdapter;
import com.example.smartid.entities.Aluno;
import com.example.smartid.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


    public class Falta extends AppCompatActivity {

        ArrayList<Aluno> ap = new ArrayList<>();
        EditText editText, et2;
        final Calendar myCalendar = Calendar.getInstance();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.falta);

            if (!haveNetwork()) {
                Toast.makeText(Falta.this, getResources().getString(R.string.NET), Toast.LENGTH_SHORT).show();
            }
        }

        private boolean haveNetwork() {
            boolean have_WIFI = false;
            boolean have_MobileData = false;
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
            for (NetworkInfo info : networkInfos) {
                if (info.getTypeName().equalsIgnoreCase("WIFI"))
                    if (info.isConnected()) have_WIFI = true;
                if (info.getTypeName().equalsIgnoreCase("MOBILE DATA"))
                    if (info.isConnected()) have_MobileData = true;
            }
            return have_WIFI || have_MobileData;
        }



        //Faz set text na edit text para depois ser passado em string quando é clicado no btn "ok"
        private void updateLabel() {
            String myFormat = "yyyy-MM-dd"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            et2.setText(sdf.format(myCalendar.getTime()));
        }

        @Override
        protected void onResume() {
            super.onResume();
            //Reset ao arraylist
            if(!ap.isEmpty()) {
                ap.clear();
            }

            String url = "http://"+ Utils.IP +"/smartid/api/faltas/1";


            // Formulate the request and handle the response.
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                //Toast.makeText(getActivity(), response.getString("status"), Toast.LENGTH_SHORT).show();
                                JSONArray arr = response.getJSONArray("DATA");
                                //Percorre o array e pega nos valores pretendidos
                                for (int i = 0; i < arr.length(); i++) {
                                    JSONObject obj = arr.getJSONObject(i);

                                    //Cria pessoa
                                    Aluno p = new Aluno(obj.getString("UC"), obj.getString("Professor"), obj.getString("Data falta"));

                                    ap.add(p);  //adiciona ao array
                                }
                                CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(Falta.this, ap);
                                ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);
                                itemsAdapter.notifyDataSetChanged();
                            } catch (JSONException ex) {
                                //Log.d("exc",ex.getMessage());
                                //Toast.makeText(Falta.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (!haveNetwork()) {
                                Toast.makeText(Falta.this, getResources().getString(R.string.NET), Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Falta.this, getResources().getString(R.string.NER), Toast.LENGTH_SHORT).show();

                                Toast.makeText(Falta.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("Erro", error.getMessage());

                            }
                        }
                    });
            MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_pf, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle item selection
            switch (item.getItemId()) {
                case R.id.action_sort:
                    //icon
                    break;
                case R.id.LP:
                    onResume();
                    break;
                case R.id.UC:

                    final Dialog dialog=new Dialog(this);
                    dialog.setContentView(R.layout.input_box);
                    editText=(EditText)dialog.findViewById(R.id.txtinput);
                    Button bt=(Button)dialog.findViewById(R.id.btdone);
                    bt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(!ap.isEmpty()) {
                                ap.clear();
                            }

                            String url = "http://"+Utils.IP+"/smartid/api/faltas/1";


                            // Formulate the request and handle the response.
                            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {
                                                //Toast.makeText(getActivity(), response.getString("status"), Toast.LENGTH_SHORT).show();
                                                JSONArray arr = response.getJSONArray("DATA");
                                                //Percorre o array e pega nos valores pretendidos
                                                for (int i = 0; i < arr.length(); i++) {
                                                    JSONObject obj = arr.getJSONObject(i);


                                                    if(editText.getText().toString().toLowerCase().equals(obj.getString("UC").toLowerCase()))
                                                    {
                                                        Aluno p = new Aluno(obj.getString("UC"), obj.getString("Professor"), obj.getString("Data falta"));
                                                        ap.add(p);  //adiciona ao array
                                                    }

                                                }
                                                if(ap.isEmpty()) {
                                                    Toast.makeText(Falta.this, getResources().getString(R.string.NEUC), Toast.LENGTH_SHORT).show();
                                                    onResume();
                                                }

                                                CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(Falta.this, ap);
                                                ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);
                                                itemsAdapter.notifyDataSetChanged();
                                            } catch (JSONException ex) {

                                                //Log.d("exc",ex.getMessage());
                                                //Toast.makeText(Falta.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            if (!haveNetwork()) {
                                                Toast.makeText(Falta.this, getResources().getString(R.string.NET), Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(Falta.this, getResources().getString(R.string.NER), Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                            MySingleton.getInstance(Falta.this).addToRequestQueue(jsObjRequest);



                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    break;
                case R.id.Dia:

                    final Dialog dialog2=new Dialog(this);
                    dialog2.setContentView(R.layout.input_box2);
                    et2=(EditText)dialog2.findViewById(R.id.txtinput2);
                    Button bt2=(Button)dialog2.findViewById(R.id.btdone2);
                    ///////////////////////////////////////////////////////
                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            updateLabel();
                        }

                    };
                    et2.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            new DatePickerDialog(Falta.this, date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });
                    ///////////////////////////////////////////////////////
                    bt2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if(!ap.isEmpty()) {
                                ap.clear();
                            }


                            String url3 = "http://"+Utils.IP+"/smartid/api/diaf/1&" + et2.getText().toString();

                            // Formulate the request and handle the response.
                            JsonObjectRequest jsObjRequest3 = new JsonObjectRequest
                                    (Request.Method.GET, url3, null, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            try {

                                                JSONArray arr = response.getJSONArray("DATA");
                                                //Percorre o array e pega nos valores pretendidos
                                                for (int i = 0; i < arr.length(); i++) {
                                                    JSONObject obj = arr.getJSONObject(i);

                                                    Aluno p = new Aluno(obj.getString("UC"), obj.getString("Professor"), obj.getString("Data falta"));

                                                    ap.add(p);  //adiciona ao array

                                                }


                                                CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(Falta.this, ap);
                                                ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);
                                                itemsAdapter.notifyDataSetChanged();
                                            } catch (JSONException ex) {

                                                //Log.d("exc",ex.getMessage());
                                                //Toast.makeText(Falta.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                            //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            if (!haveNetwork()) {
                                                Toast.makeText(Falta.this, getResources().getString(R.string.NEPND), Toast.LENGTH_SHORT).show();
                                            }
                                            else {
                                                Toast.makeText(Falta.this, getResources().getString(R.string.NER), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                            MySingleton.getInstance(Falta.this).addToRequestQueue(jsObjRequest3);

                            dialog2.dismiss();
                        }
                    });
                    dialog2.show();

                    break;
                case R.id.L10I:
                    if(!ap.isEmpty()) {
                        ap.clear();
                    }

                    String url4 = "http://"+ Utils.IP+"/smartid/api/ordeml10f/1";

                    // Formulate the request and handle the response.
                    JsonObjectRequest jsObjRequest4 = new JsonObjectRequest
                            (Request.Method.GET, url4, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        //Toast.makeText(getActivity(), response.getString("status"), Toast.LENGTH_SHORT).show();
                                        JSONArray arr = response.getJSONArray("DATA");
                                        //Percorre o array e pega nos valores pretendidos
                                        for (int i = 0; i < arr.length(); i++) {
                                            JSONObject obj = arr.getJSONObject(i);

                                            //Cria pessoa
                                            Aluno p = new Aluno(obj.getString("UC"), obj.getString("Professor"), obj.getString("Data falta"));

                                            ap.add(p);  //adiciona ao array
                                        }
                                        CustomArrayAdapter itemsAdapter = new CustomArrayAdapter(Falta.this, ap);
                                        ((ListView) findViewById(R.id.lista)).setAdapter(itemsAdapter);
                                        itemsAdapter.notifyDataSetChanged();
                                    } catch (JSONException ex) {
                                        //Log.d("exc",ex.getMessage());
                                        //Toast.makeText(Falta.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                    //Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (!haveNetwork()) {
                                        Toast.makeText(Falta.this, getResources().getString(R.string.NET), Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(Falta.this, getResources().getString(R.string.NER), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    MySingleton.getInstance(this).addToRequestQueue(jsObjRequest4);
                    break;
                default:
                    return super.onOptionsItemSelected(item);
            }
            return true;
        }
    }

