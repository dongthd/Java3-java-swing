/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Assignment;

import Assignment.ClockThread.ClockThread;
import java.awt.Color;
import static java.lang.Double.parseDouble;
import static java.lang.ProcessBuilder.Redirect.to;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author HP
 */
public final class QuanLyDiemSinhVienn extends javax.swing.JFrame {

    private final String header[] = {" Mã SV", "Họ tên", "Tiếng anh", "Tin học", "GDTC", "Điểm TB"};
    private final DefaultTableModel tblModel = new DefaultTableModel(header, 0);

    /**
     * Creates new form QuanLyDiemSinhVienn
     */
    public ArrayList<DiemSV> listSinhVien = new ArrayList<>();
    private int currentIndex = -1;

    public QuanLyDiemSinhVienn() {
        initComponents();
        this.setLocationRelativeTo(null);
        LoadData();
        LoadDataToTable();
        initClock();
    }

    private void initClock() {
        ClockThread th = new ClockThread(tblClock);
        th.start();
    }

    String urlSQL = "jdbc:sqlserver://localhost:1433;databaseName =App_Poly;userName=sa;password=123456";

    public void LoadDataToTable() {
        try {
            tblModel.setRowCount(0);
            Connection connect = DriverManager.getConnection(urlSQL, "sa", "123456");
            Statement st = connect.createStatement();
            String sql = "select top 3 students.maSV, hoTen, tiengAnh, tinHoc, GDTC, \n"
                    + "(tiengAnh + tinHoc + GDTC)/3 as DiemTB\n"
                    + "from grade, students\n"
                    + "where grade.maSV= students.maSV\n"
                    + "order by DiemTB desc";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Vector row = new Vector();
                row.add(rs.getString(1));
                row.add(rs.getString(2));
                row.add(rs.getString(3));
                row.add(rs.getString(4));
                row.add(rs.getString(5));
                double roundOff = Math.round(parseDouble(rs.getString(6)) * 100.0) / 100.0;

                row.add(roundOff);
                tblModel.addRow(row);
            }
            tblTenSV.setModel(tblModel);
            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkIdSV(String maSV) {
        ArrayList<String> listIdSv = new ArrayList<String>();
        Connection connect = null;
        Statement st = null;
        try {
            connect = DriverManager.getConnection(urlSQL);
            st = connect.createStatement();
            String sql = "";
            sql = "select  *from grade where maSV = ?;";
            PreparedStatement stm = connect.prepareStatement(sql);
            stm.setString(1, maSV);
            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String maSVTimKiem = rs.getString("maSV");
                listIdSv.add(maSVTimKiem);
            }
            // clean-up environment
            rs.close();
            st.close();
            connect.close();
            if (listIdSv.size() > 0) {
                JOptionPane.showMessageDialog(this, "Bảng Điểm MaSV đã Tồn Tại");
                return false;
            } else {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void LoadData() {
        Connection connect = null;
        Statement st = null;
        try {
            connect = DriverManager.getConnection(urlSQL);
            st = connect.createStatement();
            String sql = "";
            sql = "select id, grade.maSV as MASV, hoTen, tiengAnh, tinHoc, "
                    + "GDTC from grade, students where grade.maSV = students.maSV";
            ResultSet rs = st.executeQuery(sql);

            listSinhVien.clear();
            while (rs.next()) {
                int ID = rs.getInt("id");
                String maSV = rs.getString("maSV");
                String hoTen = rs.getString("hoTen");
                float tiengAnh = rs.getFloat("tiengAnh");
                float tinHoc = rs.getFloat("tinHoc");
                float GDTC = rs.getFloat("GDTC");
                double diemTB = (tiengAnh + tinHoc + GDTC) / 3;

                //Display values
                DiemSV diemSV = new DiemSV(ID, maSV, hoTen, tiengAnh, tinHoc, GDTC, diemTB);
                listSinhVien.add(diemSV);
            }
            // đọc giá trị  đầu tiên trong list đổ lên form
            currentIndex = 0;
            DisplayDiemSV();
            // clean-up environment
            rs.close();
            st.close();
            connect.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void Resert() {
        txtGDTC.setText("");
        txtTiengAnh.setText("");
        txtTin.setText("");
        txtMaSV.setText("");
        txtMaSV.requestFocus();
        tblHoTenSV.setText("");
        tblDiemTB.setText("");
    }

    private void DisplayDiemSV() {
        if (listSinhVien.size() > 0) {
            DiemSV diemSV = listSinhVien.get(currentIndex);
            txtMaSV.setText(diemSV.maSV + "");
            tblHoTenSV.setText(diemSV.hoTen + "");
            txtTiengAnh.setText(diemSV.tiengAnh + "");
            txtTin.setText(diemSV.tinHoc + "");
            txtGDTC.setText(diemSV.GDTC + "");

            UpdateDiemTB();
        }
    }

    public void UpdateDiemTB() {
        float ta = Float.parseFloat(txtTiengAnh.getText());
        float th = Float.parseFloat(txtTin.getText());
        float gdtc = Float.parseFloat(txtGDTC.getText());
        double tb = (ta + th + gdtc) / 3;
        // làm tròn chỉ lấy 2 số phía sau của điểm
        tb = Math.round(tb * 100.0) / 100.0;

        tblDiemTB.setText(tb + "");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnDelete = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTenSV = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTimKiemMaSV = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtMaSV = new javax.swing.JTextField();
        txtTiengAnh = new javax.swing.JTextField();
        txtTin = new javax.swing.JTextField();
        txtGDTC = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        tblDiemTB = new javax.swing.JLabel();
        tblHoTenSV = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        btnNew = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnDangXuat = new javax.swing.JButton();
        tblClock = new javax.swing.JLabel();
        btnEmail = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnDelete.setForeground(new java.awt.Color(51, 51, 255));
        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assignment/Images/delete.png"))); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnUpdate.setForeground(new java.awt.Color(51, 51, 255));
        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assignment/Images/update.png"))); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnFirst.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnFirst.setText("<<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnNext.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnNext.setText(">");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnPrev.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnPrev.setText("<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnLast.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnLast.setText(">>");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        tblTenSV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Mã SV", "Họ tên", "Tiếng anh", "Tin học", "GDTC", "Điểm TB"
            }
        ));
        tblTenSV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTenSVMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTenSV);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel2.setText("Mã SV:");

        btnSearch.setForeground(new java.awt.Color(51, 102, 255));
        btnSearch.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assignment/Images/search.png"))); // NOI18N
        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtTimKiemMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTimKiemMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 255));
        jLabel1.setText("Quản Lý Điểm Sinh Viên");

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Họ tên SV:");

        jLabel4.setText("Mã SV:");

        jLabel5.setText("Tiếng Anh:");

        jLabel6.setText("Tin:");

        jLabel7.setText("Giáo dục TC:");

        jLabel9.setText("Điểm TB:");

        tblDiemTB.setFont(new java.awt.Font("Tahoma", 1, 22)); // NOI18N
        tblDiemTB.setForeground(new java.awt.Color(255, 0, 0));

        tblHoTenSV.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblHoTenSV.setForeground(new java.awt.Color(0, 0, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtMaSV, javax.swing.GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
                            .addComponent(txtTin)
                            .addComponent(txtTiengAnh)
                            .addComponent(txtGDTC)
                            .addComponent(tblHoTenSV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(tblDiemTB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tblHoTenSV, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMaSV, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTiengAnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtGDTC, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(tblDiemTB, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("3 Sinh viên có điểm cao nhất :");

        btnNew.setForeground(new java.awt.Color(51, 51, 255));
        btnNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assignment/Images/new.png"))); // NOI18N
        btnNew.setText("New");
        btnNew.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        btnNew.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewActionPerformed(evt);
            }
        });

        btnSave.setForeground(new java.awt.Color(51, 51, 255));
        btnSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assignment/Images/save.png"))); // NOI18N
        btnSave.setText("Save");
        btnSave.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnDangXuat.setForeground(new java.awt.Color(255, 0, 0));
        btnDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assignment/Images/icons8-prev-25.png"))); // NOI18N
        btnDangXuat.setText("Come back");
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });

        tblClock.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblClock.setForeground(new java.awt.Color(255, 0, 0));
        tblClock.setText("00:00:00");

        btnEmail.setForeground(new java.awt.Color(51, 51, 255));
        btnEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Assignment/Images/icons8-send-email-30.png"))); // NOI18N
        btnEmail.setText("SendMail");
        btnEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmailActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(92, 92, 92)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnFirst)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLast))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnDelete, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnNew, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(76, 76, 76))
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(tblClock, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDangXuat)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(tblClock, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDangXuat, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(btnNew)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSave)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEmail)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(3, 3, 3)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnFirst)
                    .addComponent(btnNext)
                    .addComponent(btnPrev)
                    .addComponent(btnLast))
                .addGap(19, 19, 19)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        try {
            boolean flagDiemTiengAnh;
            boolean flagDiemTin;
            boolean flagDiemGDTC;

            Connection connect = DriverManager.getConnection(urlSQL, "sa", "123456");
            String sql = "update grade set tiengAnh=?, tinHoc=?, GDTC=? where maSV=?";
            PreparedStatement stm = connect.prepareStatement(sql);

            // check điểm
            if (flagDiemTiengAnh = checkValidatePoint("Điểm Tiếng Anh", txtTiengAnh.getText()));
            stm.setString(1, txtTiengAnh.getText().trim());

            if (flagDiemTin = checkValidatePoint("Điểm Tin", txtTin.getText()));
            stm.setString(2, txtTin.getText().trim());

            if (flagDiemGDTC = checkValidatePoint("Điểm GDTC", txtGDTC.getText()));
            stm.setString(3, txtGDTC.getText().trim());

            stm.setString(4, txtMaSV.getText().trim());
            if (flagDiemTiengAnh == true && flagDiemTin == true && flagDiemGDTC == true) {
                stm.executeUpdate();
                JOptionPane.showMessageDialog(this, "update success !");
                stm.close();
                connect.close();
                LoadData();
                UpdateDiemTB();
                LoadDataToTable();
                DoDuLieuLenComponent(0);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Update not success !");
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed

        int i;
        String maSV;
        maSV = txtTimKiemMaSV.getText();
        for (i = 0; i < listSinhVien.size(); i++) {
            DiemSV diemSV = listSinhVien.get(i);
            if (diemSV.maSV.equalsIgnoreCase(maSV)) {
                JOptionPane.showMessageDialog(this, "Đã tìm thấy !", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                currentIndex = i;
                DisplayDiemSV();
                break;
            }
        }
        if (i == listSinhVien.size()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy !", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            txtTimKiemMaSV.requestFocus();
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewActionPerformed
        Resert();
    }//GEN-LAST:event_btnNewActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // First
        currentIndex = 0;
        DisplayDiemSV();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // Next
        currentIndex++;
        if (currentIndex >= listSinhVien.size()) {
            JOptionPane.showMessageDialog(this, "Đang ở cuối");
            return;
        }
        DisplayDiemSV();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // Previous
        currentIndex--;
        if (currentIndex < 0) {
            JOptionPane.showMessageDialog(this, "Đang ở đầu");
        }
        DisplayDiemSV();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
        // Last
        currentIndex = listSinhVien.size() - 1;
        DisplayDiemSV();
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        try {
            boolean flagDiemTiengAnh;
            boolean flagDiemTin;
            boolean flagDiemGDTC;
            boolean flagMaSV;

            Connection connect = DriverManager.getConnection(urlSQL, "sa", "123456");
            String sql = "insert into grade(maSV, tiengAnh, tinHoc, GDTC) values(?,?,?,?)";
            PreparedStatement stm = connect.prepareStatement(sql);
            stm.setString(1, txtMaSV.getText().trim());

            // check ma Sv Ton tai
            if (flagMaSV = checkIdSV(txtMaSV.getText().trim()));
            if (txtMaSV.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Bạn chưa nhập Mã SV !", "Thông báo", JOptionPane.ERROR_MESSAGE);
                txtMaSV.requestFocus();
                txtMaSV.setBackground(Color.yellow);
            }
            stm.setString(2, txtTiengAnh.getText().trim());
            if (flagDiemTiengAnh = checkValidatePoint("Điểm Tiếng Anh", txtTiengAnh.getText()));
            txtTiengAnh.setBackground(Color.yellow);
            txtTin.setBackground(Color.yellow);
            txtGDTC.setBackground(Color.yellow);

            if (flagDiemTin = checkValidatePoint("Điểm Tin", txtTin.getText()));
            stm.setString(3, txtTin.getText().trim());

            if (flagDiemGDTC = checkValidatePoint("Điểm GDTC", txtGDTC.getText()));
            stm.setString(4, txtGDTC.getText().trim());

            if (flagDiemTiengAnh == true && flagDiemTin == true && flagDiemGDTC == true && flagMaSV == true) {
                stm.executeUpdate();
                JOptionPane.showMessageDialog(this, "Save success !");
                txtTiengAnh.setBackground(Color.white);
                txtTin.setBackground(Color.white);
                txtGDTC.setBackground(Color.white);
                txtMaSV.setBackground(Color.white);
                stm.close();
                connect.close();
                LoadDataToTable();
                DoDuLieuLenComponent(0);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Save not success !");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            Connection connect = DriverManager.getConnection(urlSQL, "sa", "123456");
            String sql = "delete from grade where maSV=?";
            PreparedStatement ps = null;
            ps = connect.prepareStatement(sql);
            ps.setString(1, txtMaSV.getText().trim());
            ps.execute();
            JOptionPane.showMessageDialog(this, "Delete success!");
            LoadData();
            LoadDataToTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Delete not success!");
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tblTenSVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTenSVMouseClicked
        int row = tblTenSV.getSelectedRow();
        DoDuLieuLenComponent(row);
    }//GEN-LAST:event_tblTenSVMouseClicked

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        int a = JOptionPane.showConfirmDialog(this, "Bạn có muốn thoát!");
        if (a == 0) {
            MainForm QLdiemSV = new MainForm();
            QLdiemSV.setVisible(true);
            dispose();
        }
    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void btnEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmailActionPerformed
        Connection connect = null;
        Statement st = null;
        String emailSv = null;
        try {
            connect = DriverManager.getConnection(urlSQL);
            st = connect.createStatement();
            String sql = "";
            sql = "select  *from Students where maSV = ?;";
            PreparedStatement stm = connect.prepareStatement(sql);
            stm.setString(1, txtMaSV.getText().trim());

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                emailSv = rs.getString("email");

            }
            // clean-up environment
            rs.close();
            st.close();
            connect.close();
            String bodyEmail = "Họ và tên sinh viên:" + tblHoTenSV.getText().trim() + "\n" + "Điểm Tiếng Anh là:" + txtTiengAnh.getText().trim() + "\n" + "Điểm Tin Học là:" + txtTin.getText().trim() + "\n" + "Điểm GDTC là:" + txtGDTC.getText().trim() + "\n";
            sendMail(emailSv, bodyEmail);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnEmailActionPerformed

    // Send Email
    public void sendMail(String to, String body) {
        try {
            String from = "abc@gmail.com.vn";
            Properties p = new Properties();
            // thuộc tính cần thiết của gmail
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.starttls.enable", "true");
            p.put("mail.smtp.host", "smtp.gmail.com");
            p.put("mail.smtp.auth", "true");
            p.put("mail.smtp.port", 587);
            String accountName = "abc@gmail.com.vn";
            String accountPassworld = "";
            // luồng đăng nhập
            Session s;
            s = Session.getInstance(p,
                    new javax.mail.Authenticator() {
                protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                    return new javax.mail.PasswordAuthentication(accountName, accountPassworld);
                }
            }
            );

            String subject = "Bảng Điểm";
            Message msg = new MimeMessage(s);
            msg.setSubject(subject);
            //người gửi
            msg.setFrom(new InternetAddress(from));
            //người nhận
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            //nội dung 
            msg.setText(body);
            // sẽ gửi mail
            Transport.send(msg);
            
            JOptionPane.showMessageDialog(null, "Mail was sent successfully!", "Message",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE);
        } catch (MessagingException ex) {
            Logger.getLogger(sendMail.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
    }

    public void DoDuLieuLenComponent(int row) {
        if (row < 0) {
            return;
        }
        txtMaSV.setText(tblTenSV.getValueAt(row, 0).toString());
        tblHoTenSV.setText(tblTenSV.getValueAt(row, 1).toString());
        txtTiengAnh.setText(tblTenSV.getValueAt(row, 2).toString());
        txtTin.setText(tblTenSV.getValueAt(row, 3).toString());
        txtGDTC.setText(tblTenSV.getValueAt(row, 4).toString());
        UpdateDiemTB();
    }

    // check dữ liệu nhập vào của điểm
    public boolean checkValidatePoint(String monHoc, String numberGraden) {
        String regex = "[+-]?([0-9]*[.])?[0-9]+";
        if (numberGraden.isEmpty()) {
            JOptionPane.showMessageDialog(this, monHoc + " Không được bỏ trống!");
            return false;
        } else if (!numberGraden.matches(regex)) {
            JOptionPane.showMessageDialog(this, monHoc + " Phải là kiểu số và bé hơn 10");
            return false;
        } else if (Float.parseFloat(numberGraden) > 10) {
            JOptionPane.showMessageDialog(this, monHoc + " Phải là kiểu số và bé hơn 10");
            return false;
        } else if (Float.parseFloat(numberGraden) < 0) {
            JOptionPane.showMessageDialog(this, monHoc + " Phải là kiểu số và bé hơn 10");
            return false;
        } else {
            return true;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(QuanLyDiemSinhVienn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyDiemSinhVienn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyDiemSinhVienn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyDiemSinhVienn.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyDiemSinhVienn().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEmail;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNew;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel tblClock;
    private javax.swing.JLabel tblDiemTB;
    private javax.swing.JLabel tblHoTenSV;
    private javax.swing.JTable tblTenSV;
    private javax.swing.JTextField txtGDTC;
    private javax.swing.JTextField txtMaSV;
    private javax.swing.JTextField txtTiengAnh;
    private javax.swing.JTextField txtTimKiemMaSV;
    private javax.swing.JTextField txtTin;
    // End of variables declaration//GEN-END:variables

}
