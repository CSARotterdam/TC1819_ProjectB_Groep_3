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
import android.widget.Button;
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
 * {@link Leerlingen_Single.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Leerlingen_Single#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Leerlingen_Single extends Fragment {

    private static final String orderURL = "https://eduardterlouw.com/techlab/get_all_orders_by_user.php";

    private String TAG = NavDrawer.class.getSimpleName();
    private static final String TAG_PID = "ProductID";
    private static final String TAG_NAME = "ProductName";
    private static final String TAG_AMOUNT = "ProductAmount";
    private static final String TAG_RETURN = "DateOfReturn";
    private static final String TAG_ORDERID = "OrderID";
    private static final String TAG_STATUS = "Status";
    private static final String TAG_EMAIL = "Email";

    private ListView listOrders;
    private ArrayList<HashMap<String,String>> OrderList;
    private ProgressDialog pDialog;

    private Button mijnLeningenBtn;

    private OnFragmentInteractionListener mListener;

    public Leerlingen_Single() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Leerlingen_Single.
     */
    // TODO: Rename and change types and number of parameters
    public static Leerlingen_Single newInstance() {
        Leerlingen_Single fragment = new Leerlingen_Single();
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
        for(int i = 0;i<nav.getMenu().size();i++) {
            MenuItem menuItem = (MenuItem) nav.getMenu().getItem(i);
            menuItem.setChecked(false);
        }
        toolbar.setTitle("Mijn Profiel");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leerlingen_single, container, false);
        //Initialize progressDialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        //Initialize Order list
        listOrders = view.findViewById(R.id.listResponse);
        getAllOrders();

        mijnLeningenBtn = view.findViewById(R.id.LeningenBtn);
        mijnLeningenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, Uitgeleend.newInstance()).addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    private void getAllOrders(){
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                orderURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidepDialog();
                OrderList = new ArrayList<HashMap<String, String>>();
                try {
                    //Parsing JSON response
                    JSONObject responseObj = new JSONObject(response);
                    JSONArray Orders = (JSONArray) responseObj.get("Orders");
                    for(int i = 0;i < Orders.length();i++){
                        JSONObject OrderItem = (JSONObject) Orders.get(i);
                        HashMap<String, String> map = new HashMap<>();
                        map.put(TAG_ORDERID, OrderItem.getString("OrderID"));
                        map.put(TAG_RETURN, OrderItem.getString("DateOfReturn"));
                        OrderList.add(map);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        ListAdapter adapter = new SimpleAdapter(
                                getActivity(), OrderList,
                                R.layout.mijn_leningen_list_item, new String[] {TAG_ORDERID,TAG_RETURN},
                                new int[] { R.id.detailID,R.id.detailDate});
                        listOrders.setAdapter(adapter);
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
                params.put(TAG_EMAIL, getArguments().getString(TAG_EMAIL));
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
