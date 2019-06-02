package groep3.hr.nl.techlabhr;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Uitgeleend extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Button btnSetOrderReady;
    Button btnSetOrderPickedUp;
    Button btnSetOrderHandedIn;

    // TODO: Rename and change types of parameters


    private Placeholder.OnFragmentInteractionListener mListener;

    public Uitgeleend() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *

     * @return A new instance of fragment Placeholder.
     */
    // TODO: Rename and change types and number of parameters
    public static Uitgeleend newInstance() {
        Uitgeleend fragment = new Uitgeleend();
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
        MenuItem menuItem = (MenuItem) nav.getMenu().findItem(R.id.nav_change_stock);
        menuItem.setChecked(true);
        toolbar.setTitle("Manage Orders");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_uitgeleend, container, false);

        // Handle button clicks
        btnSetOrderReady = (Button) view.findViewById(R.id.btnSetOrderReady);
        btnSetOrderReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order_Set_Ready fragment = Order_Set_Ready.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
                transaction.commit();
            }
        });

        btnSetOrderPickedUp = (Button) view.findViewById(R.id.btnSetOrderPickedUp);
        btnSetOrderPickedUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order_Set_Picked_Up fragment = Order_Set_Picked_Up.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
                transaction.commit();
            }
        });

        btnSetOrderHandedIn = (Button) view.findViewById(R.id.btnSetOrderHandedIn);
        btnSetOrderHandedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Order_Set_Handed_In fragment = Order_Set_Handed_In.newInstance();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, fragment).addToBackStack(null);
                transaction.commit();
            }
        });




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

