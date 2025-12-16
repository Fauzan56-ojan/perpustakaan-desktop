package dao;

import util.DBConnection;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PengembalianDAO {
    private static final Logger logger = Logger.getLogger(PengembalianDAO.class.getName());

    // Prosedur untuk mengembalikan buku
    public static String kembalikanBuku(int idPinjam) {
        String sql = "{call sp_KembalikanBuku(?)}";
        
        try (Connection conn = DBConnection.getConnection();
             CallableStatement cs = conn.prepareCall(sql)) {
            
            cs.setInt(1, idPinjam);
            boolean hasResult = cs.execute();
            
            if (hasResult) {
                try (ResultSet rs = cs.getResultSet()) {
                    if (rs.next()) {
                        return rs.getString(1); // Ambil pesan dari stored procedure
                    }
                }
            }
            
            return "Buku berhasil dikembalikan";
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error saat mengembalikan buku ID: " + idPinjam, e);
            return "Error sistem: " + e.getMessage();
        }
    }

    // Ambil daftar pinjaman aktif berdasarkan ID anggota
    public static List<Map<String, Object>> getDaftarPinjamanAktif(int idAnggota) {
        String sql = "SELECT p.ID_Pinjaman, b.Judul, p.TanggalPinjam, " +
                     "DATEDIFF(day, p.TanggalPinjam, GETDATE()) AS LamaPinjam " +
                     "FROM Pinjaman p " +
                     "JOIN Buku b ON p.ID_Buku = b.ID_Buku " +
                     "WHERE p.Status = 'Dipinjam' AND p.ID_Anggota = ? " +
                     "ORDER BY p.TanggalPinjam DESC";

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAnggota);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> pinjaman = new LinkedHashMap<>();
                    pinjaman.put("idPinjam", rs.getInt("ID_Pinjaman"));
                    pinjaman.put("judul", rs.getString("Judul"));
                    pinjaman.put("tglPinjam", rs.getDate("TanggalPinjam"));
                    pinjaman.put("lamaPinjam", rs.getInt("LamaPinjam"));
                    pinjaman.put("status", "Dipinjam");
                    list.add(pinjaman);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal mengambil pinjaman aktif untuk anggota ID: " + idAnggota);
        }

        return list;
    }

    // Ambil semua pinjaman dengan status "Dipinjam"
    public static List<Map<String, Object>> getSemuaPinjamanDipinjam() {
        String sql = "SELECT p.ID_Pinjaman, b.Judul, p.TanggalPinjam, " +
                     "DATEDIFF(day, p.TanggalPinjam, GETDATE()) AS LamaPinjam " +
                     "FROM Pinjaman p " +
                     "JOIN Buku b ON p.ID_Buku = b.ID_Buku " +
                     "WHERE p.Status = 'Dipinjam' " +
                     "ORDER BY p.TanggalPinjam DESC";

        List<Map<String, Object>> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String, Object> pinjaman = new LinkedHashMap<>();
                pinjaman.put("idPinjam", rs.getInt("ID_Pinjaman"));
                pinjaman.put("judul", rs.getString("Judul"));
                pinjaman.put("tglPinjam", rs.getDate("TanggalPinjam"));
                pinjaman.put("lamaPinjam", rs.getInt("LamaPinjam"));
                pinjaman.put("status", "Dipinjam");
                list.add(pinjaman);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Gagal ambil daftar pinjaman dipinjam");
        }

        return list;
    }

    // Debug: Cetak semua pinjaman aktif ke log
    public static void logAllActiveLoans() {
        String sql = "SELECT p.ID_Pinjaman, p.ID_Anggota, a.Nama, b.Judul, " +
                     "p.TanggalPinjam, p.TanggalKembali, p.Status " +
                     "FROM Pinjaman p " +
                     "JOIN Anggota a ON p.ID_Anggota = a.ID_Anggota " +
                     "JOIN Buku b ON p.ID_Buku = b.ID_Buku " +
                     "WHERE p.Status = 'Dipinjam'";
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            logger.info("==== ALL ACTIVE LOANS IN DATABASE ====");
            while (rs.next()) {
                logger.info(String.format(
                    "ID: %d, Anggota: %s (%d), Buku: %s, Pinjam: %s, Kembali: %s, Status: %s",
                    rs.getInt("ID_Pinjaman"),
                    rs.getString("Nama"),
                    rs.getInt("ID_Anggota"),
                    rs.getString("Judul"),
                    rs.getDate("TanggalPinjam"),
                    rs.getDate("TanggalKembali"),
                    rs.getString("Status")
                ));
            }
            
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error logging all active loans", e);
        }
    }
}
