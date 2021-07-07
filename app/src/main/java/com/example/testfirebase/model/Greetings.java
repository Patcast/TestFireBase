package com.example.testfirebase.model;

public class Greetings {
    String greetings;
    String sender;
    int date;
    String id;

    public Greetings() {
    }

    public Greetings(String greetings, String sender, int date, String id) {
        this.greetings = greetings;
        this.sender = sender;
        this.date = date;
        this.id = id;
    }

    public Greetings(String greetings, String sender, int date) {
        this.greetings = greetings;
        this.sender = sender;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGreetings() {
        return greetings;
    }

    public void setGreetings(String greeting) {
        this.greetings = greeting;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Greetings{" +
                "greetings='" + greetings + '\'' +
                ", sender='" + sender + '\'' +
                ", date=" + date +
                '}';
    }
}
