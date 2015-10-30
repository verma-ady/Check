package aditya.cloudstuff.www.check;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import aditya.cloudstuff.www.check.dummy.DummyContent;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Cards.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Cards#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Cards extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    RecyclerView recyclerView;
    DummyContent dummyContent = new DummyContent();
    Button button_select;
    EditText editText_title, editText_subtitle;
    RVadapter rVadapter;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Cards.
     */
    // TODO: Rename and change types and number of parameters
    public static Cards newInstance(String param1, String param2) {
        Cards fragment = new Cards();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Cards() {
        // Required empty public constructor
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
        startservice();
//        Sync sync = new Sync(call, 10*1000);
        View view = inflater.inflate(R.layout.fragment_cards, container, false);
        button_select = (Button) view.findViewById(R.id.card_button);
        editText_title = (EditText) view.findViewById(R.id.card_title);
        editText_subtitle = (EditText) view.findViewById(R.id.card_subtitle);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        dummyContent.addItem(new DummyContent.DummyItem("Delhi", "India"));
        dummyContent.addItem( new DummyContent.DummyItem("Washington", "USA"));
        dummyContent.addItem( new DummyContent.DummyItem("Dubai", "UAE"));
        dummyContent.addItem( new DummyContent.DummyItem("London", "UK"));
        dummyContent.addItem( new DummyContent.DummyItem("Berlin", "Germany"));

        rVadapter = new RVadapter(dummyContent.ITEMS);
        recyclerView.setAdapter(rVadapter);
        buttonlistener();
        cardlistener();
        return view;
    }

    public void cardlistener(){
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        String sid = dummyContent.ITEMS.get(position).id;
                        String sname = dummyContent.ITEMS.get(position).content;
                        notification(sid, sname);
                        Toast.makeText(getActivity(), sid, Toast.LENGTH_SHORT ).show();
                    }
                })
        );
    }

    public void notification ( String s1, String s2 ){
        Intent intent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity(),0,intent,0);
        Notification notification = new Notification.Builder(getActivity())
                .setContentTitle(s1)
                .setContentText(s2)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent).getNotification();
        notification.flags = Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public void buttonlistener(){
        button_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t = editText_title.getText().toString();
                String st = editText_subtitle.getText().toString();
                if (t.length() != 0 && st.length() != 0) {
                    rVadapter = (RVadapter) recyclerView.getAdapter();
                    dummyContent.addItem(new DummyContent.DummyItem(t, st), 0);
                    recyclerView.setAdapter(rVadapter);
                    notification(st, t);
                    Toast.makeText(getActivity(), "Refreshing", Toast.LENGTH_SHORT).show();

                }
                stopservice();
            }
        });
    }

    public void startservice(){
        Intent intent = new Intent( getActivity(), MyService.class);
        getActivity().startService(intent);
    }

    public void stopservice(){
        Intent intent = new Intent( getActivity(), MyService.class);
        getActivity().stopService(intent);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
        }
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    public class RVadapter extends RecyclerView.Adapter<RVadapter.CardViewHolder> {
        DummyContent dummy = new DummyContent();


        public RVadapter( List<DummyContent.DummyItem> list_dummycontent ){
            dummy.ITEMS = list_dummycontent;
        }


        @Override
        public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview , parent, false );
            CardViewHolder cardViewHolder = new CardViewHolder(view);
            return cardViewHolder;
        }

        @Override
        public void onBindViewHolder(CardViewHolder holder, int position) {
            holder.textView_title.setText(dummy.ITEMS.get(position).content);
            holder.textView_subtitle.setText(dummy.ITEMS.get(position).id);
        }

        @Override
        public int getItemCount() {
            return dummy.ITEMS.size();
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
        }

        public class CardViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;
            TextView textView_title, textView_subtitle;
            public CardViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.cards_view);
                textView_title = (TextView) itemView.findViewById(R.id.title);
                textView_subtitle = (TextView) itemView.findViewById(R.id.subtitle);
            }
        }
    }

}
