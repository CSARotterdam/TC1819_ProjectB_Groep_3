package groep3.hr.nl.techlabhr;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private final String TAG = RegisterActivity.class.getSimpleName();
    private final String urlAllEmail = "http://eduardterlouw.com/techlab/get_all_email.php";
    private final String urlNewUser = "http://eduardterlouw.com/techlab/create_new_user.php";
    //    private UserRegisterTask URTask;
    //UI reference
    private Button registerButton;
    private AutoCompleteTextView emailView;
    private EditText passwordView1;
    private EditText passwordView2;
    //    private Spinner spinner_permission;
    private String chosen_permission;

    DatabaseHelper mDatabaseHelper;
    ArrayList<String> emailList;
    ProgressDialog pDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        getAllEmail();

        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerAttempt();
            }
        });
        emailView = (AutoCompleteTextView) findViewById(R.id.email);
        passwordView1 = (EditText) findViewById(R.id.password1);
        passwordView2 = (EditText) findViewById(R.id.password2);

//        spinner_permission = (Spinner) findViewById(R.id.spinner_permission);
//        String[] permissions = new String[]{"user", "beheerder", "admin"};
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, permissions);
//        spinner_permission.setAdapter(adapter);
//        spinner_permission.setOnItemSelectedListener(this);

        mDatabaseHelper = new DatabaseHelper(this);

    }

    private void registerAttempt() {
        String password1 = passwordView1.getText().toString();
        String password2 = passwordView2.getText().toString();
        String email = emailView.getText().toString();

        Boolean permission = true;

        if (!isPasswordValid(password1)) {
            permission = false;
            passwordView1.setError(getString(R.string.error_invalid_password));
            passwordView2.setError(getString(R.string.error_invalid_password));
        }

        if (!password1.equals(password2)) {
            permission = false;
            passwordView2.setError(getString(R.string.error_password_match));
        }
        if (!isEmailValid(email)) {
            permission = false;
            emailView.setError(getString(R.string.error_invalid_email));
        }

        if (emailList.contains(email)) {
            permission = false;
            emailView.setError(getString(R.string.error_email_already_exists));
        }

        if (permission) {
//          URTask = new UserRegisterTask(email,password1);
//          URTask.execute((Void) null);
//            Contacts newUser = new Contacts();
//            newUser.setEmail(email);
//            newUser.setPassword(password1);
//            newUser.setPermission(chosen_permission);
//            mDatabaseHelper.insertContacts(newUser);
            registerNewUser();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);

        }
    }

    private void registerNewUser() {
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                urlNewUser, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidepDialog();
                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                if (emailView.getText().toString().length() > 0) {
                    params.put("Email", emailView.getText().toString());
                }
                if (passwordView1.getText().toString().length() > 0) {
                    params.put("Password", passwordView1.getText().toString());
                }
                params.put("Permission", "user");

                return params;
            }
        };

        // Adding request to request queue
        SingletonQueue.getInstance().addToRequestQueue(sr);
    }

    public void getAllEmail() {

        showpDialog();
        emailList = new ArrayList<String>();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlAllEmail, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    if (response.getInt("Success") == 1) {
                        JSONArray Emails = (JSONArray) response.get("Email");
                        for (int i = 0; i < Emails.length(); i++) {

                            emailList.add((String) Emails.get(i));
                        }
                        Log.d(TAG, emailList.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        });

        // Adding request to request queue
        SingletonQueue.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private boolean isPasswordValid(String password) {
        if (password.length() > 4) {

            return true;
        }
        return false;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }
}
