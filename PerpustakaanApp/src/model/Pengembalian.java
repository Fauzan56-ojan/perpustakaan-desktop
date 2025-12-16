package model;

import java.sql.Date;

public class Pengembalian {
    private int id;
    private int idPinjam;
    private Date tanggalKembali;

    public Pengembalian(int idPinjam, Date tanggalKembali) {
        this.idPinjam = idPinjam;
        this.tanggalKembali = tanggalKembali;
    }

    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdPinjam() { return idPinjam; }
    public void setIdPinjam(int idPinjam) { this.idPinjam = idPinjam; }

    public Date getTanggalKembali() { return tanggalKembali; }
    public void setTanggalKembali(Date tanggalKembali) { this.tanggalKembali = tanggalKembali; }
}
