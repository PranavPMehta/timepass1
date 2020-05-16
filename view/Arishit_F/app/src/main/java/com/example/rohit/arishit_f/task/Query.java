package com.example.rohit.arishit_f.task;

public class Query {

    String Query,Answer;

    public Query(String query, String answer) {
        Query = query;
        Answer = answer;
    }

    public Query(String query) {
        Query = query;
    }

    public String getQuery() {
        return Query;
    }

    public void setQuery(String query) {
        Query = query;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }
}

