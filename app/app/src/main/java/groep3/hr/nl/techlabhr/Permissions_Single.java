package groep3.hr.nl.techlabhr;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Permissions_Single extends Fragment {

    private String selectURL = "http://eduardterlouw.com/techlab/select_from_users.php";
    private String updateURL = "http://eduardterlouw.com/techlab/update_user.php";
    private String TAG = NavDrawer.class.getSimpleName();
    private String TAG_EMAIL = "Email";
    private String TAG_PERMISSION = "Permission";


    private ProgressDialog pDialog;
    private TextView inputEmail;
    private Spinner spinner_permission;
    private ArrayAdapter<String> adapter;


    private Button btnSave;

    public Permissions_Single() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment Placeholder.
     */
    // TODO: Rename and change types and number of parameters
    public static Permissions_Single newInstance() {
        Permissions_Single fragment = new Permissions_Single();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Set title and menu to appropriate fragment
        Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);
        NavigationView nav = (NavigationView) getActivity().findViewById(R.id.nav_view);
        MenuItem menuItem = (MenuItem) nav.getMenu().findItem(R.id.nav_add_beheerder);
        menuItem.setChecked(true);
        toolbar.setTitle("Permissies Wijzigen");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_permissions_single, container, false);

        inputEmail = (TextView) view.findViewById(R.id.inputEmail);

        spinner_permission = (Spinner) view.findViewById(R.id.spinner_permission);
        String[] categories = new String[]{"user","beheerder","admin"};
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item, categories);
        spinner_permission.setAdapter(adapter);
        spinner_permission.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSave = (Button) view.findViewById(R.id.btnSave);


        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                updateUser();
            }
        });

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        readSingleUser();
        return view;
    }

    private void readSingleUser() {
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                selectURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidepDialog();
                try {
                    JSONObject user = new JSONObject(response);
                    inputEmail.setText(user.getString(TAG_EMAIL));
                    spinner_permission.setSelection(adapter.getPosition(user.getString(TAG_PERMISSION)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(TAG_EMAIL, getArguments().getString(TAG_EMAIL));
                return params;
            }
        };
        // Adding request to request queue
        SingletonQueue.getInstance().addToRequestQueue(sr);
    }

    private void updateUser() {
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                updateURL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidepDialog();
                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,Permissions.newInstance()).addToBackStack(null);
                transaction.commit();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog
                hidepDialog();
            }
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Email",getArguments().getString(TAG_EMAIL));
                if (spinner_permission.getSelectedItem().toString().length() > 0){
                    params.put("Permission", spinner_permission.getSelectedItem().toString());}

                return params;
            }
        };

        // Adding request to request queue
        SingletonQueue.getInstance().addToRequestQueue(sr);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
