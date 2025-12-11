package com.mycompany.filehandlingmultithread;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MainFrame extends JFrame {

    private JTextField pathField;
    private JSpinner filesSpinner;
    private JCheckBox includeSubCheck;
    private JButton browseBtn, createBtn, addBtn, deleteBtn, startBtn, refreshBtn;
    private DefaultListModel<String> filesListModel;
    private JList<String> filesList;

    private static final String[] WORD_POOL = {
            "system", "network", "algorithm", "data", "path", "thread", "packet",
            "structure", "is", "are", "you", "process", "file", "java", "code",
            "memory", "processor", "cache", "binary", "function"
    };

    public MainFrame() {
        super("File Handling MultiThread - OS2 Project");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        initComponents();
    }

    private void initComponents() {
        JPanel top = new JPanel(new GridBagLayout());
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(8, 8, 8, 8);
        c.fill = GridBagConstraints.HORIZONTAL;

        // Directory
        c.gridx = 0; c.gridy = 0; c.weightx = 0;
         JLabel dirName = new JLabel("Directory:");
         dirName.setFont(new Font("Arial", Font.BOLD, 12));
        top.add(dirName, c);
        pathField = new JTextField();
        pathField.setPreferredSize(new Dimension(300, 25));
        c.gridx = 1; c.weightx = 1;
        top.add(pathField, c);
        browseBtn = new JButton("Browse");
        c.gridx = 2; c.weightx = 0;
        top.add(browseBtn, c);

        // Number of files
        c.gridx = 0; c.gridy = 1;
        JLabel fileCreate= new JLabel("Files to create:");
        fileCreate.setFont(new Font("Arial", Font.BOLD, 12));
        top.add(fileCreate, c);
        filesSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 500, 1));
        c.gridx = 1;
        top.add(filesSpinner, c);
        includeSubCheck = new JCheckBox("Include subdirectories" );
        includeSubCheck.setFont(new Font("Arial",Font.ITALIC, 10));

        c.gridx = 2;
        top.add(includeSubCheck, c);

        // Buttons
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        createBtn = new JButton("Create Files");
        createBtn.setFont(new Font("Arial", Font.BOLD, 12));
       createBtn.setBackground(new Color(22, 118, 213));  // أزرق
       createBtn.setForeground(Color.WHITE);               // كتابه أبيض
        createBtn.setOpaque(true);
       createBtn.setBorderPainted(false);

// Hover Effect
        createBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                createBtn.setBackground(new Color(31, 72, 136)); // لون أغمق عند المرور
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                createBtn.setBackground(new Color(22, 118, 213)); // يرجع للون الأصلي
            }
        });



        startBtn = new JButton("Start Processing");
       startBtn.setFont(new Font("Arial", Font.BOLD, 12));
        startBtn.setBackground(new Color(52, 175, 52));  // أزرق
        startBtn.setForeground(Color.WHITE);               // كتابه أبيض
        startBtn.setOpaque(true);
        startBtn.setBorderPainted(false);

// Hover Effect
        startBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startBtn.setBackground(new Color(52, 175, 52)); // لون أغمق عند المرور
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startBtn.setBackground(new Color(41, 194, 41)); // يرجع للون الأصلي
            }
        });


        refreshBtn = new JButton("Refresh List");
        refreshBtn.setFont(new Font("Arial", Font.BOLD, 12));
        refreshBtn.setBackground(new Color(22, 118, 213));  // أزرق
        refreshBtn.setForeground(Color.WHITE);               // كتابه أبيض
        refreshBtn.setOpaque(true);
        refreshBtn.setBorderPainted(false);

// Hover Effect
        refreshBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refreshBtn.setBackground(new Color(31, 72, 136)); // لون أغمق عند المرور
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refreshBtn.setBackground(new Color(22, 118, 213)); // يرجع للون الأصلي
            }
        });

        btnPanel.add(createBtn);
        btnPanel.add(startBtn);
        btnPanel.add(refreshBtn);
        c.gridx = 0; c.gridy = 2; c.gridwidth = 3;
        top.add(btnPanel, c);

        // File list + manage
        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        filesListModel = new DefaultListModel<>();
        filesList = new JList<>(filesListModel);
        filesList.setFont(new Font("Arial", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(filesList);
        scrollPane.setPreferredSize(new Dimension(600, 300));
        center.add(scrollPane, BorderLayout.CENTER);

        JPanel manage = new JPanel();
        manage.setLayout(new BoxLayout(manage, BoxLayout.Y_AXIS));
        addBtn = new JButton("Add File");
        deleteBtn = new JButton("Delete Selected");
        // Add Button
        addBtn.setFont(new Font("Arial", Font.BOLD, 12));
        addBtn.setBackground(new Color(52, 175, 52));  // أزرق
        addBtn.setForeground(Color.WHITE);               // كتابة أبيض
        addBtn.setOpaque(true);
        addBtn.setBorderPainted(false);

// Hover Effect
        addBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addBtn.setBackground(new Color(28, 119, 28)); // لون أغمق عند المرور
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addBtn.setBackground(new Color(52, 175, 52)); // يرجع للون الأصلي
            }
        });

// Delete Button
        deleteBtn.setFont(new Font("Arial", Font.BOLD, 12));
        deleteBtn.setBackground(new Color(192, 12, 43));  // أزرق
        deleteBtn.setForeground(Color.WHITE);               // كتابة أبيض
        deleteBtn.setOpaque(true);
        deleteBtn.setBorderPainted(false);

// Hover Effect
        deleteBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                deleteBtn.setBackground(new Color(143, 0, 0)); // لون أغمق عند المرور
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                deleteBtn.setBackground(new Color(192, 12, 43)); // يرجع للون الأصلي
            }
        });

        addBtn.setMaximumSize(new Dimension(150, 30));
        deleteBtn.setMaximumSize(new Dimension(150, 30));
        manage.add(addBtn);
        manage.add(Box.createVerticalStrut(10));
        manage.add(deleteBtn);
        center.add(manage, BorderLayout.EAST);

        getContentPane().add(top, BorderLayout.NORTH);
        getContentPane().add(center, BorderLayout.CENTER);

        // Action Listeners
        browseBtn.addActionListener(e -> chooseDirectory());
        createBtn.addActionListener(e -> createFiles());
        refreshBtn.addActionListener(e -> refreshFileList());
        addBtn.addActionListener(e -> addFile());
        deleteBtn.addActionListener(e -> deleteSelected());
        startBtn.addActionListener(e -> startProcessing());

        refreshFileList();
    }

    private void chooseDirectory() {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            pathField.setText(fc.getSelectedFile().getAbsolutePath());
            refreshFileList();
        }
    }

    private void createFiles() {
        String path = pathField.getText().trim();
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select directory first!");
            return;
        }
        int n = (int) filesSpinner.getValue();
        try {
            FileCreator.create(path, n);
            JOptionPane.showMessageDialog(this, n + " files created successfully!");
            refreshFileList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addFile() {
        String path = pathField.getText().trim();
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select directory first!");
            return;
        }
        String name = JOptionPane.showInputDialog(this, "File name (.txt):", "file.txt");
        if (name == null || name.trim().isEmpty()) return;
        if (!name.endsWith(".txt")) name += ".txt";
        File f = new File(path, name);
        if (f.exists()) {
            JOptionPane.showMessageDialog(this, "File already exists!");
            return;
        }
        try (PrintWriter pw = new PrintWriter(f)) {
            Random r = new Random();
            for (int i = 0; i < 30; i++)
                pw.print(WORD_POOL[r.nextInt(WORD_POOL.length)] + " ");
            refreshFileList();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Failed to create file!");
        }
    }

    private void deleteSelected() {
        String selected = filesList.getSelectedValue();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a file!");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "Delete " + selected + "?")
                == JOptionPane.YES_OPTION) {
            new File(pathField.getText(), selected).delete();
            refreshFileList();
        }
    }

    private void refreshFileList() {
        filesListModel.clear();
        String path = pathField.getText().trim();
        if (path.isEmpty()) return;
        List<File> files = FileScanner.getTextFiles(path, includeSubCheck.isSelected());
        for (File f : files) filesListModel.addElement(f.getName());
    }

    private void startProcessing() {
        String path = pathField.getText().trim();
        if (path.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select directory first!");
            return;
        }
        new ProcessingFrame(path, includeSubCheck.isSelected()).setVisible(true);
    }

    // ====================== Helper Classes ======================

    public static class FileCreator {
        public static void create(String dirPath, int count) throws IOException {
            File dir = new File(dirPath);
            if (!dir.exists()) dir.mkdirs();
            Random r = new Random();
            for (int i = 1; i <= count; i++) {
                File f = new File(dir, "file_" + i + ".txt");
                try (PrintWriter pw = new PrintWriter(f)) {
                    int words = 15 + r.nextInt(50);
                    for (int j = 0; j < words; j++) {
                        pw.print(WORD_POOL[r.nextInt(WORD_POOL.length)] + " ");
                    }
                }
            }
        }
    }

    public static class FileScanner {
        public static List<File> getTextFiles(String path, boolean recursive) {
            List<File> list = new ArrayList<>();
            scan(new File(path), recursive, list);
            return list;
        }

        private static void scan(File dir, boolean recursive, List<File> list) {
            File[] files = dir.listFiles();
            if (files == null) return;
            for (File f : files) {
                if (f.isFile() && f.getName().toLowerCase().endsWith(".txt"))
                    list.add(f);
                else if (recursive && f.isDirectory())
                    scan(f, true, list);
            }
        }
    }

    /**
     * ProcessingFrame - REAL-TIME Dynamic Multi-threaded File Processor
     * - Each file gets its own thread (cached thread pool)
     * - Each thread updates its row IMMEDIATELY via SwingUtilities.invokeLater()
     * - Visual feedback shows: Pending → Processing → Complete
     * - Color-coded status for better visualization
     * - Progress bar and live statistics
     */
    public static class ProcessingFrame extends JFrame {
        private final DefaultTableModel model;
        private final Map<String, Integer> rowMap = new ConcurrentHashMap<>();
        private final JLabel statusLabel;
        private final JLabel globalStatsLabel;
        private final JProgressBar progressBar;
        private final ExecutorService executorService;
        private final AtomicInteger completedFiles = new AtomicInteger(0);
        private final AtomicInteger processingFiles = new AtomicInteger(0);
        private int totalFiles = 0;
        private long startTime;

        // Global directory-wide longest and shortest words
        private volatile String globalLongest = "-";
        private volatile String globalShortest = "-";
        private final Object globalWordsLock = new Object();

        public ProcessingFrame(String path, boolean includeSub) {
            super("Live Processing Results - Real-Time Multi-threaded");
            setSize(1200, 700);
            setLocationRelativeTo(null);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            // Table model with all required columns
            model = new DefaultTableModel(new String[]{
                    "File", "Thread", "Status", "#Words", "#is", "#are", "#you", "Longest", "Shortest"
            }, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            JTable table = new JTable(model);
            table.setFont(new Font("Consolas", Font.PLAIN, 12));
            table.setRowHeight(26);
            table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
            table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

            // Custom cell renderer for color-coded status
            DefaultTableCellRenderer statusRenderer = new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                                                               boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    if (column == 2) { // Status column
                        String status = value.toString();
                        if (status.equals("Complete")) {
                            c.setBackground(new Color(220, 255, 220));
                            c.setForeground(new Color(0, 100, 0));
                        } else if (status.equals("Processing")) {
                            c.setBackground(new Color(255, 255, 200));
                            c.setForeground(new Color(150, 100, 0));
                        } else if (status.equals("Pending")) {
                            c.setBackground(new Color(240, 240, 240));
                            c.setForeground(Color.GRAY);
                        } else if (status.equals("ERROR")) {
                            c.setBackground(new Color(255, 220, 220));
                            c.setForeground(Color.RED);
                        }
                    } else if (!isSelected) {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                    return c;
                }
            };
            table.getColumnModel().getColumn(2).setCellRenderer(statusRenderer);

            JScrollPane scrollPane = new JScrollPane(table);
            add(scrollPane, BorderLayout.CENTER);

            // Status panel with progress bar
            JPanel statusPanel = new JPanel(new BorderLayout(10, 5));
            statusPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));

            statusLabel = new JLabel("Initializing...");
            statusLabel.setFont(new Font("Arial", Font.BOLD, 13));
            statusPanel.add(statusLabel, BorderLayout.NORTH);

            // Global statistics label
            globalStatsLabel = new JLabel("📊 Directory Stats: Longest: - | Shortest: -");
            globalStatsLabel.setFont(new Font("Arial", Font.BOLD, 13));
            globalStatsLabel.setForeground(new Color(0, 100, 200));
            statusPanel.add(globalStatsLabel, BorderLayout.CENTER);

            progressBar = new JProgressBar(0, 100);
            progressBar.setStringPainted(true);
            progressBar.setPreferredSize(new Dimension(0, 25));
            progressBar.setFont(new Font("Arial", Font.BOLD, 12));
            statusPanel.add(progressBar, BorderLayout.SOUTH);

            add(statusPanel, BorderLayout.SOUTH);

            // Use CACHED thread pool for dynamic, unlimited thread creation
            executorService = Executors.newCachedThreadPool();

            // Load files and start processing
            loadAndProcess(path, includeSub);
        }

        private void loadAndProcess(String path, boolean includeSub) {
            startTime = System.currentTimeMillis();

            // Run file loading in background to keep UI responsive
            new Thread(() -> {
                List<File> files = FileScanner.getTextFiles(path, includeSub);
                totalFiles = files.size();

                if (files.isEmpty()) {
                    SwingUtilities.invokeLater(() -> {
                        statusLabel.setText("No .txt files found!");
                        progressBar.setValue(0);
                        progressBar.setString("No files");
                    });
                    return;
                }

                // Initialize table rows - all start with "Pending" status
                SwingUtilities.invokeLater(() -> {
                    for (File f : files) {
                        model.addRow(new Object[]{
                                f.getName(), "-", "Pending", "-", "-", "-", "-", "-", "-"
                        });
                        rowMap.put(f.getAbsolutePath(), model.getRowCount() - 1);
                    }
                    updateStatus();
                });

                // Small delay to show initial state
                try { Thread.sleep(300); } catch (InterruptedException ignored) {}

                // Submit EACH file as a SEPARATE task to the cached thread pool
                // This ensures one thread per file for true parallel processing
                for (File f : files) {
                    executorService.submit(() -> processFile(f));
                }

                // Shutdown executor after all tasks are submitted
                executorService.shutdown();

                // Monitor completion in background with live updates
                new Thread(() -> {
                    try {
                        while (!executorService.awaitTermination(100, TimeUnit.MILLISECONDS)) {
                            SwingUtilities.invokeLater(this::updateStatus);
                        }
                        // Final update
                        SwingUtilities.invokeLater(() -> {
                            long elapsed = System.currentTimeMillis() - startTime;
                            statusLabel.setText(String.format(
                                    "✓ Processing complete! %d/%d files processed in %.2f seconds",
                                    completedFiles.get(), totalFiles, elapsed / 1000.0
                            ));
                            progressBar.setValue(100);
                            progressBar.setString("Complete!");
                        });
                    } catch (InterruptedException e) {
                        SwingUtilities.invokeLater(() ->
                                statusLabel.setText("Processing interrupted.")
                        );
                    }
                }).start();

            }, "FileLoader-Thread").start();
        }

        private void updateStatus() {
            int completed = completedFiles.get();
            int processing = processingFiles.get();
            int pending = totalFiles - completed - processing;
            int progress = totalFiles > 0 ? (completed * 100) / totalFiles : 0;

            long elapsed = System.currentTimeMillis() - startTime;
            statusLabel.setText(String.format(
                    "⏱ Processing: %d active | ✓ %d completed | ⏳ %d pending | Time: %.1fs",
                    processing, completed, pending, elapsed / 1000.0
            ));
            progressBar.setValue(progress);
            progressBar.setString(String.format("%d%% (%d/%d)", progress, completed, totalFiles));

            // Update global stats
            updateGlobalStats();
        }

        private void updateGlobalStats() {
            String longest = globalLongest;
            String shortest = globalShortest;
            globalStatsLabel.setText(String.format(
                    "📊 Directory Stats: Longest: \"%s\" (%d chars) | Shortest: \"%s\" (%d chars)",
                    longest, longest.equals("-") ? 0 : longest.length(),
                    shortest, shortest.equals("-") ? 0 : shortest.length()
            ));
        }

        /**
         * Process a single file - runs in its OWN dedicated thread
         * Updates its row IMMEDIATELY and INDEPENDENTLY using SwingUtilities.invokeLater()
         */
        private void processFile(File file) {
            final Integer row = rowMap.get(file.getAbsolutePath());
            if (row == null) return;

            final String threadName = Thread.currentThread().getName();
            processingFiles.incrementAndGet();

            // Update status to "Processing" with thread name
            SwingUtilities.invokeLater(() -> {
                model.setValueAt(threadName, row, 1);
                model.setValueAt("Processing", row, 2);
                updateStatus();
            });

            try {
                // Simulate realistic processing time variation
                Thread.sleep(50 + new Random().nextInt(150));

                // Read file content
                String content = Files.readString(file.toPath());
                String[] words = content.trim().isEmpty() ?
                        new String[0] : content.trim().split("\\s+");

                // Analyze metrics
                int totalWords = words.length;
                int isCount = 0, areCount = 0, youCount = 0;
                String longest = "", shortest = words.length > 0 ? words[0] : "-";

                for (String word : words) {
                    String clean = word.replaceAll("[^A-Za-z]", "").toLowerCase();
                    if (clean.isEmpty()) continue;

                    // Count specific words
                    if (clean.equals("is")) isCount++;
                    if (clean.equals("are")) areCount++;
                    if (clean.equals("you")) youCount++;

                    // Track longest and shortest
                    if (clean.length() > longest.length()) longest = clean;
                    if (clean.length() < shortest.length()) shortest = clean;
                }

                if (longest.isEmpty()) longest = "-";
                if (shortest.isEmpty()) shortest = "-";

                // Update global longest and shortest words for the entire directory
                synchronized (globalWordsLock) {
                    if (!longest.equals("-")) {
                        if (globalLongest.equals("-") || longest.length() > globalLongest.length()) {
                            globalLongest = longest;
                        }
                    }
                    if (!shortest.equals("-")) {
                        if (globalShortest.equals("-") || shortest.length() < globalShortest.length()) {
                            globalShortest = shortest;
                        }
                    }
                }

                // Final values for lambda
                final int finalTotal = totalWords;
                final int finalIs = isCount;
                final int finalAre = areCount;
                final int finalYou = youCount;
                final String finalLongest = longest;
                final String finalShortest = shortest;

                // Update THIS thread's row IMMEDIATELY using SwingUtilities.invokeLater()
                // This is the key to real-time updates - each thread updates independently!
                SwingUtilities.invokeLater(() -> {
                    model.setValueAt("Complete", row, 2);
                    model.setValueAt(finalTotal, row, 3);
                    model.setValueAt(finalIs, row, 4);
                    model.setValueAt(finalAre, row, 5);
                    model.setValueAt(finalYou, row, 6);
                    model.setValueAt(finalLongest, row, 7);
                    model.setValueAt(finalShortest, row, 8);

                    processingFiles.decrementAndGet();
                    completedFiles.incrementAndGet();
                    updateStatus();
                });

            } catch (Exception e) {
                // Handle errors gracefully with detailed error message
                SwingUtilities.invokeLater(() -> {
                    model.setValueAt("ERROR", row, 2);
                    model.setValueAt(e.getMessage(), row, 3);

                    processingFiles.decrementAndGet();
                    completedFiles.incrementAndGet();
                    updateStatus();
                });
            }
        }

        @Override
        public void dispose() {
            executorService.shutdownNow();
            super.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            new MainFrame().setVisible(true);
        });
    }
}
/**
    private void initComponents() {//GEN-BEGIN:initComponents

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
*/
