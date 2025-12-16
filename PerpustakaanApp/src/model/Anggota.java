package model;

public class Anggota {
    private int id;
    private String nama;
    private String alamat;
    private String noTelepon;
    private String email;
    private String tanggalBergabung;

    public Anggota() {}

    public Anggota(int id, String nama, String alamat, String noTelepon, String email, String tanggalBergabung) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.email = email;
        this.tanggalBergabung = tanggalBergabung;
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }

    public String getNoTelepon() { return noTelepon; }
    public void setNoTelepon(String noTelepon) { this.noTelepon = noTelepon; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTanggalBergabung() { return tanggalBergabung; }
    public void setTanggalBergabung(String tanggalBergabung) { this.tanggalBergabung = tanggalBergabung; }
}
