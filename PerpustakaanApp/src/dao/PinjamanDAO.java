package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Pinjaman;
import util.DBConnection;

public class PinjamanDAO {
    public static String pinjamBuku(int idAnggota, int idBuku) {
    String hasil = "";
    try (Connection conn = DBConnection.getConnection()) {
        CallableStatement stmt = conn.prepareCall("{call sp_PinjamBuku(?, ?)}");
        stmt.setInt(1, idAnggota);
        stmt.setInt(2, idBuku);

        boolean hasResult = stmt.execute();

        while (hasResult) {
            try (ResultSet rs = stmt.getResultSet()) {
                if (rs.next()) {
                    hasil = rs.getString("Pesan");
                }

            }
            hasResult = stmt.getMoreResults();
        }

        if (hasil.isEmpty()) {
            hasil = "Buku Berhasil Dipinjam";
        }
    } catch (SQLException e) {
        hasil = "Error: " + e.getMessage();
    }
    return hasil;
}

    public static List<Pinjaman> getDaftarPinjamanAktif() {
    List<Pinjaman> list = new ArrayList<>();
    String sql = "SELECT b.Judul, a.Nama AS Peminjam, p.TanggalPinjam, p.TanggalKembali " +
                 "FROM Pinjaman p " +
                 "JOIN Buku b ON p.ID_Buku = b.ID_Buku " +
                 "JOIN Anggota a ON p.ID_Anggota = a.ID_Anggota " +
                 "WHERE p.Status = 'Dipinjam'";
    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Pinjaman p = new Pinjaman();
            p.setJudulBuku(rs.getString("Judul"));
            p.setNamaPeminjam(rs.getString("Peminjam"));
            p.setTanggalPinjam(rs.getDate("TanggalPinjam"));
            p.setTanggalKembali(rs.getDate("TanggalKembali"));
            list.add(p);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}
}
