package com.example.smartid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.smartid.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {


    //Security s = new Security();
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private CheckBox saveLoginCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getResources().getString(R.string.login));
        setContentView(R.layout.login);

        sharedPreferences=getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
        final Boolean isloggedin=sharedPreferences.getBoolean("ISLOGGEDIN",false);
        if(isloggedin)
        {
            Intent main = new Intent(Login.this, MainActivity.class);
            startActivity(main);
        }

        final EditText name_field=(EditText)findViewById(R.id.login_name);
        final EditText password_field=(EditText)findViewById(R.id.login_password);
        Button login=(Button)findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String nome=name_field.getText().toString();
                final String password=password_field.getText().toString();

                String url = "http://"+ Utils.IP+"/smartid/api/aluno/" + nome +"&" + password;

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try{
                                    sharedPreferences = getSharedPreferences("USER_CREDENTIALS", MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    saveLoginCheckBox = (CheckBox) findViewById(R.id.logincb);
                                    editor.putInt("IDUSER", response.getInt("id"));
                                    editor.putString("NAME", nome);
                                    editor.putString("PASSWORD", password);
                                    if (saveLoginCheckBox.isChecked()) {
                                        editor.putBoolean("ISLOGGEDIN", true);
                                        editor.commit();
                                    } else {
                                        editor.commit();
                                    }

                                    Intent main = new Intent(Login.this, MainActivity.class);
                                    startActivity(main);

                                }catch(JSONException ex){ }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("Erro",error.getMessage());
                                Toast.makeText(Login.this, getResources().getString(R.string.NPI), Toast.LENGTH_SHORT).show();
                            }
                        });
                MySingleton.getInstance(Login.this).addToRequestQueue(jsObjRequest);

            }
        });

    }



}