package groep3.hr.nl.techlabhr;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Inventaris.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Inventaris#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Inventaris extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // json object response url
    private String urlJsonObj = "http://10.0.2.2/get_all_products.php";


    private static final String TAG_SUCCESS = "Success";
    private static final String TAG_PRODUCTS = "Products";
    private static final String TAG_PID = "ProductID";
    private static final String TAG_MANUFACTURER = "ProductManufacturer";
    private static final String TAG_CATEGORY = "ProductCategory";
    private static final String TAG_NAME = "ProductName";
    private static final String TAG_STOCK = "ProductStock";
    private static final String TAG_BROKEN = "ProductAmountBroken";

    private static String TAG = NavDrawer.class.getSimpleName();


    // Progress dialog for loading
    private ProgressDialog pDialog;


    private ListView lv;
    ArrayList<HashMap<String,String>> productsList;
    // temporary string to show the parsed response


    private OnFragmentInteractionListener mListener;

    public Inventaris() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment Inventaris.
     */
    // TODO: Rename and change types and number of parameters
    public static Inventaris newInstance() {
        Inventaris fragment = new Inventaris();
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

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventaris, container, false);

        lv = (ListView) view.findViewById(R.id.listResponse);


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        makeJsonObjectRequest();


        return view;
    }
    public void makeJsonObjectRequest() {

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
                        for (int i = 0; i <= response.length(); i++) {

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
                            map.put(TAG_NAME,productName);
                            map.put(TAG_STOCK,"In stock: " + Integer.toString(productStock));
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
