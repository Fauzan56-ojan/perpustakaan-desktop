package dao;

import model.Anggota;
import util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnggotaDAO {

    public List<Anggota> getAll() {
        List<Anggota> list = new ArrayList<>();
        String sql = "SELECT * FROM Anggota ORDER BY Nama";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Anggota a = new Anggota(
                    rs.getInt("ID_Anggota"),
                    rs.getString("Nama"),
                    rs.getString("Alamat"),
                    rs.getString("NoTelepon"),
                    rs.getString("Email"),
                    rs.getString("TanggalBergabung")
                );
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void insert(Anggota a) {
        String sql = "INSERT INTO Anggota (Nama, Alamat, NoTelepon, Email, TanggalBergabung) VALUES (?, ?, ?, ?, GETDATE())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getNama());
            stmt.setString(2, a.getAlamat());
            stmt.setString(3, a.getNoTelepon());
            stmt.setString(4, a.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(Anggota a) {
        String sql = "UPDATE Anggota SET Nama=?, Alamat=?, NoTelepon=?, Email=? WHERE ID_Anggota=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, a.getNama());
            stmt.setString(2, a.getAlamat());
            stmt.setString(3, a.getNoTelepon());
            stmt.setString(4, a.getEmail());
            stmt.setInt(5, a.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM Anggota WHERE ID_Anggota=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
