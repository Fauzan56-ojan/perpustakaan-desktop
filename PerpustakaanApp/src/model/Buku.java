package model;

public class Buku {
    private int id;
    private String judul;
    private String pengarang;
    private String penerbit;
    private int tahunTerbit;

    // Constructor
    public Buku(int id, String judul, String pengarang, String penerbit, int tahunTerbit) {
        this.id = id;
        this.judul = judul;
        this.pengarang = pengarang;
        this.penerbit = penerbit;
        this.tahunTerbit = tahunTerbit;
    }

    public Buku(String judul, String pengarang, String penerbit, int tahunTerbit) {
        this(0, judul, pengarang, penerbit, tahunTerbit);
    }

    // Getter & Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getJudul() { return judul; }
    public void setJudul(String judul) { this.judul = judul; }

    public String getPengarang() { return pengarang; }
    public void setPengarang(String pengarang) { this.pengarang = pengarang; }

    public String getPenerbit() { return penerbit; }
    public void setPenerbit(String penerbit) { this.penerbit = penerbit; }

    public int getTahunTerbit() { return tahunTerbit; }
    public void setTahunTerbit(int tahunTerbit) { this.tahunTerbit = tahunTerbit; }
}
