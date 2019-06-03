package groep3.hr.nl.techlabhr;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
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
 * {@link Mijn_leningen.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Mijn_leningen#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Mijn_leningen extends Fragment {

    private static final String orderURL = "https://eduardterlouw.com/techlab/get_all_orders_by_user.php";

    private String TAG = NavDrawer.class.getSimpleName();
    private static final String TAG_PID = "ProductID";
    private static final String TAG_NAME = "ProductName";
    private static final String TAG_AMOUNT = "ProductAmount";
    private static final String TAG_RETURN = "DateOfReturn";
    private static final String TAG_ORDERID = "OrderID";
    private static final String TAG_STATUS = "Status";
    private static final String TAG_EMAIL = "Email";

    private ListView listPending;
    private ListView listReady;
    private ListView listInProgress;
    private ListView listDenied;
    private ListView listCompleted;

    private ProgressDialog pDialog;
    private ArrayList<HashMap<String,String>> pendingList;
    private ArrayList<HashMap<String,String>> readyList;
    private ArrayList<HashMap<String,String>> inProgressList;
    private ArrayList<HashMap<String,String>> deniedList;
    private ArrayList<HashMap<String,String>> completedList;

    private OnFragmentInteractionListener mListener;

    public Mijn_leningen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Mijn_leningen.
     */
    // TODO: Rename and change types and number of parameters
    public static Mijn_leningen newInstance(String param1, String param2) {
        Mijn_leningen fragment = new Mijn_leningen();
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
        Toolbar toolbar= (Toolbar) getActivity().findViewById(R.id.toolbar);
        NavigationView nav = (NavigationView) getActivity().findViewById(R.id.nav_view);
        MenuItem menuItem = (MenuItem) nav.getMenu().findItem(R.id.nav_leningen);
        menuItem.setChecked(true);
        toolbar.setTitle("Mijn Leningen");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mijn_leningen, container, false);
        //Initialize progressDialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        listPending = view.findViewById(R.id.listPending);
        listReady = view.findViewById(R.id.listReady);
        listInProgress = view.findViewById(R.id.listInProgress);
        listDenied = view.findViewById(R.id.listDenied);
        listCompleted = view.findViewById(R.id.listCompleted);
        getAllOrders();

        return view;
    }

    private void getAllOrders(){
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                orderURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                String OrderIDPrev = "";
                //Initializing lists
                pendingList = new ArrayList<HashMap<String, String>>();
                readyList = new ArrayList<HashMap<String, String>>();
                inProgressList = new ArrayList<HashMap<String, String>>();
                deniedList = new ArrayList<HashMap<String, String>>();
                completedList = new ArrayList<HashMap<String, String>>();
                hidepDialog();
                try {
                    //Parsing JSON response
                    JSONObject responseObj = new JSONObject(response);
                    JSONArray Orders = (JSONArray) responseObj.get("Orders");
                    for(int i = 0;i < Orders.length();i++){
                        JSONObject OrderItem = (JSONObject) Orders.get(i);
                        if(!(OrderItem.getString("OrderID").equals(OrderIDPrev))) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put(TAG_ORDERID, OrderItem.getString("OrderID"));
                            map.put(TAG_PID, OrderItem.getString("ProductID"));
                            map.put(TAG_AMOUNT, OrderItem.getString("ProductAmount"));
                            map.put(TAG_RETURN, OrderItem.getString("DateOfReturn"));
                            map.put(TAG_STATUS, OrderItem.getString("Status"));
                            switch(OrderItem.getString("Status")){
                                case "pending":
                                    pendingList.add(map);
                                    break;
                                case "readyForPickup":
                                    readyList.add(map);
                                    break;
                                case "InProgress":
                                    inProgressList.add(map);
                                    break;
                                case "Denied":
                                    deniedList.add(map);
                                    break;
                                case "Completed":
                                    completedList.add(map);
                                    break;
                            }
                            OrderIDPrev = OrderItem.getString("OrderID");
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        /**
                         * Updating parsed JSON data into ListView
                         * */
                        ListAdapter pendingA = new SimpleAdapter(
                                getActivity(), pendingList,
                                R.layout.mijn_leningen_list_item, new String[] {TAG_ORDERID,TAG_RETURN},
                                new int[] { R.id.detailID,R.id.detailDate});
                        ListAdapter readyA = new SimpleAdapter(
                                getActivity(), readyList,
                                R.layout.mijn_leningen_list_item, new String[] {TAG_ORDERID,TAG_RETURN},
                                new int[] { R.id.detailID,R.id.detailDate});
                        ListAdapter inProgressA = new SimpleAdapter(
                                getActivity(), inProgressList,
                                R.layout.mijn_leningen_list_item, new String[] {TAG_ORDERID,TAG_RETURN},
                                new int[] { R.id.detailID,R.id.detailDate});
                        ListAdapter deniedA = new SimpleAdapter(
                                getActivity(), deniedList,
                                R.layout.mijn_leningen_list_item, new String[] {TAG_ORDERID,TAG_RETURN},
                                new int[] { R.id.detailID,R.id.detailDate});
                        ListAdapter completedA = new SimpleAdapter(
                                getActivity(), completedList,
                                R.layout.mijn_leningen_list_item, new String[] {TAG_ORDERID,TAG_RETURN},
                                new int[] { R.id.detailID,R.id.detailDate});

                        listPending.setAdapter(pendingA);
                        listReady.setAdapter(readyA);
                        listInProgress.setAdapter(inProgressA);
                        listDenied.setAdapter(deniedA);
                        listCompleted.setAdapter(completedA);
                    }
                });

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            //Adding currently logged in email to post parameters
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put(TAG_EMAIL, getActivity().getIntent().getStringExtra("email"));
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
