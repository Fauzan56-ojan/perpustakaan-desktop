package view;

import dao.StatistikDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

public class StatistikFrame extends JFrame {

    private JPanel lblBuku, lblAnggota, lblPinjam, lblKembali, lblRataRata;
    private JTable tableBulan, tableAktif;
    private JPanel statsPanel, tablesPanel;

    public StatistikFrame() {
        setTitle("Laporan Perpustakaan");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));
        
        // Set rounded frame shape (optional)
        //setUndecorated(true);
        //setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(70, 130, 180));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setLayout(new BorderLayout());
        
        JLabel titleLabel = new JLabel("Statistik Perpustakaan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Close button
        JButton closeButton = new RoundedButton("×");
        closeButton.setFont(new Font("Arial", Font.BOLD, 18));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(70, 130, 180));
        closeButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());
        
        add(headerPanel, BorderLayout.NORTH);

        // Main Content Panel
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // Statistics Cards Panel
        statsPanel = new JPanel(new GridLayout(1, 5, 15, 15));
        statsPanel.setBorder(new EmptyBorder(0, 0, 20, 0));
        
        lblBuku = createStatCard("", "Total Buku", "0");
        lblAnggota = createStatCard("", "Total Anggota", "0");
        lblPinjam = createStatCard("", "Dipinjam", "0");
        lblKembali = createStatCard("", "<html>Total Sudah<br>Dikembalikan</html>", "0");
        lblRataRata = createStatCard("", "<html><center>Rata-rata<br>Lama Peminjaman</center></html>", "0");
        
        statsPanel.add(lblBuku);
        statsPanel.add(lblAnggota);
        statsPanel.add(lblPinjam);
        statsPanel.add(lblKembali);
        statsPanel.add(lblRataRata);
        
        mainPanel.add(statsPanel, BorderLayout.NORTH);

        // Tables Panel
        tablesPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        
        // Monthly Statistics Table
        JPanel monthlyPanel = createTablePanel("Statistik Peminjaman Per Bulan", new String[]{"Bulan", "Jumlah"});
        tableBulan = (JTable) ((JScrollPane) monthlyPanel.getComponent(1)).getViewport().getView();
        
        // Active Borrowers Table
        JPanel activePanel = createTablePanel(" Peminjam Paling Aktif", new String[]{"Nama", "Jumlah Pinjam"});
        tableAktif = (JTable) ((JScrollPane) activePanel.getComponent(1)).getViewport().getView();
        
        tablesPanel.add(monthlyPanel);
        tablesPanel.add(activePanel);
        
        mainPanel.add(tablesPanel, BorderLayout.CENTER);

        // Refresh Button
        JButton btnRefresh = new JButton("Refresh Data");
        btnRefresh.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnRefresh.setBackground(new Color(70, 130, 180));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFocusPainted(false);
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnRefresh.addActionListener(e -> loadStatistik());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.add(btnRefresh);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        loadStatistik();
    }

    private JPanel createStatCard(String icon, String title, String value) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(5, 5));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JPanel content = new JPanel(new BorderLayout(5, 5));
        content.add(iconLabel, BorderLayout.NORTH);
        content.add(titleLabel, BorderLayout.CENTER);
        content.add(valueLabel, BorderLayout.SOUTH);
        content.setOpaque(false);
        
        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private JPanel createTablePanel(String title, String[] columns) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        panel.add(titleLabel, BorderLayout.NORTH);

        JTable table = new JTable();
        table.setRowHeight(25);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void loadStatistik() {
        ((JLabel) ((JPanel) lblBuku.getComponent(0)).getComponent(2))
                .setText(String.valueOf(StatistikDAO.getJumlah("Buku")));
        ((JLabel) ((JPanel) lblAnggota.getComponent(0)).getComponent(2))
                .setText(String.valueOf(StatistikDAO.getJumlah("Anggota")));
        ((JLabel) ((JPanel) lblPinjam.getComponent(0)).getComponent(2))
                .setText(String.valueOf(StatistikDAO.getJumlahPinjamanAktif()));
        ((JLabel) ((JPanel) lblKembali.getComponent(0)).getComponent(2))
                .setText(String.valueOf(StatistikDAO.getJumlahSudahKembali()));
        ((JLabel) ((JPanel) lblRataRata.getComponent(0)).getComponent(2))
                .setText(Math.round(StatistikDAO.getRataRataHariPeminjaman()) + " Hari");

        List<String[]> dataBulan = StatistikDAO.getStatistikPerBulan();
        DefaultTableModel modelBulan = new DefaultTableModel(new String[]{"Bulan", "Jumlah"}, 0);
        for (String[] row : dataBulan) modelBulan.addRow(row);
        tableBulan.setModel(modelBulan);

        List<String[]> dataAktif = StatistikDAO.getPeminjamPalingAktif();
        DefaultTableModel modelAktif = new DefaultTableModel(new String[]{"Nama", "Jumlah Pinjam"}, 0);
        for (String[] row : dataAktif) modelAktif.addRow(row);
        tableAktif.setModel(modelAktif);
        // Renderer untuk rata tengah
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        // Renderer untuk rata kiri
        DefaultTableCellRenderer leftRenderer = new DefaultTableCellRenderer();
        leftRenderer.setHorizontalAlignment(JLabel.LEFT);

        // Tabel Statistik Per Bulan
        tableBulan.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);  // Bulan
        tableBulan.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Jumlah

        // Tabel Peminjam Paling Aktif
        tableAktif.getColumnModel().getColumn(0).setCellRenderer(leftRenderer);  // Nama
        tableAktif.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); // Jumlah Pinjam

    }

    static class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isPressed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                g2.setColor(getBackground().brighter());
            } else {
                g2.setColor(getBackground());
            }

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();

            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            StatistikFrame frame = new StatistikFrame();
            frame.setVisible(true);
        });
    }
}
