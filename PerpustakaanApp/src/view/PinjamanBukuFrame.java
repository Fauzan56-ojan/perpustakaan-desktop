package view;

import dao.AnggotaDAO;
import dao.BukuDAO;
import dao.PinjamanDAO;
import model.Anggota;
import model.Buku;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PinjamanBukuFrame extends JFrame {
    private JComboBox<Anggota> cbAnggota;
    private JComboBox<Buku> cbBuku;
    private JButton btnPinjam;

    public PinjamanBukuFrame() {
        setTitle("Form Peminjaman Buku - Perpustakaan Digital");
        setSize(700, 380);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        try {
            setIconImage(new ImageIcon(getClass().getResource("/images/library-icon.png")).getImage());
        } catch (Exception e) {
            System.out.println("Icon not found");
        }

        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, new Color(52, 152, 219), getWidth(), getHeight(), new Color(41, 128, 185));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel();
        headerPanel.setOpaque(false);
        JLabel titleLabel = new JLabel("FORM PEMINJAMAN BUKU");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);

        JPanel cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(new Color(255, 255, 255, 200));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 150)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.WEST;

        AnggotaDAO anggotaDAO = new AnggotaDAO();
        BukuDAO bukuDAO = new BukuDAO();
        List<Anggota> anggotaList = anggotaDAO.getAll();
        List<Buku> bukuList = bukuDAO.getBukuTersedia();

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel anggotaLabel = new JLabel("Pilih Anggota:");
        anggotaLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cardPanel.add(anggotaLabel, gbc);

        gbc.gridx = 1; gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        cbAnggota = new JComboBox<>(new DefaultComboBoxModel<>(anggotaList.toArray(new Anggota[0])));
        cbAnggota.setRenderer(new AnggotaComboBoxRenderer());
        cbAnggota.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbAnggota.setBackground(Color.WHITE);
        cbAnggota.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        cardPanel.add(cbAnggota, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.weightx = 0;
        JLabel bukuLabel = new JLabel("Pilih Buku:");
        bukuLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cardPanel.add(bukuLabel, gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0;
        cbBuku = new JComboBox<>(new DefaultComboBoxModel<>(bukuList.toArray(new Buku[0])));
        cbBuku.setRenderer(new BukuComboBoxRenderer());
        cbBuku.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        cbBuku.setBackground(Color.WHITE);
        cbBuku.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        cardPanel.add(cbBuku, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        btnPinjam = new JButton("PINJAM BUKU");
        btnPinjam.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnPinjam.setForeground(Color.WHITE);
        btnPinjam.setBackground(new Color(46, 204, 113));
        btnPinjam.setFocusPainted(false);
        btnPinjam.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        btnPinjam.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnPinjam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnPinjam.setBackground(new Color(39, 174, 96));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnPinjam.setBackground(new Color(46, 204, 113));
            }
        });

        cardPanel.add(btnPinjam, gbc);
        formPanel.add(cardPanel);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        btnPinjam.addActionListener(e -> pinjamBuku());
    }

    private void pinjamBuku() {
        Anggota selectedAnggota = (Anggota) cbAnggota.getSelectedItem();
        Buku selectedBuku = (Buku) cbBuku.getSelectedItem();

        if (selectedAnggota == null || selectedBuku == null) {
            JOptionPane.showMessageDialog(this,
                "Harap pilih anggota dan buku terlebih dahulu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        JPanel confirmPanel = new JPanel(new BorderLayout(10, 10));
        confirmPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel iconLabel = new JLabel(new ImageIcon(getClass().getResource("/images/confirm-icon.png")));
        confirmPanel.add(iconLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel(new GridLayout(0, 1));
        JLabel confirmLabel = new JLabel("Konfirmasi Peminjaman Buku");
        confirmLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        textPanel.add(confirmLabel);
        textPanel.add(new JLabel(" "));
        JLabel detailLabel1 = new JLabel("Anggota: " + selectedAnggota.getNama() + " (ID: " + selectedAnggota.getId() + ")");
        detailLabel1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textPanel.add(detailLabel1);
        JLabel detailLabel2 = new JLabel("Buku: " + selectedBuku.getJudul() + " - " + selectedBuku.getPengarang());
        detailLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textPanel.add(detailLabel2);
        confirmPanel.add(textPanel, BorderLayout.CENTER);

        int confirm = JOptionPane.showConfirmDialog(
            this,
            confirmPanel,
            "Konfirmasi Peminjaman",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.PLAIN_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            String hasil = PinjamanDAO.pinjamBuku(
                selectedAnggota.getId(),
                selectedBuku.getId());

            JPanel successPanel = new JPanel(new BorderLayout(10, 10));
            successPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            JLabel successIcon = new JLabel(new ImageIcon(getClass().getResource("/images/success-icon.png")));
            successPanel.add(successIcon, BorderLayout.WEST);
            JLabel successLabel = new JLabel(hasil);
            successLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            successPanel.add(successLabel, BorderLayout.CENTER);
            JOptionPane.showMessageDialog(this, successPanel, "Sukses", JOptionPane.PLAIN_MESSAGE);

            cbBuku.setModel(new DefaultComboBoxModel<>(
                new BukuDAO().getBukuTersedia().toArray(new Buku[0])));
        }
    }

    private class AnggotaComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Anggota) {
                Anggota a = (Anggota) value;
                setText(a.getNama() + " (ID: " + a.getId() + ")");
                setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
            if (isSelected) {
                setBackground(new Color(52, 152, 219));
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            return this;
        }
    }

    private class BukuComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Buku) {
                Buku b = (Buku) value;
                setText(b.getJudul() + " - " + b.getPengarang() + " (ID: " + b.getId() + ")");
                setFont(new Font("Segoe UI", Font.PLAIN, 14));
            }
            if (isSelected) {
                setBackground(new Color(52, 152, 219));
                setForeground(Color.WHITE);
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }
            return this;
        }
    }
}
