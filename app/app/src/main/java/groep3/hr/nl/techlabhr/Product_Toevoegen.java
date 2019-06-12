package groep3.hr.nl.techlabhr;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Placeholder.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Placeholder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Product_Toevoegen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters\

    private String url = "http://eduardterlouw.com/techlab/create_new_product.php";
    private String TAG = NavDrawer.class.getSimpleName();


    private ProgressDialog pDialog;
    private EditText inputID;
    private EditText inputManufacturer;
    private Spinner spinner_category;
    private EditText inputName;
    private EditText inputStock;

    private Button btnCreateProduct;

    private OnFragmentInteractionListener mListener;

    public Product_Toevoegen() {
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
    public static Product_Toevoegen newInstance() {
        Product_Toevoegen fragment = new Product_Toevoegen();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
        toolbar.setTitle("Product Toevoegen");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_toevoegen, container, false);

        inputID = (EditText) view.findViewById(R.id.inputID);
        inputManufacturer = (EditText) view.findViewById(R.id.inputManufacturer);
        inputName = (EditText) view.findViewById(R.id.inputName);
        inputStock = (EditText) view.findViewById(R.id.inputStock);

        spinner_category = (Spinner) view.findViewById(R.id.spinner_category);
        String[] categories = new String[]{getString(R.string.cat_cables),
                getString(R.string.cat_console),getString(R.string.cat_computer),
                getString(R.string.cat_drone),getString(R.string.cat_game),
                getString(R.string.cat_micro),getString(R.string.cat_rc),
                getString(R.string.cat_smart),getString(R.string.cat_virtual),
                getString(R.string.cat_internet)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item, categories);
        spinner_category.setAdapter(adapter);
        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        btnCreateProduct = (Button) view.findViewById(R.id.btnCreateProduct);

        btnCreateProduct.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, Inventaris_aanpassen.newInstance()).addToBackStack(null);
                transaction.commit();
                hideKeyboard();
                createNewProduct();

            }
        });
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        return view;
    }
    private void hideKeyboard(){
        if(getActivity()!=null){
            InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null){
                manager.hideSoftInputFromWindow(getActivity().findViewById(android.R.id.content).getWindowToken(), 0);
            }
        }
    }
    private void createNewProduct() {
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                url,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidepDialog();
                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();

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
                if (inputID.getText().toString().length() > 0){
                params.put("ProductID", inputID.getText().toString());}
                if (inputManufacturer.getText().toString().length() > 0){
                params.put("ProductManufacturer", inputManufacturer.getText().toString());}
                if (spinner_category.getSelectedItem().toString().length() > 0){
                params.put("ProductCategory", spinner_category.getSelectedItem().toString());}
                if (inputName.getText().toString().length() > 0){
                params.put("ProductName", inputName.getText().toString());}
                if (inputStock.getText().toString().length() > 0){
                params.put("ProductStock", inputStock.getText().toString());}

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
