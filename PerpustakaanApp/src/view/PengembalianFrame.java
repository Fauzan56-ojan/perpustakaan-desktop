package view;

import dao.PengembalianDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.swing.table.DefaultTableCellRenderer;

public class PengembalianFrame extends JFrame {
    private JTextField tfIdAnggota;
    private JButton btnCari;
    private JButton btnLihatSemua;
    private JTable tblPinjaman;
    private JButton btnKembalikan;
    private DefaultTableModel tableModel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public PengembalianFrame() {
        initializeUI();
        setupEventHandlers();
    }

    private void initializeUI() {
        setTitle("Pengembalian Buku - Sistem Perpustakaan");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 245, 245));

        // Create main container with padding
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 245));
        add(mainPanel, BorderLayout.CENTER);

        // Top Panel - Search
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), "Pencarian Pinjaman Aktif"),
                BorderFactory.createEmptyBorder(5, 5, 15, 5)));
        topPanel.setBackground(new Color(245, 245, 245));
        topPanel.setOpaque(true);

        JLabel lblIdAnggota = new JLabel("ID Anggota:");
        lblIdAnggota.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        tfIdAnggota = new JTextField(15);
        tfIdAnggota.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfIdAnggota.setMaximumSize(new Dimension(200, 30));
        
        btnCari = createStyledButton("Cari Pinjaman", new Color(70, 130, 180));
        btnLihatSemua = createStyledButton("Lihat Semua", new Color(100, 149, 237));

        topPanel.add(lblIdAnggota);
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        topPanel.add(tfIdAnggota);
        topPanel.add(Box.createRigidArea(new Dimension(15, 0)));
        topPanel.add(btnCari);
        topPanel.add(Box.createRigidArea(new Dimension(10, 0)));
        topPanel.add(btnLihatSemua);

        // Table Panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(5, 0, 5, 0),
                BorderFactory.createLineBorder(new Color(200, 200, 200))));
        tablePanel.setBackground(Color.WHITE);

        String[] columnNames = {"ID Pinjam", "Judul Buku", "Tanggal Pinjam", "Lama Pinjam (hari)", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0:
                        return Integer.class;
                    case 3:
                        return Integer.class;
                    default:
                        return String.class;
                }
            }
        };

        tblPinjaman = new JTable(tableModel);
        tblPinjaman.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblPinjaman.setAutoCreateRowSorter(true);
        tblPinjaman.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tblPinjaman.setRowHeight(25);
        tblPinjaman.setShowGrid(true);
        tblPinjaman.setGridColor(new Color(240, 240, 240));
        tblPinjaman.setIntercellSpacing(new Dimension(0, 0));
        tblPinjaman.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tblPinjaman.getTableHeader().setBackground(new Color(70, 130, 180));
        tblPinjaman.getTableHeader().setForeground(Color.WHITE);
        tblPinjaman.getTableHeader().setPreferredSize(new Dimension(0, 35));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        tblPinjaman.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID Pinjam
        tblPinjaman.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); // Tanggal Pinjam
        tblPinjaman.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); // Lama Pinjam
        tblPinjaman.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Status


        JScrollPane scrollPane = new JScrollPane(tblPinjaman);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // Bottom Panel - Action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.setBackground(new Color(245, 245, 245));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        btnKembalikan = createStyledButton("Kembalikan Buku", new Color(46, 139, 87));
        btnKembalikan.setEnabled(false);
        bottomPanel.add(btnKembalikan);

        // Add components to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bgColor.darker(), 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.brighter());
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }

    private void setupEventHandlers() {
        btnCari.addActionListener(e -> cariPinjamanAktif());
        btnLihatSemua.addActionListener(e -> tampilkanSemuaPinjamanDipinjam());
        btnKembalikan.addActionListener(e -> kembalikanBuku());
        tblPinjaman.getSelectionModel().addListSelectionListener(e ->
            btnKembalikan.setEnabled(tblPinjaman.getSelectedRow() != -1));
    }

    private void cariPinjamanAktif() {
        try {
            int idAnggota = Integer.parseInt(tfIdAnggota.getText().trim());
            tableModel.setRowCount(0);

            List<Map<String, Object>> pinjamanList = PengembalianDAO.getDaftarPinjamanAktif(idAnggota);

            if (pinjamanList.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Tidak ada pinjaman aktif untuk anggota ID: " + idAnggota,
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Map<String, Object> pinjaman : pinjamanList) {
                    Object[] row = {
                        pinjaman.get("idPinjam"),
                        pinjaman.get("judul"),
                        formatDate(pinjaman.get("tglPinjam")),
                        pinjaman.get("lamaPinjam"),
                        pinjaman.get("status")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (NumberFormatException ex) {
            showErrorDialog("ID Anggota harus berupa angka");
        } catch (Exception ex) {
            showErrorDialog("Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void tampilkanSemuaPinjamanDipinjam() {
        try {
            tableModel.setRowCount(0);
            List<Map<String, Object>> pinjamanList = PengembalianDAO.getSemuaPinjamanDipinjam();

            if (pinjamanList.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "Tidak ada pinjaman aktif saat ini.",
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                for (Map<String, Object> pinjaman : pinjamanList) {
                    Object[] row = {
                        pinjaman.get("idPinjam"),
                        pinjaman.get("judul"),
                        formatDate(pinjaman.get("tglPinjam")),
                        pinjaman.get("lamaPinjam"),
                        pinjaman.get("status")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (Exception ex) {
            showErrorDialog("Gagal menampilkan data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void kembalikanBuku() {
        int selectedRow = tblPinjaman.getSelectedRow();
        if (selectedRow == -1) {
            showWarningDialog("Pilih pinjaman yang akan dikembalikan");
            return;
        }

        int idPinjam = (int) tableModel.getValueAt(selectedRow, 0);
        String judulBuku = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showOptionDialog(this,
                "<html><div style='font-size:14px;'>"
                + "<b>Konfirmasi pengembalian buku:</b><br><br>"
                + "<b>Judul:</b> " + judulBuku + "<br>"
                + "<b>ID Pinjam:</b> " + idPinjam + "</div></html>",
                "Konfirmasi Pengembalian",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Kembalikan", "Batal"},
                "Batal");

        if (confirm == JOptionPane.YES_OPTION) {
            String result = PengembalianDAO.kembalikanBuku(idPinjam);
            JOptionPane.showMessageDialog(this, result, "Hasil Pengembalian", JOptionPane.INFORMATION_MESSAGE);
            tampilkanSemuaPinjamanDipinjam(); // Refresh tabel
        }
    }

    private String formatDate(Object date) {
        if (date instanceof java.sql.Date) {
            return dateFormat.format((java.sql.Date) date);
        } else if (date instanceof java.util.Date) {
            return dateFormat.format((java.util.Date) date);
        }
        return String.valueOf(date);
    }

    private void showErrorDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showWarningDialog(String message) {
        JOptionPane.showMessageDialog(this, message, "Peringatan", JOptionPane.WARNING_MESSAGE);
    }
}