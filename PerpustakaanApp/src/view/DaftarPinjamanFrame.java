package view;

import dao.PinjamanDAO;
import model.Pinjaman;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;

public class DaftarPinjamanFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private TableRowSorter<DefaultTableModel> sorter;
    private Color primaryColor = new Color(0, 120, 215); // Modern blue
    private Color accentColor = new Color(46, 204, 113); // Green for status
    private Color backgroundColor = new Color(245, 245, 245); // Light gray

    public DaftarPinjamanFrame() {
        setTitle("Daftar Pinjaman Aktif");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(backgroundColor);
        
        initComponents();
        loadData();
    }

    private void initComponents() {
        // ===== HEADER PANEL =====
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("DAFTAR PINJAMAN AKTIF");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        searchPanel.setOpaque(false);
        
        JTextField searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.WHITE, 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        JButton searchButton = new JButton("Cari");
        searchButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        searchButton.setBackground(Color.WHITE);
        searchButton.setForeground(primaryColor);
        searchButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        searchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // ===== MAIN CONTENT =====
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ===== TOOLBAR =====
        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        toolbarPanel.setBackground(backgroundColor);
        
        JButton refreshButton = createModernButton("Refresh", primaryColor);
        
        
        toolbarPanel.add(refreshButton);
     
        
        mainPanel.add(toolbarPanel, BorderLayout.NORTH);

        // ===== TABLE =====
        model = new DefaultTableModel(
            new Object[]{"ID", "Judul Buku", "Peminjam", "Tanggal Pinjam", "Tenggat Kembali", "Status"}, 
            0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 5 ? Icon.class : Object.class;
            }
        };

        table = new JTable(model);
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        
        // Hide the ID column (column 0)
        table.removeColumn(table.getColumnModel().getColumn(0));
        
        // Custom table styling
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFillsViewportHeight(true);
        
        // Custom renderer for modern look
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                   boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));
                
                if (isSelected) {
                    setBackground(new Color(220, 240, 255));
                    setForeground(Color.BLACK);
                } else {
                    setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 248, 248));
                    setForeground(Color.BLACK);
                }
                
                // Status column styling
                if (column == 4 && value instanceof Icon) { // Now status is column 4 after hiding ID
                    setHorizontalAlignment(JLabel.CENTER);
                } else {
                    setHorizontalAlignment(JLabel.LEFT);
                }
                
                return this;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ===== STATUS BAR =====
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(Color.WHITE);
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));
        
        JLabel statusLabel = new JLabel(" Total pinjaman aktif: 0");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusPanel.add(statusLabel);
        
        mainPanel.add(statusPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // ===== ACTION LISTENERS =====
        refreshButton.addActionListener(e -> loadData());
        searchButton.addActionListener(e -> {
            loadData();
            String text = searchField.getText();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });
        
        
//        printButton.addActionListener(e -> printReport());
    }

    private JButton createModernButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
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

    private void loadData() {
    // Reset filter terlebih dahulu
    sorter.setRowFilter(null);
    
    model.setRowCount(0);
    List<Pinjaman> list = PinjamanDAO.getDaftarPinjamanAktif();
    
    // Create status icons
    Icon overdueIcon = createStatusIcon(new Color(231, 76, 60), "Terlambat");
    Icon activeIcon = createStatusIcon(new Color(46, 204, 113), "Aktif");
    Icon dueSoonIcon = createStatusIcon(new Color(241, 196, 15), "Akan Jatuh Tempo");
    
    for (Pinjaman p : list) {
        // Determine status
        Icon statusIcon;
        if (p.getTanggalKembali().before(new java.util.Date())) {
            statusIcon = overdueIcon;
        } else if (daysBetween(new java.util.Date(), p.getTanggalKembali()) <= 2) {
            statusIcon = dueSoonIcon;
        } else {
            statusIcon = activeIcon;
        }
        
        model.addRow(new Object[]{
            p.getId(),
            p.getJudulBuku(), 
            p.getNamaPeminjam(), 
            formatDate(p.getTanggalPinjam()),
            formatDate(p.getTanggalKembali()),
            statusIcon
        });
    }
    
    updateStatusBar();
}
    
    private Icon createStatusIcon(Color color, String tooltip) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(color);
                g2d.fillOval(x, y, getIconWidth(), getIconHeight());
                g2d.dispose();
            }

            @Override
            public int getIconWidth() {
                return 12;
            }

            @Override
            public int getIconHeight() {
                return 12;
            }
        };
    }
    
    private String formatDate(java.util.Date date) {
        return new java.text.SimpleDateFormat("dd-MM-yyyy").format(date);
    }
    
    private int daysBetween(java.util.Date d1, java.util.Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
    
    private void updateStatusBar() {
        Component[] components = getContentPane().getComponents();
        for (Component comp : components) {
            if (comp instanceof JPanel) {
                JPanel panel = (JPanel) comp;
                Component[] subComponents = panel.getComponents();
                for (Component subComp : subComponents) {
                    if (subComp instanceof JPanel) {
                        JPanel innerPanel = (JPanel) subComp;
                        if (innerPanel.getComponentCount() > 0 && 
                            innerPanel.getComponent(0) instanceof JLabel) {
                            ((JLabel)innerPanel.getComponent(0)).setText(
                                " Total pinjaman aktif: " + model.getRowCount());
                            return;
                        }
                    }
                }
            }
        }
    }
    
    private void processReturn() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Pilih pinjaman yang akan dikembalikan", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Convert view row index to model row index
        int modelRow = table.convertRowIndexToModel(selectedRow);
        
        // Get the ID from the model (column 0)
        Object idValue = model.getValueAt(modelRow, 0);
        int id;
        
        try {
            if (idValue instanceof Number) {
                id = ((Number)idValue).intValue();
            } else {
                id = Integer.parseInt(idValue.toString());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "ID pinjaman tidak valid", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Konfirmasi pengembalian buku:\n" +
            "Judul: " + model.getValueAt(modelRow, 1) + "\n" +
            "Peminjam: " + model.getValueAt(modelRow, 2),
            "Konfirmasi Pengembalian",
            JOptionPane.YES_NO_OPTION);
            
        if (confirm == JOptionPane.YES_OPTION) {
            //boolean success = PinjamanDAO.kembalikanBuku(id);
            
           // if (success) {
                JOptionPane.showMessageDialog(this,
                    "Buku berhasil dikembalikan",
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadData();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal memproses pengembalian",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
   // private void printReport() {
    //    JOptionPane.showMessageDialog(this,
   //         "Fitur cetak laporan akan diimplementasikan",
   //         "Info", JOptionPane.INFORMATION_MESSAGE);
   // }
//}