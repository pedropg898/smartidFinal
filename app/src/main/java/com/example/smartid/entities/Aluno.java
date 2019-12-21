package com.example.smartid.entities;

import java.io.Serializable;

public class Aluno implements Serializable {
    private String uc;
    private String prof;
    private String data_p;

    public Aluno(String uc, String prof, String data_p) {
        this.uc = uc;
        this.prof = prof;
        this.data_p = data_p;
    }

    public String getUc() {
        return uc;
    }

    public String getProf() {
        return prof;
    }

    public String getData_p() {
        return data_p;
    }
}
