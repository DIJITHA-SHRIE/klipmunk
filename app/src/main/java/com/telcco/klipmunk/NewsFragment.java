package com.telcco.klipmunk;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewsFragment extends android.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    @BindView(R.id.take_shots)
    Button take_shots_btn;
    @BindView(R.id.etSearch)
    EditText etSearch;
    private int PICK_IMAGE_REQUEST = 1;
    String imagepath;
    DataBaseHelper db;
    ArrayList<ScreensModel> getScreens = new ArrayList<>();

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    String getNotes;
    NewsAdapter adapter;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            Log.e("uri",""+selectedImageUri);
            if (selectedImageUri.getHost().contains("com.android.providers.media")) {
                @SuppressLint({"NewApi", "LocalSuppress"}) String wholeID = DocumentsContract.getDocumentId(selectedImageUri);
                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];

                String[] column = {MediaStore.Images.Media.DATA};

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                try
                {
                    Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
                    int columnIndex = cursor.getColumnIndex(column[0]);

                    if (cursor.moveToFirst()) {
                        imagepath = cursor.getString(columnIndex);
                        showNotesDialog();
                        Log.e("imagepath1",imagepath);

                    }
                    cursor.close();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }


            }else
            {
                imagepath =getPath(selectedImageUri);
                showNotesDialog();
                Log.e("imagepath2",imagepath);
            }







        }
        else if (resultCode == RESULT_CANCELED) {

            // user cancelled Image capture
            Toast.makeText(getActivity(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();

        } else {
            // failed to capture image
            Toast.makeText(getActivity(),
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.

     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1) {
        NewsFragment fragment = new NewsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this,view);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) getActivity().findViewById(R.id.toolbar);
        TextView title = (TextView) toolbar.findViewById(R.id.title_toolbar);
        title.setText(mParam1);
        db = new DataBaseHelper(getActivity());
        getScreens= db.getScreenShotsPath(mParam1);
        if(getScreens.size()>0){
            Log.i("getScreens",getScreens+"");
            adapter = new NewsAdapter(getActivity(),getScreens);
            GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (getScreens.size()>0) {
                    filter(editable.toString());
                }

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

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

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
    @OnClick(R.id.take_shots)
    public void onShots(){
        GetImage();
    }
    private void GetImage() {
        List<String> getImageType = new ArrayList<>();

        getImageType.add("Gallery");
        final CharSequence[] ImageTypes = getImageType.toArray(new String[getImageType.size()]);
        android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        dialogBuilder.setItems(ImageTypes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                String selectedText = ImageTypes[item].toString();

                if(selectedText.equals("Gallery"))
                {

                    showFileChooser();
                }



            }
        });
        android.support.v7.app.AlertDialog alertDialogObject = dialogBuilder.create();
        alertDialogObject.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    public String getPath(Uri uri) {
        Cursor cursor = null;
        try {

            if ("content".equals(uri.getScheme())) {
                String[] projection = {MediaStore.Images.Media.DATA};
                cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            } else {
                return uri.getPath();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    private void showNotesDialog() {
        LayoutInflater li = LayoutInflater.from(getActivity());
        View view = li.inflate(R.layout.tag_dialog,null);
        TextView addnote =(TextView)view.findViewById(R.id.title_dialog);
        addnote.setText("Add Notes");
        AlertDialog.Builder alertdialogBuilder = new AlertDialog.Builder(getActivity());
        alertdialogBuilder.setView(view);
        final EditText tag_edit = (EditText)view.findViewById(R.id.tag_edit);
        tag_edit.setHint("Enter Notes ");
        alertdialogBuilder.setCancelable(false);
        alertdialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                if(tag_edit.getText().toString().length()>0)
                {
                   getNotes=tag_edit.getText().toString();
                    db.insertPath(imagepath,mParam1,getNotes);
                    getScreens= db.getScreenShotsPath(mParam1);
                    if(getScreens.size()>0){
                        Log.i("getScreens",getScreens+"");
                        adapter = new NewsAdapter(getActivity(),getScreens);
                        GridLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(),2);
                        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }

                }
                else{
                    tag_edit.setError("Enter Notes");
                }



            }
        });
       /* alertdialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
              dialog.dismiss();
            }
        });*/
        AlertDialog alertDialog = alertdialogBuilder.create();
        alertDialog.show();
    }

    void filter(String text) {
        Log.i("textFilter",text);
        ArrayList<ScreensModel> temp = new ArrayList();
        for (ScreensModel d : getScreens ) {
            if(d.getNotes()!=null){
            if (d.getNotes().toLowerCase().contains(text.toLowerCase()) ) {
                temp.add(d);
            }
            else{

            }
            }
        }
        Log.i("TEMP",temp.size()+"");
        if(temp.size()>0){
        adapter.updateList(temp);}
    }

}
