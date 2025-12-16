package view;

import util.DBConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class MainDashboard extends JFrame {
    // Warna modern
    private final Color WARNA_PRIMER = new Color(0, 105, 180);  // Biru tua profesional
    private final Color WARNA_SEKUNDER = new Color(255, 183, 0);  // Kuning emas
    private final Color WARNA_LATAR = new Color(248, 249, 250);  // Abu-abu sangat muda
    private final Color WARNA_KARTU = Color.WHITE;
    private final Color WARNA_SIDEBAR = new Color(33, 37, 41);  // Hitam kebiruan
    private final Color WARNA_MENU_HOVER = new Color(52, 58, 64);
    private final Color WARNA_TEKS_SIDEBAR = new Color(200, 200, 200);
    
    public MainDashboard() {
        setTitle("Sistem Manajemen Perpustakaan");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // === Header ===
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(WARNA_PRIMER);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 70));

        // Judul dengan gradient
        JLabel titleLabel = new JLabel("SISTEM PERPUSTAKAAN DIGITAL");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        
        // Info pengguna
        JLabel userLabel = new JLabel(" ");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(200, 200, 200));
        
        // Panel untuk elemen kanan header
        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightHeaderPanel.setOpaque(false);
        rightHeaderPanel.add(userLabel);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // === Panel Utama ===
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(WARNA_LATAR);

        // === Sidebar ===
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setPreferredSize(new Dimension(280, 0));
        sidebarPanel.setBackground(WARNA_SIDEBAR);
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Logo sidebar
        JPanel logoPanel = new JPanel();
        logoPanel.setBackground(new Color(28, 32, 36));
        logoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel logoLabel = new JLabel("MENU UTAMA");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        logoLabel.setForeground(WARNA_SEKUNDER);
        logoPanel.add(logoLabel);
        sidebarPanel.add(logoPanel);

        // Menu sidebar
        String[] menuItems = {"Data Buku", "Data Anggota", "Daftar Pinjaman", "Pinjam Buku", "Kembalikan Buku", "Laporan"};
        for (String item : menuItems) {
            JButton menuButton = createMenuButton(item);
            menuButton.addActionListener(new MenuButtonListener());
            sidebarPanel.add(menuButton);
            sidebarPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        sidebarPanel.add(Box.createVerticalGlue());
        
        // Footer sidebar
        JPanel sidebarFooter = new JPanel();
        sidebarFooter.setBackground(new Color(28, 32, 36));
        sidebarFooter.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        JLabel versionLabel = new JLabel("Versi 2.0.1");
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        versionLabel.setForeground(WARNA_TEKS_SIDEBAR);
        sidebarFooter.add(versionLabel);
        sidebarPanel.add(sidebarFooter);
        
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        // === Konten Dashboard ===
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(WARNA_LATAR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Panel selamat datang
        JPanel welcomePanel = createDashboardCard("Selamat Datang", "Sistem Manajemen Perpustakaan Digital");
        contentPanel.add(welcomePanel, gbc);

        gbc.gridwidth = 1;
        gbc.weightx = 0.33;  // Membagi menjadi 3 kolom sama lebar

        // Ambil data dari DB
        String totalBuku = getJumlah("SELECT COUNT(*) FROM Buku");
        String totalAnggota = getJumlah("SELECT COUNT(*) FROM Anggota");
        String totalPinjaman = getJumlah("SELECT COUNT(*) FROM Pinjaman WHERE status = 'Dipinjam'");

        // Kartu statistik
        JPanel booksPanel = createStatsCard("Total Buku", totalBuku, "📚", WARNA_PRIMER);
        contentPanel.add(booksPanel, gbc);

        JPanel membersPanel = createStatsCard("Anggota", totalAnggota, "👥", new Color(40, 167, 69));
        contentPanel.add(membersPanel, gbc);

        JPanel loansPanel = createStatsCard("Pinjaman Aktif", totalPinjaman, "📖", new Color(220, 53, 69));
        contentPanel.add(loansPanel, gbc);

        // Baris kedua untuk statistik tambahan (jika ada)
        gbc.gridy = 2;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
        // Panel aktivitas terakhir
        JPanel activityPanel = createActivityPanel();
        contentPanel.add(activityPanel, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // === Footer ===
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(WARNA_SIDEBAR);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(12, 0, 12, 0));

        JLabel footerLabel = new JLabel("© 2023 Perpustakaan Digital - Hak Cipta Dilindungi");
        footerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerLabel.setForeground(WARNA_TEKS_SIDEBAR);
        footerPanel.add(footerLabel);

        add(footerPanel, BorderLayout.SOUTH);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(WARNA_SIDEBAR);
        button.setForeground(WARNA_TEKS_SIDEBAR);
        button.setBorder(BorderFactory.createEmptyBorder(12, 25, 12, 25));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(WARNA_MENU_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(WARNA_SIDEBAR);
            }
        });
        
        return button;
    }

    private JPanel createDashboardCard(String title, String content) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WARNA_KARTU);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 230)),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        // Shadow effect
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 0, 0, new Color(0,0,0,0)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(60, 60, 60));

        JLabel contentLabel = new JLabel(content);
        contentLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentLabel.setForeground(new Color(100, 100, 100));

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(contentLabel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createStatsCard(String title, String value, String icon, Color accentColor) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WARNA_KARTU);
        
        // Border dengan aksen warna
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 4, 0, 0, accentColor),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JPanel iconPanel = new JPanel();
        iconPanel.setBackground(new Color(accentColor.getRed(), accentColor.getGreen(), accentColor.getBlue(), 20));
        iconPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        iconPanel.setLayout(new BorderLayout());
        
        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        iconLabel.setForeground(accentColor);
        iconPanel.add(iconLabel, BorderLayout.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(new Color(120, 120, 120));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(new Color(60, 60, 60));

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.add(titleLabel);
        textPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        textPanel.add(valueLabel);
        
        JPanel contentPanel = new JPanel(new BorderLayout(15, 0));
        contentPanel.setOpaque(false);
        contentPanel.add(iconPanel, BorderLayout.WEST);
        contentPanel.add(textPanel, BorderLayout.CENTER);
        
        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createActivityPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(WARNA_KARTU);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel titleLabel = new JLabel("Aktivitas Terakhir");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(60, 60, 60));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Dummy data aktivitas
        String[] activities = {
            "Buku 'Pemrograman Java' dipinjam oleh Andi",
            "Buku 'Struktur Data' dikembalikan oleh Budi",
            "Anggota baru 'Citra' terdaftar",
            "Buku 'Algoritma' diperbarui stoknya"
        };

        JPanel activityList = new JPanel();
        activityList.setLayout(new BoxLayout(activityList, BoxLayout.Y_AXIS));
        activityList.setOpaque(false);

        for (String activity : activities) {
            JPanel activityItem = new JPanel(new FlowLayout(FlowLayout.LEFT));
            activityItem.setOpaque(false);
            activityItem.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
            
            JLabel bullet = new JLabel("• ");
            bullet.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            bullet.setForeground(WARNA_PRIMER);
            
            JLabel activityLabel = new JLabel(activity);
            activityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            activityLabel.setForeground(new Color(100, 100, 100));
            
            activityItem.add(bullet);
            activityItem.add(activityLabel);
            activityList.add(activityItem);
        }

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(new JScrollPane(activityList), BorderLayout.CENTER);

        return panel;
    }

    private String getJumlah(String query) {
        String jumlah = "0";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                jumlah = String.format("%,d", rs.getInt(1));  // Format dengan separator ribuan
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jumlah;
    }

    private class MenuButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton source = (JButton) e.getSource();
            String text = source.getText();
            switch (text) {
                case "Data Buku": new BukuFrame().setVisible(true); break;
                case "Data Anggota": new AnggotaFrame().setVisible(true); break;
                case "Daftar Pinjaman": new DaftarPinjamanFrame().setVisible(true); break;
                case "Pinjam Buku": new PinjamanBukuFrame().setVisible(true); break;
                case "Kembalikan Buku": new PengembalianFrame().setVisible(true); break;
                case "Laporan": new StatistikFrame().setVisible(true); break;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set look and feel untuk tampilan modern
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Custom UI improvements
                UIManager.put("Button.arc", 10);
                UIManager.put("Component.arc", 10);
                UIManager.put("ProgressBar.arc", 10);
                UIManager.put("TextComponent.arc", 10);
                
                // Set font default
                Font defaultFont = new Font("Segoe UI", Font.PLAIN, 14);
                UIManager.put("Button.font", defaultFont);
                UIManager.put("Label.font", defaultFont);
                UIManager.put("TextField.font", defaultFont);
            } catch (Exception e) {
                e.printStackTrace();
            }

            new MainDashboard().setVisible(true);
        });
    }
}