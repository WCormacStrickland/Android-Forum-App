package com.example.hw06;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ForumFragment extends Fragment {
    private static final String ARG_PARAM_FORUM = "ARG_PARAM_FORUM";

    private Data2.Forum mForum;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    FirebaseFirestore forumsFire = FirebaseFirestore.getInstance();
    ArrayList<Data2.Comment> commentsArr = new ArrayList<Data2.Comment>();


    public ForumFragment() {
    }

    public static ForumFragment newInstance(Data2.Forum forum) {
        ForumFragment fragment = new ForumFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_FORUM, forum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mForum = (Data2.Forum) getArguments().getSerializable(ARG_PARAM_FORUM);
        }
    }

    TextView textViewForumTitle, textViewForumOwnerName, textViewForumDesc, textViewNumComments;
    EditText editTextTextComment;
    RecyclerView recyclerView;
    CommentsAdapter adapter;
    LinearLayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle("Forum Details");

        View view = inflater.inflate(R.layout.fragment_forum, container, false);

        textViewForumTitle = view.findViewById(R.id.textViewForumTitle);
        textViewForumOwnerName = view.findViewById(R.id.textViewForumOwnerName);
        textViewForumDesc = view.findViewById(R.id.textViewForumDesc);
        textViewNumComments = view.findViewById(R.id.textViewNumComments);

        editTextTextComment = view.findViewById(R.id.editTextTextComment);
        recyclerView = view.findViewById(R.id.recyclerView);

        textViewForumTitle.setText(mForum.getTitle());
        textViewForumOwnerName.setText(mForum.getCreatedBy());
        textViewForumDesc.setText(mForum.getDescription());
        textViewNumComments.setText("");


        view.findViewById(R.id.buttonPostSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String commentText = editTextTextComment.getText().toString();

                if(commentText.isEmpty()){
                    Toast.makeText(getActivity(), "Enter comment text!", Toast.LENGTH_SHORT).show();
                } else {
                    createComment(commentText);
                }
            }
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        adapter = new CommentsAdapter();
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        getComments();

        return view;
    }

    private void getComments(){
        forumsFire.collection("Comments")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        commentsArr.clear();
                        for (DocumentSnapshot document: value) {
                            Data2.Comment comment = document.toObject(Data2.Comment.class);
                            comment.setId(document.getId());
                            if (comment.getforumId().equals(mForum.getId())) {
                                commentsArr.add(comment);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
    private void createComment(String text) {
        Map<String, Object> comment = new HashMap<>();
        comment.put("comment", text);
        comment.put("createdBy", mAuth.getCurrentUser().getDisplayName());
        Date date = new Date();
        comment.put("createdAt", date);
        comment.put("forumId", mForum.getId());
        forumsFire.collection("Comments")
                .add(comment)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                });
    }

    class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentHolder>{
        @NonNull
        @Override
        public CommentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_list_item, parent, false);
            return new CommentHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CommentHolder holder, int position) {
            Data2.Comment comment = commentsArr.get(position);
            holder.setupComment(comment);
        }

        @Override
        public int getItemCount() {
            return commentsArr.size();
        }

        class CommentHolder extends RecyclerView.ViewHolder{
            TextView textViewDesc, textViewOwner, textViewDate;
            ImageView imageViewDeleteComment;

            public CommentHolder(@NonNull View itemView) {
                super(itemView);
                textViewDesc = itemView.findViewById(R.id.textViewDesc);
                textViewOwner = itemView.findViewById(R.id.textViewOwner);
                textViewDate = itemView.findViewById(R.id.textViewDate);
                imageViewDeleteComment = itemView.findViewById(R.id.imageViewDeleteComment);
            }

            public void setupComment(Data2.Comment comment){
                textViewDesc.setText(comment.getComment());
                textViewOwner.setText(comment.getCreatedBy());

                SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy h:m a");
                textViewDate.setText(formatter.format(comment.getCreatedAt()));


                if(comment.getCreatedBy().equals(mAuth.getCurrentUser().getDisplayName())){
                    imageViewDeleteComment.setVisibility(View.VISIBLE);
                    imageViewDeleteComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            forumsFire.collection("Comments").document(comment.getId()).delete();
                        }
                    });
                } else {
                    imageViewDeleteComment.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

}