package com.example.hw06;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Data2 {
    private static long COMMENT_ID_COUNTER = 1;

    public static class Forum implements Serializable {
        private String title;
        private String createdBy;
        private Date createdAt;
        private String description;
        private ArrayList<String> likedBy;
        private String id;
//        private ArrayList<String> comments;

        public Forum() {
            this.title = null;
            this.createdAt = null;
            this.createdBy = null;
            this.description = null;
            this.likedBy = null;
            this.id = null;
        }
        public Forum(String title, Date createdAt, String createdBy, String description, ArrayList<String> likedBy, String id) {
            this.title = title;
            this.createdAt = createdAt;
            this.createdBy = createdBy;
            this.description = description;
            this.likedBy = likedBy;
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public String getDescription() {
            return description;
        }

        public ArrayList<String> getLikedBy() {
            return likedBy;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }


        public void addLikedBy(String name) {
            likedBy.add(name);
        }

        public void removeLikedBy(String name) {
            likedBy.remove(name);
        }

//        public ArrayList<String> getComments() {
//            return comments;
//        }
    }

    public static class Comment implements Serializable {
        String comment;
        String createdBy;
        Date createdAt;
        String forumId;
        String commentId;
        public Comment() {
        }

        public Comment(String comment, String createdBy, Date createdAt, String forumId) {
            this.comment = comment;
            this.createdBy = createdBy;
            this.createdAt = createdAt;
            this.forumId = forumId;

        }

        public String getforumId() {
            return forumId;
        }

        public String getComment() {
            return comment;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public Date getCreatedAt() {
            return createdAt;
        }

        public String getId() {
            return commentId;
        }

        public void setId(String id) {
            this.commentId = id;
        }

    }
}
