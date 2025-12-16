package model;

import java.util.Date;

public class Pinjaman {
    private int id;
    private int idAnggota;
    private int idBuku;
    private Date tanggalPinjam;
    private Date tanggalKembali;
    private String status;
    private String judulBuku;
    private String namaPeminjam;

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdAnggota() { return idAnggota; }
    public void setIdAnggota(int idAnggota) { this.idAnggota = idAnggota; }

    public int getIdBuku() { return idBuku; }
    public void setIdBuku(int idBuku) { this.idBuku = idBuku; }

    public Date getTanggalPinjam() { return tanggalPinjam; }
    public void setTanggalPinjam(Date tanggalPinjam) { this.tanggalPinjam = tanggalPinjam; }

    public Date getTanggalKembali() { return tanggalKembali; }
    public void setTanggalKembali(Date tanggalKembali) { this.tanggalKembali = tanggalKembali; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getJudulBuku() {
    return judulBuku;
}

public void setJudulBuku(String judulBuku) {
    this.judulBuku = judulBuku;
}

public String getNamaPeminjam() {
    return namaPeminjam;
}

public void setNamaPeminjam(String namaPeminjam) {
    this.namaPeminjam = namaPeminjam;
}
}
