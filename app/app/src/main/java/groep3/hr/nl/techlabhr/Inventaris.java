package groep3.hr.nl.techlabhr;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

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
import java.util.Map;


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
    private String urlJsonObj = "http://eduardterlouw.com/techlab/get_all_in_category.php";


    private static final String TAG_SUCCESS = "Success";
    private static final String TAG_PRODUCTS = "Products";
    private static final String TAG_PID = "ProductID";
    private static final String TAG_MANUFACTURER = "ProductManufacturer";
    private static final String TAG_CATEGORY = "ProductCategory";
    private static final String TAG_NAME = "ProductName";
    private static final String TAG_STOCK = "ProductStock";
    private static final String TAG_ICON ="";
    private static final String TAG_AMOUNT = "Amount";
    private static final String TAG_BROKEN = "ProductAmountBroken";

    private EditText inputAmount;
    private ImageButton btnCart;

    private static String TAG = NavDrawer.class.getSimpleName();


    // Progress dialog for loading
    private ProgressDialog pDialog;


    private ListView lv;
    ArrayList<Product> productsList;
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
        Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);
        NavigationView nav = (NavigationView) getActivity().findViewById(R.id.nav_view);
        MenuItem menuItem = (MenuItem) nav.getMenu().findItem(R.id.nav_inventaris);
        menuItem.setChecked(true);
        toolbar.setTitle("Inventaris");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inventaris, container, false);

        lv = (ListView) view.findViewById(R.id.listResponse);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product_Details fragment =(Product_Details) Product_Details.newInstance();
                Bundle product = new Bundle();
                product.putString(TAG_PID,((TextView) view.findViewById(R.id.pid)).getText().toString());
                fragment.setArguments(product);
                Log.d(TAG,product.toString());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment).addToBackStack(null);
                transaction.commit();
            }
        });


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        getAllProducts();


        return view;
    }
    public void getAllProducts() {

        showpDialog();

        StringRequest stringReq = new StringRequest(Request.Method.POST,
                urlJsonObj, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                productsList = new ArrayList<Product>();
                try {
                    JSONObject json = new JSONObject(response);
                    if(json.getInt("Success")==1) {
                        JSONArray Products =(JSONArray) json.get("Products");
                        for (int i = 0; i < Products.length(); i++) {

                            JSONObject product = (JSONObject) Products.get(i);
                            // Parsing json object response
                            // response will be a json object
                            String productID = product.getString("ProductID");
                            String productManufacturer = product.getString("ProductManufacturer");
                            String productCategory = product.getString("ProductCategory");
                            String productName = product.getString("ProductName");
                            String productStock = Integer.toString(product.getInt("ProductStock")
                                    - (product.getInt("ProductAmountBroken") + product.getInt("ProductAmountInProgress")));
                            int productAmountBroken = product.getInt("ProductAmountBroken");
                            String encodedImage = product.getString("ProductImage");

                            //Default icon
                            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.item_icon);
                            if (encodedImage.length() > 0){
                                byte[]decodedString = Base64.decode(encodedImage,Base64.DEFAULT);
                                icon = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                            }
                            Product productItem = new Product(productID,productName,productStock,icon);
                            productsList.add(productItem);
                            Log.d(TAG,productsList.toString());


                        }
                        hidepDialog();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                /**
                                 * Updating parsed JSON data into ListView
                                 * */
                                ListAdapter adapter = new ProductAdapter(getContext(),productsList);
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
        }){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(TAG_CATEGORY, getArguments().getString(TAG_CATEGORY));
                return params;
            }
        };

        // Adding request to request queue
        SingletonQueue.getInstance().addToRequestQueue(stringReq);
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
