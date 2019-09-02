package awsm.awsmizng.u.alanguageapp.fragments;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.helper.MyReceiver;
import awsm.awsmizng.u.alanguageapp.helper.NotificationBuilder;
import awsm.awsmizng.u.alanguageapp.helper.UploadArticlesService;
import awsm.awsmizng.u.alanguageapp.statics.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class UploadArticlesFragment extends Fragment {
    Context context = null;
    public static String fileName, theme = null;
    @BindView(R.id.transitionContainer)
    LinearLayout transitionContainer;
    @BindView(R.id.btSelect)
    Button btSelect;
    @BindView(R.id.llUpload)
    LinearLayout llUpload;
    @BindView(R.id.ivBottomCardView)
    ImageView ivBottomCardView;
    @BindView(R.id.btCancel)
    Button btCancel;
    //    @BindView(R.id.llProgress)
//    LinearLayout llProgress;
    private OnFragmentInteractionListener mListener;
    private Uri fileData = null;
    static String uploads;
    private int progress = 0;

    public MyReceiver myReceiver;

    @BindView(R.id.etFileName)
    TextInputEditText etFileName;
    @BindView(R.id.etContainer)
    TextInputLayout etContainer;
    @BindView(R.id.spinner)
    Spinner spinner;
    @BindView(R.id.btUpload)
    Button btUpload;
    @BindView(R.id.UploadProgress)
    ProgressBar UploadProgress;
    @BindView(R.id.tvUploadStatus)
    TextView tvUploadStatus;
    Unbinder unbinder;

    public UploadArticlesFragment() {
        // Required empty public constructor
    }

    ViewGroup transitionsContainer, uploadTransitionsContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_upload_articles, container, false);
        unbinder = ButterKnife.bind(this, view);

        readyUIforInput();
        context = getContext();

        transitionsContainer = (ViewGroup) view.findViewById(R.id.transitionContainer);
        uploadTransitionsContainer = (ViewGroup) view.findViewById(R.id.llUpload);

        etFileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!etFileName.getText().toString().isEmpty()) {
                    btSelect.setEnabled(true);
                    if (fileData != null) {
                        TransitionManager.beginDelayedTransition(uploadTransitionsContainer);
                        llUpload.setVisibility(View.VISIBLE);
                    } else {
                        TransitionManager.beginDelayedTransition(uploadTransitionsContainer);
                        llUpload.setVisibility(View.INVISIBLE);
                    }
                } else {
//                    btSelect.setEnabled(false);
                    TransitionManager.beginDelayedTransition(uploadTransitionsContainer);
                    llUpload.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //TODO get items from here
                // Toast.makeText(getContext(), parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();

                theme = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                theme = parent.getItemAtPosition(0).toString();
            }
        });

        setupServiceReceiver();

        if(Constants.theme == Constants.DARK_THEME){
            ivBottomCardView.setImageResource(R.drawable.dark_upload_bottom_card_view);
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void getPDFS() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};
//        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), Constants.PICK_PDF_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
//                uploadFile(data.getData());

                fileData = data.getData();
                if (!etFileName.getText().toString().isEmpty()) {
                    TransitionManager.beginDelayedTransition(uploadTransitionsContainer);
                    llUpload.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(getContext(), "Enter Article Title", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(Uri data) {

     /* TransitionManager.beginDelayedTransition(transitionsContainer);
        UploadProgress.setVisibility(View.VISIBLE);
        etContainer.setVisibility(View.GONE);
        etFileName.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        btUpload.setVisibility(View.GONE); */

        fileName = etFileName.getText().toString();
        Intent uploadIntent = new Intent(getContext(), UploadArticlesService.class);
        uploadIntent.setData(data)
                .putExtra("fileName", fileName)
                .putExtra("theme", theme)
                .putExtra("receiver", myReceiver)
                .putExtra("extension", getMimeType(getContext(), data))
        ;

        getActivity().startService(uploadIntent);
        readyUIforInput();
    }

    public static String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    public void setupServiceReceiver() {
        myReceiver = new MyReceiver(new Handler());
        myReceiver.setReceiver(new MyReceiver.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                String resultValue = resultData.getString("resultValue");
                String uploadProgress = resultData.getString("uploadProgress");

                if (uploadProgress.equals("uploading")) {
                    progress = resultData.getInt("progress");
                    NotificationBuilder.uploadArticleNotificationProgress(context, fileName, resultValue, progress);
//                    setProgressBarWidth(progress);
                } else {
                    NotificationBuilder.uploadArticleNotification(context, fileName, resultValue);
                }

                if (tvUploadStatus != null) {
                    tvUploadStatus.setText(resultValue);
                }

            }
        });
    }

//    private void setProgressBarWidth(int progress) {
//        LinearLayout.LayoutParams parms = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
//        transitionContainer.setLayoutParams(parms);
//    }

    private void readyUIforInput() {
        UploadProgress.setVisibility(View.GONE);
        etFileName.setText(null);
        etContainer.setVisibility(View.VISIBLE);
        etFileName.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
//        btSelect.setEnabled(false);
        btSelect.setVisibility(View.VISIBLE);
        fileData = null;
        llUpload.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @OnClick({R.id.btSelect, R.id.btUpload, R.id.btCancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btSelect:
                fileName = etFileName.getText().toString();
                getPDFS();
                break;
            case R.id.btUpload:
                uploadFile(fileData);
                break;
            case R.id.btCancel:
                readyUIforInput();
                break;
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }
}
