package aditya.cloudstuff.www.check;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
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
//        Sync sync = new Sync(call, 5000);
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
//                        volley_JSON_array("https://api.github.com/users/verma-ady/repos");
//                        volley_JSON_object("http://swiftintern.com/home.json");
                        UploadFile();
//                        alert();
//                        Toast.makeText(getActivity(), sid, Toast.LENGTH_SHORT ).show();
                    }
                })
        );
    }

    private static String encodeFileToBase64Binary(File fileName) throws IOException {
        byte[] bytes = new byte[(int)fileName.length()];
        FileInputStream fileInputStream = new FileInputStream(fileName);
        fileInputStream.read(bytes);
        fileInputStream.close();
//        byte[] encoded = Base64.encodeBase64(bytes, Base64.DEFAULT);
        String encode = Base64.encodeToString(bytes, Base64.DEFAULT);
//        String encodedString = new String(encoded);
        Log.v("MyApp", encode);
        return encode;
    }

    public void UploadFile(){
        Log.v("MyApp", "Uploadfile()");
        try {
            // Set your file path here
            Log.v("MyApp", "File Dir : " + Environment.getExternalStorageDirectory().toString()+"/Check.pdf" );
//            FileInputStream file = new FileInputStream(Environment.getExternalStorageDirectory().toString()+"/Check.pdf");
            File file = new File(Environment.getExternalStorageDirectory().toString()+"/Check.pdf");
            // Set your server page url (and the file title/description)

            String encode = encodeFileToBase64Binary(file);

            HTTPFileUpload hfu = new HTTPFileUpload("http://www.swiftintern.com/app/upload", "check","check", encode);

            hfu.Send_Now();

        } catch (FileNotFoundException e ) {
            Log.v("MyApp", "File Not Found");
            // Error: File not found
        } catch (IOException e ){
            Log.v("MyApp", "IOException");
        }
    }

    public void volley_JSON_array(String url) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.v("MyApp", "response : " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("MyApp", "error : " + error.toString());
            }
        });

        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);
    }

    public void volley_JSON_object(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.v("MyApp", "response : " + response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.v("MyApp", "error : " + error.toString());
            }
        });
        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
    }

    public void alert(){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity())
                .setMessage("Write your message here." + '\n' + "Check it" )
                .setCancelable(true).setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        }).setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        AlertDialog alertDialog = builder1.show();
    }

//    final private Runnable call = new Runnable() {
//        @Override
//        public void run() {
//            Long time = System.currentTimeMillis();
//            Log.v("Check", "runnable  " + time.toString());
////            Toast.makeText(getActivity(), "Check for every 5 Secs", Toast.LENGTH_SHORT).show();
//            handler.postDelayed(call, 5000);
//        }
//    };

//    public final Handler handler = new Handler();
//    public class Sync{
//        Runnable task;
//        public Sync ( Runnable runnable, long time ){
//            this.task = runnable;
//            handler.removeCallbacks(task);
//            handler.postDelayed(runnable, time);
//        }
//    }

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

    public void alert(String enddate, String oppurtunity, String organisation){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity())
                .setMessage("Organisation : " + organisation + '\n' +
                        "Oppurtunities are : " + oppurtunity + '\n' + "Last Date : " + enddate  )
                .setCancelable(true).setPositiveButton("Apply",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                //code to take user to apply page
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                //nothing to do here
                            }
                        });
        AlertDialog alertDialog = builder1.show();
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

    String urlParameterskey="email";
    String urlParametersValue = "mkv241064@gmail.com";





}
