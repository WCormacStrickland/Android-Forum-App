package com.example.hw06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ForumsListFragment extends Fragment {
    FirebaseFirestore forumsFire = FirebaseFirestore.getInstance();
    ArrayList<Data2.Forum> forumsArr = new ArrayList<>();

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();


    public ForumsListFragment() {
        // Required empty public constructor
    }

    public static ForumsListFragment newInstance() {
        ForumsListFragment fragment = new ForumsListFragment();
//        Bundle args = new Bundle();
        //args.putSerializable(ARG_PARAM_AUTH_RES, (Serializable) user);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mAuth = (FirebaseAuth) getArguments().getSerializable(ARG_PARAM_AUTH_RES);
//        }
    }

    RecyclerView recyclerView;
    ForumsAdapter adapter;
    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Forums List");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forums_list, container, false);

        view.findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.logOut();
            }
        });

        view.findViewById(R.id.buttonAddNewForum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoAddNewForum();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new ForumsAdapter();
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        getForums();

        return view;
    }
    private void getForums(){
        forumsFire.collection("Forums")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        forumsArr.clear();
                        for (DocumentSnapshot document: value) {
                            Data2.Forum forum = document.toObject(Data2.Forum.class);
                            forum.setId(document.getId());
                            forumsArr.add(forum);

                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }


    class ForumsAdapter extends RecyclerView.Adapter<ForumsAdapter.ForumViewHolder> {
        @NonNull
        @Override
        public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.forum_list_item, parent, false);
            return new ForumViewHolder(view);
        }

        @Override
        public int getItemCount() {
            return forumsArr.size();

        }

        @Override
        public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
            Data2.Forum forum = forumsArr.get(position);
            holder.setupForumItem(forum);


        }

        class ForumViewHolder extends RecyclerView.ViewHolder{
            TextView textViewTitle, textViewDesc, textViewOwner, textViewLikesAndDate;
            ImageView imageViewLike, imageViewDeleteForum;
            Data2.Forum mForum;


            public ForumViewHolder(@NonNull View itemView) {
                super(itemView);
                textViewTitle = itemView.findViewById(R.id.textViewTitle);
                textViewDesc = itemView.findViewById(R.id.textViewDesc);
                textViewOwner = itemView.findViewById(R.id.textViewOwner);
                textViewLikesAndDate = itemView.findViewById(R.id.textViewLikesAndDate);
                imageViewLike = itemView.findViewById(R.id.imageViewLike);
                imageViewDeleteForum = itemView.findViewById(R.id.imageViewDeleteForum);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.gotoForumDetails(mForum);
                    }
                });
            }

            public void setupForumItem(Data2.Forum forum){
                this.mForum = forum;
                textViewTitle.setText(forum.getTitle());

                String desc200 = forum.getDescription().substring(0, Math.min(200, forum.getDescription().length()));

                textViewDesc.setText(desc200);
                textViewOwner.setText(forum.getCreatedBy());

                int likeCount = forum.getLikedBy().size();
                String likeString = "No Likes";
                if(likeCount == 1){
                    likeString = "1 Like";
                } else {
                    likeString = likeCount + " Likes";
                }

                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m a");
                textViewLikesAndDate.setText(likeString + " | " + formatter.format(forum.getCreatedAt()));

                if(forum.getCreatedBy().equals(mAuth.getCurrentUser().getDisplayName())){
                    imageViewDeleteForum.setVisibility(View.VISIBLE);
                    imageViewDeleteForum.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            forumsFire.collection("Forums").document(mForum.getId()).delete();

                        }
                    });
                } else {
                    imageViewDeleteForum.setVisibility(View.INVISIBLE);
                }

                if(forum.getLikedBy().contains(mAuth.getCurrentUser().getDisplayName())){
                    imageViewLike.setImageResource(R.drawable.like_favorite);
                } else {
                    imageViewLike.setImageResource(R.drawable.like_not_favorite);
                }

                imageViewLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("DERP", "onClick: " + forum.getLikedBy());
                        if(forum.getLikedBy().contains(mAuth.getCurrentUser().getDisplayName())){
                            forum.removeLikedBy(mAuth.getCurrentUser().getDisplayName());
                        } else {
                            forum.addLikedBy(mAuth.getCurrentUser().getDisplayName());
                        }
                        forumsFire.collection("Forums").document(forum.getId()).update("likedBy", forum.getLikedBy()).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });
            }
        }
    }


    ForumsListListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (ForumsListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement ForumsListListener");
        }
    }

    interface ForumsListListener{
        void gotoForumDetails(Data2.Forum forum);
        void logOut();
        void gotoAddNewForum();
    }

}
