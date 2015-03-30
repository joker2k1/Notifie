package ru.notofie.notifie;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class SignIn extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
    }

    public void checkPassword(View v) {

        RequestQueue queue = Volley.newRequestQueue(this);

        final TextView login = (TextView) findViewById(R.id.login);
        final TextView password = (TextView) findViewById(R.id.password);

        String strLogin = login.getText().toString();
        String strPassword = password.getText().toString();

        String url ="http://notifie.ru/users/sign_in.json";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email",             strLogin);
        params.put("password",          strPassword);
        params.put("device_token",      "1");
        params.put("android",           "true");
        params.put("android_version",   "4.1.2");

        JsonObjectRequest getJSON = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            Intent nextScreen = new Intent(getApplicationContext(), main.class);

                            // parse token

                            String token = response.getString("authentication_token");

                            Log.d("token: ", token);

                            startActivity(nextScreen.putExtra("token", token));

                        } catch (JSONException e) {

                            e.printStackTrace();

                        }
                    }
                },

                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(SignIn.this);
                        builder.setTitle( getString(R.string.sign_in_err))
                                //.setMessage("Messa")
                                //.setIcon(R.drawable.ic_android_cat)
                                .setCancelable(false)
                                .setNegativeButton( getString(R.string.sign_in_err_button),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                        //Log.e("error: ", error.getMessage());

                    }

                }

        );

        queue.add(getJSON);

    }



    // settings

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
