package U2T2.presentacion;

import U2T2.logica.ControlEstudiante;
import U2T2.logica.command.ActualizarEstudianteComando;
import U2T2.logica.command.EliminarEstudianteComando;
import U2T2.logica.command.InvocadorComandos;
import U2T2.logica.command.RegistrarEstudianteComando;
import U2T2.logica.observer.EventoEstudiante;
import U2T2.logica.observer.ObservadorAuditoriaEstudiante;
import U2T2.logica.observer.ObservadorEstudiante;
import U2T2.logica.strategy.BusquedaPorIdStrategy;
import U2T2.logica.strategy.BusquedaPorNombreStrategy;
import U2T2.logica.strategy.BusquedaPorRangoEdadStrategy;
import U2T2.modelo.Estudiante;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
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
 * CAPA: Presentacion
 * Interfaz grafica minimalista del CRUD. Solo captura datos y muestra resultados:
 * las operaciones se envian como comandos al InvocadorComandos (Command),
 * la busqueda usa la estrategia elegida (Strategy) y la tabla y la actividad
 * se refrescan cuando el control notifica un evento (Observer).
 */
public class CRUDEstudiantesGUI extends JFrame {

    // Paleta
    private static final Color FONDO = new Color(0xF4F6FA);
    private static final Color TARJETA = Color.WHITE;
    private static final Color BORDE = new Color(0xE3E7EF);
    private static final Color TEXTO = new Color(0x1E2433);
    private static final Color TEXTO_SUAVE = new Color(0x6B7385);
    private static final Color PRIMARIO = new Color(0x3D5AFE);
    private static final Color EXITO = new Color(0x1E9E62);
    private static final Color ERROR = new Color(0xD64545);
    private static final Color GRIS_BOTON = new Color(0xEEF1F6);
    private static final Font FUENTE = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FUENTE_NEGRITA = new Font("Segoe UI", Font.BOLD, 14);

    // Logica
    private final ControlEstudiante control = new ControlEstudiante();
    private final InvocadorComandos invocador = new InvocadorComandos();
    private final ObservadorAuditoriaEstudiante auditoria = new ObservadorAuditoriaEstudiante();

    // Componentes
    private final JTextField txtId = campo();
    private final JTextField txtNombre = campo();
    private final JTextField txtEdad = campo();
    private final JTextField txtBuscar = campo();
    private final JComboBox<String> cmbEstrategia =
            new JComboBox<>(new String[]{"Por ID", "Por Nombre", "Por rango de edad"});
    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(new Object[]{"ID ESPE", "Nombre", "Edad"}, 0) {
                @Override
                public boolean isCellEditable(int fila, int columna) { return false; }
            };
    private final JTable tabla = new JTable(modeloTabla);
    private final DefaultListModel<String> modeloActividad = new DefaultListModel<>();
    private final JLabel lblEstado = new JLabel("Listo. Ingrese un estudiante con su ID ESPE.");
    private final JLabel lblContador = new JLabel("0 estudiantes");
    private final JLabel lblHistorial = new JLabel("Historial: 0");

    public CRUDEstudiantesGUI() {
        super("CRUD Estudiantes - Observer, Strategy y Command");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1080, 640));

        // Observer: la auditoria guarda la bitacora y la vista se actualiza sola
        control.agregarObservador(auditoria);
        // la ventana tambien es un observador
        ObservadorEstudiante observadorVista = this::alRecibirEvento;
        control.agregarObservador(observadorVista);

        JPanel raiz = new JPanel(new BorderLayout(0, 0));
        raiz.setBackground(FONDO);
        raiz.add(crearEncabezado(), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setOpaque(false);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(16, 20, 12, 20));
        cuerpo.add(crearFormulario(), BorderLayout.WEST);
        cuerpo.add(crearPanelTabla(), BorderLayout.CENTER);
        cuerpo.add(crearPanelActividad(), BorderLayout.EAST);
        raiz.add(cuerpo, BorderLayout.CENTER);
        raiz.add(crearBarraEstado(), BorderLayout.SOUTH);

        setContentPane(raiz);
        pack();
        setLocationRelativeTo(null);
        refrescarTabla(control.listarTodos());
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
        JLabel sub = new JLabel("CRUD en tres capas con patrones Observer, Strategy y Command");
        sub.setFont(FUENTE);
        sub.setForeground(TEXTO_SUAVE);
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(2));
        textos.add(sub);
        p.add(textos, BorderLayout.WEST);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 6));
        derecha.setOpaque(false);
        lblHistorial.setFont(FUENTE);
        lblHistorial.setForeground(TEXTO_SUAVE);
        JButton btnDeshacer = boton("Deshacer", GRIS_BOTON, TEXTO);
        btnDeshacer.addActionListener(e -> deshacer());
        derecha.add(lblHistorial);
        derecha.add(btnDeshacer);
        p.add(derecha, BorderLayout.EAST);
        return p;
    }

    private JComponent crearFormulario() {
        JPanel tarjeta = tarjeta();
        tarjeta.setLayout(new BoxLayout(tarjeta, BoxLayout.Y_AXIS));
        tarjeta.setPreferredSize(new Dimension(280, 0));

        tarjeta.add(tituloSeccion("Datos del estudiante"));
        tarjeta.add(Box.createVerticalStrut(14));
        tarjeta.add(etiqueta("ID ESPE"));
        tarjeta.add(txtId);
        tarjeta.add(ayuda("Formato: L y 8 digitos, ej. L00434011"));
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(etiqueta("Nombre completo"));
        tarjeta.add(txtNombre);
        tarjeta.add(Box.createVerticalStrut(10));
        tarjeta.add(etiqueta("Edad"));
        tarjeta.add(txtEdad);
        tarjeta.add(ayuda("Entre 15 y 99 anios"));
        tarjeta.add(Box.createVerticalStrut(18));

        JButton btnRegistrar = boton("Registrar", PRIMARIO, Color.WHITE);
        JButton btnActualizar = boton("Actualizar", GRIS_BOTON, TEXTO);
        JButton btnEliminar = boton("Eliminar", new Color(0xFDECEC), ERROR);
        JButton btnLimpiar = boton("Limpiar", GRIS_BOTON, TEXTO);
        btnRegistrar.addActionListener(e -> registrar());
        btnActualizar.addActionListener(e -> actualizar());
        btnEliminar.addActionListener(e -> eliminar());
        btnLimpiar.addActionListener(e -> limpiarFormulario());

        JPanel botones = new JPanel(new GridLayout(2, 2, 8, 8));
        botones.setOpaque(false);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);
        botones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));
        botones.add(btnRegistrar);
        botones.add(btnActualizar);
        botones.add(btnEliminar);
        botones.add(btnLimpiar);
        tarjeta.add(botones);
        tarjeta.add(Box.createVerticalStrut(12));
        tarjeta.add(ayuda("Seleccione una fila para editar. El ID no se puede modificar."));
        tarjeta.add(Box.createVerticalGlue());
        return tarjeta;
    }

    private JComponent crearPanelTabla() {
        JPanel tarjeta = tarjeta();
        tarjeta.setLayout(new BorderLayout(0, 12));

        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(tituloSeccion("Estudiantes registrados"), BorderLayout.WEST);
        lblContador.setFont(FUENTE);
        lblContador.setForeground(TEXTO_SUAVE);
        cabecera.add(lblContador, BorderLayout.EAST);

        // Strategy: el usuario elige el algoritmo de busqueda
        JPanel busqueda = new JPanel(new BorderLayout(8, 0));
        busqueda.setOpaque(false);
        cmbEstrategia.setUI(new BasicComboBoxUI());
        cmbEstrategia.setFont(FUENTE);
        cmbEstrategia.setBackground(TARJETA);
        cmbEstrategia.setForeground(TEXTO);
        cmbEstrategia.setBorder(BorderFactory.createLineBorder(BORDE));
        cmbEstrategia.setPreferredSize(new Dimension(170, 36));
        cmbEstrategia.addActionListener(e -> cambiarEstrategia());
        txtBuscar.setToolTipText("ID, nombre o rango de edad (ej. 18-25)");
        txtBuscar.addActionListener(e -> buscar());
        JButton btnBuscar = boton("Buscar", PRIMARIO, Color.WHITE);
        JButton btnTodos = boton("Ver todos", GRIS_BOTON, TEXTO);
        btnBuscar.addActionListener(e -> buscar());
        btnTodos.addActionListener(e -> {
            txtBuscar.setText("");
            refrescarTabla(control.listarTodos());
        });
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        acciones.setOpaque(false);
        acciones.add(btnBuscar);
        acciones.add(btnTodos);
        busqueda.add(cmbEstrategia, BorderLayout.WEST);
        busqueda.add(txtBuscar, BorderLayout.CENTER);
        busqueda.add(acciones, BorderLayout.EAST);

        JPanel arriba = new JPanel(new BorderLayout(0, 12));
        arriba.setOpaque(false);
        arriba.add(cabecera, BorderLayout.NORTH);
        arriba.add(busqueda, BorderLayout.SOUTH);
        tarjeta.add(arriba, BorderLayout.NORTH);

        estilizarTabla();
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        scroll.getViewport().setBackground(TARJETA);
        tarjeta.add(scroll, BorderLayout.CENTER);
        return tarjeta;
    }

    private JComponent crearPanelActividad() {
        JPanel tarjeta = tarjeta();
        tarjeta.setLayout(new BorderLayout(0, 10));
        tarjeta.setPreferredSize(new Dimension(290, 0));
        JPanel cab = new JPanel();
        cab.setOpaque(false);
        cab.setLayout(new BoxLayout(cab, BoxLayout.Y_AXIS));
        cab.add(tituloSeccion("Actividad"));
        cab.add(ayuda("Bitacora del observador de auditoria"));
        tarjeta.add(cab, BorderLayout.NORTH);

        JList<String> lista = new JList<>(modeloActividad);
        lista.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lista.setForeground(TEXTO);
        lista.setFixedCellHeight(-1);
        lista.setCellRenderer((l, valor, i, sel, foco) -> {
            JLabel c = new JLabel("<html><body style='width:180px'>" + valor + "</body></html>");
            c.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            c.setForeground(TEXTO);
            c.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
                    BorderFactory.createEmptyBorder(8, 4, 8, 4)));
            return c;
        });
        JScrollPane scroll = new JScrollPane(lista);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        tarjeta.add(scroll, BorderLayout.CENTER);
        return tarjeta;
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

    // ---------------- Acciones (Command) ----------------

    private void registrar() {
        Integer edad = leerEdad();
        if (edad == null) return;
        mostrarResultado(invocador.ejecutar(  // cada boton arma un comando y se lo pasa al invocador
                new RegistrarEstudianteComando(control, txtId.getText(), txtNombre.getText(), edad)));
        if (!lblEstado.getForeground().equals(ERROR)) limpiarFormulario();
    }

    private void actualizar() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarResultado("Error: seleccione un estudiante de la tabla para actualizar.");
            return;
        }
        Integer edad = leerEdad();
        if (edad == null) return;
        mostrarResultado(invocador.ejecutar(
                new ActualizarEstudianteComando(control, txtId.getText(), txtNombre.getText(), edad)));
    }

    private void eliminar() {
        if (txtId.getText().trim().isEmpty()) {
            mostrarResultado("Error: seleccione un estudiante de la tabla para eliminar.");
            return;
        }
        mostrarResultado(invocador.ejecutar(new EliminarEstudianteComando(control, txtId.getText())));
        if (!lblEstado.getForeground().equals(ERROR)) limpiarFormulario();
    }

    private void deshacer() {
        mostrarResultado(invocador.deshacerUltimo());
        limpiarFormulario();
    }

    // ---------------- Busqueda (Strategy) ----------------

    private void cambiarEstrategia() {
        switch (cmbEstrategia.getSelectedIndex()) {
            // segun lo que elija en el combo cambia la estrategia
            case 1: control.cambiarEstrategiaBusqueda(new BusquedaPorNombreStrategy()); break;
            case 2: control.cambiarEstrategiaBusqueda(new BusquedaPorRangoEdadStrategy()); break;
            default: control.cambiarEstrategiaBusqueda(new BusquedaPorIdStrategy());
        }
        lblEstado.setForeground(TEXTO_SUAVE);
        lblEstado.setText("Estrategia de busqueda: " + control.getNombreEstrategia());
    }

    private void buscar() {
        String criterio = txtBuscar.getText();
        if (criterio.trim().isEmpty()) {
            refrescarTabla(control.listarTodos());
            return;
        }
        // misma llamada para las 3 busquedas
        List<Estudiante> resultado = control.buscarEstudiantes(criterio);
        refrescarTabla(resultado);
        lblEstado.setForeground(TEXTO_SUAVE);
        lblEstado.setText("Busqueda " + control.getNombreEstrategia() + " '" + criterio.trim() + "': "
                + resultado.size() + " resultado(s).");
    }

    // ---------------- Observer en la vista ----------------

    private void alRecibirEvento(EventoEstudiante evento) {
        SwingUtilities.invokeLater(() -> {
            refrescarTabla(control.listarTodos());
            // el evento aparece arriba en el panel Actividad
            modeloActividad.add(0, "<b>" + evento.getTipo() + "</b><br>" + evento.getMensaje());
            lblHistorial.setText("Historial: " + invocador.verHistorial().size());
        });
    }

    // ---------------- Utilidades ----------------

    private Integer leerEdad() {
        try {
            return Integer.parseInt(txtEdad.getText().trim());
        } catch (NumberFormatException e) {
            mostrarResultado("Error: la edad debe ser un numero entero.");
            return null;
        }
    }

    private void mostrarResultado(String mensaje) {
        boolean error = mensaje.startsWith("Error") || mensaje.startsWith("No ");
        lblEstado.setForeground(error ? ERROR : EXITO);
        lblEstado.setText(mensaje);
        lblHistorial.setText("Historial: " + invocador.verHistorial().size());
    }

    private void refrescarTabla(List<Estudiante> lista) {
        modeloTabla.setRowCount(0);
        for (Estudiante e : lista) {
            modeloTabla.addRow(new Object[]{e.getId(), e.getNombre(), e.getEdad()});
        }
        int total = control.listarTodos().size();
        lblContador.setText(total + (total == 1 ? " estudiante" : " estudiantes"));
    }

    private void limpiarFormulario() {
        txtId.setText("");
        txtId.setEditable(true);
        txtId.setBackground(TARJETA);
        txtNombre.setText("");
        txtEdad.setText("");
        tabla.clearSelection();
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtId.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
        txtId.setEditable(false);  // al seleccionar, el ID se bloquea para que no lo cambien
        txtId.setBackground(new Color(0xF1F3F8));
        txtNombre.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        txtEdad.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
    }

    private void estilizarTabla() {
        tabla.setFont(FUENTE);
        tabla.setForeground(TEXTO);
        tabla.setRowHeight(34);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(BORDE);
        tabla.setSelectionBackground(new Color(0xE8ECFF));
        tabla.setSelectionForeground(TEXTO);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });
        JTableHeader h = tabla.getTableHeader();
        h.setReorderingAllowed(false);
        h.setPreferredSize(new Dimension(0, 36));
        h.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = new JLabel(String.valueOf(v));
                l.setFont(FUENTE_NEGRITA);
                l.setForeground(TEXTO_SUAVE);
                l.setOpaque(true);
                l.setBackground(new Color(0xF7F8FB));
                l.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 0, 1, 0, BORDE),
                        BorderFactory.createEmptyBorder(0, 12, 0, 12)));
                return l;
            }
        });
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, s, false, r, c);
                l.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
                l.setHorizontalAlignment(c == 2 ? JLabel.CENTER : JLabel.LEFT);
                return l;
            }
        });
        tabla.getColumnModel().getColumn(0).setPreferredWidth(120);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(240);
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
        Border borde = BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDE),
                BorderFactory.createEmptyBorder(6, 10, 6, 10));
        t.setBorder(borde);
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

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignorada) {
            // Se usa el aspecto por defecto si no se puede cambiar
        }
        SwingUtilities.invokeLater(() -> new CRUDEstudiantesGUI().setVisible(true));
    }
}
