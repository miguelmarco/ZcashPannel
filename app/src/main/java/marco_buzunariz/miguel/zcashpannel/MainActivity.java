package marco_buzunariz.miguel.zcashpannel;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.guardianproject.netcipher.client.StrongBuilder;
import info.guardianproject.netcipher.client.StrongVolleyQueueBuilder;
import info.guardianproject.netcipher.proxy.OrbotHelper;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> listBalancesArray=new ArrayList<String>();
    ArrayAdapter<String> adapterBalances;
    ArrayList<String> listShieldedBalancesArray=new ArrayList<String>();
    ArrayAdapter<String> adapterShieldedBalances;
    ArrayList<String> listSendBalancesArray=new ArrayList<String>();
    ArrayAdapter<String> adapterSendBalances;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost host = (TabHost)findViewById(android.R.id.tabhost);
        host.setup();

        TabHost.TabSpec spec = host.newTabSpec("Tab One");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Balances");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Tab Two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Send");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Tab Three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Receive");
        host.addTab(spec);

        spec = host.newTabSpec("Tab Four");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Config");
        host.addTab(spec);

        adapterBalances=new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1,
            listBalancesArray);

        adapterShieldedBalances=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listShieldedBalancesArray);

        adapterSendBalances=new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listSendBalancesArray);




        ListView listbalances = (ListView) findViewById(R.id.listBalances);
        listbalances.setAdapter(adapterBalances);
        ListView listshieldedbalances = (ListView) findViewById(R.id.listShieldedAddresses);
        listshieldedbalances.setAdapter(adapterShieldedBalances);
        Spinner spinneraddresses = (Spinner)findViewById(R.id.spinnersendaddresses);
        spinneraddresses.setAdapter(adapterSendBalances);

        final Spinner spinnerreceiveaddresses = (Spinner)findViewById(R.id.spinnerreceiveadresses);
        spinnerreceiveaddresses.setAdapter(adapterShieldedBalances);


        Button buttonscan = (Button)findViewById(R.id.buttonscan);
        buttonscan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.setPackage("com.google.zxing.client.android");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 0);
            }


        });

        Button buttonscanrequest = (Button)findViewById(R.id.buttonScanRequest);
        buttonscanrequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                intent.setPackage("com.google.zxing.client.android");
                intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
                startActivityForResult(intent, 1);
            }


        });




        Button buttonupdate = (Button)findViewById(R.id.buttonUpdate);
        buttonupdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                ListView listbalances = (ListView) findViewById(R.id.listBalances);
//                String[] values = new String[]{"Getting values"
//                };
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
//                        android.R.layout.simple_list_item_1, android.R.id.text1, values);
//
//                listbalances.setAdapter(adapter);

                EditText onionedit = (EditText)findViewById(R.id.editTextOnion);
                String onion = onionedit.getText().toString();

                try {


                    JSONObject req = new JSONObject();
                    req.put("version", "2");
                    req.put("method","z_gettotalbalance");
                    req.put("id",0);
                    JSONArray params = new JSONArray();

                    req.put("params",params);

                    adapterBalances.clear();
                    adapterBalances.add("Getting balances");

                    doRequestBalances("http://"+onion, req);

                    adapterShieldedBalances.clear();
                    adapterShieldedBalances.add("Getting addresses");

                    adapterSendBalances.clear();
                    adapterSendBalances.add("Getting addresses");

                    JSONObject reqsh = new JSONObject();
                    reqsh.put("version", "2");
                    reqsh.put("method","z_listaddresses");
                    reqsh.put("id",0);
                    JSONArray paramssh = new JSONArray();

                    req.put("params",paramssh);

                    doRequestBalancesShielded("http://"+onion, reqsh);


                    JSONObject reqtr = new JSONObject();
                    reqtr.put("version", "2");
                    reqtr.put("method","getaddressesbyaccount");
                    reqtr.put("id",0);
                    JSONArray paramstr = new JSONArray();
                    paramstr.put("");

                    reqtr.put("params",paramstr);

                    doRequestBalancesShielded("http://"+onion, reqtr);

                }
                catch (Exception e)
                {};



            }
        });

        SharedPreferences settings = getPreferences(MODE_PRIVATE);

        String username = settings.getString("username", "");
        String onion = settings.getString("onion", "");
        String password = settings.getString("password", "");

        EditText useredit = (EditText)findViewById(R.id.editTextUsername);
        useredit.setText(username);
        EditText onionedit = (EditText)findViewById(R.id.editTextOnion);
        onionedit.setText(onion);
        EditText passedit = (EditText)findViewById(R.id.editTextPassword);
        passedit.setText(password);

        OrbotHelper.get(this).init();






    }




    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

                String[] parts = contents.split(":");
                String username = parts[0];
                String[] parts2 = parts[1].split("@");
                String password = parts2[0];
                String onion = parts2[1];

                EditText useredit = (EditText)findViewById(R.id.editTextUsername);
                useredit.setText(username);
                EditText onionedit = (EditText)findViewById(R.id.editTextOnion);
                onionedit.setText(onion);

                EditText passedit = (EditText)findViewById(R.id.editTextPassword);
                passedit.setText(password);



            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }

        else if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");





                String[] parts = contents.split(":");
                String protocol = parts[0];
                if (protocol.matches("zcash")) {

                    String[] body = parts[1].split("\\?");
                    String address = body[0];
                    String amount = body[1].split("amount=")[1].split("&")[0];


                    EditText addressedit = (EditText) findViewById(R.id.editTextToAdress);
                    addressedit.setText(address);
                    EditText amountedit = (EditText) findViewById(R.id.editTextSendAmount);
                    amountedit.setText(amount);
                }


            } else if (resultCode == RESULT_CANCELED) {
                // Handle cancel
            }
        }
    }

    public void sendAction(View view) {
        Spinner spinerreceiveaddresses = (Spinner) findViewById(R.id.spinnerreceiveadresses);
        String address = spinerreceiveaddresses.getSelectedItem().toString();
        String[] components = address.split("  ");
        EditText amountedit = (EditText) findViewById(R.id.editTextReceiveAmount);
        String amount = amountedit.getText().toString();


        Intent intent = new Intent(this, SendActivity.class);
        intent.putExtra("address", components[1]);
        intent.putExtra("value", amount);
        startActivity(intent);
    }

    public void sendingAction(View view) {
        Spinner spinerfromaddresses = (Spinner) findViewById(R.id.spinnersendaddresses);
        String fromaddressfull = spinerfromaddresses.getSelectedItem().toString();
        String[] components = fromaddressfull.split("  ");
        BigDecimal available = new BigDecimal(components[0]);
        String fromaddress = components[1];

        EditText amountedit = (EditText) findViewById(R.id.editTextSendAmount);
        BigDecimal amount = new BigDecimal(amountedit.getText().toString());

        EditText feedit = (EditText)findViewById(R.id.editTextFee);
        BigDecimal fee = new BigDecimal(feedit.getText().toString());

        EditText toaddressedit = (EditText)findViewById(R.id.editTextToAdress);
        String toaddress = toaddressedit.getText().toString();

        EditText useredit = (EditText)findViewById(R.id.editTextUsername);
        String username = useredit.getText().toString();
        EditText passedit = (EditText)findViewById(R.id.editTextPassword);
        String password = passedit.getText().toString();

        if (amount.add(fee).compareTo(available) == 1) {
             AlertDialog.Builder dialog = new AlertDialog.Builder(this);

                 dialog.setTitle( "Error:" )
                    .setMessage("Not enough funds")
                     .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                        }).show();
        }

        else {
            Intent intent = new Intent(this, SendingActivity.class);
            intent.putExtra("fromaddress", fromaddress);
            intent.putExtra("value", amount.toString());
            intent.putExtra("fee", fee.toString());
            intent.putExtra("toaddress", toaddress);
            EditText onionedit = (EditText)findViewById(R.id.editTextOnion);
            String onion = onionedit.getText().toString();
            intent.putExtra("onion",onion);
            intent.putExtra("username", username);
            intent.putExtra("password", password);
            startActivity(intent);
        }
    }

    private void doRequestBalances (final String currentUrl, final JSONObject jsonrequest)
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
                                                                        adapterBalances.clear();
                                                                        JSONObject result = response.getJSONObject("result");
                                                                        String transparentbalance = result.getString("transparent");
                                                                        String shieldedbalance = result.getString("private");
                                                                        String totalbalance = result.getString("total");
                                                                        adapterBalances.add("Transparent: " + transparentbalance);
                                                                        adapterBalances.add("Shielded: "+ shieldedbalance);
                                                                        adapterBalances.add("Total:" + totalbalance);
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
                                                EditText useredit = (EditText)findViewById(R.id.editTextUsername);
                                                String username = useredit.getText().toString();
                                                EditText passedit = (EditText)findViewById(R.id.editTextPassword);
                                                String password = passedit.getText().toString();
                                                String creds = String.format("%s:%s",username,password);
                                                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                                                params.put("Authorization", auth);
                                                return params;
                                            }
                                            };
                                    RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
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


    private void doRequestBalancesShielded (final String currentUrl, final JSONObject jsonrequest)
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
                                                                        int length = result.length();

                                                                        for (int i=0; i<length; i++)
                                                                        {
                                                                            String address = result.getString(i);
                                                                            try {



                                                                                EditText onionedit = (EditText)findViewById(R.id.editTextOnion);
                                                                                String onion = onionedit.getText().toString();

                                                                                JSONObject req = new JSONObject();
                                                                                req.put("version", "2");
                                                                                req.put("method","z_getbalance");
                                                                                req.put("id",0);
                                                                                JSONArray params = new JSONArray();

                                                                                params.put(address);

                                                                                req.put("params",params);

                                                                                doRequestBalanceOfShieldedAddress("http://"+onion, req, address);
                                                                            }
                                                                            catch (Exception e) {}

                                                                        }
//                                                                        String addresses = result.getString(0);
//
//                                                                        adapterShieldedBalances.clear();
//                                                                        adapterShieldedBalances.add(addresses);
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
                                                    EditText useredit = (EditText)findViewById(R.id.editTextUsername);
                                                    String username = useredit.getText().toString();
                                                    EditText passedit = (EditText)findViewById(R.id.editTextPassword);
                                                    String password = passedit.getText().toString();
                                                    String creds = String.format("%s:%s",username,password);
                                                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                                                    params.put("Authorization", auth);
                                                    return params;
                                                }
                                            };
                                    RetryPolicy policy = new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
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


    private void doRequestBalanceOfShieldedAddress (final String currentUrl, final JSONObject jsonrequest, final String address)
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

                                                                        String result = response.getString ("result");
                                                                        if (adapterShieldedBalances.getCount() == 1 && adapterShieldedBalances.getItem(0).toString().matches("Getting addresses") ){
                                                                            adapterShieldedBalances.clear();
                                                                            adapterSendBalances.clear();
                                                                        }
                                                                        adapterShieldedBalances.add(result + "  " + address);
                                                                        if (!(result.matches("0.0"))) {
                                                                            adapterSendBalances.add(result + "  " + address);
                                                                        }
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
                                                    EditText useredit = (EditText)findViewById(R.id.editTextUsername);
                                                    String username = useredit.getText().toString();
                                                    EditText passedit = (EditText)findViewById(R.id.editTextPassword);
                                                    String password = passedit.getText().toString();
                                                    String creds = String.format("%s:%s",username,password);
                                                    String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                                                    params.put("Authorization", auth);
                                                    return params;
                                                }
                                            };
                                    RetryPolicy policy = new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
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



    @Override
    protected void onStop(){
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        EditText useredit = (EditText)findViewById(R.id.editTextUsername);
        String username = useredit.getText().toString();
        editor.putString("username", username);
        EditText onionedit = (EditText)findViewById(R.id.editTextOnion);
        String onion = onionedit.getText().toString();
        editor.putString("onion", onion);
        EditText passedit = (EditText)findViewById(R.id.editTextPassword);
        String password = passedit.getText().toString();
        editor.putString("password", password);

        // Commit the edits!
        editor.commit();
    }
}
