package com.example.smartid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String user_name;
    int id_user;
    TextView et,et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        sharedPreferences = getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
        user_name = sharedPreferences.getString("NAME","DEFAULT_NAME");
        id_user = sharedPreferences.getInt("IDUSER", -1);
        et   =  findViewById(R.id.nome);
        et.append(" "+user_name);
        et2   = findViewById(R.id.numero);
        et2.append(" "+id_user);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_logout:
                //Logout
                Intent login=new Intent(this, Login.class);
                final SharedPreferences sharedPreferences= getSharedPreferences("USER_CREDENTIALS",MODE_PRIVATE);
                sharedPreferences.edit().putBoolean("ISLOGGEDIN",false).commit();
                startActivity(login);
                finish();

                break;
            case R.id.verpresencas:
                //Logout
                Intent presenca=new Intent(this, Presenca.class);
                startActivity(presenca);


                break;
            case R.id.verfaltas:
                //Logout
                Intent falta=new Intent(this, Falta.class);
                startActivity(falta);


                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
