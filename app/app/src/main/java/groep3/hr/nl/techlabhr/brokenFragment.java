package groep3.hr.nl.techlabhr;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
 * {@link brokenFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link brokenFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class brokenFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG_PID = "ProductID";
    private static final String TAG_NAME = "ProductName";
    private static final String TAG_BROKEN = "ProductAmountBroken";

    private static String TAG = NavDrawer.class.getSimpleName();


    private ListView lv;
    private Button btnAddBroken;
    ArrayList<Product> productsList;

    private ProgressDialog pDialog;
    private String urlJsonObj = "https://eduardterlouw.nl/techlab/get_all_broken.php";

    private OnFragmentInteractionListener mListener;

    public brokenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment brokenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static brokenFragment newInstance() {
        brokenFragment fragment = new brokenFragment();
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

        View view = inflater.inflate(R.layout.fragment_broken, container, false);

        btnAddBroken = (Button) view.findViewById(R.id.buttonAddBroken);
        btnAddBroken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, Product_Broken_categories.newInstance()).addToBackStack(null);
                transaction.commit();
            }
        });

        lv = view.findViewById(R.id.listBrokenItems);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product_Broken_Details fragment = (Product_Broken_Details) Product_Broken_Details.newInstance();
                Bundle product = new Bundle();
                product.putString(TAG_PID,((TextView) view.findViewById(R.id.pid)).getText().toString());
                fragment.setArguments(product);
                Log.d(TAG,product.toString());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment).addToBackStack(null);
                transaction.commit();
            }
        });
        // Inflate the layout for this fragment

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        getAllBroken();

        return view;
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


    private void getAllBroken(){
        showpDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
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
//                             Parsing json object response
//                             response will be a json object
                            String productID = product.getString("ProductID");
//                            String productManufacturer = product.getString("ProductManufacturer");
//                            String productCategory = product.getString("ProductCategory");
                            String productName = product.getString("ProductName");
                            String productStock = Integer.toString(product.getInt("ProductStock")
                                    - (product.getInt("ProductAmountBroken") + product.getInt("ProductAmountInProgress")));
                            String productAmountBroken = Integer.toString(product.getInt("ProductAmountBroken"));
                            String encodedImage = product.getString("ProductImage");
                            Log.d(TAG, Integer.toString(encodedImage.length()));
                            //Default icon
                            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.item_icon);
                            if (encodedImage.length() > 0){
                                byte[]decodedString = Base64.decode(encodedImage,Base64.DEFAULT);
                                icon = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                            }
                            Product productItem = new Product(productID,productName,productStock,icon,productAmountBroken);
                            productsList.add(productItem);
//
                        }

                        hidepDialog();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                /**
                                 * Updating parsed JSON data into ListView
                                 * */
                                ListAdapter adapter = new ProductBrokenAdapter(getContext(),productsList);
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
//                params.put(TAG_CATEGORY, getArguments().getString(TAG_CATEGORY));
                return params;
            }
        };

        // Adding request to request queue
        SingletonQueue.getInstance().addToRequestQueue(stringRequest);
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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
