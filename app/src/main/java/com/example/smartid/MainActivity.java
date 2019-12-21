package com.example.smartid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
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
