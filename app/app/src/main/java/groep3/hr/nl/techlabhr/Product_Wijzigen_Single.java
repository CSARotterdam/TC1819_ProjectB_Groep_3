package groep3.hr.nl.techlabhr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class Product_Wijzigen_Single extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters\

    private String selectURL = "https://eduardterlouw.com/techlab/select_from_products.php";
    private String updateURL = "https://eduardterlouw.com/techlab/update_product.php";
    private String deleteURL = "https://eduardterlouw.com/techlab/delete_product.php";
    private String TAG = NavDrawer.class.getSimpleName();
    private String TAG_PID = "ProductID";


    private ProgressDialog pDialog;
    private TextView inputID;
    private EditText inputManufacturer;
    private Spinner spinner_category;
    private ArrayAdapter<String> adapter;
    private EditText inputName;
    private EditText inputStock;
    private EditText inputBroken;

    private Button btnSave;
    private Button btnDelete;

    private Product_Wijzigen_Single.OnFragmentInteractionListener mListener;

    public Product_Wijzigen_Single() {
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
    public static Product_Wijzigen_Single newInstance() {
        Product_Wijzigen_Single fragment = new Product_Wijzigen_Single();
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
        MenuItem menuItem = (MenuItem) nav.getMenu().findItem(R.id.nav_change_stock);
        menuItem.setChecked(true);
        toolbar.setTitle("Product Wijzigen");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_wijzigen_single, container, false);

        inputID = (TextView) view.findViewById(R.id.inputID);
        inputManufacturer = (EditText) view.findViewById(R.id.inputManufacturer);
        inputName = (EditText) view.findViewById(R.id.inputName);
        inputStock = (EditText) view.findViewById(R.id.inputStock);
        inputBroken = (EditText) view.findViewById(R.id.inputBroken);

        spinner_category = (Spinner) view.findViewById(R.id.spinner_category);
        String[] categories = new String[]{getString(R.string.cat_cables),
                getString(R.string.cat_console),getString(R.string.cat_computer),
                getString(R.string.cat_drone),getString(R.string.cat_game),
                getString(R.string.cat_micro),getString(R.string.cat_rc),
                getString(R.string.cat_smart),getString(R.string.cat_virtual),
                getString(R.string.cat_internet)};
        adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item, categories);
        spinner_category.setAdapter(adapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnDelete = (Button) view.findViewById(R.id.btnDelete);

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                updateProduct();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                deleteProduct();
                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.fragment_container,Product_Wijzigen.newInstance()).addToBackStack(null);
                                transaction.commit();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                FragmentTransaction transaction2 = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction2.replace(R.id.fragment_container,Product_Wijzigen.newInstance()).addToBackStack(null);
                                transaction2.commit();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure? This action cannot be undone").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        readSingleProduct();
        return view;
    }

    /*
     * JsonObjectRequest takes in five paramaters
     * Request Type - This specifies the type of the request eg: GET,POST
     *
     * URL - This String param specifies the Request URL
     *
     * JSONObject - This parameter takes in the POST parameters."null" in
     * case of GET request.
     *
     * Listener -This parameter takes in a implementation of Response.Listener()
     * interface which is invoked if the request is successful
     *
     * Listener -This parameter takes in a implementation of Error.Listener()
     * interface which is invoked if any error is encountered while processing
     * the request
     */
    private void readSingleProduct() {
        StringRequest sr = new StringRequest(Request.Method.POST,
                selectURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidepDialog();
                try {
                    JSONObject product = new JSONObject(response);
                    inputID.setText(product.getString("ProductID"));
                    inputManufacturer.setText(product.getString("ProductManufacturer"));
                    inputName.setText(product.getString("ProductName"));
                    inputStock.setText(Integer.toString(product.getInt("ProductStock")));
                    inputBroken.setText(product.getString("ProductAmountBroken"));
                    spinner_category.setSelection(adapter.getPosition(product.getString("ProductCategory")));
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
                params.put(TAG_PID, getArguments().getString(TAG_PID));
                return params;
            }
        };
        // Adding request to request queue
        SingletonQueue.getInstance().addToRequestQueue(sr);


    }

    private void updateProduct() {
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                updateURL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidepDialog();
                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,Product_Wijzigen.newInstance()).addToBackStack(null);
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
                    params.put("ProductID",getArguments().getString(TAG_PID));
                if (inputManufacturer.getText().toString().length() > 0){
                    params.put("ProductManufacturer", inputManufacturer.getText().toString());}
                if (spinner_category.getSelectedItem().toString().length() > 0){
                    params.put("ProductCategory", spinner_category.getSelectedItem().toString());}
                if (inputName.getText().toString().length() > 0){
                    params.put("ProductName", inputName.getText().toString());}
                if (inputStock.getText().toString().length() > 0){
                    params.put("ProductStock", inputStock.getText().toString());}
                if (inputBroken.getText().toString().length() > 0){
                    params.put("ProductAmountBroken", inputBroken.getText().toString());}

                return params;
            }
        };

        // Adding request to request queue
        SingletonQueue.getInstance().addToRequestQueue(sr);
    }
    private void deleteProduct() {
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                deleteURL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidepDialog();
                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,Product_Wijzigen.newInstance()).addToBackStack(null);
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
                params.put("ProductID",getArguments().getString(TAG_PID));

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
