package com.hnsh.dialogue.bean.cbs;

import java.util.Objects;

/**
 * Created by Unpar on 18/1/27.
 */

public class PushToken {
    private String query;
    private int session;

    public PushToken() {
        this("");
    }

    public PushToken(String query) {
        this(query, 0);
    }

    public PushToken(String query, int session) {
        this.query = query;
        this.session = session;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PushToken pushToken = (PushToken) o;

        if (session != pushToken.session) return false;
        return Objects.equals(query, pushToken.query);
    }

    @Override
    public int hashCode() {
        int result = query != null ? query.hashCode() : 0;
        result = 31 * result + session;
        return result;
    }

    @Override
    public String toString() {
        return "PushToken{" +
                "query='" + query + '\'' +
                ", session=" + session +
                '}';
    }
}
