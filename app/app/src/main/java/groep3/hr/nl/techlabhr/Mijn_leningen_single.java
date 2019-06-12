package groep3.hr.nl.techlabhr;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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

public class Mijn_leningen_single extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters\

    private String selectURL = "http://eduardterlouw.com/techlab/select_from_orders.php";
    private String TAG = NavDrawer.class.getSimpleName();
    private static final String TAG_PID = "ProductID";
    private static final String TAG_NAME = "ProductName";
    private static final String TAG_AMOUNT = "ProductAmount";
    private static final String TAG_ORDERID = "OrderID";
    private static final String TAG_EMAIL = "Email";


    private ProgressDialog pDialog;
    ArrayList<HashMap<String,String>> orderList;

    private TextView OrderID;
    private ListView lv;

    private Mijn_leningen_single.OnFragmentInteractionListener mListener;

    public Mijn_leningen_single() {
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
    public static Mijn_leningen_single newInstance() {
        Mijn_leningen_single fragment = new Mijn_leningen_single();
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
        MenuItem menuItem = (MenuItem) nav.getMenu().findItem(R.id.nav_leningen);
        menuItem.setChecked(true);
        toolbar.setTitle("Mijn leningen");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mijn_leningen_single, container, false);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        OrderID = (TextView) view.findViewById(R.id.OrderID);
        OrderID.setText(getArguments().getString(TAG_ORDERID));

        lv = (ListView) view.findViewById(R.id.listResponse);
        readSingleOrder();


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
    private void readSingleOrder() {
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                selectURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                orderList = new ArrayList<HashMap<String, String>>();
                hidepDialog();
                try {
                    JSONObject responseObj = new JSONObject(response);
                    JSONArray Orders = (JSONArray) responseObj.get("Orders");
                    for(int i = 0;i < Orders.length();i++){
                        JSONObject OrderItem = (JSONObject) Orders.get(i);
                        HashMap<String,String> map = new HashMap<>();
                        map.put(TAG_PID,OrderItem.getString("ProductID"));
                        map.put(TAG_NAME,OrderItem.getString("ProductName"));
                        map.put(TAG_AMOUNT,OrderItem.getString("ProductAmount"));
                        orderList.add(map);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        /**
                         * Updating parsed JSON data into ListView
                         * */
                        ListAdapter adapter = new SimpleAdapter(
                                getActivity(), orderList,
                                R.layout.order_list_item_single, new String[] { TAG_PID,TAG_NAME,
                                TAG_AMOUNT},
                                new int[] { R.id.detailID,R.id.detailName, R.id.detailAmount});

                        lv.setAdapter(adapter);
                    }
                });

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
                params.put(TAG_ORDERID, getArguments().getString(TAG_ORDERID));
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
