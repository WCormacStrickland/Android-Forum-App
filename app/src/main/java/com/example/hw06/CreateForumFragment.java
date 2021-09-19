package com.example.hw06;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateForumFragment extends Fragment {
    FirebaseFirestore forumsFire = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth= FirebaseAuth.getInstance();

    public CreateForumFragment() {
        // Required empty public constructor
    }

    public static CreateForumFragment newInstance() {
        CreateForumFragment fragment = new CreateForumFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    EditText editTextForumDesc, editTextForumTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        getActivity().setTitle("Create Forum");
        View view = inflater.inflate(R.layout.fragment_create_forum, container, false);

        editTextForumDesc = view.findViewById(R.id.editTextForumDesc);
        editTextForumTitle = view.findViewById(R.id.editTextForumTitle);

        view.findViewById(R.id.buttonCreateForumSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTextForumTitle.getText().toString();
                String desc = editTextForumDesc.getText().toString();

                if(title.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Title !!", Toast.LENGTH_SHORT).show();
                } else if(desc.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Desc !!", Toast.LENGTH_SHORT).show();
                } else {
                    createForum(title, desc);
                    mListener.doneCreateForum();

                }
            }
        });

        view.findViewById(R.id.buttonCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.doneCreateForum();
            }
        });

        return view;
    }
    private void createForum(String title, String desc) {
        Map<String, Object> forum = new HashMap<>();
        forum.put("title", title);
        forum.put("description", desc);
        forum.put("createdBy", mAuth.getCurrentUser().getDisplayName());
        Date date = new Date();
        forum.put("createdAt", date);
        ArrayList<String> likedByList = new ArrayList<>();
        likedByList.add(mAuth.getCurrentUser().getDisplayName());
        forum.put("likedBy", likedByList);
        forumsFire.collection("Forums")
                .add(forum)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {

                    }
                });
    }


    CreateForumListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (CreateForumListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CreateForumListener");
        }
    }

    interface CreateForumListener{
        void doneCreateForum();
    }

}