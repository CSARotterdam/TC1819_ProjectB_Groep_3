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

public class Order_Set_Handed_In extends Fragment {


    private ListView lv;
    ArrayList<HashMap<String,String>> orderList;

    private Order_Set_Handed_In.OnFragmentInteractionListener mListener;
    private ProgressDialog pDialog;

    // json object response url
    private String urlJsonObj = "https://eduardterlouw.com/techlab/get_all_running_orders.php";
    private String TAG = NavDrawer.class.getSimpleName();

    private static final String TAG_ORDERID = "OrderID";
    private static final String TAG_EMAIL = "Email";
    private static final String TAG_AMOUNT_TOTAL = "Total amount of items";


    public Order_Set_Handed_In() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Order_Set_Handed_In.
     */
    // TODO: Rename and change types and number of parameters
    public static Order_Set_Handed_In newInstance() {
        Order_Set_Handed_In fragment = new Order_Set_Handed_In();
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
        toolbar.setTitle("All Running/Overdue Orders");
        //Initialize pDialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_handed_in, container, false);
        // Load all Orders into list
        lv = (ListView) view.findViewById(R.id.listResponse);
        getAllOrders();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order_Set_Handed_In_Single fragment =(Order_Set_Handed_In_Single) Order_Set_Handed_In_Single.newInstance();
                Bundle order = new Bundle();
                order.putString(TAG_ORDERID,((TextView) view.findViewById(R.id.OrderID)).getText().toString());
                order.putString(TAG_EMAIL,((TextView) view.findViewById(R.id.user_email)).getText().toString());
                fragment.setArguments(order);
                Log.d(TAG,order.toString());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment).addToBackStack(null);
                transaction.commit();
            }
        });


        return view;
    }
    public void getAllOrders() {
        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                orderList = new ArrayList<HashMap<String, String>>();
                try {
                    int OrderID = 0;
                    String Email = "";
                    if(response.getInt("Success")==1) {
                        JSONArray Orders = (JSONArray) response.get("Orders");
                        int OrderIDPrev = 0;
                        String EmailPrev = "";
                        int ProductAmountTotal = 0;

                        //Code to execute for each order item
                        for (int i = 0; i < Orders.length(); i++) {

                            JSONObject order = (JSONObject) Orders.get(i);
                            // Parsing json object response
                            // response will be a json object
                            OrderID = order.getInt("OrderID");
                            Email = order.getString("Email");
                            int ProductAmount = order.getInt("ProductAmount");

                            //Code that's executed when orderitem from new order is reached
                            if (OrderID != OrderIDPrev){
                                //Check if not first item
                                if (OrderIDPrev != 0){
                                    //Add previous item in response to orderlist, then overwrite control fields
                                    HashMap<String,String> map = new HashMap<String,String>();
                                    map.put(TAG_ORDERID,Integer.toString(OrderIDPrev));
                                    map.put(TAG_EMAIL,EmailPrev);
                                    map.put(TAG_AMOUNT_TOTAL,Integer.toString(ProductAmountTotal));
                                    orderList.add(map);

                                    OrderIDPrev = OrderID;
                                    EmailPrev = Email;
                                    ProductAmountTotal = ProductAmount;
                                }else{
                                    OrderIDPrev = OrderID;
                                    EmailPrev = Email;
                                    ProductAmountTotal = ProductAmount;
                                }
                            }else{
                                OrderIDPrev = OrderID;
                                EmailPrev = Email;
                                ProductAmountTotal += ProductAmount;
                            }
                        }
                        //Add final item in response to orderlist
                        //TODO: I am 99% sure there is a better way to do this, so find out how and implement that instead.
                        HashMap<String,String> map = new HashMap<String,String>();
                        map.put(TAG_ORDERID,Integer.toString(OrderID));
                        map.put(TAG_EMAIL,Email);
                        map.put(TAG_AMOUNT_TOTAL,Integer.toString(ProductAmountTotal));
                        orderList.add(map);

                        Log.d(TAG,orderList.toString());
                        hidepDialog();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                /**
                                 * Updating parsed JSON data into ListView
                                 * */
                                ListAdapter adapter = new SimpleAdapter(
                                        getActivity(), orderList,
                                        R.layout.order_list_item, new String[] { TAG_ORDERID,
                                        TAG_EMAIL,TAG_AMOUNT_TOTAL},
                                        new int[] { R.id.OrderID, R.id.user_email,R.id.product_amount_total });

                                lv.setAdapter(adapter);
                            }
                        });

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity().getApplicationContext(),
                            "JSONError: " + e.getMessage(),
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
