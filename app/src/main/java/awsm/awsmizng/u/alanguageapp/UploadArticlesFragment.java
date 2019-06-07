package awsm.awsmizng.u.alanguageapp;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import awsm.awsmizng.u.alanguageapp.models.Upload;
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

    StorageReference storageReference;
    DatabaseReference databaseReference;
    public static String fileName, theme = null;
    private OnFragmentInteractionListener mListener;

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

    ViewGroup transitionsContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upload_articles, container, false);
        unbinder = ButterKnife.bind(this, view);

        readyUIforInput();

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);

        transitionsContainer = (ViewGroup) view.findViewById(R.id.transitionContainer);
        etFileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!etFileName.getText().toString().isEmpty()) {
                    btUpload.setEnabled(true);
                } else {
                    btUpload.setEnabled(false);
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

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.btUpload)
    public void onViewClicked() {
        fileName = etFileName.getText().toString();
        getPDFS();
    }

    private void getPDFS() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), Constants.PICK_PDF_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PICK_PDF_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            //if a file is selected
            if (data.getData() != null) {
                //uploading the file
                uploadFile(data.getData());
            } else {
                Toast.makeText(getContext(), "No file chosen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadFile(Uri data) {

        TransitionManager.beginDelayedTransition(transitionsContainer);
        UploadProgress.setVisibility(View.VISIBLE);
        etContainer.setVisibility(View.GONE);
        etFileName.setVisibility(View.GONE);
        spinner.setVisibility(View.GONE);
        btUpload.setVisibility(View.GONE);

        StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + fileName + UUID.randomUUID() + ".pdf");
        sRef.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Upload upload = new Upload(fileName, uri.toString(), theme);
                                databaseReference.child(databaseReference.push().getKey()).setValue(upload);
                            }
                        });

                        TransitionManager.beginDelayedTransition(transitionsContainer);
                        readyUIforInput();
                        tvUploadStatus.setText("File Uploaded Successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        tvUploadStatus.setText("Failure To Upload File");
                        Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @SuppressWarnings("VisibleForTests")
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        tvUploadStatus.setText((int) progress + "% Uploading...");
                    }
                });

    }

    private void readyUIforInput() {
        UploadProgress.setVisibility(View.GONE);
        etFileName.setText(null);
        etContainer.setVisibility(View.VISIBLE);
        etFileName.setVisibility(View.VISIBLE);
        spinner.setVisibility(View.VISIBLE);
        btUpload.setEnabled(false);
        btUpload.setVisibility(View.VISIBLE);
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


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void messageFromChildFragment(Uri uri);
    }
}
