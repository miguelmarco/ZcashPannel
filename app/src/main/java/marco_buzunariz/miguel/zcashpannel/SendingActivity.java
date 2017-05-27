package marco_buzunariz.miguel.zcashpannel;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import info.guardianproject.netcipher.client.StrongBuilder;
import info.guardianproject.netcipher.client.StrongVolleyQueueBuilder;

public class SendingActivity extends AppCompatActivity {

    String operationid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending);

        Intent intent = getIntent();
        String fromaddress = intent.getStringExtra("fromaddress");
        String toaddress = intent.getStringExtra("toaddress");
        String onion = intent.getStringExtra("onion");
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");
        BigDecimal amount = new BigDecimal(intent.getStringExtra("value"));
        BigDecimal fee = new BigDecimal(intent.getStringExtra("fee"));

        ProgressBar progressbar = (ProgressBar)findViewById(R.id.progressBar);
        progressbar.setIndeterminate(true);

        TextView message = (TextView)findViewById(R.id.textViewResult);
        message.setText("Processing transaction\nFrom: "+fromaddress+"\nTo: "+toaddress+"\nAmount: "+amount.toString()+"\nFee: "+fee.toString());



        try{
            JSONObject req = new JSONObject();
            req.put("version", "2");
            req.put("method","z_sendmany");
            req.put("id",0);
            JSONArray params = new JSONArray();
            params.put(fromaddress);

            JSONArray targets = new JSONArray();
            JSONObject uniquetarget = new JSONObject();
            uniquetarget.put("address", toaddress);
            uniquetarget.put("amount", amount.doubleValue());
            targets.put(uniquetarget);
            params.put(targets);
            params.put(1);
            params.put(fee.doubleValue());


            req.put("params",params);

            sendrequest("http://"+onion, req, username, password);


        }
        catch (Exception e) {}

    }


    private void sendrequest (final String currentUrl, final JSONObject jsonrequest, final String username, final String password)
    {
        try {
            StrongVolleyQueueBuilder
                    .forMaxSecurity(this)
                    .withTorValidation()
                    .build(new StrongBuilder.Callback<RequestQueue>() {

                        @Override
                        public void onConnected(final RequestQueue rq) {
                            new Thread() {
                                @Override
                                public void run() {
                                    final JsonObjectRequest jsonRequest=
                                            new JsonObjectRequest(Request.Method.POST, currentUrl, jsonrequest,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(final JSONObject response) {


                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    //this is where you call the code to set the user interface with the response
                                                                    try {
                                                                        String result = response.getString("result");
                                                                        operationid = result;

                                                                        TextView tv = (TextView)findViewById(R.id.textViewResult);
                                                                        String content = tv.getText().toString();
                                                                        tv.setText(content + "\n" + result);

                                                                        JSONObject req = new JSONObject();
                                                                        req.put("version", "2");
                                                                        req.put("method","z_getoperationstatus");
                                                                        req.put("id",0);
                                                                        JSONArray params = new JSONArray();
                                                                        JSONArray ids = new JSONArray();
                                                                        ids.put(operationid);
                                                                        params.put(ids);
                                                                        req.put("params",params);


                                                                        checkrequest(currentUrl, req, username, password);
                                                                    }
                                                                    catch (Exception e) {}
                                                                }

                                                            });
                                                        }

                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError err) {

                                                        }
                                                    })
                                            {
                                                @Override
                                                public Map<String, String> getHeaders() throws AuthFailureError {
                                                    HashMap<String, String> params = new HashMap<String, String>();

                                                    String creds = String.format("%s:%s",username,password);
                                                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                                                    params.put("Authorization", auth);
                                                    return params;
                                                }
                                            };
                                    RetryPolicy policy = new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                    jsonRequest.setRetryPolicy(policy);
                                    rq.add(jsonRequest);
                                }
                            }.start();
                        }

                        @Override
                        public void onConnectionException(Exception e) {

                        }

                        @Override
                        public void onTimeout() {

                        }

                        @Override
                        public void onInvalid() {
                        }

                    });
        }
        catch (Exception e) {

        }
    }


    private void checkrequest (final String currentUrl, final JSONObject jsonrequest, final String username, final String password)
    {
        try {
            StrongVolleyQueueBuilder
                    .forMaxSecurity(this)
                    .withTorValidation()
                    .build(new StrongBuilder.Callback<RequestQueue>() {

                        @Override
                        public void onConnected(final RequestQueue rq) {
                            new Thread() {
                                @Override
                                public void run() {
                                    final JsonObjectRequest jsonRequest=
                                            new JsonObjectRequest(Request.Method.POST, currentUrl, jsonrequest,
                                                    new Response.Listener<JSONObject>() {
                                                        @Override
                                                        public void onResponse(final JSONObject response) {


                                                            runOnUiThread(new Runnable() {
                                                                @Override
                                                                public void run() {

                                                                    //this is where you call the code to set the user interface with the response
                                                                    try {
                                                                        JSONArray result = response.getJSONArray("result");
                                                                        if (result.length() == 1) {
                                                                            JSONObject resultunique = result.getJSONObject(0);

                                                                            String status = resultunique.getString("status");

                                                                            if (status.equals("success")) {
                                                                                AlertDialog.Builder dialog = new AlertDialog.Builder(SendingActivity.this);

                                                                                dialog.setTitle("Success:")
                                                                                        .setMessage("The transaction was processed and broadcasted")
                                                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                            public void onClick(DialogInterface dialoginterface, int i) {
                                                                                                finish();
                                                                                            }
                                                                                        }).show();
                                                                            } else if (status.equals("failed")) {
                                                                                JSONObject errorobject = resultunique.getJSONObject("error");
                                                                                String errormessage = errorobject.getString("message");

                                                                                AlertDialog.Builder dialog = new AlertDialog.Builder(SendingActivity.this);
                                                                                dialog.setTitle("Error:")
                                                                                        .setMessage("The transaction failed\n"+errormessage)
                                                                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                                                            public void onClick(DialogInterface dialoginterface, int i) {
                                                                                                finish();
                                                                                            }
                                                                                        }).show();
                                                                            }

                                                                            else if (status.equals("executing") || status.equals("pending"))
                                                                            {
                                                                                checkrequest(currentUrl, jsonrequest, username, password);
                                                                            }
                                                                        }
                                                                    }
                                                                    catch (Exception e) {
                                                                        finish();
                                                                    }
                                                                }

                                                            });
                                                        }

                                                    },
                                                    new Response.ErrorListener() {
                                                        @Override
                                                        public void onErrorResponse(VolleyError err) {

                                                        }
                                                    })
                                            {
                                                @Override
                                                public Map<String, String> getHeaders() throws AuthFailureError {
                                                    HashMap<String, String> params = new HashMap<String, String>();

                                                    String creds = String.format("%s:%s",username,password);
                                                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                                                    params.put("Authorization", auth);
                                                    return params;
                                                }
                                            };
                                    RetryPolicy policy = new DefaultRetryPolicy(12000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                                    jsonRequest.setRetryPolicy(policy);
                                    rq.add(jsonRequest);
                                }
                            }.start();
                        }

                        @Override
                        public void onConnectionException(Exception e) {

                        }

                        @Override
                        public void onTimeout() {

                        }

                        @Override
                        public void onInvalid() {
                        }

                    });
        }
        catch (Exception e) {

        }
    }
}
