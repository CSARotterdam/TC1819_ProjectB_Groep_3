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
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Winkelmandje.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Winkelmandje#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Winkelmandje extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String orderURL = "https://eduardterlouw.com/test/create_new_order.php";

    private static final String TAG = NavDrawer.class.getSimpleName();
    private static final String TAG_SUCCESS = "Success";
    private static final String TAG_PRODUCTS = "Products";
    private static final String TAG_PID = "ProductID";
    private static final String TAG_MANUFACTURER = "ProductManufacturer";
    private static final String TAG_CATEGORY = "ProductCategory";
    private static final String TAG_NAME = "ProductName";
    private static final String TAG_STOCK = "ProductStock";
    private static final String TAG_BROKEN = "ProductAmountBroken";
    private static final String TAG_AMOUNT = "Amount";

    private ListView lv;
    private Button btnOrder;
    private Button btnEmptyCart;
    private OnFragmentInteractionListener mListener;
    private ProgressDialog pDialog;

    public Winkelmandje() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Winkelmandje.
     */
    // TODO: Rename and change types and number of parameters
    public static Winkelmandje newInstance() {
        Winkelmandje fragment = new Winkelmandje();
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
        MenuItem menuItem = (MenuItem) nav.getMenu().findItem(R.id.nav_winkelmandje);
        menuItem.setChecked(true);
        toolbar.setTitle("Winkelmandje");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_winkelmandje, container, false);

        lv = (ListView) view.findViewById(R.id.listWinkelmandje);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                ListAdapter adapter = new SimpleAdapter(
                        getActivity(), ((NavDrawer) getActivity()).winkelmandje,
                        R.layout.winkelmandje_list_item, new String[]{TAG_PID,TAG_NAME,TAG_AMOUNT},
                        new int[]{R.id.pid, R.id.product_name, R.id.product_amount});


                lv.setAdapter(adapter);

            }
        });

        EditText et;
        for(int i = 0; i < ((NavDrawer) getActivity()).winkelmandje.size();i++){
            //TODO: add checks for editing amount of items from within winkelmandje
        }
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

        btnOrder = (Button) view.findViewById(R.id.btnOrder);

        btnOrder.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                createNewOrder();
            }

        });
        btnEmptyCart = (Button) view.findViewById(R.id.btnEmptyCart);

        btnEmptyCart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // Empty cart
                ((NavDrawer) getActivity()).winkelmandje.clear();
                //Refresh page
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, Winkelmandje.newInstance());
                transaction.commit();
            }

        });
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        return view;
    }

    private void createNewOrder() {
        showpDialog();
        StringRequest sr = new StringRequest(Request.Method.POST,
                orderURL,new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                hidepDialog();
                Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_SHORT).show();

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
                params.put("email",getActivity().getIntent().getStringExtra("email"));


                ArrayList<HashMap<String,String>> winkelmandje = ((NavDrawer) getActivity()).winkelmandje;
                for (int i = 0;i < winkelmandje.size();i++){
                    params.put("Product" + i, winkelmandje.get(i).get(TAG_PID));
                    params.put("ProductAmount" + i, winkelmandje.get(i).get(TAG_AMOUNT));
                    params.put("DateOfReturn" + i,"27-05-2019");


                }

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
