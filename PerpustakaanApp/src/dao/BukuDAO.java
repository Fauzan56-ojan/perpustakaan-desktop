/*package dao;

import model.Buku;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {

    public List<Buku> getAllBuku() {
        List<Buku> list = new ArrayList<>();
        String sql = "SELECT ID_Buku, Judul, Pengarang, Penerbit, TahunTerbit FROM Buku";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Buku b = new Buku(
                    rs.getInt("ID_Buku"),
                    rs.getString("Judul"),
                    rs.getString("Pengarang"),
                    rs.getString("Penerbit"),
                    rs.getInt("TahunTerbit")
                );
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void tambahBuku(Buku buku) {
        String sql = "INSERT INTO Buku (Judul, Pengarang, Penerbit, TahunTerbit, Status) VALUES (?, ?, ?, ?, 'Tersedia')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, buku.getJudul());
            stmt.setString(2, buku.getPengarang());
            stmt.setString(3, buku.getPenerbit());
            stmt.setInt(4, buku.getTahunTerbit());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void hapusBuku(int id) {
        String sql = "DELETE FROM Buku WHERE ID_Buku = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}*/
package dao;

import model.Buku;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BukuDAO {

    public List<Buku> getAllBuku() {
        List<Buku> list = new ArrayList<>();
        String sql = "SELECT ID_Buku, Judul, Pengarang, Penerbit, TahunTerbit FROM Buku";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Buku b = new Buku(
                    rs.getInt("ID_Buku"),
                    rs.getString("Judul"),
                    rs.getString("Pengarang"),
                    rs.getString("Penerbit"),
                    rs.getInt("TahunTerbit")
                );
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void tambahBuku(Buku buku) {
        String sql = "INSERT INTO Buku (Judul, Pengarang, Penerbit, TahunTerbit, Status) VALUES (?, ?, ?, ?, 'Tersedia')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, buku.getJudul());
            stmt.setString(2, buku.getPengarang());
            stmt.setString(3, buku.getPenerbit());
            stmt.setInt(4, buku.getTahunTerbit());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void hapusBuku(int id) {
        String sql = "DELETE FROM Buku WHERE ID_Buku = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tambahkan metode ini di akhir class
    public List<Buku> getBukuTersedia() {
        List<Buku> list = new ArrayList<>();
        String sql = "SELECT ID_Buku, Judul, Pengarang, Penerbit, TahunTerbit " +
                     "FROM Buku " +
                     "WHERE ID_Buku NOT IN (SELECT ID_Buku FROM Pinjaman WHERE Status = 'Dipinjam')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Buku b = new Buku(
                    rs.getInt("ID_Buku"),
                    rs.getString("Judul"),
                    rs.getString("Pengarang"),
                    rs.getString("Penerbit"),
                    rs.getInt("TahunTerbit")
                );
                list.add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
