/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JInternalFrame.java to edit this template
 */
package vista;

import datos.AseguradoraDAO;
import datos.ClienteDAO;
import datos.PolizaDAO;
import datos.TipoSeguroDAO;
import java.sql.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import modelo.Aseguradora;
import modelo.Cliente;
import modelo.ComboItem;
import modelo.Poliza;
import modelo.TipoSeguro;

/**
 *
 * @author deino
 */
public class FrmPoliza extends javax.swing.JInternalFrame {
    private final PolizaDAO dao = new PolizaDAO();
    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final AseguradoraDAO aseguradoraDAO = new AseguradoraDAO();
    private final TipoSeguroDAO tipoSeguroDAO = new TipoSeguroDAO();
    
    private int idPolizaSeleccionada = 0;
    /**
     * Creates new form FrmPoliza
     */
    public FrmPoliza() {
        initComponents();
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Gestión de Pólizas");

        cargarCombos();
        listarPolizas("");
    }

    private void aplicarColoresTabla() {
    tablaPolizas.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
        @Override
        public java.awt.Component getTableCellRendererComponent(
                javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

            java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Asegura que la celda vuelva a renderizar su color de fondo
            setOpaque(true);

            if (!isSelected) {
                try {
                    // Columna 7: Fecha de Vencimiento
                    Object fechaObj = table.getValueAt(row, 7);

                    if (fechaObj != null) {
                        java.time.LocalDate fVencimiento = java.time.LocalDate.parse(fechaObj.toString().trim());
                        java.time.LocalDate hoy = java.time.LocalDate.now();

                        if (fVencimiento.isBefore(hoy)) {
                            c.setBackground(new java.awt.Color(255, 180, 180)); // Rojo suave (Vencida)
                            c.setForeground(java.awt.Color.BLACK);
                        } else if (!fVencimiento.isAfter(hoy.plusDays(30))) {
                            c.setBackground(new java.awt.Color(255, 235, 150)); // Amarillo suave (Próxima a vencer)
                        } else {
                            c.setBackground(java.awt.Color.WHITE);
                            c.setForeground(java.awt.Color.BLACK);
                        }
                    }
                } catch (Exception e) {
                    c.setBackground(java.awt.Color.WHITE);
                    c.setForeground(java.awt.Color.BLACK);
                }
            } else {
                c.setBackground(table.getSelectionBackground());
                c.setForeground(table.getSelectionForeground());
            }

            return c;
        }
    });
}
    
    private boolean validarCampos() {
    // 1. Validar selección de Combos
    ComboItem clienteSel = (ComboItem) cboCliente.getSelectedItem();
    ComboItem aseguradoraSel = (ComboItem) cboAseguradora.getSelectedItem();
    ComboItem tipoSel = (ComboItem) cboTipoSeguro.getSelectedItem();
    ComboItem agenteSel = (ComboItem) cboAgente.getSelectedItem();

    if (txtPoliza.getText().trim().isEmpty()) {
        JOptionPane.showMessageDialog(this, "Debe ingresar el número de póliza.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
        txtPoliza.requestFocus();
        return false;
    }

    if (clienteSel == null || clienteSel.getId() == 0 ||
        aseguradoraSel == null || aseguradoraSel.getId() == 0 ||
        tipoSel == null || tipoSel.getId() == 0 ||
        agenteSel == null || agenteSel.getId() == 0) {
        JOptionPane.showMessageDialog(this, "Debe seleccionar Cliente, Aseguradora, Tipo de Seguro y Agente.", "Campos Requeridos", JOptionPane.WARNING_MESSAGE);
        return false;
    }

    // 2. Validar Prima Anual (Decimal)
    try {
        double prima = Double.parseDouble(txtPrima.getText().trim().replace(",", "."));
        if (prima <= 0) {
            JOptionPane.showMessageDialog(this, "La prima anual debe ser un valor mayor a cero.", "Valor Inválido", JOptionPane.WARNING_MESSAGE);
            txtPrima.requestFocus();
            return false;
        }
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "La prima anual debe ser un número válido (ej. 15000.50).", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
        txtPrima.requestFocus();
        return false;
    }

    // 3. Validar Fechas (YYYY-MM-DD) y Coherencia Lógica
    try {
        Date fInicio = Date.valueOf(txtFechaInicio.getText().trim());
        Date fVencimiento = Date.valueOf(txtFechaVencimiento.getText().trim());

        if (!fVencimiento.after(fInicio)) {
            JOptionPane.showMessageDialog(this, "La fecha de vencimiento debe ser posterior a la fecha de inicio.", "Rango de Fechas Inválido", JOptionPane.WARNING_MESSAGE);
            txtFechaVencimiento.requestFocus();
            return false;
        }
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, "Las fechas deben tener el formato válido AAAA-MM-DD (ej. 2026-01-15).", "Formato de Fecha Inválido", JOptionPane.ERROR_MESSAGE);
        return false;
    }

    return true;
}
    
    private void cargarCombos() {
        // Cargar Clientes
        cboCliente.removeAllItems();
        cboCliente.addItem(new ComboItem(0, "-- Seleccionar Cliente --"));
        List<Cliente> clientes = clienteDAO.listar("");
        for (Cliente c : clientes) {
            cboCliente.addItem(new ComboItem(c.getIdCliente(), c.getNombre()));
        }

        cboAseguradora.removeAllItems();
        cboAseguradora.addItem(new ComboItem(0, "-- Seleccionar Aseguradora --"));
        List<Aseguradora> aseguradoras = aseguradoraDAO.listar("");
        for (Aseguradora a : aseguradoras) {
            cboAseguradora.addItem(new ComboItem(a.getIdAseguradora(), a.getNombreComercial()));
        }

        cboTipoSeguro.removeAllItems();
        cboTipoSeguro.addItem(new ComboItem(0, "-- Seleccionar Tipo --"));
        List<TipoSeguro> tipos = tipoSeguroDAO.listar("");
        for (TipoSeguro ts : tipos) {
            cboTipoSeguro.addItem(new ComboItem(ts.getIdTipoSeguro(), ts.getNombre()));
        }

        // Cargar Agentes (Por defecto asignamos el ID 1 o puedes listar de Usuarios)
        cboAgente.removeAllItems();
        cboAgente.addItem(new ComboItem(0, "-- Seleccionar Agente --"));
        cboAgente.addItem(new ComboItem(1, "Agente Principal")); // ID 1 estático para pruebas
        
        // Cargar Estado Combo
        cboEstado.removeAllItems();
        cboEstado.addItem("ACTIVA");
        cboEstado.addItem("VENCIDA");
        cboEstado.addItem("CANCELADA");
    }

    private void listarPolizas(String filtro) {
        DefaultTableModel modelo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        modelo.addColumn("ID");
        modelo.addColumn("N° Póliza");
        modelo.addColumn("Cliente");
        modelo.addColumn("Aseguradora");
        modelo.addColumn("Tipo");
        modelo.addColumn("Agente");
        modelo.addColumn("Fecha Inicio");
        modelo.addColumn("Fecha Vencimiento");
        modelo.addColumn("Prima Anual");
        modelo.addColumn("Estado");

        List<Poliza> lista = dao.listar(filtro);

        for (Poliza p : lista) {
            Object[] fila = new Object[10];
            fila[0] = p.getIdPoliza();
            fila[1] = p.getNumPoliza();
            fila[2] = p.getNombreCliente();
            fila[3] = p.getNombreAseguradora();
            fila[4] = p.getNombreTipoSeguro();
            fila[5] = p.getNombreAgente();
            fila[6] = p.getFechaInicio();
            fila[7] = p.getFechaVencimiento();
            fila[8] = String.format("%.2f", p.getPrimaAnual());
            fila[9] = p.getEstadoPoliza();
            modelo.addRow(fila);
        }

        tablaPolizas.setModel(modelo);
        aplicarColoresTabla();
    }

    private void limpiarFormulario() {
        idPolizaSeleccionada = 0;
        txtPoliza.setText("");
        if (cboCliente.getItemCount() > 0) cboCliente.setSelectedIndex(0);
        if (cboAseguradora.getItemCount() > 0) cboAseguradora.setSelectedIndex(0);
        if (cboTipoSeguro.getItemCount() > 0) cboTipoSeguro.setSelectedIndex(0);
        if (cboAgente.getItemCount() > 0) cboAgente.setSelectedIndex(0);
        txtFechaInicio.setText("");
        txtFechaVencimiento.setText("");
        txtPrima.setText("");
        if (cboEstado.getItemCount() > 0) cboEstado.setSelectedIndex(0);
        tablaPolizas.clearSelection();
    }

    private void seleccionarFilaTabla() {
        int fila = tablaPolizas.getSelectedRow();
        if (fila != -1) {
            idPolizaSeleccionada = Integer.parseInt(tablaPolizas.getValueAt(fila, 0).toString());
            txtPoliza.setText(tablaPolizas.getValueAt(fila, 1).toString());
            
            // Seleccionar elementos en los Combos por Texto
            seleccionarItemCombo(cboCliente, tablaPolizas.getValueAt(fila, 2).toString());
            seleccionarItemCombo(cboAseguradora, tablaPolizas.getValueAt(fila, 3).toString());
            seleccionarItemCombo(cboTipoSeguro, tablaPolizas.getValueAt(fila, 4).toString());
            seleccionarItemCombo(cboAgente, tablaPolizas.getValueAt(fila, 5).toString());
            
            txtFechaInicio.setText(tablaPolizas.getValueAt(fila, 6).toString());
            txtFechaVencimiento.setText(tablaPolizas.getValueAt(fila, 7).toString());
            
            String primaStr = tablaPolizas.getValueAt(fila, 8).toString().replace(",", ".");
            txtPrima.setText(primaStr);
            
            cboEstado.setSelectedItem(tablaPolizas.getValueAt(fila, 9).toString());
        }
    }

    private void seleccionarItemCombo(javax.swing.JComboBox combo, String texto) {
        for (int i = 0; i < combo.getItemCount(); i++) {
            Object item = combo.getItemAt(i);
            if (item instanceof ComboItem && ((ComboItem) item).getTexto().equalsIgnoreCase(texto)) {
                combo.setSelectedIndex(i);
                break;
                
                
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPoliza = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cboCliente = new javax.swing.JComboBox<>();
        cboAseguradora = new javax.swing.JComboBox<>();
        cboTipoSeguro = new javax.swing.JComboBox<>();
        cboAgente = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtFechaInicio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtFechaVencimiento = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtPrima = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cboEstado = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        btnGuardar = new javax.swing.JButton();
        btnEditar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaPolizas = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jLabel1.setText("N° de Poliza");

        jLabel2.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jLabel2.setText("Cliente:");

        jLabel3.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jLabel3.setText("Aseguradora:");

        jLabel4.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jLabel4.setText("Tipo Seguro:");

        jLabel5.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jLabel5.setText("Agente:");

        cboCliente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboAseguradora.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboTipoSeguro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboAgente.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel6.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jLabel6.setText("Fecha-Inicio:");

        jLabel7.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jLabel7.setText("Fecha-Vencimiento:");

        jLabel8.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jLabel8.setText("Prima Anual:");

        txtPrima.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtPrimaKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jLabel9.setText("Estado:");

        cboEstado.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Activa", "Vencida", "Cancelada" }));

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnGuardar.setBackground(new java.awt.Color(37, 99, 235));
        btnGuardar.setIcon(new javax.swing.ImageIcon("C:\\Users\\deino\\Downloads\\disk.png")); // NOI18N
        btnGuardar.addActionListener(this::btnGuardarActionPerformed);

        btnEditar.setBackground(new java.awt.Color(225, 225, 57));
        btnEditar.setIcon(new javax.swing.ImageIcon("C:\\Users\\deino\\Downloads\\edit.png")); // NOI18N
        btnEditar.addActionListener(this::btnEditarActionPerformed);

        btnEliminar.setBackground(new java.awt.Color(153, 27, 27));
        btnEliminar.setIcon(new javax.swing.ImageIcon("C:\\Users\\deino\\Downloads\\trash.png")); // NOI18N
        btnEliminar.addActionListener(this::btnEliminarActionPerformed);

        btnLimpiar.setIcon(new javax.swing.ImageIcon("C:\\Users\\deino\\Downloads\\broom.png")); // NOI18N
        btnLimpiar.addActionListener(this::btnLimpiarActionPerformed);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addComponent(btnGuardar)
                .addGap(35, 35, 35)
                .addComponent(btnEditar)
                .addGap(37, 37, 37)
                .addComponent(btnEliminar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(btnLimpiar, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnEditar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnEliminar)
                    .addComponent(btnLimpiar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnGuardar))
                .addContainerGap(24, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtFechaInicio, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(txtPoliza, javax.swing.GroupLayout.Alignment.LEADING))
                    .addComponent(jLabel6))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtFechaVencimiento, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cboCliente, javax.swing.GroupLayout.Alignment.LEADING, 0, 127, Short.MAX_VALUE))
                    .addComponent(jLabel7))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(txtPrima, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(cboAseguradora, javax.swing.GroupLayout.Alignment.LEADING, 0, 127, Short.MAX_VALUE))
                    .addComponent(jLabel8))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel9)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(cboEstado, 0, 127, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cboTipoSeguro, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(cboAgente, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(142, 142, 142))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtPoliza, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboAseguradora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboTipoSeguro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboAgente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFechaVencimiento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtPrima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Busqueda y Tabla", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Bahnschrift", 1, 14))); // NOI18N

        tablaPolizas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "N° Poliza", "Cliente", "Aseguradora", "Tipo", "Agente", "Fecha-Inicio", "Fecha-Vencimiento", "Prima Anual", "Estado"
            }
        ));
        tablaPolizas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaPolizasMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaPolizas);

        jLabel10.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jLabel10.setText("Buscar:");

        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 762, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel10)
                .addGap(18, 18, 18)
                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 298, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tablaPolizasMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaPolizasMouseClicked
        // TODO add your handling code here:
        seleccionarFilaTabla();
    }//GEN-LAST:event_tablaPolizasMouseClicked

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        // TODO add your handling code here:
        listarPolizas(txtBuscar.getText().trim());
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        if (!validarCampos()) {
        return; 
    }
        ComboItem clienteSel = (ComboItem) cboCliente.getSelectedItem();
        ComboItem aseguradoraSel = (ComboItem) cboAseguradora.getSelectedItem();
        ComboItem tipoSel = (ComboItem) cboTipoSeguro.getSelectedItem();
        ComboItem agenteSel = (ComboItem) cboAgente.getSelectedItem();

        if (txtPoliza.getText().trim().isEmpty() || clienteSel.getId() == 0 || 
            aseguradoraSel.getId() == 0 || tipoSel.getId() == 0 || agenteSel.getId() == 0) {
            JOptionPane.showMessageDialog(this, "Complete el N° de Póliza y seleccione todas las entidades correspondientes.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            Poliza p = new Poliza();
            p.setNumPoliza(txtPoliza.getText().trim());
            p.setIdCliente(clienteSel.getId());
            p.setIdAseguradora(aseguradoraSel.getId());
            p.setIdTipoSeguro(tipoSel.getId());
            p.setIdAgente(agenteSel.getId());
            p.setFechaInicio(Date.valueOf(txtFechaInicio.getText().trim()));
            p.setFechaVencimiento(Date.valueOf(txtFechaVencimiento.getText().trim()));
            p.setPrimaAnual(Double.parseDouble(txtPrima.getText().trim()));
            p.setEstadoPoliza(cboEstado.getSelectedItem().toString());

            if (dao.registrar(p)) {
                JOptionPane.showMessageDialog(this, "Póliza registrada exitosamente.");
                listarPolizas("");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al registrar la póliza.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha u orden numérico inválido. Use el formato YYYY-MM-DD para fechas.", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnGuardarActionPerformed

    private void btnEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditarActionPerformed
        // TODO add your handling code here:
        if (!validarCampos()) {
        return; 
    }
        if (idPolizaSeleccionada == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una póliza de la tabla para editar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ComboItem clienteSel = (ComboItem) cboCliente.getSelectedItem();
        ComboItem aseguradoraSel = (ComboItem) cboAseguradora.getSelectedItem();
        ComboItem tipoSel = (ComboItem) cboTipoSeguro.getSelectedItem();
        ComboItem agenteSel = (ComboItem) cboAgente.getSelectedItem();

        try {
            Poliza p = new Poliza();
            p.setIdPoliza(idPolizaSeleccionada);
            p.setNumPoliza(txtPoliza.getText().trim());
            p.setIdCliente(clienteSel.getId());
            p.setIdAseguradora(aseguradoraSel.getId());
            p.setIdTipoSeguro(tipoSel.getId());
            p.setIdAgente(agenteSel.getId());
            p.setFechaInicio(Date.valueOf(txtFechaInicio.getText().trim()));
            p.setFechaVencimiento(Date.valueOf(txtFechaVencimiento.getText().trim()));
            p.setPrimaAnual(Double.parseDouble(txtPrima.getText().trim()));
            p.setEstadoPoliza(cboEstado.getSelectedItem().toString());

            if (dao.modificar(p)) {
                JOptionPane.showMessageDialog(this, "Póliza actualizada exitosamente.");
                listarPolizas("");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al actualizar la póliza.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Verifique los datos numéricos y el formato de fecha (YYYY-MM-DD).", "Error de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnEditarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        // TODO add your handling code here:
        if (idPolizaSeleccionada == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione una póliza de la tabla para eliminar.", "Atención", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int opcion = JOptionPane.showConfirmDialog(this, "¿Desea eliminar la póliza seleccionada?", "Confirmación", JOptionPane.YES_NO_OPTION);
        if (opcion == JOptionPane.YES_OPTION) {
            if (dao.eliminar(idPolizaSeleccionada)) {
                JOptionPane.showMessageDialog(this, "Póliza eliminada correctamente.");
                listarPolizas("");
                limpiarFormulario();
            } else {
                JOptionPane.showMessageDialog(this, "Error al eliminar la póliza.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        // TODO add your handling code here:
        limpiarFormulario();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtPrimaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPrimaKeyTyped
        // TODO add your handling code here:
        char c = evt.getKeyChar();
    // Permite solo dígitos y el punto decimal
    if (!Character.isDigit(c) && c != '.') {
        evt.consume();
    }
    // Evita ingresar más de un punto decimal
    if (c == '.' && txtPrima.getText().contains(".")) {
        evt.consume();
    }
    }//GEN-LAST:event_txtPrimaKeyTyped


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEditar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<Object> cboAgente;
    private javax.swing.JComboBox<Object> cboAseguradora;
    private javax.swing.JComboBox<Object> cboCliente;
    private javax.swing.JComboBox<String> cboEstado;
    private javax.swing.JComboBox<Object> cboTipoSeguro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
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
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tablaPolizas;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtFechaInicio;
    private javax.swing.JTextField txtFechaVencimiento;
    private javax.swing.JTextField txtPoliza;
    private javax.swing.JTextField txtPrima;
    // End of variables declaration//GEN-END:variables
}
