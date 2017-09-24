package com.shwetabh.ghost.studentdemo.model;

public class Details {
    String name;
    String imageUrl;
    String id;

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    String clas;
    String roll;
    String subject;
    String marks;
 
   /* public Results(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }*/
 
    public String getName() {
        return name;
    }
 
    public String getImageUrl() {
        return imageUrl;
    }
}