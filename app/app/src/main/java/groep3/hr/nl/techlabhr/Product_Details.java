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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Product_Details extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters\

    private String selectURL = "http://10.0.2.2/select_from_products.php";
    private String updateURL = "http://10.0.2.2/update_product.php";
    private String deleteURL = "http://10.0.2.2/delete_product.php";
    private String TAG = NavDrawer.class.getSimpleName();
    private String TAG_PID = "ProductID";


    private ProgressDialog pDialog;
    private TextView detailID;
    private TextView detailManufacturer;
    private TextView detailCategory;
    private TextView detailName;
    private TextView detailStock;
    private TextView detailBroken;

    private Button btnCart;

    private Product_Details.OnFragmentInteractionListener mListener;

    public Product_Details() {
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
    public static Product_Details newInstance() {
        Product_Details fragment = new Product_Details();
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
        MenuItem menuItem = (MenuItem) nav.getMenu().findItem(R.id.nav_inventaris);
        menuItem.setChecked(true);
        toolbar.setTitle("Inventaris");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);

        detailID = (TextView) view.findViewById(R.id.detailID);
        detailManufacturer = (TextView) view.findViewById(R.id.detailManufacturer);
        detailName = (TextView) view.findViewById(R.id.detailName);
        detailStock = (TextView) view.findViewById(R.id.detailStock);
        detailBroken = (TextView) view.findViewById(R.id.detailBroken);
        detailCategory = (TextView) view.findViewById(R.id.detailCategory);
        btnCart = (Button) view.findViewById(R.id.btnCart);


        btnCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // creating new product in background thread

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
                    detailID.setText(product.getString("ProductID"));
                    detailManufacturer.setText(product.getString("ProductManufacturer"));
                    detailCategory.setText(product.getString("ProductCategory"));
                    detailName.setText(product.getString("ProductName"));
                    detailStock.setText(product.getString("ProductStock"));
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
