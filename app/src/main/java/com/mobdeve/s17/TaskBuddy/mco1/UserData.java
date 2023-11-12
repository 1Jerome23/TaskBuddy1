package com.mobdeve.s17.TaskBuddy.mco1;

public class UserData {
        public String fullName;
        public String email;
        public String password;
        private String uid;


    public UserData() {
        }
        public UserData(String fullName, String email, String password, String uid) {
            this.fullName = fullName;
            this.email = email;
            this.password = password;
            this.uid = uid;
        }
    public String getId() {
        return uid;
    }

    public void setId(String id) {
        this.uid = uid;
    }

        // Getters and setters (if needed)
        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


