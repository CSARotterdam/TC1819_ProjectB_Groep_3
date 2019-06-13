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

public class Leerlingen extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // json object response url
    private String urlJsonObj = "http://eduardterlouw.com/techlab/get_all_email_and_perm.php";

    private static final String TAG_EMAIL = "Email";
    private static final String TAG_PERMISSION = "Permission";


    private static String TAG = NavDrawer.class.getSimpleName();


    // Progress dialog for loading
    private ProgressDialog pDialog;


    private ListView lv;
    ArrayList<HashMap<String,String>> emailList;
    // temporary string to show the parsed response


    private Inventaris.OnFragmentInteractionListener mListener;

    public Leerlingen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment Inventaris.
     */
    // TODO: Rename and change types and number of parameters
    public static Leerlingen newInstance() {
        Leerlingen fragment = new Leerlingen();
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
        MenuItem menuItem = (MenuItem) nav.getMenu().findItem(R.id.nav_leerlingen);
        menuItem.setChecked(true);
        toolbar.setTitle("Leerlingen");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leerlingen, container, false);

        lv = (ListView) view.findViewById(R.id.listResponse);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Leerlingen_Single fragment =(Leerlingen_Single) Leerlingen_Single.newInstance();
                Bundle user = new Bundle();
                user.putString(TAG_EMAIL,((TextView) view.findViewById(R.id.user_Email)).getText().toString());
                fragment.setArguments(user);
                Log.d(TAG,user.toString());
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container,fragment).addToBackStack(null);
                transaction.commit();
            }
        });


        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        getAllEmail();


        return view;
    }
    public void getAllEmail() {

        showpDialog();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                urlJsonObj, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());

                emailList = new ArrayList<HashMap<String, String>>();
                try {
                    if(response.getInt("Success")==1) {
                        JSONArray EmailPermlist =(JSONArray) response.get("Email");
                        for (int i = 0; i < EmailPermlist.length(); i++) {
                            JSONObject EmailPerm = (JSONObject) EmailPermlist.get(i);
                            HashMap<String,String> map = new HashMap<String,String>();
                            map.put(TAG_EMAIL,(String) EmailPerm.get(TAG_EMAIL));
                            map.put(TAG_PERMISSION,(String) EmailPerm.get(TAG_PERMISSION));

                            emailList.add(map);
                        }
                        Log.d(TAG, emailList.toString());
                        hidepDialog();
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                /**
                                 * Updating parsed JSON data into ListView
                                 * */
                                ListAdapter adapter = new SimpleAdapter(
                                        getActivity(), emailList,
                                        R.layout.user_list_item, new String[] {TAG_EMAIL,TAG_PERMISSION},
                                        new int[] { R.id.user_Email, R.id.user_Permission});

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