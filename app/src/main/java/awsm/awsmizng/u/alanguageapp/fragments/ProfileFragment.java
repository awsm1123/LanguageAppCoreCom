package awsm.awsmizng.u.alanguageapp.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import awsm.awsmizng.u.alanguageapp.R;
import awsm.awsmizng.u.alanguageapp.activities.Settings;
import awsm.awsmizng.u.alanguageapp.models.FirebaseProfileRV;
import awsm.awsmizng.u.alanguageapp.models.FirebaseUserProfile;
import awsm.awsmizng.u.alanguageapp.statics.Constants;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ProfileFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.rvProfiles)
    RecyclerView rvProfiles;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.tvExpPpoints)
    TextView ExPoints;
    @BindView(R.id.tvArticleNumber)
    TextView tvArticleNumber;
    @BindView(R.id.ivLanguageFlag)
    ImageView icon;
    @BindView(R.id.settings)
    ImageView settings;

    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        unbinder = ButterKnife.bind(this, view);

        linearLayoutManager = new LinearLayoutManager(getContext());
        rvProfiles.setLayoutManager(linearLayoutManager);
        rvProfiles.setHasFixedSize(true);
        fetch();
        setIconAndName();


        Query ref = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADERS).orderByChild("userID").equalTo(Constants.uploaderID);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        FirebaseUserProfile userProfile = snapshot.getValue(FirebaseUserProfile.class);
                        if(ExPoints != null && tvArticleNumber != null){
                            ExPoints.setText(userProfile.getPoints());
                            tvArticleNumber.setText(userProfile.getPoints());
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }

    private void setIconAndName() {
        if (Constants.language.equals("GERMAN")) {
            icon.setImageResource(R.drawable.germany);
        }
        if (Constants.language.equals("JAPANESE")) {
            icon.setImageResource(R.drawable.japan);
        }
        if (Constants.language.equals("SANSKRIT")) {
            icon.setImageResource(R.drawable.india);
        }
        if (Constants.language.equals("SPANISH")) {
            icon.setImageResource(R.drawable.spain);
        }
        if (Constants.language.equals("FRENCH")) {
            icon.setImageResource(R.drawable.france);
        }

        name.setText(Constants.uploaderName);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        firebaseRecyclerAdapter.stopListening();
        unbinder.unbind();
    }

    @Override
    public void onStart() {
        super.onStart();
        // firebaseRecyclerAdapter.startListening();
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADERS);

        FirebaseRecyclerOptions<FirebaseProfileRV> options =
                new FirebaseRecyclerOptions.Builder<FirebaseProfileRV>()
                        .setQuery(query, new SnapshotParser<FirebaseProfileRV>() {
                            @NonNull
                            @Override
                            public FirebaseProfileRV parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new FirebaseProfileRV(
                                        snapshot.child("userName").getValue().toString(),
                                        snapshot.child("points").getValue().toString(),
                                        snapshot.child("language").getValue().toString()
                                );
                            }
                        })
                        .build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FirebaseProfileRV, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull FirebaseProfileRV model) {
                holder.setName(model.getUserName());
                holder.setLanguaage(model.getLanguage());
                holder.setPoints(model.getPoints());

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "AWSM", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = (View) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.profile_row_layout, viewGroup, false);
                return new ViewHolder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        rvProfiles.setAdapter(firebaseRecyclerAdapter);
    }

    @OnClick(R.id.settings)
    public void onViewClicked() {
        startActivity(new Intent(getActivity(), Settings.class));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout root;
        public TextView name, points, languaage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.llRoot);
            name = itemView.findViewById(R.id.tvName);
            points = itemView.findViewById(R.id.tvPoints);
            languaage = itemView.findViewById(R.id.tvLanguage);
        }

        public void setName(String s) {
            name.setText(s);
        }

        public void setPoints(String s) {
            points.setText(s);
        }

        public void setLanguaage(String s) {
            languaage.setText(s);
        }
    }

}