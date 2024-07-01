package forms;

import com.raven.datechooser.DateBetween;
import com.raven.datechooser.DateChooser;
import com.raven.datechooser.listener.DateChooserAction;
import com.raven.datechooser.listener.DateChooserAdapter;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Employee;
import model.Person;
import model.Leave;
//import model.PaySlip;
//import model.Payroll;
import model.Print;
import model.SingletonConnection;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.image.*;
import java.awt.print.*;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;
import javax.swing.table.TableRowSorter;
import model.SQLRun;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.text.MessageFormat;

public class Home extends javax.swing.JFrame {
//Creating Objects

    private DateChooser chDate = new DateChooser();
    private DateChooser chDate2 = new DateChooser();
    private DateChooser chDate3 = new DateChooser();
    private DateChooser chDate7 = new DateChooser();
    private DefaultTableModel model;
    private DefaultTableModel model2;
    private DefaultTableModel model3;
    JTable empDetails;
    Employee objEmployee = new Employee();
    Leave objLeave = new Leave();
    Person objPerson = new Person();
    JFrame frame = new JFrame("Confirmation Dialog Example");

    PrinterJob printJob = PrinterJob.getPrinterJob();

    BufferedImage image1;
    BufferedImage image2;

    /**
     * Creates new form Home
     */
    public Home() {

        initComponents();
        addButtonGroup();
        changeIcon();

        //chDate.setTextField(txt_benefits_SSS);
        chDate.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        chDate.setLabelCurrentDayVisible(false);
        chDate.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        chDate7.setTextField(txt_date1);
        chDate7.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        chDate7.setLabelCurrentDayVisible(false);
        chDate7.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        chDate7.setTextField(txt_violationdate);
        chDate7.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        chDate7.setLabelCurrentDayVisible(false);
        chDate7.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        chDate3.setTextField(txt_uniformdate1);
        chDate3.setDateSelectionMode(DateChooser.DateSelectionMode.SINGLE_DATE_SELECTED);
        chDate3.setLabelCurrentDayVisible(false);
        chDate3.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

        //chDate2.setTextField(txtDate2);
        chDate2.setDateSelectionMode(DateChooser.DateSelectionMode.BETWEEN_DATE_SELECTED);
        chDate2.setLabelCurrentDayVisible(false);
        chDate2.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        model = (DefaultTableModel) jTable1.getModel();
        chDate2.addActionDateChooserListener(new DateChooserAdapter() {
            @Override
            public void dateBetweenChanged(DateBetween date, DateChooserAction action) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                String dateFrom = df.format(date.getFromDate());
                String toDate = df.format(date.getToDate());
                //loadData("SELECT * FROM `salary_details` WHERE date BETWEEN '" + dateFrom + "' and '" +toDate+"'");
                loadData("SELECT * FROM leavelog WHERE e.empId=s.empId AND fromcutoffdate = '" + dateFrom + "' AND tocutoffdate = '" + toDate + "'");
                txt_violationdate.setText(null);

            }

        });

        try {
            SingletonConnection.getInstance().connectDatabase();

        } catch (Exception e) {
            System.err.println(e);
        }
        chDate2.setSelectedDateBetween(new DateBetween(getLast28Day(), new Date()), true);

    }
    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, null);
        JOptionPane.showMessageDialog(this, "Text copied to clipboard!");
    }

    public boolean insertLeavelog() {
        SQLRun objSQLRun = new SQLRun();
        /*String sql = "INSERT INTO employee (empId,nic,fname,lname,dob,address,city,tel_home,tel_mobile,designation,"
                + "department,date_of_joining,gender,salType) VALUES ('" + empId + "','" + nic + "','" + fName + "','" + lName + "','" + dob + "',"
                + "'" + address + "','" + city + "','" + telHome + "','" + telMobile + "','" + designation + "',"
                + "'" + department + "','" + dateOfJoining + "','" + gender + "','" + salType + "')";*/
 /*String sql = "INSERT INTO leavelog (`No`, `Employee_Name`, `Position`, `Date_Filed`, `Type_Of_Loa`, `Inclusive_Date`, `no_of_days`, `Approved_days`, `Approval_By`, `HRD`) "
                + "VALUES ('" + objEmployee.getEmpId() + "','" + objEmployee.getFname() + " " + objEmployee.getLname() + "','"
                + objEmployee.getDesignation() + "','"
                + txtfld_datefiled.getText() +  "', '" + txtfld_loatype.getText() + "', '"+ txtfld_inclusivedate.getText() + "', '"+ txtfld_numofdays.getText() + "', '"+ txtfld_approveddays.getText() + "', '"+ txtfld_approvalby.getText() + "', '"+ txtfld_hrd.getText()+ "')";
        
        int inserted = objSQLRun.sqlUpdate(sql);
        
        if (inserted > 0) {
            JOptionPane.showMessageDialog(null, "Employee " + objEmployee.getFname() + objEmployee.getLname() + " has been added "
                    + "to the system successfully", "Success", 1);
            return true;

        } else {
            JOptionPane.showMessageDialog(null, "Error occurred while trying to add Employee "
                    + "" + objEmployee.getFname() + objEmployee.getLname() + " to the system", "ERROR", 0);
            return false;

        }
         */
        return false;

    }

    private void loadData(String sql) {
        try {

            model.setRowCount(0);
            PreparedStatement p = SingletonConnection.getInstance().openConnection().prepareStatement(sql);
            ResultSet r = p.executeQuery();
            while (r.next()) {

                String empfname = r.getString("First Name");
                String emplname = r.getString("Last Name");
                String dept = r.getString("Department");
                String violation = r.getString("Violation");
                String date = r.getString("Date");

                model.addRow(new Object[]{empfname, emplname, dept, violation, date});
            }
            r.close();
            p.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void loadDataUniform(String sql) {
        try {
            model3.setRowCount(0);
            PreparedStatement p = SingletonConnection.getInstance().openConnection().prepareStatement(sql);
            ResultSet r = p.executeQuery();
            while (r.next()) {

                String empfname = r.getString("fname");
                String emplname = r.getString("lname");
                String position = r.getString("position");
                String dept = r.getString("department");
                String date = r.getString("dateclaimed");
                String size = r.getString("size");
                String quantity = String.valueOf(r.getInt("quantity"));

                model3.addRow(new Object[]{empfname, emplname, position, dept, date, size, quantity});
            }
            r.close();
            p.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private Date getLast28Day() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -28);
        return cal.getTime();
    }

    public void openFile(String file) {
        try {
            File path = new File(file);
            Desktop.getDesktop().open(path);
        } catch (IOException ioe) {
            System.out.println(ioe);
        }
    }

    public void exportarExcel(JTable jt) {
        try {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.showSaveDialog(jt);
            File saveFile = jFileChooser.getSelectedFile();
            if (saveFile != null) {
                saveFile = new File(saveFile.toString() + ".xlsx");
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("benefits");
                Row rowCol = sheet.createRow(0);

                for (int i = 0; i < jt.getColumnCount(); i++) {
                    Cell cell = rowCol.createCell(i);
                    cell.setCellValue(jt.getColumnName(i));
                }

                for (int j = 0; j < jt.getRowCount(); j++) {
                    Row row = sheet.createRow(j + 1);
                    for (int k = 0; k < jt.getColumnCount(); k++) {
                        Cell cell = row.createCell(k);
                        if (jt.getValueAt(j, k) != null) {
                            cell.setCellValue(jt.getValueAt(j, k).toString());
                        }
                    }
                }
                FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                wb.write(out);
                wb.close();
                out.close();
                openFile(saveFile.toString());

            } else {
                JOptionPane.showMessageDialog(null, "Error generating file");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException io) {
            System.out.println(io);
        }
    }

    public int findNetPayColumnIndex(JTable jt, String netPayColumnName) {
        int columnIndex = -1; // Default value if column is not found

        // Iterate over column headers to find the net pay column
        for (int i = 0; i < jt.getColumnCount(); i++) {
            String columnName = jt.getColumnName(i);
            if (columnName.equals(netPayColumnName)) {
                columnIndex = i;
                break; // Column found, exit loop
            }
        }

        return columnIndex;
    }

    public int findTotalDeductionsColumnIndex(JTable jt, String totalDeductionsColumnName) {
        int columnIndex = -1; // Default value if column is not found

        // Iterate over column headers to find the total deductions column
        for (int i = 0; i < jt.getColumnCount(); i++) {
            String columnName = jt.getColumnName(i);
            if (columnName.equals(totalDeductionsColumnName)) {
                columnIndex = i;
                break; // Column found, exit loop
            }
        }

        return columnIndex;
    }

    public int findColumnIndexByName(JTable jt, String columnName) {
        int columnIndex = -1; // Default value if column is not found

        // Iterate over column headers to find the column by name
        for (int i = 0; i < jt.getColumnCount(); i++) {
            String columnHeaderText = jt.getColumnName(i);
            if (columnHeaderText.equals(columnName)) {
                columnIndex = i;
                break; // Column found, exit loop
            }
        }

        return columnIndex;
    }

    public double calculateTotalColumnSum(JTable jt, int columnIndex) {
        double totalSum = 0.0;

        // Iterate over rows to calculate the total sum of the column
        for (int j = 0; j < jt.getRowCount(); j++) {
            Object value = jt.getValueAt(j, columnIndex);
            if (value != null && value instanceof Number) {
                totalSum += ((Number) value).doubleValue();
            }
        }

        return totalSum;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnGroup_rd = new javax.swing.ButtonGroup();
        jDialog3 = new javax.swing.JDialog();
        jPanel3 = new javax.swing.JPanel();
        jLabel62 = new javax.swing.JLabel();
        leave_empfname = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        leave_emplname = new javax.swing.JTextField();
        jLabel64 = new javax.swing.JLabel();
        jDialog4 = new javax.swing.JDialog();
        jPanel4 = new javax.swing.JPanel();
        jButton7 = new javax.swing.JButton();
        employee_emplname1 = new javax.swing.JTextField();
        jLabel70 = new javax.swing.JLabel();
        jDialog5 = new javax.swing.JDialog();
        jPanel5 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        employee_desig = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jDialog6 = new javax.swing.JDialog();
        jPanel6 = new javax.swing.JPanel();
        jLabel72 = new javax.swing.JLabel();
        employee_dept = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jDialog7 = new javax.swing.JDialog();
        jPanel7 = new javax.swing.JPanel();
        jLabel73 = new javax.swing.JLabel();
        log_empfname = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        log_emplname = new javax.swing.JTextField();
        jLabel74 = new javax.swing.JLabel();
        jDialog8 = new javax.swing.JDialog();
        jPanel8 = new javax.swing.JPanel();
        jLabel75 = new javax.swing.JLabel();
        log_empfname1 = new javax.swing.JTextField();
        jButton17 = new javax.swing.JButton();
        log_emplname1 = new javax.swing.JTextField();
        jLabel76 = new javax.swing.JLabel();
        jDialog9 = new javax.swing.JDialog();
        jPanel9 = new javax.swing.JPanel();
        jLabel67 = new javax.swing.JLabel();
        del_employee_empfname2 = new javax.swing.JTextField();
        jButton18 = new javax.swing.JButton();
        del_employee_emplname2 = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jDialog10 = new javax.swing.JDialog();
        jPanel10 = new javax.swing.JPanel();
        jLabel78 = new javax.swing.JLabel();
        updt_empfname2 = new javax.swing.JTextField();
        jButton19 = new javax.swing.JButton();
        updt_emplname2 = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        jDialog11 = new javax.swing.JDialog();
        jPanel11 = new javax.swing.JPanel();
        jLabel80 = new javax.swing.JLabel();
        benefitssearch_empfname3 = new javax.swing.JTextField();
        jButton21 = new javax.swing.JButton();
        benefitssearch_emplname3 = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        intFrame_cutoff = new javax.swing.JInternalFrame();
        btn_exit_payroll1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jLabel65 = new javax.swing.JLabel();
        txt_violationdate = new javax.swing.JTextField();
        jButton10 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        intFrame_employee_new = new javax.swing.JInternalFrame();
        btn_exit = new javax.swing.JButton();
        btn_add = new javax.swing.JButton();
        panel_empDetails = new javax.swing.JPanel();
        lbl_fname = new javax.swing.JLabel();
        lbl_lname = new javax.swing.JLabel();
        txt_fname = new javax.swing.JTextField();
        txt_lname = new javax.swing.JTextField();
        lbl_designation = new javax.swing.JLabel();
        txt_designation = new javax.swing.JTextField();
        lbl_department = new javax.swing.JLabel();
        txt_deparment = new javax.swing.JTextField();
        lbl_fname2 = new javax.swing.JLabel();
        txt_mname = new javax.swing.JTextField();
        intFrame_employee_update = new javax.swing.JInternalFrame();
        btn_update = new javax.swing.JButton();
        btn_exit_update = new javax.swing.JButton();
        btn_search_update = new javax.swing.JButton();
        panel_empUpdate = new javax.swing.JPanel();
        lbl_fname1 = new javax.swing.JLabel();
        lbl_lname1 = new javax.swing.JLabel();
        txt_fname_update = new javax.swing.JTextField();
        txt_lname_update = new javax.swing.JTextField();
        lbl_designation1 = new javax.swing.JLabel();
        txt_designation_update = new javax.swing.JTextField();
        lbl_department1 = new javax.swing.JLabel();
        txt_deparment_update = new javax.swing.JTextField();
        lbl_fname3 = new javax.swing.JLabel();
        txt_mname_update = new javax.swing.JTextField();
        intFrame_employee_search = new javax.swing.JInternalFrame();
        jScrollPane_tableContainer = new javax.swing.JScrollPane();
        btn_searchEmp1 = new javax.swing.JButton();
        btn_searchEmp3 = new javax.swing.JButton();
        intFrame_violationentry = new javax.swing.JInternalFrame();
        panel_empDetails_payroll2 = new javax.swing.JPanel();
        lbl_fname_allowance2 = new javax.swing.JLabel();
        lbl_lname_allowance2 = new javax.swing.JLabel();
        lbl_desig_allowance2 = new javax.swing.JLabel();
        lbl_depart_allowance2 = new javax.swing.JLabel();
        txt_fname_violation1 = new javax.swing.JTextField();
        txt_lname_violation1 = new javax.swing.JTextField();
        txt_position_violation1 = new javax.swing.JTextField();
        txt_depart_violation1 = new javax.swing.JTextField();
        panel_salAllow_payroll2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        txt_date1 = new javax.swing.JTextField();
        txt_violation1 = new javax.swing.JTextField();
        btn_exit_leave1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        intFrame_viewuniform = new javax.swing.JInternalFrame();
        btn_exit_payroll2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jButton13 = new javax.swing.JButton();
        jLabel66 = new javax.swing.JLabel();
        txt_uniformdate1 = new javax.swing.JTextField();
        jButton14 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        intFrame_benefits = new javax.swing.JInternalFrame();
        panel_empDetails_payroll1 = new javax.swing.JPanel();
        lbl_fname_allowance1 = new javax.swing.JLabel();
        lbl_lname_allowance1 = new javax.swing.JLabel();
        lbl_desig_allowance1 = new javax.swing.JLabel();
        lbl_depart_allowance1 = new javax.swing.JLabel();
        txt_fname_benefits = new javax.swing.JTextField();
        txt_lname_benefits = new javax.swing.JTextField();
        txt_position_benefits = new javax.swing.JTextField();
        txt_depart_benefits = new javax.swing.JTextField();
        lbl_fname_allowance3 = new javax.swing.JLabel();
        txt_mname_benefits = new javax.swing.JTextField();
        panel_salAllow_payroll1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        txt_benefits_SSS = new javax.swing.JTextField();
        txt_benefits_philhealth = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        txt_benefits_pagibig = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        txt_benefits_tin = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        btn_exit_leave = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        lbl_pms = new javax.swing.JLabel();
        lbl_background = new javax.swing.JLabel();
        menu_menuBar = new javax.swing.JMenuBar();
        menuBar_file = new javax.swing.JMenu();
        menuBar_file_logout = new javax.swing.JMenuItem();
        menuBar_file_exit = new javax.swing.JMenuItem();
        menuBar_employee = new javax.swing.JMenu();
        menuBar_employee_new = new javax.swing.JMenuItem();
        menuBar_employee_update = new javax.swing.JMenuItem();
        menuBar_employee_delete = new javax.swing.JMenuItem();
        menuBar_employee_search = new javax.swing.JMenuItem();
        menuBar_leave1 = new javax.swing.JMenu();
        menuBar_leave_apply1 = new javax.swing.JMenuItem();

        jDialog3.setLocation(new java.awt.Point(500, 250));
        jDialog3.setResizable(false);
        jDialog3.setSize(new java.awt.Dimension(500, 250));

        jLabel62.setText("Employee First Name:");

        leave_empfname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leave_empfnameActionPerformed(evt);
            }
        });

        jButton6.setText("Search");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        leave_emplname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                leave_emplnameActionPerformed(evt);
            }
        });

        jLabel64.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton6)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel62)
                            .addComponent(jLabel64))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(leave_empfname, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(leave_emplname, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel62)
                    .addComponent(leave_empfname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel64)
                    .addComponent(leave_emplname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton6)
                .addContainerGap(263, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog3Layout = new javax.swing.GroupLayout(jDialog3.getContentPane());
        jDialog3.getContentPane().setLayout(jDialog3Layout);
        jDialog3Layout.setHorizontalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog3Layout.setVerticalGroup(
            jDialog3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog4.setLocation(new java.awt.Point(500, 250));
        jDialog4.setResizable(false);
        jDialog4.setSize(new java.awt.Dimension(500, 250));

        jButton7.setText("Search");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        employee_emplname1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_emplname1ActionPerformed(evt);
            }
        });

        jLabel70.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabel70)
                        .addGap(7, 7, 7)
                        .addComponent(employee_emplname1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel70)
                    .addComponent(employee_emplname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addContainerGap(266, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog4Layout = new javax.swing.GroupLayout(jDialog4.getContentPane());
        jDialog4.getContentPane().setLayout(jDialog4Layout);
        jDialog4Layout.setHorizontalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog4Layout.setVerticalGroup(
            jDialog4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog5.setLocation(new java.awt.Point(500, 250));
        jDialog5.setResizable(false);
        jDialog5.setSize(new java.awt.Dimension(500, 250));

        jLabel71.setText("Employee Designation:");

        employee_desig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_desigActionPerformed(evt);
            }
        });

        jButton8.setText("Search");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton8)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel71)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(employee_desig, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel71)
                    .addComponent(employee_desig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8)
                .addContainerGap(303, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog5Layout = new javax.swing.GroupLayout(jDialog5.getContentPane());
        jDialog5.getContentPane().setLayout(jDialog5Layout);
        jDialog5Layout.setHorizontalGroup(
            jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog5Layout.setVerticalGroup(
            jDialog5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog6.setLocation(new java.awt.Point(500, 250));
        jDialog6.setResizable(false);
        jDialog6.setSize(new java.awt.Dimension(500, 250));

        jLabel72.setText("Employee Department:");

        employee_dept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employee_deptActionPerformed(evt);
            }
        });

        jButton9.setText("Search");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton9)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabel72)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(employee_dept, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(63, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel72)
                    .addComponent(employee_dept, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addContainerGap(303, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog6Layout = new javax.swing.GroupLayout(jDialog6.getContentPane());
        jDialog6.getContentPane().setLayout(jDialog6Layout);
        jDialog6Layout.setHorizontalGroup(
            jDialog6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog6Layout.setVerticalGroup(
            jDialog6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog7.setLocation(new java.awt.Point(500, 250));
        jDialog7.setResizable(false);
        jDialog7.setSize(new java.awt.Dimension(500, 250));

        jLabel73.setText("Employee First Name:");

        log_empfname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                log_empfnameActionPerformed(evt);
            }
        });

        jButton12.setText("Search");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });

        log_emplname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                log_emplnameActionPerformed(evt);
            }
        });

        jLabel74.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton12, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel73)
                        .addGap(18, 18, 18)
                        .addComponent(log_empfname, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel74)
                        .addGap(18, 18, 18)
                        .addComponent(log_emplname, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel73)
                    .addComponent(log_empfname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel74)
                    .addComponent(log_emplname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jButton12)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog7Layout = new javax.swing.GroupLayout(jDialog7.getContentPane());
        jDialog7.getContentPane().setLayout(jDialog7Layout);
        jDialog7Layout.setHorizontalGroup(
            jDialog7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog7Layout.setVerticalGroup(
            jDialog7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialog8.setLocation(new java.awt.Point(500, 250));
        jDialog8.setResizable(false);
        jDialog8.setSize(new java.awt.Dimension(500, 250));

        jLabel75.setText("Employee First Name:");

        log_empfname1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                log_empfname1ActionPerformed(evt);
            }
        });

        jButton17.setText("Search");
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        log_emplname1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                log_emplname1ActionPerformed(evt);
            }
        });

        jLabel76.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton17, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel75)
                        .addGap(18, 18, 18)
                        .addComponent(log_empfname1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel76)
                        .addGap(18, 18, 18)
                        .addComponent(log_emplname1, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel75)
                    .addComponent(log_empfname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel76)
                    .addComponent(log_emplname1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jButton17)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog8Layout = new javax.swing.GroupLayout(jDialog8.getContentPane());
        jDialog8.getContentPane().setLayout(jDialog8Layout);
        jDialog8Layout.setHorizontalGroup(
            jDialog8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog8Layout.setVerticalGroup(
            jDialog8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialog9.setLocation(new java.awt.Point(500, 250));
        jDialog9.setResizable(false);
        jDialog9.setSize(new java.awt.Dimension(500, 250));

        jLabel67.setText("Employee First Name:");

        del_employee_empfname2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                del_employee_empfname2ActionPerformed(evt);
            }
        });

        jButton18.setText("Delete");
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        del_employee_emplname2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                del_employee_emplname2ActionPerformed(evt);
            }
        });

        jLabel77.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton18)
                    .addGroup(jPanel9Layout.createSequentialGroup()
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel67)
                            .addComponent(jLabel77))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(del_employee_empfname2, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
                            .addComponent(del_employee_emplname2, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE))))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel67)
                    .addComponent(del_employee_empfname2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel77)
                    .addComponent(del_employee_emplname2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton18)
                .addContainerGap(263, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog9Layout = new javax.swing.GroupLayout(jDialog9.getContentPane());
        jDialog9.getContentPane().setLayout(jDialog9Layout);
        jDialog9Layout.setHorizontalGroup(
            jDialog9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog9Layout.setVerticalGroup(
            jDialog9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jDialog10.setLocation(new java.awt.Point(500, 250));
        jDialog10.setResizable(false);
        jDialog10.setSize(new java.awt.Dimension(500, 250));

        jLabel78.setText("Employee First Name:");

        updt_empfname2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updt_empfname2ActionPerformed(evt);
            }
        });

        jButton19.setText("Search");
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        updt_emplname2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updt_emplname2ActionPerformed(evt);
            }
        });

        jLabel79.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton19, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel78)
                        .addGap(18, 18, 18)
                        .addComponent(updt_empfname2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel10Layout.createSequentialGroup()
                        .addComponent(jLabel79)
                        .addGap(18, 18, 18)
                        .addComponent(updt_emplname2, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel78)
                    .addComponent(updt_empfname2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel79)
                    .addComponent(updt_emplname2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jButton19)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog10Layout = new javax.swing.GroupLayout(jDialog10.getContentPane());
        jDialog10.getContentPane().setLayout(jDialog10Layout);
        jDialog10Layout.setHorizontalGroup(
            jDialog10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog10Layout.setVerticalGroup(
            jDialog10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jDialog11.setLocation(new java.awt.Point(500, 250));
        jDialog11.setResizable(false);
        jDialog11.setSize(new java.awt.Dimension(500, 250));

        jLabel80.setText("Employee First Name:");

        benefitssearch_empfname3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                benefitssearch_empfname3ActionPerformed(evt);
            }
        });

        jButton21.setText("Search");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        benefitssearch_emplname3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                benefitssearch_emplname3ActionPerformed(evt);
            }
        });

        jLabel81.setText("Employee Last Name:");

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton21, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel80)
                        .addGap(18, 18, 18)
                        .addComponent(benefitssearch_empfname3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel11Layout.createSequentialGroup()
                        .addComponent(jLabel81)
                        .addGap(18, 18, 18)
                        .addComponent(benefitssearch_emplname3, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(58, Short.MAX_VALUE))
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel80)
                    .addComponent(benefitssearch_empfname3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel81)
                    .addComponent(benefitssearch_emplname3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jButton21)
                .addContainerGap(45, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jDialog11Layout = new javax.swing.GroupLayout(jDialog11.getContentPane());
        jDialog11.getContentPane().setLayout(jDialog11Layout);
        jDialog11Layout.setHorizontalGroup(
            jDialog11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jDialog11Layout.setVerticalGroup(
            jDialog11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Benefits Monitoring System | Home");
        setBackground(new java.awt.Color(255, 255, 255));
        setLocation(new java.awt.Point(100, 0));
        setMinimumSize(new java.awt.Dimension(1200, 700));
        setName("Home"); // NOI18N
        setResizable(false);
        getContentPane().setLayout(null);

        intFrame_cutoff.setClosable(true);
        intFrame_cutoff.setVisible(false);

        btn_exit_payroll1.setText("Exit");
        btn_exit_payroll1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exit_payroll1ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "First Name", "Last Name", "Department", "Violation", "Date"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable1.setAutoResizeMode(0);
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(2).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(3).setPreferredWidth(200);
            jTable1.getColumnModel().getColumn(4).setPreferredWidth(200);
        }

        jButton4.setText("Display All");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel65.setText("Search by Date:");

        jButton10.setText("Export");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        jButton11.setText("Search");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        jButton5.setText("Search by Employee Name");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout intFrame_cutoffLayout = new javax.swing.GroupLayout(intFrame_cutoff.getContentPane());
        intFrame_cutoff.getContentPane().setLayout(intFrame_cutoffLayout);
        intFrame_cutoffLayout.setHorizontalGroup(
            intFrame_cutoffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(intFrame_cutoffLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_exit_payroll1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(972, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, intFrame_cutoffLayout.createSequentialGroup()
                .addGroup(intFrame_cutoffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(intFrame_cutoffLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(intFrame_cutoffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(intFrame_cutoffLayout.createSequentialGroup()
                                .addComponent(jLabel65)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_violationdate, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(intFrame_cutoffLayout.createSequentialGroup()
                                .addComponent(jButton5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4))))
                    .addGroup(intFrame_cutoffLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1019, Short.MAX_VALUE)))
                .addGap(153, 153, 153))
        );
        intFrame_cutoffLayout.setVerticalGroup(
            intFrame_cutoffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(intFrame_cutoffLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(intFrame_cutoffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_violationdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel65)
                    .addComponent(jButton11)
                    .addComponent(jButton10, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(intFrame_cutoffLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(155, 155, 155)
                .addComponent(btn_exit_payroll1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(intFrame_cutoff);
        intFrame_cutoff.setBounds(0, 0, 1190, 680);

        intFrame_employee_new.setClosable(true);
        intFrame_employee_new.setTitle("Enter Employee Details");
        intFrame_employee_new.setToolTipText("");
        intFrame_employee_new.setMaximumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_new.setMinimumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_new.setPreferredSize(new java.awt.Dimension(800, 500));
        intFrame_employee_new.setVisible(false);
        intFrame_employee_new.getContentPane().setLayout(null);

        btn_exit.setText("Exit");
        btn_exit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exitActionPerformed(evt);
            }
        });
        intFrame_employee_new.getContentPane().add(btn_exit);
        btn_exit.setBounds(220, 390, 120, 23);

        btn_add.setText("Add Employee");
        btn_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_addActionPerformed(evt);
            }
        });
        intFrame_employee_new.getContentPane().add(btn_add);
        btn_add.setBounds(80, 390, 120, 20);

        panel_empDetails.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "New Employee Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18), new java.awt.Color(255, 0, 0))); // NOI18N

        lbl_fname.setText("First Name*");

        lbl_lname.setText("Last Name*");

        txt_fname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_fnameActionPerformed(evt);
            }
        });

        lbl_designation.setText("Position*");

        lbl_department.setText("Department*");

        lbl_fname2.setText("Middle Name*");

        txt_mname.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_mnameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_empDetailsLayout = new javax.swing.GroupLayout(panel_empDetails);
        panel_empDetails.setLayout(panel_empDetailsLayout);
        panel_empDetailsLayout.setHorizontalGroup(
            panel_empDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empDetailsLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(panel_empDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_fname)
                    .addComponent(lbl_lname)
                    .addComponent(lbl_designation)
                    .addComponent(lbl_department)
                    .addComponent(lbl_fname2))
                .addGap(100, 100, 100)
                .addGroup(panel_empDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_empDetailsLayout.createSequentialGroup()
                        .addGroup(panel_empDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_lname, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_fname, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_designation, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_mname, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(19, Short.MAX_VALUE))
                    .addGroup(panel_empDetailsLayout.createSequentialGroup()
                        .addComponent(txt_deparment, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panel_empDetailsLayout.setVerticalGroup(
            panel_empDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empDetailsLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(panel_empDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_fname)
                    .addComponent(txt_fname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_fname2)
                    .addComponent(txt_mname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_lname)
                    .addComponent(txt_lname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_designation)
                    .addComponent(txt_designation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_department)
                    .addComponent(txt_deparment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(141, Short.MAX_VALUE))
        );

        intFrame_employee_new.getContentPane().add(panel_empDetails);
        panel_empDetails.setBounds(60, 40, 470, 340);

        getContentPane().add(intFrame_employee_new);
        intFrame_employee_new.setBounds(0, 0, 1200, 680);
        try {
            intFrame_employee_new.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        intFrame_employee_update.setClosable(true);
        intFrame_employee_update.setTitle("Update Employee Details");
        intFrame_employee_update.setMaximumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_update.setMinimumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_update.setPreferredSize(new java.awt.Dimension(800, 500));
        intFrame_employee_update.setVisible(false);
        intFrame_employee_update.getContentPane().setLayout(null);

        btn_update.setText("Update Employee");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });
        intFrame_employee_update.getContentPane().add(btn_update);
        btn_update.setBounds(190, 300, 150, 23);

        btn_exit_update.setText("Exit");
        btn_exit_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exit_updateActionPerformed(evt);
            }
        });
        intFrame_employee_update.getContentPane().add(btn_exit_update);
        btn_exit_update.setBounds(350, 300, 150, 23);

        btn_search_update.setText("Search");
        btn_search_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_search_updateActionPerformed(evt);
            }
        });
        intFrame_employee_update.getContentPane().add(btn_search_update);
        btn_search_update.setBounds(30, 300, 150, 23);

        panel_empUpdate.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Update Employee Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18), new java.awt.Color(255, 0, 0))); // NOI18N

        lbl_fname1.setText("First Name*");

        lbl_lname1.setText("Last Name*");

        lbl_designation1.setText("Designation*");

        lbl_department1.setText("Department*");

        lbl_fname3.setText("Middle Name*");

        javax.swing.GroupLayout panel_empUpdateLayout = new javax.swing.GroupLayout(panel_empUpdate);
        panel_empUpdate.setLayout(panel_empUpdateLayout);
        panel_empUpdateLayout.setHorizontalGroup(
            panel_empUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empUpdateLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panel_empUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_fname1)
                    .addComponent(lbl_fname3)
                    .addComponent(lbl_lname1)
                    .addComponent(lbl_designation1)
                    .addComponent(lbl_department1))
                .addGap(95, 95, 95)
                .addGroup(panel_empUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_empUpdateLayout.createSequentialGroup()
                        .addComponent(txt_fname_update, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(44, Short.MAX_VALUE))
                    .addGroup(panel_empUpdateLayout.createSequentialGroup()
                        .addGroup(panel_empUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_deparment_update, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_designation_update, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_lname_update, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_mname_update, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panel_empUpdateLayout.setVerticalGroup(
            panel_empUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empUpdateLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(panel_empUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_fname_update, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_fname1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_fname3)
                    .addComponent(txt_mname_update, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_lname1)
                    .addComponent(txt_lname_update, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_designation1)
                    .addComponent(txt_designation_update, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empUpdateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_department1)
                    .addComponent(txt_deparment_update, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(62, Short.MAX_VALUE))
        );

        intFrame_employee_update.getContentPane().add(panel_empUpdate);
        panel_empUpdate.setBounds(30, 10, 470, 280);

        getContentPane().add(intFrame_employee_update);
        intFrame_employee_update.setBounds(0, 0, 1200, 680);
        try {
            intFrame_employee_update.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        intFrame_employee_search.setClosable(true);
        intFrame_employee_search.setTitle("Search Employee Details");
        intFrame_employee_search.setMaximumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_search.setMinimumSize(new java.awt.Dimension(800, 500));
        intFrame_employee_search.setPreferredSize(new java.awt.Dimension(800, 500));
        intFrame_employee_search.setVisible(false);

        jScrollPane_tableContainer.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employee Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18), new java.awt.Color(255, 0, 20))); // NOI18N

        btn_searchEmp1.setText("Search Employee Name");
        btn_searchEmp1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchEmp1ActionPerformed(evt);
            }
        });

        btn_searchEmp3.setText("Search Department");
        btn_searchEmp3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_searchEmp3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout intFrame_employee_searchLayout = new javax.swing.GroupLayout(intFrame_employee_search.getContentPane());
        intFrame_employee_search.getContentPane().setLayout(intFrame_employee_searchLayout);
        intFrame_employee_searchLayout.setHorizontalGroup(
            intFrame_employee_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(intFrame_employee_searchLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(intFrame_employee_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane_tableContainer)
                    .addGroup(intFrame_employee_searchLayout.createSequentialGroup()
                        .addComponent(btn_searchEmp1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_searchEmp3, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        intFrame_employee_searchLayout.setVerticalGroup(
            intFrame_employee_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, intFrame_employee_searchLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(intFrame_employee_searchLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_searchEmp1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_searchEmp3, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane_tableContainer, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(198, 198, 198))
        );

        getContentPane().add(intFrame_employee_search);
        intFrame_employee_search.setBounds(0, 0, 1200, 680);
        try {
            intFrame_employee_search.setMaximum(true);
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        intFrame_violationentry.setClosable(true);
        intFrame_violationentry.setResizable(true);
        intFrame_violationentry.setTitle("Violation Details");
        intFrame_violationentry.setMaximumSize(new java.awt.Dimension(800, 500));
        intFrame_violationentry.setMinimumSize(new java.awt.Dimension(800, 500));
        intFrame_violationentry.setPreferredSize(new java.awt.Dimension(800, 500));
        intFrame_violationentry.setVisible(false);

        panel_empDetails_payroll2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employee Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18), new java.awt.Color(255, 0, 0))); // NOI18N

        lbl_fname_allowance2.setText("First Name");

        lbl_lname_allowance2.setText("Last Name");

        lbl_desig_allowance2.setText("Position");

        lbl_depart_allowance2.setText("Department");

        txt_lname_violation1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_lname_violation1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_empDetails_payroll2Layout = new javax.swing.GroupLayout(panel_empDetails_payroll2);
        panel_empDetails_payroll2.setLayout(panel_empDetails_payroll2Layout);
        panel_empDetails_payroll2Layout.setHorizontalGroup(
            panel_empDetails_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empDetails_payroll2Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(panel_empDetails_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panel_empDetails_payroll2Layout.createSequentialGroup()
                        .addGroup(panel_empDetails_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_empDetails_payroll2Layout.createSequentialGroup()
                                .addComponent(lbl_fname_allowance2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_fname_violation1, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panel_empDetails_payroll2Layout.createSequentialGroup()
                                .addComponent(lbl_lname_allowance2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txt_lname_violation1, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(134, 145, Short.MAX_VALUE))
                    .addGroup(panel_empDetails_payroll2Layout.createSequentialGroup()
                        .addGroup(panel_empDetails_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(panel_empDetails_payroll2Layout.createSequentialGroup()
                                .addComponent(lbl_desig_allowance2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txt_position_violation1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panel_empDetails_payroll2Layout.createSequentialGroup()
                                .addComponent(lbl_depart_allowance2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_depart_violation1, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        panel_empDetails_payroll2Layout.setVerticalGroup(
            panel_empDetails_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empDetails_payroll2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panel_empDetails_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_fname_allowance2)
                    .addComponent(txt_fname_violation1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_lname_allowance2)
                    .addComponent(txt_lname_violation1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_desig_allowance2)
                    .addComponent(txt_position_violation1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_depart_allowance2)
                    .addComponent(txt_depart_violation1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        panel_salAllow_payroll2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Violation Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18), new java.awt.Color(255, 0, 0)));

        jLabel13.setText("Date:");

        jLabel22.setText("Violation:");

        txt_date1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_date1ActionPerformed(evt);
            }
        });

        txt_violation1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_violation1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panel_salAllow_payroll2Layout = new javax.swing.GroupLayout(panel_salAllow_payroll2);
        panel_salAllow_payroll2.setLayout(panel_salAllow_payroll2Layout);
        panel_salAllow_payroll2Layout.setHorizontalGroup(
            panel_salAllow_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_salAllow_payroll2Layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(panel_salAllow_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel22)
                    .addComponent(jLabel13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_salAllow_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_date1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_violation1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        panel_salAllow_payroll2Layout.setVerticalGroup(
            panel_salAllow_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_salAllow_payroll2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(panel_salAllow_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(txt_date1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_salAllow_payroll2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txt_violation1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        btn_exit_leave1.setText("Exit");
        btn_exit_leave1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exit_leave1ActionPerformed(evt);
            }
        });

        jButton2.setText("Submit");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout intFrame_violationentryLayout = new javax.swing.GroupLayout(intFrame_violationentry.getContentPane());
        intFrame_violationentry.getContentPane().setLayout(intFrame_violationentryLayout);
        intFrame_violationentryLayout.setHorizontalGroup(
            intFrame_violationentryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(intFrame_violationentryLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(intFrame_violationentryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panel_salAllow_payroll2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panel_empDetails_payroll2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, intFrame_violationentryLayout.createSequentialGroup()
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(353, 353, 353)
                .addComponent(btn_exit_leave1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(291, 291, 291))
        );
        intFrame_violationentryLayout.setVerticalGroup(
            intFrame_violationentryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(intFrame_violationentryLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(panel_empDetails_payroll2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panel_salAllow_payroll2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(intFrame_violationentryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_exit_leave1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(137, Short.MAX_VALUE))
        );

        getContentPane().add(intFrame_violationentry);
        intFrame_violationentry.setBounds(0, 0, 1200, 680);

        intFrame_viewuniform.setClosable(true);
        intFrame_viewuniform.setVisible(false);

        btn_exit_payroll2.setText("Exit");
        btn_exit_payroll2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_exit_payroll2ActionPerformed(evt);
            }
        });

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "First Name", "Last Name", "Position", "Department", "Date", "Size", "Quantity"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTable2.setAutoResizeMode(0);
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(200);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(200);
            jTable2.getColumnModel().getColumn(4).setPreferredWidth(200);
            jTable2.getColumnModel().getColumn(5).setPreferredWidth(200);
        }

        jButton13.setText("Display All");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });

        jLabel66.setText("Search by Date:");

        jButton14.setText("Export");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });

        jButton15.setText("Search");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        jButton16.setText("Search by Employee Name");
        jButton16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton16ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout intFrame_viewuniformLayout = new javax.swing.GroupLayout(intFrame_viewuniform.getContentPane());
        intFrame_viewuniform.getContentPane().setLayout(intFrame_viewuniformLayout);
        intFrame_viewuniformLayout.setHorizontalGroup(
            intFrame_viewuniformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(intFrame_viewuniformLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_exit_payroll2, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(972, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, intFrame_viewuniformLayout.createSequentialGroup()
                .addGroup(intFrame_viewuniformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(intFrame_viewuniformLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(intFrame_viewuniformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(intFrame_viewuniformLayout.createSequentialGroup()
                                .addComponent(jLabel66)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_uniformdate1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(intFrame_viewuniformLayout.createSequentialGroup()
                                .addComponent(jButton16)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton13))))
                    .addGroup(intFrame_viewuniformLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1019, Short.MAX_VALUE)))
                .addGap(153, 153, 153))
        );
        intFrame_viewuniformLayout.setVerticalGroup(
            intFrame_viewuniformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(intFrame_viewuniformLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(intFrame_viewuniformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_uniformdate1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel66)
                    .addComponent(jButton15)
                    .addComponent(jButton14, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(intFrame_viewuniformLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton16)
                    .addComponent(jButton13))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(155, 155, 155)
                .addComponent(btn_exit_payroll2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(intFrame_viewuniform);
        intFrame_viewuniform.setBounds(0, 0, 1190, 680);

        intFrame_benefits.setClosable(true);
        intFrame_benefits.setResizable(true);
        intFrame_benefits.setTitle("Benefits Details");
        intFrame_benefits.setMaximumSize(new java.awt.Dimension(800, 500));
        intFrame_benefits.setMinimumSize(new java.awt.Dimension(800, 500));
        intFrame_benefits.setPreferredSize(new java.awt.Dimension(800, 500));
        intFrame_benefits.setVisible(false);

        panel_empDetails_payroll1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Employee Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18), new java.awt.Color(255, 0, 0))); // NOI18N

        lbl_fname_allowance1.setText("First Name");

        lbl_lname_allowance1.setText("Last Name");

        lbl_desig_allowance1.setText("Position");

        lbl_depart_allowance1.setText("Department");

        txt_lname_benefits.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_lname_benefitsActionPerformed(evt);
            }
        });

        lbl_fname_allowance3.setText("Middle Name");

        javax.swing.GroupLayout panel_empDetails_payroll1Layout = new javax.swing.GroupLayout(panel_empDetails_payroll1);
        panel_empDetails_payroll1.setLayout(panel_empDetails_payroll1Layout);
        panel_empDetails_payroll1Layout.setHorizontalGroup(
            panel_empDetails_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empDetails_payroll1Layout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addComponent(lbl_fname_allowance1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_empDetails_payroll1Layout.createSequentialGroup()
                .addContainerGap(58, Short.MAX_VALUE)
                .addGroup(panel_empDetails_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbl_fname_allowance3)
                    .addComponent(lbl_lname_allowance1)
                    .addComponent(lbl_desig_allowance1)
                    .addComponent(lbl_depart_allowance1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panel_empDetails_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_fname_benefits, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_mname_benefits, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_lname_benefits, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_position_benefits, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_depart_benefits, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(67, 67, 67))
        );
        panel_empDetails_payroll1Layout.setVerticalGroup(
            panel_empDetails_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panel_empDetails_payroll1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(panel_empDetails_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_fname_allowance1)
                    .addComponent(txt_fname_benefits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_fname_allowance3)
                    .addComponent(txt_mname_benefits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(9, 9, 9)
                .addGroup(panel_empDetails_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_lname_allowance1)
                    .addComponent(txt_lname_benefits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_desig_allowance1)
                    .addComponent(txt_position_benefits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panel_empDetails_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_depart_allowance1)
                    .addComponent(txt_depart_benefits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panel_salAllow_payroll1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Benefits"
            + " Details", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 18), new java.awt.Color(255, 0, 0)));

    jLabel12.setText("SSS:");

    jLabel21.setText("PHILHEALTH:");

    txt_benefits_SSS.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txt_benefits_SSSActionPerformed(evt);
        }
    });

    txt_benefits_philhealth.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txt_benefits_philhealthActionPerformed(evt);
        }
    });

    jLabel23.setText("PAG-IBIG:");

    txt_benefits_pagibig.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txt_benefits_pagibigActionPerformed(evt);
        }
    });

    jLabel24.setText("TIN:");

    txt_benefits_tin.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            txt_benefits_tinActionPerformed(evt);
        }
    });

    jButton3.setText("Copy");
    jButton3.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton3ActionPerformed(evt);
        }
    });

    jButton22.setText("Copy");
    jButton22.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton22ActionPerformed(evt);
        }
    });

    jButton23.setText("Copy");
    jButton23.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton23ActionPerformed(evt);
        }
    });

    jButton24.setText("Copy");
    jButton24.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton24ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout panel_salAllow_payroll1Layout = new javax.swing.GroupLayout(panel_salAllow_payroll1);
    panel_salAllow_payroll1.setLayout(panel_salAllow_payroll1Layout);
    panel_salAllow_payroll1Layout.setHorizontalGroup(
        panel_salAllow_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(panel_salAllow_payroll1Layout.createSequentialGroup()
            .addGap(32, 32, 32)
            .addGroup(panel_salAllow_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jLabel23)
                .addComponent(jLabel21)
                .addComponent(jLabel12)
                .addComponent(jLabel24))
            .addGap(18, 18, 18)
            .addGroup(panel_salAllow_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(txt_benefits_philhealth)
                .addComponent(txt_benefits_pagibig, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE)
                .addComponent(txt_benefits_SSS)
                .addComponent(txt_benefits_tin, javax.swing.GroupLayout.DEFAULT_SIZE, 167, Short.MAX_VALUE))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(panel_salAllow_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jButton3)
                .addComponent(jButton22)
                .addComponent(jButton23)
                .addComponent(jButton24))
            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );
    panel_salAllow_payroll1Layout.setVerticalGroup(
        panel_salAllow_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panel_salAllow_payroll1Layout.createSequentialGroup()
            .addGap(12, 12, 12)
            .addGroup(panel_salAllow_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel12)
                .addComponent(txt_benefits_SSS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton3))
            .addGap(18, 18, 18)
            .addGroup(panel_salAllow_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel21)
                .addComponent(txt_benefits_philhealth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton22))
            .addGap(18, 18, 18)
            .addGroup(panel_salAllow_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel23)
                .addComponent(txt_benefits_pagibig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton23))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
            .addGroup(panel_salAllow_payroll1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(jLabel24)
                .addComponent(txt_benefits_tin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jButton24))
            .addGap(56, 56, 56))
    );

    btn_exit_leave.setText("Exit");
    btn_exit_leave.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            btn_exit_leaveActionPerformed(evt);
        }
    });

    jButton1.setText("Update");
    jButton1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton1ActionPerformed(evt);
        }
    });

    jButton20.setText("Search");
    jButton20.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            jButton20ActionPerformed(evt);
        }
    });

    javax.swing.GroupLayout intFrame_benefitsLayout = new javax.swing.GroupLayout(intFrame_benefits.getContentPane());
    intFrame_benefits.getContentPane().setLayout(intFrame_benefitsLayout);
    intFrame_benefitsLayout.setHorizontalGroup(
        intFrame_benefitsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(intFrame_benefitsLayout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addGroup(intFrame_benefitsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(panel_salAllow_payroll1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(panel_empDetails_payroll1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(27, 27, 27)
            .addGroup(intFrame_benefitsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                .addComponent(jButton20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_exit_leave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addContainerGap(635, Short.MAX_VALUE))
    );
    intFrame_benefitsLayout.setVerticalGroup(
        intFrame_benefitsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
        .addGroup(intFrame_benefitsLayout.createSequentialGroup()
            .addGroup(intFrame_benefitsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(intFrame_benefitsLayout.createSequentialGroup()
                    .addGap(180, 180, 180)
                    .addComponent(jButton20, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(btn_exit_leave, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(intFrame_benefitsLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(panel_empDetails_payroll1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(panel_salAllow_payroll1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(213, Short.MAX_VALUE))
    );

    panel_salAllow_payroll1.getAccessibleContext().setAccessibleName("");

    getContentPane().add(intFrame_benefits);
    intFrame_benefits.setBounds(0, 0, 1200, 680);

    lbl_pms.setFont(new java.awt.Font("URW Palladio L", 1, 48)); // NOI18N
    lbl_pms.setForeground(new java.awt.Color(36, 121, 158));
    lbl_pms.setText("Benefits Monitoring System");
    getContentPane().add(lbl_pms);
    lbl_pms.setBounds(280, 110, 640, 60);

    lbl_background.setIcon(new javax.swing.ImageIcon("C:\\Users\\User\\Desktop\\Dota Aero\\DOAV_LOGO-removebg-preview.png")); // NOI18N
    getContentPane().add(lbl_background);
    lbl_background.setBounds(300, 10, 1200, 700);

    menu_menuBar.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

    menuBar_file.setText("   File   ");
    menuBar_file.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            menuBar_fileActionPerformed(evt);
        }
    });

    menuBar_file_logout.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    menuBar_file_logout.setText("Log Out");
    menuBar_file_logout.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            menuBar_file_logoutActionPerformed(evt);
        }
    });
    menuBar_file.add(menuBar_file_logout);

    menuBar_file_exit.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    menuBar_file_exit.setText("Exit");
    menuBar_file_exit.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            menuBar_file_exitActionPerformed(evt);
        }
    });
    menuBar_file.add(menuBar_file_exit);

    menu_menuBar.add(menuBar_file);

    menuBar_employee.setText("   Employee   ");
    menuBar_employee.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            menuBar_employeeActionPerformed(evt);
        }
    });

    menuBar_employee_new.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    menuBar_employee_new.setText("New Employee");
    menuBar_employee_new.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            menuBar_employee_newActionPerformed(evt);
        }
    });
    menuBar_employee.add(menuBar_employee_new);

    menuBar_employee_update.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    menuBar_employee_update.setText("Update Employee");
    menuBar_employee_update.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            menuBar_employee_updateActionPerformed(evt);
        }
    });
    menuBar_employee.add(menuBar_employee_update);

    menuBar_employee_delete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    menuBar_employee_delete.setText("Delete Employee");
    menuBar_employee_delete.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            menuBar_employee_deleteActionPerformed(evt);
        }
    });
    menuBar_employee.add(menuBar_employee_delete);

    menuBar_employee_search.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_DOWN_MASK));
    menuBar_employee_search.setText("Search Employee");
    menuBar_employee_search.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            menuBar_employee_searchActionPerformed(evt);
        }
    });
    menuBar_employee.add(menuBar_employee_search);

    menu_menuBar.add(menuBar_employee);

    menuBar_leave1.setText("Benefits");

    menuBar_leave_apply1.setText("Update Benefits");
    menuBar_leave_apply1.addActionListener(new java.awt.event.ActionListener() {
        public void actionPerformed(java.awt.event.ActionEvent evt) {
            menuBar_leave_apply1ActionPerformed(evt);
        }
    });
    menuBar_leave1.add(menuBar_leave_apply1);

    menu_menuBar.add(menuBar_leave1);

    setJMenuBar(menu_menuBar);

    pack();
    }// </editor-fold>//GEN-END:initComponents
//Change Title bar Icon of the Form

    public void changeIcon() {
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("images/icon.png")));
    }

//Disable menu on form initialization    
    public void disableMenu() {

        //menuBar_employee.setEnabled(false);
        //menuBar_payroll.setEnabled(false);
        //menuBar_paySlip.setEnabled(false);
    }

//Add radio buttons to a group
    public void addButtonGroup() {

        //btnGroup_rd.add(rd_male);
        //btnGroup_rd.add(rd_female);
    }

//clear new employee form    
    public void clearEmployeeNew() {
        //txt_empID.setText(null);
        //txt_nic.setText(null);
        txt_fname.setText(null);
        txt_lname.setText(null);
        txt_mname.setText(null);
        //txt_address.setText(null);
        //txt_city.setText(null);
        //txt_dob.setText(null);
        //txt_dateJoin.setText(null);
        txt_deparment.setText(null);
        txt_designation.setText(null);
        //txt_telHome.setText(null);
        //txt_telMobile.setText(null);
    }

//clear update employee form    
    public void clearEmployeeUpdate() {
        //txt_empID_update.setText(null);
        //txt_nic_update.setText(null);
        txt_fname_update.setText(null);
        txt_mname_update.setText(null);
        txt_lname_update.setText(null);
        //txt_address_update.setText(null);
        //txt_city_update.setText(null);
        //txt_dob_update.setText(null);
        //txt_dateJoin_update.setText(null);
        txt_deparment_update.setText(null);
        txt_designation_update.setText(null);
        //txt_telHome_update.setText(null);
        //txt_telMobile_update.setText(null);

    }
    
    public void clearBenefits() {
        //txt_empID_update.setText(null);
        //txt_nic_update.setText(null);
        txt_fname_benefits.setText(null);
        txt_mname_benefits.setText(null);
        txt_lname_benefits.setText(null);
        txt_position_benefits.setText(null);
        txt_depart_benefits.setText(null);
        txt_benefits_SSS.setText(null);
        txt_benefits_philhealth.setText(null);
        txt_benefits_pagibig.setText(null);
        txt_benefits_tin.setText(null);
        //txt_telHome_update.setText(null);
        //txt_telMobile_update.setText(null);

    }

//clear leave form    
    public void clearLeave() {

        txt_fname_benefits.setText(null);
        txt_lname_benefits.setText(null);
        txt_position_benefits.setText(null);
        txt_depart_benefits.setText(null);

    }

//validate new employee fields    
    public boolean validateEmployeeNew() {
        /*
        //if (txt_empID.getText().isEmpty()
                //|| txt_nic.getText().isEmpty()
                //|| txt_fname.getText().isEmpty()
                //|| txt_lname.getText().isEmpty()
           //     || txt_dob.getText().isEmpty()
                || txt_designation.getText().isEmpty()
                || txt_deparment.getText().isEmpty())
         //       || txt_dateJoin.getText().isEmpty()) 
        {
            return false;
        } else {
            return true;
        }
         */
        return true;
    }

//validate update employee form fields    
    public boolean validateEmployeeUpdate() {


            return true;
        
    }

//hide frames on opening a new form    
    public void hideFrames() {

        intFrame_employee_new.setVisible(false);
        intFrame_employee_update.setVisible(false);
        //intFrame_payroll.setVisible(false);
        //intFrame_print.setVisible(false);
        intFrame_employee_search.setVisible(false);
        intFrame_benefits.setVisible(false);

    }

//dialog box to get employee id    
    public String getEmpId() {

        return JOptionPane.showInputDialog("Enter Employee ID");

    }

    private void menuBar_fileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_fileActionPerformed

    }//GEN-LAST:event_menuBar_fileActionPerformed

    private void menuBar_file_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_file_exitActionPerformed

        System.exit(0);
    }//GEN-LAST:event_menuBar_file_exitActionPerformed

    private void btn_exitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exitActionPerformed

        intFrame_employee_new.setVisible(false);
    }//GEN-LAST:event_btn_exitActionPerformed

    private void btn_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_addActionPerformed

        if (!validateEmployeeNew()) {

            JOptionPane.showMessageDialog(null, "Fields Marked with * are Mandatory", "ERROR", 0);

        } else {

            //objEmployee.setEmpId(txt_empID.getText());
            //objEmployee.setNic(txt_nic.getText());
            objEmployee.setFname(txt_fname.getText());
            objEmployee.setMname(txt_mname.getText());
            objEmployee.setLname(txt_lname.getText());
            //objEmployee.setDob(txt_dob.getText());
            //objEmployee.setAddress(txt_address.getText());
            // objEmployee.setCity(txt_city.getText());
            // objEmployee.setTel_home(txt_telHome.getText());
            // objEmployee.setTel_mobile(txt_telMobile.getText());
            objEmployee.setDesignation(txt_designation.getText());
            objEmployee.setDepartment(txt_deparment.getText());
            //objEmployee.setVacationleave(Double.parseDouble(txt_vacleave.getText()));
            //objEmployee.setSickleave(Double.parseDouble(txt_sicleave.getText()));
            // objEmployee.setDateOfJoining(txt_dateJoin.getText());
            // objEmployee.setSalType(combo_salType.getSelectedItem().toString());

            /* if (rd_male.isSelected()) {

                objEmployee.setGender(rd_male.getText());

            } else if (rd_female.isSelected()) {

                objEmployee.setGender(rd_female.getText());

            }
             */
            if (objEmployee.insertEmployee()) {
                clearEmployeeNew();
            }

        }


    }//GEN-LAST:event_btn_addActionPerformed

    private void menuBar_file_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_file_logoutActionPerformed

        Login loginForm = new Login();
        loginForm.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_menuBar_file_logoutActionPerformed

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed

        if (!validateEmployeeUpdate()) {

            JOptionPane.showMessageDialog(null, "Fields Marked with * are Mandatory", "ERROR", 0);

        } else {


            objEmployee.setFname(txt_fname_update.getText());
            objEmployee.setMname(txt_mname_update.getText());
            objEmployee.setLname(txt_lname_update.getText());
            objEmployee.setDesignation(txt_designation_update.getText());
            objEmployee.setDepartment(txt_deparment_update.getText());

            if (objEmployee.updateEmployee()) {
                clearEmployeeUpdate();
            }
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_exit_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exit_updateActionPerformed

        intFrame_employee_update.setVisible(false);
    }//GEN-LAST:event_btn_exit_updateActionPerformed

    private void btn_search_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_search_updateActionPerformed
            jDialog10.show();
        

        
    }//GEN-LAST:event_btn_search_updateActionPerformed

    private void btn_exit_leaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exit_leaveActionPerformed

        intFrame_benefits.setVisible(false);
    }//GEN-LAST:event_btn_exit_leaveActionPerformed


    private void btn_exit_payroll1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exit_payroll1ActionPerformed
        intFrame_cutoff.setVisible(false);
    }//GEN-LAST:event_btn_exit_payroll1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

        model = (DefaultTableModel) jTable1.getModel();
        String sql = "SELECT * FROM violationlog";
        loadData(sql);
        //txtDate2.setText(null);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        exportarExcel(jTable1);
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        model2 = (DefaultTableModel) jTable1.getModel();
        String date = txt_violationdate.getText();
        loadData("SELECT * FROM violationlog WHERE Date = '" + date + "'");
        //txtDate2.setText(null);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void txt_benefits_SSSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_benefits_SSSActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_benefits_SSSActionPerformed

    private void txt_benefits_philhealthActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_benefits_philhealthActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_benefits_philhealthActionPerformed

    private void leave_empfnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leave_empfnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_leave_empfnameActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        String empfname = leave_empfname.getText();
        String emplname = leave_emplname.getText();
        if (objLeave.getLeaveDetailsvianame(empfname, emplname)) {

            txt_fname_benefits.setText(objLeave.objEmployee.getFname());
            txt_lname_benefits.setText(objLeave.objEmployee.getLname());
            txt_position_benefits.setText(objLeave.objEmployee.getDesignation());
            txt_depart_benefits.setText(objLeave.objEmployee.getDepartment());

            objEmployee.setFname(txt_fname_benefits.getText());
            objEmployee.setLname(txt_lname_benefits.getText());
            objEmployee.setDesignation(txt_position_benefits.getText());
        }

        jDialog3.setVisible(false);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void leave_emplnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_leave_emplnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_leave_emplnameActionPerformed

    private void btn_searchEmp1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchEmp1ActionPerformed
        jDialog4.setVisible(true);

    }//GEN-LAST:event_btn_searchEmp1ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        //String empfname = employee_empfname1.getText();
        String emplname = employee_emplname1.getText();
        String sql = MessageFormat.format("SELECT * FROM empbenefits WHERE lname=''{0}''", emplname);
        empDetails = new JTable(objEmployee.getAllEmployeeDetails(objEmployee.getAllEmployeeDetails(sql)), objEmployee.getColumnNames(objEmployee.getAllEmployeeDetails(sql)));
        jScrollPane_tableContainer.setViewportView(empDetails);
        jDialog4.setVisible(false);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void employee_emplname1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_emplname1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_emplname1ActionPerformed

    private void employee_desigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_desigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_desigActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        String desig = employee_desig.getText();
        String sql = "SELECT * FROM employee WHERE designation='" + desig + "'";
        JTable empDetails = new JTable(objEmployee.getAllEmployeeDetails(objEmployee.getAllEmployeeDetails(sql)), objEmployee.getColumnNames(objEmployee.getAllEmployeeDetails(sql)));
        jScrollPane_tableContainer.setViewportView(empDetails);
        jDialog5.setVisible(false);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void btn_searchEmp3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_searchEmp3ActionPerformed
        jDialog6.setVisible(true);
    }//GEN-LAST:event_btn_searchEmp3ActionPerformed

    private void employee_deptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employee_deptActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employee_deptActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        String dept = employee_dept.getText();
        String sql = "SELECT * FROM empbenefits WHERE department='" + dept + "'";
        JTable empDetails = new JTable(objEmployee.getAllEmployeeDetails(objEmployee.getAllEmployeeDetails(sql)), objEmployee.getColumnNames(objEmployee.getAllEmployeeDetails(sql)));
        jScrollPane_tableContainer.setViewportView(empDetails);
        jDialog6.setVisible(false);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        jDialog7.setVisible(true);
    }//GEN-LAST:event_jButton5ActionPerformed

    private void log_empfnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_log_empfnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_log_empfnameActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        model3 = (DefaultTableModel) jTable1.getModel();
        String logfname = log_empfname.getText();
        String loglname = log_emplname.getText();
        loadData("SELECT * FROM violationlog WHERE `First Name` = '" + logfname + "' AND `Last Name` = '" + loglname + "'");

        jDialog7.setVisible(false);
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        SQLRun objSQLRun = new SQLRun();
        String benefits_fname = txt_fname_benefits.getText();
        String benefits_mname = txt_mname_benefits.getText();
        String benefits_lname = txt_lname_benefits.getText();
        String benefits_position = txt_position_benefits.getText();
        String benefits_department = txt_depart_benefits.getText();
        String benefits_sss = txt_benefits_SSS.getText();
        String benefits_philhealth = txt_benefits_philhealth.getText();
        String benefits_pagibig = txt_benefits_pagibig.getText();
        String benefits_tin = txt_benefits_tin.getText();

        String sql = "UPDATE empbenefits set fname='" + benefits_fname + "', mname ='" + benefits_mname + "', lname='" + benefits_lname + "',position='" + benefits_position + "',"
                + "department='" + benefits_department + "', sss = '" + benefits_sss + "', philhealth = '" + benefits_philhealth + "', pagibig = '" + benefits_pagibig + "', tin = '" + benefits_tin + "' WHERE fname ='" + benefits_fname + "' AND lname ='" + benefits_lname + "'";
 
        int inserted = objSQLRun.sqlUpdate(sql);

        if (inserted > 0) {
            JOptionPane.showMessageDialog(null, "Entry for " + benefits_fname + " " + benefits_mname + " has been updated "
                    + "to the system successfully", "Success", 1);
            clearBenefits();

        } else {
            JOptionPane.showMessageDialog(null, "Error occurred while trying to add entry for "
                    + "" + benefits_fname + " " + benefits_mname + " to the system", "ERROR", 0);

        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txt_lname_benefitsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_lname_benefitsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_lname_benefitsActionPerformed

    private void menuBar_leave_apply1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_leave_apply1ActionPerformed
        hideFrames();
        intFrame_benefits.setVisible(true);
    }//GEN-LAST:event_menuBar_leave_apply1ActionPerformed

    private void txt_lname_violation1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_lname_violation1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_lname_violation1ActionPerformed

    private void txt_date1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_date1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_date1ActionPerformed

    private void txt_violation1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_violation1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_violation1ActionPerformed

    private void btn_exit_leave1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exit_leave1ActionPerformed
        intFrame_violationentry.setVisible(false);
    }//GEN-LAST:event_btn_exit_leave1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        SQLRun objSQLRun = new SQLRun();
        String violationfirstName = txt_fname_violation1.getText();
        String violationlastName = txt_lname_violation1.getText();
        String violationDepartment = txt_depart_violation1.getText();
        String violation = txt_violation1.getText();
        String violationDate = txt_date1.getText();

        String sql = "INSERT INTO `violationlog`(`First Name`, `Last Name`, `Department`, `Violation`, `Date`) VALUES ('" + violationfirstName + "', '" + violationlastName + "', '" + violationDepartment + "', '" + violation + "', '" + violationDate + "')";

        int inserted = objSQLRun.sqlUpdate(sql);

        if (inserted > 0) {
            JOptionPane.showMessageDialog(null, "Violation of " + violationfirstName + " " + violationlastName + " has been added "
                    + "to the system successfully", "Success", 1);

        } else {
            JOptionPane.showMessageDialog(null, "Error occurred while trying to add entry for "
                    + "" + violationfirstName + " " + violationlastName + " to the system", "ERROR", 0);

        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txt_benefits_pagibigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_benefits_pagibigActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_benefits_pagibigActionPerformed

    private void log_emplnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_log_emplnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_log_emplnameActionPerformed

    private void btn_exit_payroll2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_exit_payroll2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btn_exit_payroll2ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed
        model3 = (DefaultTableModel) jTable2.getModel();
        String sql = "SELECT * FROM uniformlog";
        loadDataUniform(sql);
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
         exportarExcel(jTable2);
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        model3 = (DefaultTableModel) jTable2.getModel();
        String date = txt_uniformdate1.getText();
        loadDataUniform("SELECT * FROM uniformlog WHERE dateclaimed = '" + date + "'");
    }//GEN-LAST:event_jButton15ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        jDialog8.setVisible(true);
    }//GEN-LAST:event_jButton16ActionPerformed

    private void log_empfname1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_log_empfname1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_log_empfname1ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        model3 = (DefaultTableModel) jTable2.getModel();
        String logfname = log_empfname1.getText();
        String loglname = log_emplname1.getText();
        loadDataUniform("SELECT * FROM uniformlog WHERE fname = '" + logfname + "' AND lname = '" + loglname + "'");

        jDialog7.setVisible(false);
    }//GEN-LAST:event_jButton17ActionPerformed

    private void log_emplname1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_log_emplname1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_log_emplname1ActionPerformed

    private void menuBar_employee_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_employee_newActionPerformed

        hideFrames();
        intFrame_employee_new.setVisible(true);
    }//GEN-LAST:event_menuBar_employee_newActionPerformed

    private void menuBar_employee_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_employee_updateActionPerformed

        hideFrames();
        intFrame_employee_update.setVisible(true);
    }//GEN-LAST:event_menuBar_employee_updateActionPerformed

    private void menuBar_employee_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_employee_deleteActionPerformed

        hideFrames();
        jDialog9.show();
    }//GEN-LAST:event_menuBar_employee_deleteActionPerformed

    private void menuBar_employee_searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_employee_searchActionPerformed
        //populate table with employee details
        hideFrames();
        intFrame_employee_search.setVisible(true);
        String sql = "SELECT * FROM empbenefits";
        JTable empDetails = new JTable(objEmployee.getAllEmployeeDetails(objEmployee.getAllEmployeeDetails(sql)), objEmployee.getColumnNames(objEmployee.getAllEmployeeDetails(sql)));
        jScrollPane_tableContainer.setViewportView(empDetails);
    }//GEN-LAST:event_menuBar_employee_searchActionPerformed

    private void menuBar_employeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuBar_employeeActionPerformed

    }//GEN-LAST:event_menuBar_employeeActionPerformed

    private void txt_fnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_fnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_fnameActionPerformed

    private void txt_mnameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_mnameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_mnameActionPerformed

    private void del_employee_empfname2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_del_employee_empfname2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_del_employee_empfname2ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        String delfname = del_employee_empfname2.getText();
        String dellname = del_employee_emplname2.getText();
        SQLRun objSQLRun = new SQLRun();
        String sql = "DELETE FROM empbenefits WHERE fname='" + delfname + "' AND lname ='" + dellname + "'";

        int deleted = objSQLRun.sqlUpdate(sql);

        if (deleted > 0) {
            JOptionPane.showMessageDialog(null, "Employee " + delfname + " " + dellname + " has been deleted successfully", "ERROR", 1);
            jDialog9.dispose();

        } else {
            
                JOptionPane.showMessageDialog(null, "Employee " + delfname + " " + dellname + " does not exist", "ERROR", 0);
                
            
        }
        
    }//GEN-LAST:event_jButton18ActionPerformed

    private void del_employee_emplname2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_del_employee_emplname2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_del_employee_emplname2ActionPerformed

    private void updt_empfname2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updt_empfname2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updt_empfname2ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        String updtempfname = updt_empfname2.getText();
        String updtemplname = updt_emplname2.getText();
        if(objEmployee.getEmployeeDetailsViaName(updtempfname,updtemplname)){
            txt_fname_update.setText(objEmployee.getFname());
            txt_mname_update.setText(objEmployee.getMname());
            txt_lname_update.setText(objEmployee.getLname());
            txt_designation_update.setText(objEmployee.getDesignation());
            txt_deparment_update.setText(objEmployee.getDepartment());
            jDialog10.dispose();
        }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void updt_emplname2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updt_emplname2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_updt_emplname2ActionPerformed

    private void txt_benefits_tinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_benefits_tinActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_benefits_tinActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
       jDialog11.setVisible(true);
    }//GEN-LAST:event_jButton20ActionPerformed

    private void benefitssearch_empfname3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_benefitssearch_empfname3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_benefitssearch_empfname3ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        String updtempfname = benefitssearch_empfname3.getText();
        String updtemplname = benefitssearch_emplname3.getText();
        if(objEmployee.getEmployeeBenefitsDetailsViaName(updtempfname,updtemplname)){
            txt_fname_benefits.setText(objEmployee.getFname());
            txt_mname_benefits.setText(objEmployee.getMname());
            txt_lname_benefits.setText(objEmployee.getLname());
            txt_position_benefits.setText(objEmployee.getDesignation());
            txt_depart_benefits.setText(objEmployee.getDepartment());
            txt_benefits_SSS.setText(objEmployee.getsss());
            txt_benefits_philhealth.setText(objEmployee.getphilhealth());
            txt_benefits_pagibig.setText(objEmployee.getpagibig());
            txt_benefits_tin.setText(objEmployee.gettin());
            jDialog11.dispose();
        }
    }//GEN-LAST:event_jButton21ActionPerformed

    private void benefitssearch_emplname3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_benefitssearch_emplname3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_benefitssearch_emplname3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        copyToClipboard(txt_benefits_SSS.getText());
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        copyToClipboard(txt_benefits_philhealth.getText());
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
       copyToClipboard(txt_benefits_pagibig.getText());
    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        copyToClipboard(txt_benefits_tin.getText());
    }//GEN-LAST:event_jButton24ActionPerformed

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
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    class PrintableImage implements Printable {

        private BufferedImage image;

        PrintableImage(BufferedImage image) {
            this.image = image;
        }

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) graphics;
            double scaleX = pageFormat.getImageableWidth() / image.getWidth();
            double scaleY = pageFormat.getImageableHeight() / image.getHeight();
            double scaleFactor = Math.min(scaleX, scaleY);
            int scaledWidth = (int) (image.getWidth() * scaleFactor);
            int scaledHeight = (int) (image.getHeight() * scaleFactor);

            // Center the image on the page
            int x = (int) ((pageFormat.getImageableWidth() - scaledWidth) / 2);
            int y = (int) ((pageFormat.getImageableHeight() - scaledHeight) / 2);

            g2d.drawImage(image, x, y, scaledWidth, scaledHeight, null);
            return PAGE_EXISTS;
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField benefitssearch_empfname3;
    private javax.swing.JTextField benefitssearch_emplname3;
    private javax.swing.ButtonGroup btnGroup_rd;
    private javax.swing.JButton btn_add;
    private javax.swing.JButton btn_exit;
    private javax.swing.JButton btn_exit_leave;
    private javax.swing.JButton btn_exit_leave1;
    private javax.swing.JButton btn_exit_payroll1;
    private javax.swing.JButton btn_exit_payroll2;
    private javax.swing.JButton btn_exit_update;
    private javax.swing.JButton btn_searchEmp1;
    private javax.swing.JButton btn_searchEmp3;
    private javax.swing.JButton btn_search_update;
    private javax.swing.JButton btn_update;
    private javax.swing.JTextField del_employee_empfname2;
    private javax.swing.JTextField del_employee_emplname2;
    private javax.swing.JTextField employee_dept;
    private javax.swing.JTextField employee_desig;
    private javax.swing.JTextField employee_emplname1;
    private javax.swing.JInternalFrame intFrame_benefits;
    private javax.swing.JInternalFrame intFrame_cutoff;
    private javax.swing.JInternalFrame intFrame_employee_new;
    private javax.swing.JInternalFrame intFrame_employee_search;
    private javax.swing.JInternalFrame intFrame_employee_update;
    private javax.swing.JInternalFrame intFrame_viewuniform;
    private javax.swing.JInternalFrame intFrame_violationentry;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JDialog jDialog10;
    private javax.swing.JDialog jDialog11;
    private javax.swing.JDialog jDialog3;
    private javax.swing.JDialog jDialog4;
    private javax.swing.JDialog jDialog5;
    private javax.swing.JDialog jDialog6;
    private javax.swing.JDialog jDialog7;
    private javax.swing.JDialog jDialog8;
    private javax.swing.JDialog jDialog9;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel70;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane_tableContainer;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JLabel lbl_background;
    private javax.swing.JLabel lbl_depart_allowance1;
    private javax.swing.JLabel lbl_depart_allowance2;
    private javax.swing.JLabel lbl_department;
    private javax.swing.JLabel lbl_department1;
    private javax.swing.JLabel lbl_desig_allowance1;
    private javax.swing.JLabel lbl_desig_allowance2;
    private javax.swing.JLabel lbl_designation;
    private javax.swing.JLabel lbl_designation1;
    private javax.swing.JLabel lbl_fname;
    private javax.swing.JLabel lbl_fname1;
    private javax.swing.JLabel lbl_fname2;
    private javax.swing.JLabel lbl_fname3;
    private javax.swing.JLabel lbl_fname_allowance1;
    private javax.swing.JLabel lbl_fname_allowance2;
    private javax.swing.JLabel lbl_fname_allowance3;
    private javax.swing.JLabel lbl_lname;
    private javax.swing.JLabel lbl_lname1;
    private javax.swing.JLabel lbl_lname_allowance1;
    private javax.swing.JLabel lbl_lname_allowance2;
    private javax.swing.JLabel lbl_pms;
    private javax.swing.JTextField leave_empfname;
    private javax.swing.JTextField leave_emplname;
    private javax.swing.JTextField log_empfname;
    private javax.swing.JTextField log_empfname1;
    private javax.swing.JTextField log_emplname;
    private javax.swing.JTextField log_emplname1;
    private javax.swing.JMenu menuBar_employee;
    private javax.swing.JMenuItem menuBar_employee_delete;
    private javax.swing.JMenuItem menuBar_employee_new;
    private javax.swing.JMenuItem menuBar_employee_search;
    private javax.swing.JMenuItem menuBar_employee_update;
    private javax.swing.JMenu menuBar_file;
    private javax.swing.JMenuItem menuBar_file_exit;
    private javax.swing.JMenuItem menuBar_file_logout;
    private javax.swing.JMenu menuBar_leave1;
    private javax.swing.JMenuItem menuBar_leave_apply1;
    private javax.swing.JMenuBar menu_menuBar;
    private javax.swing.JPanel panel_empDetails;
    private javax.swing.JPanel panel_empDetails_payroll1;
    private javax.swing.JPanel panel_empDetails_payroll2;
    private javax.swing.JPanel panel_empUpdate;
    private javax.swing.JPanel panel_salAllow_payroll1;
    private javax.swing.JPanel panel_salAllow_payroll2;
    private javax.swing.JTextField txt_benefits_SSS;
    private javax.swing.JTextField txt_benefits_pagibig;
    private javax.swing.JTextField txt_benefits_philhealth;
    private javax.swing.JTextField txt_benefits_tin;
    private javax.swing.JTextField txt_date1;
    private javax.swing.JTextField txt_deparment;
    private javax.swing.JTextField txt_deparment_update;
    private javax.swing.JTextField txt_depart_benefits;
    private javax.swing.JTextField txt_depart_violation1;
    private javax.swing.JTextField txt_designation;
    private javax.swing.JTextField txt_designation_update;
    private javax.swing.JTextField txt_fname;
    private javax.swing.JTextField txt_fname_benefits;
    private javax.swing.JTextField txt_fname_update;
    private javax.swing.JTextField txt_fname_violation1;
    private javax.swing.JTextField txt_lname;
    private javax.swing.JTextField txt_lname_benefits;
    private javax.swing.JTextField txt_lname_update;
    private javax.swing.JTextField txt_lname_violation1;
    private javax.swing.JTextField txt_mname;
    private javax.swing.JTextField txt_mname_benefits;
    private javax.swing.JTextField txt_mname_update;
    private javax.swing.JTextField txt_position_benefits;
    private javax.swing.JTextField txt_position_violation1;
    private javax.swing.JTextField txt_uniformdate1;
    private javax.swing.JTextField txt_violation1;
    private javax.swing.JTextField txt_violationdate;
    private javax.swing.JTextField updt_empfname2;
    private javax.swing.JTextField updt_emplname2;
    // End of variables declaration//GEN-END:variables
}
