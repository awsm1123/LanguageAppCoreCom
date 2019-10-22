package awsm.awsmizng.u.alanguageapp.fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.adapters.ArchiveArticleRecyclerAdapter;
import awsm.awsmizng.u.alanguageapp.adapters.ArchiveRecyclerAdapter;
import awsm.awsmizng.u.alanguageapp.models.Upload;
import awsm.awsmizng.u.alanguageapp.statics.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArchiveFragment extends Fragment implements ArchiveRecyclerAdapter.OnThemeListener, ArchiveArticleRecyclerAdapter.OnArticleListener {

    String TAG = "bhjtytt";

    DatabaseReference databaseReference;
    Unbinder unbinder;
    @BindView(R.id.btArchive)
    TextView btArchive;
    @BindView(R.id.rvArchive)
    RecyclerView rvArchive;
    @BindView(R.id.transitionContainer)
    RelativeLayout transitionContainer;
    @BindView(R.id.tvDisplayTheme)
    Button tvDisplayTheme;
    @BindView(R.id.llArticles)
    LinearLayout llArticles;
    @BindView(R.id.rvArticles)
    RecyclerView rvArticles;

    private ArrayList<String> theme = new ArrayList<>();
    List<Upload> uploadList;
    private ArchiveRecyclerAdapter archiveRecyclerAdapter;
    private ArchiveArticleRecyclerAdapter articleRecyclerAdapter;
    ViewGroup transition;

    public ArchiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archive, container, false);
        unbinder = ButterKnife.bind(this, view);
        transition = transitionContainer;
        setLanguageTheme();
        initArchiveRecyclerView();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onThemeClick(int position) {
        getList(databaseReference.child(theme.get(position)));
        TransitionManager.beginDelayedTransition(transition);
        rvArchive.setVisibility(View.GONE);
        llArticles.setVisibility(View.VISIBLE);
        tvDisplayTheme.setText(theme.get(position));
    }

    @Override
    public void onArticleClick(int position) {
        Upload upload = uploadList.get(position);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(upload.getUrl()));
        startActivity(intent);
    }

    private void initArchiveRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvArchive.setLayoutManager(linearLayoutManager);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvArchive);
        archiveRecyclerAdapter = new ArchiveRecyclerAdapter(theme, this);
        rvArchive.setAdapter(archiveRecyclerAdapter);
    }

    private void initArchiveArticleRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvArticles.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvArticles.getContext(),
                linearLayoutManager.getOrientation());
        rvArticles.addItemDecoration(dividerItemDecoration);
//        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(rvArchive);
        articleRecyclerAdapter = new ArchiveArticleRecyclerAdapter(uploadList, this);
        rvArticles.setAdapter(articleRecyclerAdapter);
    }

    private void getList(DatabaseReference databaseReference) {
        uploadList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Upload upload = postSnapshot.getValue(Upload.class);
                    uploadList.add(upload);
                }

                initArchiveArticleRecyclerView();
                articleRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                tvDisplayTheme.setText("No Data to Display!");
            }
        });
    }

    @OnClick(R.id.tvDisplayTheme)
    public void onViewClicked() {
        TransitionManager.beginDelayedTransition(transition);
        llArticles.setVisibility(View.GONE);
        rvArchive.setVisibility(View.VISIBLE);
    }

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

    //    private void hideLists(){
//        TransitionManager.beginDelayedTransition(transition);
//        lvCulture.setVisibility(View.GONE);
//        lvHistory.setVisibility(View.GONE);
//        lvA1.setVisibility(View.GONE);
//        lvA2.setVisibility(View.GONE);
//        lvB1.setVisibility(View.GONE);
//        lvB2.setVisibility(View.GONE);
//    }

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
}
