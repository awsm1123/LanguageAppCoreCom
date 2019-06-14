package awsm.awsmizng.u.alanguageapp.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.models.Upload;
import awsm.awsmizng.u.alanguageapp.statics.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArchiveFragment extends Fragment {

    DatabaseReference databaseReference;
    List<Upload> uploadList;
    Unbinder unbinder;
    @BindView(R.id.btCulture)
    Button btCulture;
    @BindView(R.id.lvCulture)
    ListView lvCulture;
    ViewGroup transition;
    @BindView(R.id.btHistory)
    Button btHistory;
    @BindView(R.id.lvHistory)
    ListView lvHistory;
    @BindView(R.id.btA1)
    Button btA1;
    @BindView(R.id.lvA1)
    ListView lvA1;
    @BindView(R.id.btA2)
    Button btA2;
    @BindView(R.id.lvA2)
    ListView lvA2;
    @BindView(R.id.btB1)
    Button btB1;
    @BindView(R.id.lvB1)
    ListView lvB1;
    @BindView(R.id.btB2)
    Button btB2;
    @BindView(R.id.lvB2)
    ListView lvB2;

    public ArchiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        unbinder = ButterKnife.bind(this, view);
        transition = (ViewGroup) view.findViewById(R.id.transitionContainer);
        hideLists();
        databaseReference = Constants.DATABASE_BASE_REFERENCE.child(Constants.language);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btCulture, R.id.btHistory, R.id.btA1, R.id.btA2, R.id.btB1, R.id.btB2, R.id.btArchive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btCulture:
                hideLists();
                displayList(databaseReference.child("Culture"), lvCulture);
                break;
            case R.id.btHistory:
                hideLists();
                displayList(databaseReference.child("History"), lvHistory);
                break;
            case R.id.btA1:
                hideLists();
                displayList(databaseReference.child("A1"), lvA1);
                break;
            case R.id.btA2:
                hideLists();
                displayList(databaseReference.child("A2"), lvA2);
                break;
            case R.id.btB1:
                hideLists();
                displayList(databaseReference.child("B1"), lvB1);
                break;
            case R.id.btB2:
                hideLists();
                displayList(databaseReference.child("B2"), lvB2);
                break;
            case R.id.btArchive:
                hideLists();
                break;
        }
    }

    private void displayList(DatabaseReference databaseReference, final ListView listView){
        uploadList = new ArrayList<>();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {
                Upload upload = uploadList.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(upload.getUrl()));
                startActivity(intent);
            }
        });



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadList.add(upload);
                }

                String[] uploads = new String[uploadList.size()];

                for (int i = 0; i < uploads.length; i++) {
                    uploads[i] = uploadList.get(i).getName();
                }

                //displaying it to list

                if (getActivity() != null) {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, uploads);
                    listView.setAdapter(adapter);
                    setListViewHeightBasedOnChildren(listView);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        TransitionManager.beginDelayedTransition(transition);
        listView.setVisibility(View.VISIBLE);
    }

    private void hideLists(){
        TransitionManager.beginDelayedTransition(transition);
        lvCulture.setVisibility(View.GONE);
        lvHistory.setVisibility(View.GONE);
        lvA1.setVisibility(View.GONE);
        lvA2.setVisibility(View.GONE);
        lvB1.setVisibility(View.GONE);
        lvB2.setVisibility(View.GONE);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight() *
                (listAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
