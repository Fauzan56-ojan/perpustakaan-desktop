package view;

import dao.AnggotaDAO;
import model.Anggota;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

public class AnggotaFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private AnggotaDAO anggotaDAO;
    private Color primaryColor = new Color(42, 128, 185);
    private Color backgroundColor = new Color(236, 240, 241);

    public AnggotaFrame() {
        setTitle("Manajemen Anggota Perpustakaan");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        anggotaDAO = new AnggotaDAO();

        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("DATA ANGGOTA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // ===== MAIN CONTENT =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // ===== TOOLBAR =====
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbarPanel.setBackground(backgroundColor);

        JButton btnTambah = createStyledButton("Tambah Anggota", new Color(46, 204, 113));
        JButton btnEdit = createStyledButton("Edit Data", new Color(52, 152, 219));
        JButton btnHapus = createStyledButton("Hapus Data", new Color(231, 76, 60));
        JButton btnRefresh = createStyledButton("Refresh", new Color(241, 196, 15));

        toolbarPanel.add(btnTambah);
        toolbarPanel.add(btnEdit);
        toolbarPanel.add(btnHapus);
        toolbarPanel.add(btnRefresh);

        // ===== SEARCH PANEL =====
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setBackground(backgroundColor);
        
        JTextField searchField = new JTextField(13);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JButton btnSearch = createStyledButton("Cari", primaryColor);

        searchPanel.add(new JLabel("Pencarian (NAMA) :"));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);

        // Combine toolbar and search
        JPanel topControlPanel = new JPanel(new BorderLayout());
        topControlPanel.setBackground(backgroundColor);
        topControlPanel.add(toolbarPanel, BorderLayout.WEST);
        topControlPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(topControlPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "NAMA", "ALAMAT", "NO. TELEPON", "EMAIL", "TGL BERGABUNG"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // Kolom ID
        table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); 

        // Custom renderer for alternate row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, 
                       isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                }
                return c;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== STATUS BAR =====
        JLabel statusLabel = new JLabel(" Total data: 0");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        statusPanel.add(statusLabel);
        
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Load initial data
        loadData();
        updateStatusBar();

        // ===== ACTION LISTENERS =====
        btnTambah.addActionListener(e -> showTambahDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnHapus.addActionListener(e -> hapusData());
        btnRefresh.addActionListener(e -> {
            loadData();
            updateStatusBar();
        });
        btnSearch.addActionListener(e -> searchData(searchField.getText()));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<Anggota> list = anggotaDAO.getAll();
        for (Anggota a : list) {
            tableModel.addRow(new Object[]{
                a.getId(), 
                a.getNama(), 
                a.getAlamat(), 
                a.getNoTelepon(), 
                a.getEmail(), 
                a.getTanggalBergabung()
            });
        }
        updateStatusBar();
    }

    private void searchData(String keyword) {
        tableModel.setRowCount(0);
        List<Anggota> list = anggotaDAO.getAll(); // In practice, you should create a search method in DAO
        for (Anggota a : list) {
            if (a.getNama().toLowerCase().contains(keyword.toLowerCase()) ||
                a.getEmail().toLowerCase().contains(keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    a.getId(), 
                    a.getNama(), 
                    a.getAlamat(), 
                    a.getNoTelepon(), 
                    a.getEmail(), 
                    a.getTanggalBergabung()
                });
            }
        }
        updateStatusBar();
    }

   private void updateStatusBar() {
    int count = tableModel.getRowCount();
    
    // Find the status label in a more reliable way
    Component[] mainComponents = getContentPane().getComponents();
    for (Component mainComp : mainComponents) {
        if (mainComp instanceof JPanel) {
            JPanel mainPanel = (JPanel) mainComp;
            Component[] panelComponents = mainPanel.getComponents();
            for (Component panelComp : panelComponents) {
                if (panelComp instanceof JPanel) {
                    JPanel statusPanel = (JPanel) panelComp;
                    if (statusPanel.getComponentCount() > 0 && 
                        statusPanel.getComponent(0) instanceof JLabel) {
                        ((JLabel)statusPanel.getComponent(0)).setText(" Total data: " + count);
                        return;
                    }
                }
            }
        }
    }
}
     

    private void showTambahDialog() {
        // Create custom dialog
        JDialog dialog = new JDialog(this, "Tambah Anggota Baru", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = new JLabel("FORM TAMBAH ANGGOTA");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(primaryColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblTitle, gbc);

        JTextField[] fields = new JTextField[4];
        String[] labels = {"Nama Lengkap", "Alamat", "No. Telepon", "Email"};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i+1; gbc.gridwidth = 1;
            JLabel label = new JLabel(labels[i] + ":");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            formPanel.add(label, gbc);

            gbc.gridx = 1;
            fields[i] = new JTextField(20);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            formPanel.add(fields[i], gbc);
        }

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnSimpan = createStyledButton("Simpan", new Color(46, 204, 113));
        JButton btnBatal = createStyledButton("Batal", new Color(231, 76, 60));

        buttonPanel.add(btnBatal);
        buttonPanel.add(btnSimpan);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnBatal.addActionListener(e -> dialog.dispose());
        btnSimpan.addActionListener(e -> {
            Anggota a = new Anggota();
            a.setNama(fields[0].getText());
            a.setAlamat(fields[1].getText());
            a.setNoTelepon(fields[2].getText());
            a.setEmail(fields[3].getText());

            anggotaDAO.insert(a);
            loadData();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Data anggota berhasil ditambahkan!", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        });

        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data anggota yang akan diedit!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        Anggota anggota = anggotaDAO.getAll().stream()
            .filter(a -> a.getId() == id)
            .findFirst()
            .orElse(null);

        if (anggota == null) return;

        // Create custom dialog
        JDialog dialog = new JDialog(this, "Edit Data Anggota", true);
        dialog.setSize(500, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = new JLabel("EDIT DATA ANGGOTA");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(primaryColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblTitle, gbc);

        JTextField[] fields = new JTextField[4];
        String[] labels = {"Nama Lengkap", "Alamat", "No. Telepon", "Email"};
        String[] values = {
            anggota.getNama(),
            anggota.getAlamat(),
            anggota.getNoTelepon(),
            anggota.getEmail()
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i+1; gbc.gridwidth = 1;
            JLabel label = new JLabel(labels[i] + ":");
            label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            formPanel.add(label, gbc);

            gbc.gridx = 1;
            fields[i] = new JTextField(values[i], 20);
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            formPanel.add(fields[i], gbc);
        }

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnSimpan = createStyledButton("Simpan Perubahan", new Color(52, 152, 219));
        JButton btnBatal = createStyledButton("Batal", new Color(231, 76, 60));

        buttonPanel.add(btnBatal);
        buttonPanel.add(btnSimpan);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnBatal.addActionListener(e -> dialog.dispose());
        btnSimpan.addActionListener(e -> {
            anggota.setNama(fields[0].getText());
            anggota.setAlamat(fields[1].getText());
            anggota.setNoTelepon(fields[2].getText());
            anggota.setEmail(fields[3].getText());

            anggotaDAO.update(anggota);
            loadData();
            dialog.dispose();
            JOptionPane.showMessageDialog(this, "Data anggota berhasil diperbarui!", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        });

        dialog.setVisible(true);
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data anggota yang akan dihapus!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus data ini?\n" +
            "ID: " + id + "\n" +
            "Nama: " + table.getValueAt(row, 1),
            "Konfirmasi Hapus Data",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            anggotaDAO.delete(id);
            loadData();
            JOptionPane.showMessageDialog(this, "Data anggota berhasil dihapus!", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}