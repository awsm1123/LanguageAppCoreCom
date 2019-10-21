package awsm.awsmizng.u.alanguageapp.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.adapters.ArchiveRecyclerAdapter;
import awsm.awsmizng.u.alanguageapp.models.Upload;
import awsm.awsmizng.u.alanguageapp.statics.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArchiveFragment extends Fragment implements ArchiveRecyclerAdapter.OnThemeListener {

    DatabaseReference databaseReference;
    List<Upload> uploadList;
    Unbinder unbinder;
    @BindView(R.id.btArchive)
    TextView btArchive;
    @BindView(R.id.rvArchive)
    RecyclerView rvArchive;
    @BindView(R.id.transitionContainer)
    ScrollView transitionContainer;

    private RecyclerView rvTheme;
    private ArrayList<String> theme = new ArrayList<>();
    private ArchiveRecyclerAdapter archiveRecyclerAdapter;
    ViewGroup transition;

    public ArchiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        unbinder = ButterKnife.bind(this, view);
        setLanguageTheme();
        initRecyclerView();
        databaseReference = Constants.DATABASE_BASE_REFERENCE.child(Constants.language);
        return view;
    }

    private void setLanguageTheme() {
        if (Constants.language.equals("GERMAN")) {
            theme = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.german_theme_array)));
        }
        if (Constants.language.equals("JAPANESE")) {
            theme = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.japanese_theme_array)));
        }
        if (Constants.language.equals("SANSKRIT")) {
            theme = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.sanskrit_theme_array)));
        }
        if (Constants.language.equals("SPANISH")) {
            theme = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.spanish_theme_array)));
        }
        if (Constants.language.equals("FRENCH")) {
            theme = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.french_theme_array)));
        }
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvArchive.setLayoutManager(linearLayoutManager);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvArchive);
        archiveRecyclerAdapter = new ArchiveRecyclerAdapter(theme, this);
        rvArchive.setAdapter(archiveRecyclerAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onThemeClick(int position) {
        Toast.makeText(getContext(), theme.get(position), Toast.LENGTH_SHORT).show();
    }

//    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//        @Override
//        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//            return false;
//        }
//
//        @Override
//        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//            //put swpie code
//        }
//    };

//    @OnClick({R.id.btCulture, R.id.btHistory, R.id.btA1, R.id.btA2, R.id.btB1, R.id.btB2, R.id.btArchive})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.btCulture:
//                hideLists();
//                displayList(databaseReference.child("Culture"), lvCulture);
//                break;
//            case R.id.btHistory:
//                hideLists();
//                displayList(databaseReference.child("History"), lvHistory);
//                break;
//            case R.id.btA1:
//                hideLists();
//                displayList(databaseReference.child("A1"), lvA1);
//                break;
//            case R.id.btA2:
//                hideLists();
//                displayList(databaseReference.child("A2"), lvA2);
//                break;
//            case R.id.btB1:
//                hideLists();
//                displayList(databaseReference.child("B1"), lvB1);
//                break;
//            case R.id.btB2:
//                hideLists();
//                displayList(databaseReference.child("B2"), lvB2);
//                break;
//            case R.id.btArchive:
//                hideLists();
//                break;
//        }
//    }

//    private void displayList(DatabaseReference databaseReference, final ListView listView){
//        uploadList = new ArrayList<>();
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {
//                Upload upload = uploadList.get(position);
//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(upload.getUrl()));
//                startActivity(intent);
//            }
//        });
//
//
//
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
//                    Upload upload = postSnapshot.getValue(Upload.class);
//                    uploadList.add(upload);
//                }
//
//                String[] uploads = new String[uploadList.size()];
//                for (int i = 0; i < uploads.length; i++) {
//                    uploads[i] = uploadList.get(i).getName();
//                }
//
//                //displaying it to list
//
//                if (getActivity() != null) {
//                    ArrayAdapter <String> adapter;
//                    adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, uploads);
//                    listView.setAdapter(adapter);
//                    setListViewHeightBasedOnChildren(listView);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        TransitionManager.beginDelayedTransition(transition);
//        listView.setVisibility(View.VISIBLE);
//    }

//    private void hideLists(){
//        TransitionManager.beginDelayedTransition(transition);
//        lvCulture.setVisibility(View.GONE);
//        lvHistory.setVisibility(View.GONE);
//        lvA1.setVisibility(View.GONE);
//        lvA2.setVisibility(View.GONE);
//        lvB1.setVisibility(View.GONE);
//        lvB2.setVisibility(View.GONE);
//    }

//    public static void setListViewHeightBasedOnChildren(ListView listView) {
//        ListAdapter listAdapter = listView.getAdapter();
//        if (listAdapter == null) return;
//        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
//                View.MeasureSpec.UNSPECIFIED);
//        int totalHeight = 0;
//        View view = null;
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            view = listAdapter.getView(i, view, listView);
//            if (i == 0) view.setLayoutParams(new
//                    ViewGroup.LayoutParams(desiredWidth,
//                    ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
//            totalHeight += view.getMeasuredHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//
//        params.height = totalHeight + (listView.getDividerHeight() *
//                (listAdapter.getCount() - 1));
//
//        listView.setLayoutParams(params);
//        listView.requestLayout();
//    }
}
