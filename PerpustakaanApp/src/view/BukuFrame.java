package view;

import dao.BukuDAO;
import model.Buku;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

public class BukuFrame extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;
    private BukuDAO bukuDAO;
    private Color primaryColor = new Color(58, 83, 155);
    private Color backgroundColor = new Color(246, 247, 251);
    private Color successColor = new Color(46, 204, 113);
    private Color dangerColor = new Color(231, 76, 60);

    public BukuFrame() {
        setTitle("Manajemen Koleksi Buku Perpustakaan");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        bukuDAO = new BukuDAO();

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titleLabel = new JLabel("KOLEKSI BUKU PERPUSTAKAAN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        add(headerPanel, BorderLayout.NORTH);

        // Main Content
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Toolbar
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        toolbarPanel.setBackground(backgroundColor);

        JButton btnTambah = createStyledButton("Tambah Buku", successColor);
        JButton btnEdit = createStyledButton("Edit Buku", new Color(52, 152, 219));
        JButton btnHapus = createStyledButton("Hapus Buku", dangerColor);
        JButton btnRefresh = createStyledButton("Refresh", new Color(241, 196, 15));

        toolbarPanel.add(btnTambah);
        toolbarPanel.add(btnEdit);
        toolbarPanel.add(btnHapus);
        toolbarPanel.add(btnRefresh);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        searchPanel.setBackground(backgroundColor);
        
        JTextField searchField = new JTextField(18);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton btnSearch = createStyledButton("Cari", primaryColor);
        btnSearch.setPreferredSize(new Dimension(80, 30));

        searchPanel.add(new JLabel("Pencarian:"));
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);

        // Combine toolbar and search
        JPanel topControlPanel = new JPanel(new BorderLayout());
        topControlPanel.setBackground(backgroundColor);
        topControlPanel.add(toolbarPanel, BorderLayout.WEST);
        topControlPanel.add(searchPanel, BorderLayout.EAST);

        mainPanel.add(topControlPanel, BorderLayout.NORTH);

        // Table
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "JUDUL", "PENGARANG", "PENERBIT", "TAHUN TERBIT"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) return Integer.class;
                return String.class;
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(32);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(150);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
       


        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Status Bar
        JLabel statusLabel = new JLabel(" Total buku: 0");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        statusPanel.add(statusLabel);
        
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Load initial data
        loadData();

        // Action Listeners
        btnTambah.addActionListener(e -> showTambahDialog());
        btnEdit.addActionListener(e -> showEditDialog());
        btnHapus.addActionListener(e -> hapusData());
        btnRefresh.addActionListener(e -> loadData());
        btnSearch.addActionListener(e -> searchData(searchField.getText()));
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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

    private void loadData() {
        tableModel.setRowCount(0);
        List<Buku> list = bukuDAO.getAllBuku();
        for (Buku b : list) {
            tableModel.addRow(new Object[]{
                b.getId(), 
                b.getJudul(), 
                b.getPengarang(), 
                b.getPenerbit(), 
                b.getTahunTerbit()
            });
        }
        updateStatusBar();
    }

    private void searchData(String keyword) {
        tableModel.setRowCount(0);
        List<Buku> list = bukuDAO.getAllBuku();
        for (Buku b : list) {
            if (b.getJudul().toLowerCase().contains(keyword.toLowerCase()) ||
                b.getPengarang().toLowerCase().contains(keyword.toLowerCase()) ||
                b.getPenerbit().toLowerCase().contains(keyword.toLowerCase())) {
                tableModel.addRow(new Object[]{
                    b.getId(), 
                    b.getJudul(), 
                    b.getPengarang(), 
                    b.getPenerbit(), 
                    b.getTahunTerbit()
                });
            }
        }
        updateStatusBar();
    }

    private void updateStatusBar() {
        int total = tableModel.getRowCount();
        
        // Find the status label
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
                            JLabel label = (JLabel)statusPanel.getComponent(0);
                            label.setText(" Total buku: " + total);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void showTambahDialog() {
        JDialog dialog = new JDialog(this, "Tambah Buku Baru", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = new JLabel("FORM TAMBAH BUKU BARU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(primaryColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblTitle, gbc);

        JTextField[] fields = new JTextField[4];
        String[] labels = {"Judul Buku", "Pengarang", "Penerbit", "Tahun Terbit"};

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
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnSimpan = createStyledButton("Simpan", successColor);
        JButton btnBatal = createStyledButton("Batal", dangerColor);

        buttonPanel.add(btnBatal);
        buttonPanel.add(btnSimpan);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnBatal.addActionListener(e -> dialog.dispose());
        btnSimpan.addActionListener(e -> {
            try {
                Buku b = new Buku(
                    fields[0].getText(), 
                    fields[1].getText(), 
                    fields[2].getText(), 
                    Integer.parseInt(fields[3].getText())
                );

                bukuDAO.tambahBuku(b);
                loadData();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Data buku berhasil ditambahkan!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Tahun terbit harus berupa angka!", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void showEditDialog() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data buku yang akan diedit!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        Buku buku = bukuDAO.getAllBuku().stream()
            .filter(b -> b.getId() == id)
            .findFirst()
            .orElse(null);

        if (buku == null) return;

        JDialog dialog = new JDialog(this, "Edit Data Buku", true);
        dialog.setSize(500, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblTitle = new JLabel("EDIT DATA BUKU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitle.setForeground(primaryColor);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblTitle, gbc);

        JTextField[] fields = new JTextField[4];
        String[] labels = {"Judul Buku", "Pengarang", "Penerbit", "Tahun Terbit"};
        String[] values = {
            buku.getJudul(),
            buku.getPengarang(),
            buku.getPenerbit(),
            String.valueOf(buku.getTahunTerbit())
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
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JButton btnSimpan = createStyledButton("Simpan Perubahan", new Color(52, 152, 219));
        JButton btnBatal = createStyledButton("Batal", dangerColor);

        buttonPanel.add(btnBatal);
        buttonPanel.add(btnSimpan);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnBatal.addActionListener(e -> dialog.dispose());
        btnSimpan.addActionListener(e -> {
            try {
                buku.setJudul(fields[0].getText());
                buku.setPengarang(fields[1].getText());
                buku.setPenerbit(fields[2].getText());
                buku.setTahunTerbit(Integer.parseInt(fields[3].getText()));

                
                loadData();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Data buku berhasil diperbarui!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, 
                    "Tahun terbit harus berupa angka!", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void hapusData() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih data buku yang akan dihapus!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = (int) table.getValueAt(row, 0);
        String judul = table.getValueAt(row, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "<html><b>Konfirmasi Penghapusan Buku</b><br><br>" +
            "Anda akan menghapus buku berikut:<br>" +
            "ID: " + id + "<br>" +
            "Judul: " + judul + "<br><br>" +
            "Apakah Anda yakin ingin melanjutkan?</html>", 
            "Konfirmasi Hapus Data",
            JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            bukuDAO.hapusBuku(id);
            loadData();
            JOptionPane.showMessageDialog(this, 
                "Buku \"" + judul + "\" berhasil dihapus!", 
                "Sukses", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            BukuFrame frame = new BukuFrame();
            frame.setVisible(true);
        });
    }
}