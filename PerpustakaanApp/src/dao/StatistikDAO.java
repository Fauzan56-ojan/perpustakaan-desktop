package dao;

import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatistikDAO {

    public static int getJumlah(String tabel) {
        int jumlah = 0;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM " + tabel)) {
            if (rs.next()) {
                jumlah = rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jumlah;
    }

    public static int getJumlahPinjamanAktif() {
        int jumlah = 0;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Pinjaman WHERE status = 'Dipinjam'")) {
            if (rs.next()) {
                jumlah = rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jumlah;
    }

    public static int getJumlahSudahKembali() {
        int jumlah = 0;
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS total FROM Pinjaman WHERE status = 'Dikembalikan'")) {
            if (rs.next()) {
                jumlah = rs.getInt("total");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jumlah;
    }

    // Statistik peminjaman per bulan
    public static List<String[]> getStatistikPerBulan() {
        List<String[]> data = new ArrayList<>();
        String sql = "SELECT DATENAME(MONTH, TanggalPinjam) AS Bulan, " +
                     "COUNT(*) AS JumlahPeminjaman " +
                     "FROM Pinjaman " +
                     "GROUP BY DATENAME(MONTH, TanggalPinjam), MONTH(TanggalPinjam) " +
                     "ORDER BY MONTH(TanggalPinjam)";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String bulan = rs.getString("Bulan");
                String jumlah = String.valueOf(rs.getInt("JumlahPeminjaman"));
                data.add(new String[]{bulan, jumlah});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    // Peminjam paling aktif
    public static List<String[]> getPeminjamPalingAktif() {
        List<String[]> data = new ArrayList<>();
        String sql = "SELECT a.Nama, COUNT(p.ID_Pinjaman) AS JumlahPeminjaman " +
                     "FROM Anggota a " +
                     "LEFT JOIN Pinjaman p ON a.ID_Anggota = p.ID_Anggota " +
                     "GROUP BY a.ID_Anggota, a.Nama " +
                     "ORDER BY JumlahPeminjaman DESC";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                data.add(new String[]{
                    rs.getString("Nama"),
                    String.valueOf(rs.getInt("JumlahPeminjaman"))
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    // Rata-rata lama peminjaman
    public static double getRataRataHariPeminjaman() {
        double hasil = 0;
        String sql = "SELECT AVG(DATEDIFF(day, TanggalPinjam, TanggalPengembalian)) AS RataRata " +
                     "FROM Pinjaman p " +
                     "JOIN Pengembalian pg ON p.ID_Pinjaman = pg.ID_Pinjaman " +
                     "WHERE p.Status = 'Dikembalikan'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                hasil = rs.getDouble("RataRata");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hasil;
    }
}
