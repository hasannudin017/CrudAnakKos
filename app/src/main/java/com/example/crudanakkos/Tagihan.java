package com.example.crudanakkos;

public class Tagihan {

    private String idTagihan, username, totalTagihan, tanggal, status, noKos;
    private String nama, url, desc;

    public Tagihan() {

    }

    public Tagihan(String idTagihan, String username, String totalTagihan, String tanggal, String status, String noKos, String nama, String url, String desc) {
        this.idTagihan = idTagihan;
        this.username = username;
        this.totalTagihan = totalTagihan;
        this.tanggal = tanggal;
        this.status = status;
        this.noKos = noKos;
        this.nama = nama;
        this.url = url;
        this.desc = desc;
    }

    public String getIdTagihan() {
        return idTagihan;
    }

    public void setIdTagihan(String idTagihan) {
        this.idTagihan = idTagihan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTotalTagihan() {
        return totalTagihan;
    }

    public void setTotalTagihan(String totalTagihan) {
        this.totalTagihan = totalTagihan;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNoKos() {
        return noKos;
    }

    public void setNoKos(String noKos) {
        this.noKos = noKos;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}