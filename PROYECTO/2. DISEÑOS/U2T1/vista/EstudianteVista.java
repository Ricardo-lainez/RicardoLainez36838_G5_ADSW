package U2T1.vista;

import U2T1.controlador.EstudianteControlador;
import U2T1.modelo.Estudiante;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

/**
 * VISTA: interfaz grafica minimalista. Solo muestra datos y captura lo que
 * escribe el usuario; cada boton le pide la accion al controlador y muestra
 * el mensaje que este devuelve. No guarda datos ni valida reglas.
 */
public class EstudianteVista extends JFrame {

    private static final Color FONDO = new Color(0xF4F6FA);
    private static final Color TARJETA = Color.WHITE;
    private static final Color BORDE = new Color(0xE3E7EF);
    private static final Color TEXTO = new Color(0x1E2433);
    private static final Color TEXTO_SUAVE = new Color(0x6B7385);
    private static final Color PRIMARIO = new Color(0x0F8B6E);
    private static final Color EXITO = new Color(0x1E9E62);
    private static final Color ERROR = new Color(0xD64545);
    private static final Color GRIS_BOTON = new Color(0xEEF1F6);
    private static final Font FUENTE = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FUENTE_NEGRITA = new Font("Segoe UI", Font.BOLD, 14);

    private final EstudianteControlador controlador;

    private final JTextField txtId = campo();
    private final JTextField txtNombre = campo();
    private final JTextField txtEdad = campo();
    private final JTextField txtBuscar = campo();
    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(new Object[]{"ID ESPE", "Nombre", "Edad"}, 0) {
                @Override
                public boolean isCellEditable(int fila, int columna) { return false; }
            };
    private final JTable tabla = new JTable(modeloTabla);
    private final JLabel lblEstado = new JLabel("Listo. Ingrese un estudiante con su ID ESPE.");
    private final JLabel lblContador = new JLabel("0 estudiantes");

    public EstudianteVista(EstudianteControlador controlador) {
        super("CRUD Estudiantes - MVC");
        this.controlador = controlador;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(920, 580));

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);
        raiz.add(crearEncabezado(), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setOpaque(false);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(16, 20, 12, 20));
        cuerpo.add(crearFormulario(), BorderLayout.WEST);
        cuerpo.add(crearPanelTabla(), BorderLayout.CENTER);
        raiz.add(cuerpo, BorderLayout.CENTER);
        raiz.add(crearBarraEstado(), BorderLayout.SOUTH);

        setContentPane(raiz);
        pack();
        setLocationRelativeTo(null);
        mostrarLista(controlador.listar());
    }

    // ---------------- Acciones: la vista solo delega en el controlador ----------------

    private void onRegistrar() {
        // la vista no valida nada, solo pasa lo que escribio el usuario
        String r = controlador.registrar(txtId.getText(), txtNombre.getText(), txtEdad.getText());
        mostrarMensaje(r);
        if (!esError(r)) limpiar();
        mostrarLista(controlador.listar());
    }

    private void onActualizar() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarMensaje("Error: seleccione un estudiante de la tabla para actualizar.");
            return;
        }
        mostrarMensaje(controlador.actualizar(txtId.getText(), txtNombre.getText(), txtEdad.getText()));
        mostrarLista(controlador.listar());
    }

    private void onEliminar() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarMensaje("Error: seleccione un estudiante de la tabla para eliminar.");
            return;
        }
        String r = controlador.eliminar(txtId.getText());  // el controlador decide si se puede o no
        mostrarMensaje(r);
        if (!esError(r)) limpiar();
        mostrarLista(controlador.listar());
    }

    private void onBuscar() {
        if (txtBuscar.getText().trim().isEmpty()) {
            mostrarLista(controlador.listar());
            return;
        }
        List<Estudiante> r = controlador.buscarPorId(txtBuscar.getText());  // busca por ID parcial
        mostrarLista(r);
        lblEstado.setForeground(TEXTO_SUAVE);
        lblEstado.setText("Busqueda por ID '" + txtBuscar.getText().trim() + "': " + r.size() + " resultado(s).");
    }

    // ---------------- Metodos de presentacion ----------------

    public void mostrarLista(List<Estudiante> lista) {
        modeloTabla.setRowCount(0);
        for (Estudiante e : lista) {
            modeloTabla.addRow(new Object[]{e.getId(), e.getNombre(), e.getEdad()});
        }
        int total = controlador.listar().size();
        lblContador.setText(total + (total == 1 ? " estudiante" : " estudiantes"));
    }

    public void mostrarMensaje(String mensaje) {
        lblEstado.setForeground(esError(mensaje) ? ERROR : EXITO);
        lblEstado.setText(mensaje);
    }

    private boolean esError(String mensaje) {
        return mensaje.startsWith("Error");
    }

    private void limpiar() {
        txtId.setText("");
        txtId.setEditable(true);  // al limpiar se habilita otra vez para registrar uno nuevo
        txtId.setBackground(TARJETA);
        txtNombre.setText("");
        txtEdad.setText("");
        tabla.clearSelection();
    }

    /** Al seleccionar una fila el ID se bloquea: solo se editan nombre y edad. */
    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtId.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
        txtId.setEditable(false);  // al seleccionar se bloquea el ID para que no lo cambien
        txtId.setBackground(new Color(0xF1F3F8));
        txtNombre.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        txtEdad.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
    }

    // ---------------- Construccion de la interfaz ----------------

    private JComponent crearEncabezado() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(TARJETA);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
                BorderFactory.createEmptyBorder(16, 24, 16, 24)));
        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Gestion de Estudiantes");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(TEXTO);
        JLabel sub = new JLabel("CRUD con arquitectura Modelo, Vista y Controlador");
        sub.setFont(FUENTE);
        sub.setForeground(TEXTO_SUAVE);
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(2));
        textos.add(sub);
        p.add(textos, BorderLayout.WEST);
        return p;
    }

    private JComponent crearFormulario() {
        JPanel t = tarjeta();
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.setPreferredSize(new Dimension(280, 0));
        t.add(tituloSeccion("Datos del estudiante"));
        t.add(Box.createVerticalStrut(14));
        t.add(etiqueta("ID ESPE"));
        t.add(txtId);
        t.add(ayuda("Formato: L y 8 digitos, ej. L00434011"));
        t.add(Box.createVerticalStrut(10));
        t.add(etiqueta("Nombre completo"));
        t.add(txtNombre);
        t.add(Box.createVerticalStrut(10));
        t.add(etiqueta("Edad"));
        t.add(txtEdad);
        t.add(ayuda("Entre 15 y 99 anios"));
        t.add(Box.createVerticalStrut(18));

        JButton btnRegistrar = boton("Registrar", PRIMARIO, Color.WHITE);
        JButton btnActualizar = boton("Actualizar", GRIS_BOTON, TEXTO);
        JButton btnEliminar = boton("Eliminar", new Color(0xFDECEC), ERROR);
        JButton btnLimpiar = boton("Limpiar", GRIS_BOTON, TEXTO);
        btnRegistrar.addActionListener(e -> onRegistrar());
        btnActualizar.addActionListener(e -> onActualizar());
        btnEliminar.addActionListener(e -> onEliminar());
        btnLimpiar.addActionListener(e -> limpiar());

        JPanel botones = new JPanel(new GridLayout(2, 2, 8, 8));
        botones.setOpaque(false);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);
        botones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));
        botones.add(btnRegistrar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);
        t.add(botones);
        t.add(Box.createVerticalStrut(12));
        t.add(ayuda("Seleccione una fila para editar. El ID no se puede modificar."));
        t.add(Box.createVerticalGlue());
        return t;
    }

    private JComponent crearPanelTabla() {
        JPanel t = tarjeta();
        t.setLayout(new BorderLayout(0, 12));

        JPanel cab = new JPanel(new BorderLayout());
        cab.setOpaque(false);
        cab.add(tituloSeccion("Estudiantes registrados"), BorderLayout.WEST);
        lblContador.setFont(FUENTE);
        lblContador.setForeground(TEXTO_SUAVE);
        cab.add(lblContador, BorderLayout.EAST);

        JPanel busqueda = new JPanel(new BorderLayout(8, 0));
        busqueda.setOpaque(false);
        JLabel lbl = new JLabel("Buscar por ID");
        lbl.setFont(FUENTE_NEGRITA);
        lbl.setForeground(TEXTO);
        txtBuscar.addActionListener(e -> onBuscar());
        JButton btnBuscar = boton("Buscar", PRIMARIO, Color.WHITE);
        JButton btnTodos = boton("Ver todos", GRIS_BOTON, TEXTO);
        btnBuscar.addActionListener(e -> onBuscar());
        btnTodos.addActionListener(e -> {
            txtBuscar.setText("");
            mostrarLista(controlador.listar());
        });
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.setOpaque(false);
        acciones.add(btnBuscar);
        acciones.add(btnTodos);
        busqueda.add(lbl, BorderLayout.WEST);
        busqueda.add(txtBuscar, BorderLayout.CENTER);
        busqueda.add(acciones, BorderLayout.EAST);

        JPanel arriba = new JPanel(new BorderLayout(0, 12));
        arriba.setOpaque(false);
        arriba.add(cab, BorderLayout.NORTH);
        arriba.add(busqueda, BorderLayout.SOUTH);
        t.add(arriba, BorderLayout.NORTH);

        estilizarTabla();
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        scroll.getViewport().setBackground(TARJETA);
        t.add(scroll, BorderLayout.CENTER);
        return t;
    }

    private JComponent crearBarraEstado() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(TARJETA);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDE),
                BorderFactory.createEmptyBorder(10, 24, 10, 24)));
        lblEstado.setFont(FUENTE);
        lblEstado.setForeground(TEXTO_SUAVE);
        p.add(lblEstado, BorderLayout.WEST);
        return p;
    }

    private void estilizarTabla() {
        tabla.setFont(FUENTE);
        tabla.setForeground(TEXTO);
        tabla.setRowHeight(34);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(BORDE);
        tabla.setSelectionBackground(new Color(0xE3F4EF));
        tabla.setSelectionForeground(TEXTO);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });
        JTableHeader h = tabla.getTableHeader();
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(0, 36));
        h.setDefaultRenderer((tb, v, s, f, r, c) -> {
            JLabel l = new JLabel(String.valueOf(v));
            l.setFont(FUENTE_NEGRITA);
            l.setForeground(TEXTO_SUAVE);
            l.setOpaque(true);
            l.setBackground(new Color(0xF7F8FB));
            l.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
                    BorderFactory.createEmptyBorder(0, 12, 0, 12)));
            return l;
        });
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable tb, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(tb, v, s, false, r, c);
                l.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                l.setHorizontalAlignment(c == 2 ? JLabel.CENTER : JLabel.LEFT);
                return l;
            }
        });
        tabla.getColumnModel().getColumn(0).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(260);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(60);
    }

    private static JPanel tarjeta() {
        JPanel p = new JPanel();
        p.setBackground(TARJETA);
        p.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)));
        return p;
    }

    private static JLabel tituloSeccion(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font("Segoe UI", Font.BOLD, 16));
        l.setForeground(TEXTO);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_NEGRITA);
        l.setForeground(TEXTO);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        return l;
    }

    private static JLabel ayuda(String texto) {
        JLabel l = new JLabel("<html>" + texto + "</html>");
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        l.setForeground(TEXTO_SUAVE);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        l.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));
        return l;
    }

    private static JTextField campo() {
        JTextField t = new JTextField();
        t.setFont(FUENTE);
        t.setForeground(TEXTO);
        t.setAlignmentX(Component.LEFT_ALIGNMENT);
        t.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        t.setPreferredSize(new Dimension(200, 38));
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return t;
    }

    private static JButton boton(String texto, Color fondo, Color letra) {
        JButton b = new JButton(texto);
        b.setUI(new BasicButtonUI());
        b.setFont(FUENTE_NEGRITA);
        b.setBackground(fondo);
        b.setForeground(letra);
        b.setFocusPainted(false);
        b.setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }
}
