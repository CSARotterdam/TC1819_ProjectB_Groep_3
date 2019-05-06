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
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Product_Wijzigen extends Fragment {


    private ListView lv;
    ArrayList<HashMap<String,String>> productsList;

    private Product_Wijzigen.OnFragmentInteractionListener mListener;
    private ProgressDialog pDialog;

    // json object response url
    private String urlJsonObj = "http://10.0.2.2/get_all_products.php";
    private String TAG = NavDrawer.class.getSimpleName();
    private static final String TAG_SUCCESS = "Success";
    private static final String TAG_PRODUCTS = "Products";
    private static final String TAG_PID = "ProductID";
    private static final String TAG_MANUFACTURER = "ProductManufacturer";
    private static final String TAG_CATEGORY = "ProductCategory";
    private static final String TAG_NAME = "ProductName";
    private static final String TAG_STOCK = "ProductStock";
    private static final String TAG_BROKEN = "ProductAmountBroken";

    public Product_Wijzigen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Product_Wijzigen.
     */
    // TODO: Rename and change types and number of parameters
    public static Product_Wijzigen newInstance() {
        Product_Wijzigen fragment = new Product_Wijzigen();
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
        //// Set title and menu to appropriate fragment
        Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);
        NavigationView nav = (NavigationView) getActivity().findViewById(R.id.nav_view);
        MenuItem menuItem = (MenuItem) nav.getMenu().findItem(R.id.nav_change_stock);
        menuItem.setChecked(true);
        toolbar.setTitle("Product Wijzigen");
        //Initialize pdialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product_wijzigen, container, false);
        // Load all products into list
        lv = (ListView) view.findViewById(R.id.listResponse);
        getAllProducts();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product_Wijzigen_Single fragment =(Product_Wijzigen_Single) Product_Wijzigen_Single.newInstance();
                Bundle product = new Bundle();
                product.putString(TAG_PID,((TextView) view.findViewById(R.id.pid)).getText().toString());
                fragment.setArguments(product);
                Log.d(TAG,product.toString());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment).addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }
    public void getAllProducts() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                productsList = new ArrayList<HashMap<String, String>>();
                try {
                    if(response.getInt("Success")==1) {
                        JSONArray Products =(JSONArray) response.get("Products");
                        for (int i = 0; i < Products.length(); i++) {

                            JSONObject product = (JSONObject) Products.get(i);
                            // Parsing json object response
                            // response will be a json object
                            String productID = product.getString("ProductID");
                            String productManufacturer = product.getString("ProductManufacturer");
                            String productCategory = product.getString("ProductCategory");
                            String productName = product.getString("ProductName");
                            int productStock = product.getInt("ProductStock");
                            int productAmountBroken = product.getInt("ProductAmountBroken");

                            HashMap<String,String> map = new HashMap<String,String>();
                            map.put(TAG_PID,productID);
                            map.put(TAG_MANUFACTURER,productManufacturer);
                            map.put(TAG_CATEGORY,productCategory);
                            map.put(TAG_NAME,productName);
                            map.put(TAG_STOCK,Integer.toString(productStock));
                            map.put(TAG_BROKEN,Integer.toString(productAmountBroken));
                            productsList.add(map);
                            Log.d(TAG,productsList.toString());


                        }
                        hidepDialog();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                /**
                                 * Updating parsed JSON data into ListView
                                 * */
                                ListAdapter adapter = new SimpleAdapter(
                                        getActivity(), productsList,
                                        R.layout.product_list_item, new String[] { TAG_PID,
                                        TAG_NAME,TAG_STOCK},
                                        new int[] { R.id.pid, R.id.product_name,R.id.product_stock });

                                lv.setAdapter(adapter);
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
                hidepDialog();
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
