package groep3.hr.nl.techlabhr;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Product_Broken_Details.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Product_Broken_Details#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Product_Broken_Details extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String selectURL = "https://eduardterlouw.nl/techlab/select_from_products.php";
    private String updateURL = "https://eduardterlouw.nl/techlab/update_amount_broken.php";

    private String TAG = NavDrawer.class.getSimpleName();
    private static final String TAG_SUCCESS = "Success";
    private static final String TAG_PRODUCTS = "Products";
    private static final String TAG_PID = "ProductID";
    private static final String TAG_MANUFACTURER = "ProductManufacturer";
    private static final String TAG_CATEGORY = "ProductCategory";
    private static final String TAG_NAME = "ProductName";
    private static final String TAG_AMOUNT = "Amount";
    private static final String TAG_STOCK = "ProductStock";
    private static final String TAG_DATE = "StartDate";


    private ProgressDialog pDialog;
    private TextView detailID;
    private TextView detailManufacturer;
    private TextView detailCategory;
    private TextView detailName;
    private TextView detailStock;
    private TextView detailTotal;
    private EditText detailBroken;
    private EditText inputAmount;




    private Button btnBroken;

    private OnFragmentInteractionListener mListener;

    public Product_Broken_Details() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Product_Broken_Details.
     */
    // TODO: Rename and change types and number of parameters
    public static Product_Broken_Details newInstance() {
        Product_Broken_Details fragment = new Product_Broken_Details();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);
        NavigationView nav = (NavigationView) getActivity().findViewById(R.id.nav_view);
        MenuItem menuItem = (MenuItem) nav.getMenu().findItem(R.id.nav_broken);
        menuItem.setChecked(true);
        toolbar.setTitle("Beschadigingen");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product__broken__details, container, false);

        detailID = (TextView) view.findViewById(R.id.detailID);
        detailManufacturer = (TextView) view.findViewById(R.id.detailManufacturer);
        detailName = (TextView) view.findViewById(R.id.detailName);
        detailStock = (TextView) view.findViewById(R.id.detailStock);
        detailTotal = (TextView) view.findViewById(R.id.detailTotal);
        detailBroken = (EditText) view.findViewById(R.id.inputAmount);
        detailCategory = (TextView) view.findViewById(R.id.detailCategory);
        inputAmount = (EditText) view.findViewById(R.id.inputAmount);
        btnBroken = (Button) view.findViewById(R.id.btnBroken);
        btnBroken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterBroken();
            }
        });



        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        readSingleProduct();
        return view;
    }

    private void alterBroken(){
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                updateURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidepDialog();
                try {
                    JSONObject succes = new JSONObject(response);
                    if(succes.getInt("success") == 0){
                        Log.d(TAG,succes.getString("update"));
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Amount broken updated successfully",
                                Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Amount broken updated successfully",
                                Toast.LENGTH_LONG).show();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container,brokenFragment.newInstance());
                        transaction.commit();
                    }

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
                params.put(TAG_PID, detailID.getText().toString());
                if (Integer.parseInt(inputAmount.getText().toString()) > Integer.parseInt(detailTotal.getText().toString())) {
                    inputAmount.setText(detailTotal.getText().toString());
                }
                params.put("ProductAmountBroken", inputAmount.getText().toString());

                return params;
            }
        };
        // Adding request to request queue
        SingletonQueue.getInstance().addToRequestQueue(sr);


    }

    private void readSingleProduct() {
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                selectURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidepDialog();
                try {
                    JSONObject product = new JSONObject(response);
                    detailID.setText(product.getString("ProductID"));
                    detailManufacturer.setText(product.getString("ProductManufacturer"));
                    detailCategory.setText(product.getString("ProductCategory"));
                    detailName.setText(product.getString("ProductName"));
                    detailStock.setText(Integer.toString(product.getInt("ProductStock")
                            - (product.getInt("ProductAmountBroken") + product.getInt("ProductAmountInProgress"))));
                    detailTotal.setText(product.getString("ProductStock"));
                    detailBroken.setText(product.getString("ProductAmountBroken"));

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
