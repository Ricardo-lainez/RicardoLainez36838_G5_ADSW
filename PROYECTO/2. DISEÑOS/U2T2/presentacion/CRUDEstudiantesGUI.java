package U2T2.presentacion;

import U2T2.logica.ControlEstudiante;
import U2T2.logica.command.ActualizarEstudianteComando;
import U2T2.logica.command.ComandoEstudiante;
import U2T2.logica.command.EliminarEstudianteComando;
import U2T2.logica.command.InvocadorComandos;
import U2T2.logica.command.RegistrarEstudianteComando;
import U2T2.logica.observer.EventoEstudiante;
import U2T2.logica.observer.ObservadorAuditoriaEstudiante;
import U2T2.logica.observer.ObservadorEstudiante;
import U2T2.logica.observer.ObservadorNotificacionEstudiante;
import U2T2.logica.strategy.BusquedaPorIdStrategy;
import U2T2.logica.strategy.BusquedaPorNombreStrategy;
import U2T2.logica.strategy.BusquedaPorRangoEdadStrategy;
import U2T2.logica.strategy.EstrategiaBusquedaEstudiante;
import U2T2.modelo.Estudiante;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.util.Arrays;
import java.util.List;

/**
 * CAPA: Presentacion
 * Interfaz del CRUD con un panel que muestra en vivo los tres patrones:
 * - Command: cada boton crea un comando, el invocador lo ejecuta y lo apila para deshacer.
 * - Observer: cada cambio se avisa a los tres observadores suscritos y se ve quien lo recibio.
 * - Strategy: la busqueda usa la estrategia elegida y se muestra cual clase hizo el trabajo.
 * La traza de patrones lista, paso a paso, que patron intervino en cada accion.
 */
public class CRUDEstudiantesGUI extends JFrame {

    // ---------------- Paleta ----------------
    private static final Color FONDO = new Color(0xF2F4F8);
    private static final Color TARJETA = Color.WHITE;
    private static final Color BORDE = new Color(0xE2E6EE);
    private static final Color TEXTO = new Color(0x18202F);
    private static final Color TEXTO_SUAVE = new Color(0x6A7386);
    private static final Color CAMPO = new Color(0xF7F8FB);
    private static final Color EXITO = new Color(0x159A5B);
    private static final Color ERROR = new Color(0xD8434B);
    // un color por patron, se repite en toda la interfaz
    private static final Color C_COMMAND = new Color(0x5B5BF0);
    private static final Color C_OBSERVER = new Color(0x0F9F7A);
    private static final Color C_STRATEGY = new Color(0xE08A00);
    private static final Color C_SISTEMA = new Color(0x6A7386);

    private static final String FAMILIA = elegirFuente();
    private static final Font F_NORMAL = new Font(FAMILIA, Font.PLAIN, 13);
    private static final Font F_NEGRITA = new Font(FAMILIA, Font.BOLD, 13);
    private static final Font F_PEQUENA = new Font(FAMILIA, Font.PLAIN, 11);
    private static final Font F_TITULO = new Font(FAMILIA, Font.BOLD, 15);
    private static final Font F_MONO = new Font(Font.MONOSPACED, Font.PLAIN, 12);

    // ---------------- Logica ----------------
    private final ControlEstudiante control = new ControlEstudiante();
    private final InvocadorComandos invocador = new InvocadorComandos();
    private final ObservadorAuditoriaEstudiante auditoria = new ObservadorAuditoriaEstudiante();
    private final ObservadorNotificacionEstudiante notificacion = new ObservadorNotificacionEstudiante();
    private final String[] nombresEstrategias = {"BusquedaPorIdStrategy", "BusquedaPorNombreStrategy", "BusquedaPorRangoEdadStrategy"};
    private int estrategiaActual = 0;

    // ---------------- Formulario y tabla ----------------
    private final JTextField txtId = campo("Ej. L00434011");
    private final JTextField txtNombre = campo("Nombre completo");
    private final JTextField txtEdad = campo("15 a 99");
    private final JTextField txtBuscar = campo("Texto a buscar");
    private final DefaultTableModel modeloTabla =
            new DefaultTableModel(new Object[]{"ID ESPE", "Nombre", "Edad"}, 0) {
                @Override
                public boolean isCellEditable(int fila, int columna) { return false; }
            };
    private final JTable tabla = new JTable(modeloTabla);
    private final JLabel lblEstado = new JLabel("Listo. Registre un estudiante con su ID ESPE.");
    private final JLabel lblContador = new JLabel("0 estudiantes");
    private final JButton[] botonesEstrategia = new JButton[3];

    // ---------------- Panel de patrones ----------------
    private final TarjetaPatron tarjetaCommand = new TarjetaPatron("COMMAND", "Cada boton es un objeto comando", C_COMMAND);
    private final TarjetaPatron tarjetaObserver = new TarjetaPatron("OBSERVER", "Los cambios se avisan a los suscritos", C_OBSERVER);
    private final TarjetaPatron tarjetaStrategy = new TarjetaPatron("STRATEGY", "La busqueda cambia de algoritmo", C_STRATEGY);
    private final JLabel lblComando = valor("Ninguno todavia");
    private final JLabel lblReceptor = valor("-");
    private final DefaultListModel<String> modeloHistorial = new DefaultListModel<>();
    private final JLabel lblEvento = valor("Sin eventos");
    private final Chip chipAuditoria = new Chip("Auditoria");
    private final Chip chipNotificacion = new Chip("Notificacion");
    private final Chip chipVista = new Chip("Vista");
    private final JLabel lblEstrategia = valor(nombresEstrategias[0]);
    private final JLabel lblBusqueda = valor("Sin busquedas");
    private final DefaultListModel<Traza> modeloTraza = new DefaultListModel<>();
    private final JList<Traza> listaTraza = new JList<>(modeloTraza);
    private int paso = 0;

    public CRUDEstudiantesGUI() {
        super("CRUD Estudiantes - Observer, Strategy y Command");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1360, 820));

        // Observer: se suscriben tres observadores al control.
        // Cada uno se envuelve para que la traza muestre cuando recibe el aviso.
        control.agregarObservador(conTraza(chipAuditoria, auditoria));
        control.agregarObservador(conTraza(chipNotificacion, notificacion));
        control.agregarObservador(conTraza(chipVista, this::alRecibirEvento));  // la ventana tambien es un observador

        JPanel raiz = new JPanel(new BorderLayout());
        raiz.setBackground(FONDO);
        raiz.add(crearEncabezado(), BorderLayout.NORTH);

        JPanel cuerpo = new JPanel(new BorderLayout(16, 0));
        cuerpo.setOpaque(false);
        cuerpo.setBorder(BorderFactory.createEmptyBorder(16, 18, 10, 18));
        cuerpo.add(crearFormulario(), BorderLayout.WEST);
        cuerpo.add(crearCentro(), BorderLayout.CENTER);
        cuerpo.add(crearPanelPatrones(), BorderLayout.EAST);
        raiz.add(cuerpo, BorderLayout.CENTER);
        raiz.add(crearBarraEstado(), BorderLayout.SOUTH);

        setContentPane(raiz);
        pack();
        setLocationRelativeTo(null);
        refrescarTabla(control.listarTodos());
        marcarEstrategia(0);
        trazar(C_SISTEMA, "INICIO", "Se suscribieron 3 observadores: Auditoria, Notificacion y Vista");
    }

    // ============================================================
    //  Acciones (Command): cada boton arma un comando y lo entrega
    // ============================================================

    private void registrar() {
        Integer edad = leerEdad();
        if (edad == null) return;
        ejecutarComando("Registrar", new RegistrarEstudianteComando(control, txtId.getText(), txtNombre.getText(), edad),
                "agregarEstudiante()");
        if (!lblEstado.getForeground().equals(ERROR)) limpiarFormulario();
    }

    private void actualizar() {
        Integer edad = leerEdad();
        if (edad == null) return;
        ejecutarComando("Actualizar", new ActualizarEstudianteComando(control, txtId.getText(), txtNombre.getText(), edad),
                "actualizarEstudiante()");
    }

    private void eliminar() {
        ejecutarComando("Eliminar", new EliminarEstudianteComando(control, txtId.getText()), "eliminarEstudiante()");
        if (!lblEstado.getForeground().equals(ERROR)) limpiarFormulario();
    }

    /** Lo que pasa con cualquier comando: se crea, el invocador lo ejecuta y, si salio bien, se apila. */
    private void ejecutarComando(String accion, ComandoEstudiante comando, String metodoReceptor) {
        iniciarAccion(accion);
        tarjetaCommand.activar();
        String clase = comando.getClass().getSimpleName();
        lblComando.setText(clase);
        lblReceptor.setText("ControlEstudiante." + metodoReceptor);
        trazar(C_COMMAND, "COMMAND", "Se crea " + clase + " con los datos del formulario");
        trazar(C_COMMAND, "COMMAND", "InvocadorComandos.ejecutar(comando)");
        trazar(C_COMMAND, "COMMAND", "El receptor hace el trabajo: ControlEstudiante." + metodoReceptor);

        String resultado = invocador.ejecutar(comando);  // aqui se disparan los avisos de Observer

        if (comando.fueExitoso()) {
            trazar(C_COMMAND, "COMMAND", "Salio bien: se apila en el historial (" + invocador.verHistorial().size() + ")");
        } else {
            trazar(C_COMMAND, "COMMAND", "Fallo: no entra al historial y no se avisa a nadie");
        }
        mostrarResultado(resultado);
    }

    private void deshacer() {
        iniciarAccion("Deshacer");
        tarjetaCommand.activar();
        List<String> historial = invocador.verHistorial();
        if (!historial.isEmpty()) {
            trazar(C_COMMAND, "COMMAND", "deshacerUltimo() saca el tope: " + historial.get(historial.size() - 1));
            trazar(C_COMMAND, "COMMAND", "comando.deshacer(): el receptor revierte el cambio");
            lblComando.setText("deshacer() de " + historial.get(historial.size() - 1));
            lblReceptor.setText("ControlEstudiante.revertir...()");
        }
        mostrarResultado(invocador.deshacerUltimo());  // si hay algo que deshacer, Observer avisa DESHECHO
        limpiarFormulario();
    }

    // ============================================================
    //  Busqueda (Strategy)
    // ============================================================

    private void cambiarEstrategia(int indice) {
        iniciarAccion("Cambiar estrategia");
        // segun el boton que se presiono se crea un objeto estrategia distinto
        EstrategiaBusquedaEstudiante estrategia;
        switch (indice) {
            case 1: estrategia = new BusquedaPorNombreStrategy(); break;
            case 2: estrategia = new BusquedaPorRangoEdadStrategy(); break;
            default: estrategia = new BusquedaPorIdStrategy();
        }
        control.cambiarEstrategiaBusqueda(estrategia);  // se cambia el algoritmo sin tocar el control
        marcarEstrategia(indice);
        tarjetaStrategy.activar();  // se ilumina la tarjeta naranja
        trazar(C_STRATEGY, "STRATEGY", "cambiarEstrategiaBusqueda(new " + nombresEstrategias[indice] + "())");
        lblEstado.setForeground(TEXTO_SUAVE);
        lblEstado.setText("Estrategia de busqueda: " + control.getNombreEstrategia());
    }

    private void buscar() {
        String criterio = txtBuscar.getText();
        if (criterio.trim().isEmpty()) {
            refrescarTabla(control.listarTodos());
            return;
        }
        iniciarAccion("Buscar");
        tarjetaStrategy.activar();
        List<Estudiante> resultado = control.buscarEstudiantes(criterio);  // misma llamada para las 3 busquedas
        refrescarTabla(resultado);
        String nombre = nombresEstrategias[estrategiaActual];
        lblBusqueda.setText("'" + criterio.trim() + "' -> " + resultado.size() + " resultado(s)");
        trazar(C_STRATEGY, "STRATEGY", "buscarEstudiantes('" + criterio.trim() + "'): el control no sabe que algoritmo usa");
        trazar(C_STRATEGY, "STRATEGY", nombre + ".buscar() devolvio " + resultado.size() + " resultado(s)");
        lblEstado.setForeground(TEXTO_SUAVE);
        lblEstado.setText("Busqueda " + control.getNombreEstrategia() + " '" + criterio.trim() + "': "
                + resultado.size() + " resultado(s).");
    }

    // ============================================================
    //  Observer en la vista
    // ============================================================

    /** Envuelve un observador para que su chip se encienda y la traza diga que recibio el aviso. */
    private ObservadorEstudiante conTraza(Chip chip, ObservadorEstudiante real) {
        return evento -> {
            tarjetaObserver.activar();
            chip.encender();
            lblEvento.setText(evento.getTipo().name());
            trazar(C_OBSERVER, "OBSERVER", chip.nombre + ".actualizar(" + evento.getTipo() + ")");
            real.actualizar(evento);
        };
    }

    private void alRecibirEvento(EventoEstudiante evento) {
        refrescarTabla(control.listarTodos());  // la tabla se refresca sola, nadie se lo pide
    }

    // ============================================================
    //  Interfaz
    // ============================================================

    private JComponent crearEncabezado() {
        JPanel barra = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(0, 0, new Color(0x161B33), getWidth(), 0, new Color(0x2B2F6B)));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        barra.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));

        JPanel textos = new JPanel();
        textos.setOpaque(false);
        textos.setLayout(new BoxLayout(textos, BoxLayout.Y_AXIS));
        JLabel titulo = new JLabel("Gestion de Estudiantes");
        titulo.setFont(new Font(FAMILIA, Font.BOLD, 21));
        titulo.setForeground(Color.WHITE);
        JLabel sub = new JLabel("CRUD con ID ESPE y patrones de comportamiento");
        sub.setFont(F_NORMAL);
        sub.setForeground(new Color(0xB8BEE0));
        textos.add(titulo);
        textos.add(Box.createVerticalStrut(2));
        textos.add(sub);

        JPanel leyenda = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        leyenda.setOpaque(false);
        leyenda.add(pastilla("Command", C_COMMAND));
        leyenda.add(pastilla("Observer", C_OBSERVER));
        leyenda.add(pastilla("Strategy", C_STRATEGY));

        barra.add(textos, BorderLayout.WEST);
        barra.add(leyenda, BorderLayout.EAST);
        return barra;
    }

    private JComponent crearFormulario() {
        Tarjeta t = new Tarjeta();
        t.setLayout(new BoxLayout(t, BoxLayout.Y_AXIS));
        t.setPreferredSize(new Dimension(280, 0));

        t.add(tituloSeccion("Datos del estudiante"));
        t.add(Box.createVerticalStrut(4));
        t.add(textoAyuda("Cada boton crea un comando (Command)"));
        t.add(Box.createVerticalStrut(16));
        t.add(etiqueta("ID ESPE"));
        t.add(txtId);
        t.add(textoAyuda("L y 8 digitos. No se puede modificar."));
        t.add(Box.createVerticalStrut(12));
        t.add(etiqueta("Nombre"));
        t.add(txtNombre);
        t.add(Box.createVerticalStrut(12));
        t.add(etiqueta("Edad"));
        t.add(txtEdad);
        t.add(Box.createVerticalStrut(20));

        JPanel botones = new JPanel(new GridLayout(2, 2, 8, 8));
        botones.setOpaque(false);
        botones.setAlignmentX(Component.LEFT_ALIGNMENT);
        botones.setMaximumSize(new Dimension(Integer.MAX_VALUE, 84));
        botones.add(boton("Registrar", C_COMMAND, Color.WHITE, e -> registrar()));
        botones.add(boton("Actualizar", new Color(0xECEDFD), C_COMMAND, e -> actualizar()));
        botones.add(boton("Eliminar", new Color(0xFDECEE), ERROR, e -> eliminar()));
        botones.add(boton("Limpiar", new Color(0xEEF1F6), TEXTO, e -> limpiarFormulario()));
        t.add(botones);

        t.add(Box.createVerticalStrut(22));
        t.add(tituloSeccion("Reglas"));
        t.add(Box.createVerticalStrut(6));
        t.add(textoAyuda("El ID ESPE es unico y no cambia."));
        t.add(textoAyuda("La edad debe estar entre 15 y 99."));
        t.add(textoAyuda("Seleccione una fila para editar o eliminar."));
        t.add(Box.createVerticalGlue());
        return t;
    }

    private JComponent crearCentro() {
        JPanel centro = new JPanel(new BorderLayout(0, 14));
        centro.setOpaque(false);

        // ---- tabla con buscador
        Tarjeta t = new Tarjeta();
        t.setLayout(new BorderLayout(0, 12));
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setOpaque(false);
        cabecera.add(tituloSeccion("Estudiantes registrados"), BorderLayout.WEST);
        lblContador.setFont(F_PEQUENA);
        lblContador.setForeground(TEXTO_SUAVE);
        cabecera.add(lblContador, BorderLayout.EAST);

        JPanel buscador = new JPanel(new BorderLayout(8, 0));
        buscador.setOpaque(false);
        JPanel segmentos = new JPanel(new GridLayout(1, 3, 4, 0));
        segmentos.setOpaque(false);
        String[] textos = {"Por ID", "Por nombre", "Por rango edad"};
        for (int i = 0; i < 3; i++) {
            final int indice = i;
            // cada boton naranja cambia la estrategia de busqueda (Strategy)
            botonesEstrategia[i] = boton(textos[i], new Color(0xFFF4E0), C_STRATEGY, e -> cambiarEstrategia(indice));
            segmentos.add(botonesEstrategia[i]);
        }
        JPanel accionesBusqueda = new JPanel(new GridLayout(1, 2, 6, 0));
        accionesBusqueda.setOpaque(false);
        accionesBusqueda.add(boton("Buscar", C_STRATEGY, Color.WHITE, e -> buscar()));
        accionesBusqueda.add(boton("Ver todos", new Color(0xEEF1F6), TEXTO, e -> {
            txtBuscar.setText("");
            refrescarTabla(control.listarTodos());
        }));
        txtBuscar.setToolTipText("ID, nombre o rango de edad (ej. 18-25)");
        txtBuscar.addActionListener(e -> buscar());
        JLabel lblEst = etiqueta("Estrategia de busqueda (Strategy)");
        JPanel filaEst = new JPanel(new BorderLayout(10, 0));
        filaEst.setOpaque(false);
        filaEst.add(lblEst, BorderLayout.WEST);
        filaEst.add(segmentos, BorderLayout.CENTER);
        JPanel filaTexto = new JPanel(new BorderLayout(8, 0));
        filaTexto.setOpaque(false);
        filaTexto.add(txtBuscar, BorderLayout.CENTER);
        filaTexto.add(accionesBusqueda, BorderLayout.EAST);
        buscador.setLayout(new BorderLayout(0, 8));
        buscador.add(filaEst, BorderLayout.NORTH);
        buscador.add(filaTexto, BorderLayout.SOUTH);

        JPanel arriba = new JPanel(new BorderLayout(0, 10));
        arriba.setOpaque(false);
        arriba.add(cabecera, BorderLayout.NORTH);
        arriba.add(buscador, BorderLayout.SOUTH);

        estilizarTabla();
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createLineBorder(BORDE));
        scroll.getViewport().setBackground(TARJETA);
        scroll.getVerticalScrollBar().setUI(new BarraFina());
        t.add(arriba, BorderLayout.NORTH);
        t.add(scroll, BorderLayout.CENTER);

        // ---- traza de patrones
        Tarjeta traza = new Tarjeta();
        traza.setLayout(new BorderLayout(0, 8));
        traza.setPreferredSize(new Dimension(0, 300));
        JPanel cab = new JPanel(new BorderLayout());
        cab.setOpaque(false);
        cab.add(tituloSeccion("Traza de patrones"), BorderLayout.WEST);
        cab.add(textoAyuda("Que patron intervino en cada paso"), BorderLayout.EAST);
        listaTraza.setCellRenderer(new RenderTraza());
        listaTraza.setBackground(TARJETA);
        listaTraza.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaTraza.setFocusable(false);
        JScrollPane scrollTraza = new JScrollPane(listaTraza);
        scrollTraza.setBorder(BorderFactory.createLineBorder(BORDE));
        scrollTraza.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTraza.getVerticalScrollBar().setUI(new BarraFina());
        traza.add(cab, BorderLayout.NORTH);
        traza.add(scrollTraza, BorderLayout.CENTER);

        centro.add(t, BorderLayout.CENTER);
        centro.add(traza, BorderLayout.SOUTH);
        return centro;
    }

    private JComponent crearPanelPatrones() {
        JPanel columna = new JPanel();
        columna.setOpaque(false);
        columna.setLayout(new BoxLayout(columna, BoxLayout.Y_AXIS));
        columna.setPreferredSize(new Dimension(360, 0));

        // ---- Command
        tarjetaCommand.contenido.add(fila("Ultimo comando", lblComando));
        tarjetaCommand.contenido.add(fila("Receptor", lblReceptor));
        tarjetaCommand.contenido.add(Box.createVerticalStrut(6));
        tarjetaCommand.contenido.add(etiqueta("Historial (pila, el de arriba se deshace primero)"));
        JList<String> listaHistorial = new JList<>(modeloHistorial);
        listaHistorial.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
        listaHistorial.setForeground(TEXTO);
        listaHistorial.setBackground(new Color(0xF5F5FE));
        listaHistorial.setFocusable(false);
        listaHistorial.setVisibleRowCount(4);
        listaHistorial.setCellRenderer(new RenderPila());
        JScrollPane sp = new JScrollPane(listaHistorial);
        sp.setBorder(BorderFactory.createLineBorder(new Color(0xDCDDFB)));
        sp.setAlignmentX(Component.LEFT_ALIGNMENT);
        sp.setPreferredSize(new Dimension(300, 96));
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.getVerticalScrollBar().setUI(new BarraFina());
        sp.setMaximumSize(new Dimension(Integer.MAX_VALUE, 96));
        tarjetaCommand.contenido.add(sp);
        tarjetaCommand.contenido.add(Box.createVerticalStrut(8));
        JButton btnDeshacer = boton("Deshacer ultimo", C_COMMAND, Color.WHITE, e -> deshacer());
        btnDeshacer.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnDeshacer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        tarjetaCommand.contenido.add(btnDeshacer);

        // ---- Observer
        tarjetaObserver.contenido.add(fila("Ultimo evento", lblEvento));
        tarjetaObserver.contenido.add(Box.createVerticalStrut(4));
        tarjetaObserver.contenido.add(etiqueta("Suscritos (se encienden al recibir el aviso)"));
        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        chips.setOpaque(false);
        chips.setAlignmentX(Component.LEFT_ALIGNMENT);
        chips.add(chipAuditoria);
        chips.add(chipNotificacion);
        chips.add(chipVista);
        tarjetaObserver.contenido.add(chips);

        // ---- Strategy
        tarjetaStrategy.contenido.add(fila("Estrategia activa", lblEstrategia));
        tarjetaStrategy.contenido.add(fila("Contexto", valor("ControlEstudiante")));
        tarjetaStrategy.contenido.add(fila("Ultima busqueda", lblBusqueda));

        columna.add(tarjetaCommand);
        columna.add(Box.createVerticalStrut(12));
        columna.add(tarjetaObserver);
        columna.add(Box.createVerticalStrut(12));
        columna.add(tarjetaStrategy);
        columna.add(Box.createVerticalGlue());
        return columna;
    }

    private JComponent crearBarraEstado() {
        JPanel barra = new JPanel(new BorderLayout());
        barra.setBackground(TARJETA);
        barra.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDE),
                BorderFactory.createEmptyBorder(9, 20, 9, 20)));
        lblEstado.setFont(F_NEGRITA);
        lblEstado.setForeground(TEXTO_SUAVE);
        barra.add(lblEstado, BorderLayout.WEST);
        return barra;
    }

    // ============================================================
    //  Utilidades
    // ============================================================

    /** Antes de cada accion se apagan las tarjetas, asi solo brillan los patrones que intervinieron. */
    private void iniciarAccion(String accion) {
        tarjetaCommand.apagar();
        tarjetaObserver.apagar();
        tarjetaStrategy.apagar();
        chipAuditoria.apagar();
        chipNotificacion.apagar();
        chipVista.apagar();
        paso = 0;
        modeloTraza.addElement(new Traza(null, accion, null, true));
    }

    private void trazar(Color color, String patron, String texto) {
        paso++;
        modeloTraza.addElement(new Traza(color, patron, texto, false));
        listaTraza.ensureIndexIsVisible(modeloTraza.size() - 1);
    }

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
        actualizarPila();
    }

    private void actualizarPila() {
        modeloHistorial.clear();
        List<String> historial = invocador.verHistorial();
        for (int i = historial.size() - 1; i >= 0; i--) {
            modeloHistorial.addElement(historial.get(i));
        }
        if (historial.isEmpty()) modeloHistorial.addElement("(vacio)");
    }

    private void marcarEstrategia(int indice) {
        estrategiaActual = indice;
        lblEstrategia.setText(nombresEstrategias[indice]);
        for (int i = 0; i < 3; i++) {
            boolean activo = i == indice;
            botonesEstrategia[i].setBackground(activo ? C_STRATEGY : new Color(0xFFF4E0));
            botonesEstrategia[i].setForeground(activo ? Color.WHITE : C_STRATEGY);
        }
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
        txtId.setBackground(CAMPO);
        txtNombre.setText("");
        txtEdad.setText("");
        tabla.clearSelection();
    }

    private void cargarSeleccion() {
        int fila = tabla.getSelectedRow();
        if (fila < 0) return;
        txtId.setText(String.valueOf(modeloTabla.getValueAt(fila, 0)));
        txtId.setEditable(false);  // al seleccionar, el ID se bloquea para que no lo cambien
        txtId.setBackground(new Color(0xE9ECF3));
        txtNombre.setText(String.valueOf(modeloTabla.getValueAt(fila, 1)));
        txtEdad.setText(String.valueOf(modeloTabla.getValueAt(fila, 2)));
    }

    private void estilizarTabla() {
        tabla.setFont(F_NORMAL);
        tabla.setRowHeight(32);
        tabla.setShowVerticalLines(false);
        tabla.setGridColor(new Color(0xEEF0F5));
        tabla.setSelectionBackground(new Color(0xE6E7FD));
        tabla.setSelectionForeground(TEXTO);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setFillsViewportHeight(true);
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) cargarSeleccion();
        });
        JTableHeader cabecera = tabla.getTableHeader();
        cabecera.setReorderingAllowed(false);
        cabecera.setPreferredSize(new Dimension(0, 34));
        cabecera.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
                JLabel l = (JLabel) super.getTableCellRendererComponent(t, v, s, f, r, c);
                l.setFont(new Font(FAMILIA, Font.BOLD, 12));
                l.setForeground(TEXTO_SUAVE);
                l.setBackground(new Color(0xF6F7FA));
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
                if (!s) l.setBackground(r % 2 == 0 ? TARJETA : new Color(0xFAFBFD));
                l.setFont(c == 0 ? new Font(Font.MONOSPACED, Font.BOLD, 13) : F_NORMAL);
                l.setForeground(c == 0 ? C_COMMAND : TEXTO);
                return l;
            }
        });
        tabla.getColumnModel().getColumn(0).setPreferredWidth(130);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(260);
        tabla.getColumnModel().getColumn(2).setPreferredWidth(60);
    }

    private static String elegirFuente() {
        List<String> disponibles = Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames());
        for (String f : new String[]{"Segoe UI", "Inter", "Helvetica Neue", "Arial"}) {
            if (disponibles.contains(f)) return f;
        }
        return Font.SANS_SERIF;
    }

    private static JTextField campo(String ayuda) {
        JTextField c = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        c.setOpaque(false);
        c.setFont(F_NORMAL);
        c.setForeground(TEXTO);
        c.setBackground(CAMPO);
        c.setToolTipText(ayuda);
        c.setBorder(BorderFactory.createCompoundBorder(new BordeRedondo(BORDE, 10),
                BorderFactory.createEmptyBorder(7, 10, 7, 10)));
        c.setAlignmentX(Component.LEFT_ALIGNMENT);
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        c.setPreferredSize(new Dimension(180, 38));
        return c;
    }

    private static JButton boton(String texto, Color fondo, Color letra, java.awt.event.ActionListener accion) {
        JButton b = new JButton(texto);
        b.setUI(new BasicButtonUI() {
            @Override
            public void paint(Graphics g, JComponent c) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(c.getBackground());
                g2.fillRoundRect(0, 0, c.getWidth(), c.getHeight(), 12, 12);
                g2.dispose();
                super.paint(g, c);
            }
        });
        b.setFont(F_NEGRITA);
        b.setBackground(fondo);
        b.setForeground(letra);
        b.setOpaque(false);
        b.setContentAreaFilled(false);
        b.setBorder(BorderFactory.createEmptyBorder(9, 12, 9, 12));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(accion);
        return b;
    }

    private static JLabel tituloSeccion(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(F_TITULO);
        l.setForeground(TEXTO);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private static JLabel etiqueta(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font(FAMILIA, Font.BOLD, 11));
        l.setForeground(TEXTO_SUAVE);
        l.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private static JLabel textoAyuda(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(F_PEQUENA);
        l.setForeground(TEXTO_SUAVE);
        l.setBorder(BorderFactory.createEmptyBorder(3, 0, 0, 0));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private static JLabel valor(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(new Font(Font.MONOSPACED, Font.BOLD, 12));
        l.setForeground(TEXTO);
        return l;
    }

    private static JPanel fila(String nombre, JLabel valor) {
        JPanel p = new JPanel(new BorderLayout(0, 2));
        p.setOpaque(false);
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        p.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        JLabel n = new JLabel(nombre);
        n.setFont(new Font(FAMILIA, Font.BOLD, 11));
        n.setForeground(TEXTO_SUAVE);
        p.add(n, BorderLayout.NORTH);
        p.add(valor, BorderLayout.CENTER);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        return p;
    }

    private static JLabel pastilla(String texto, Color color) {
        JLabel l = new JLabel(texto, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        l.setFont(new Font(FAMILIA, Font.BOLD, 12));
        l.setForeground(Color.WHITE);
        l.setBorder(BorderFactory.createEmptyBorder(5, 14, 5, 14));
        return l;
    }

    // ============================================================
    //  Componentes propios
    // ============================================================

    /** Panel blanco con esquinas redondeadas. */
    private static class Tarjeta extends JPanel {
        Color borde = BORDE;
        int grosor = 1;

        Tarjeta() {
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(16, 18, 16, 18));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 14));
            g2.fillRoundRect(1, 3, getWidth() - 2, getHeight() - 3, 18, 18);
            g2.setColor(TARJETA);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 3, 18, 18);
            g2.setColor(borde);
            g2.setStroke(new BasicStroke(grosor));
            g2.drawRoundRect(grosor / 2, grosor / 2, getWidth() - 1 - grosor, getHeight() - 4 - grosor, 18, 18);
            g2.dispose();
        }
    }

    /** Tarjeta de un patron: se resalta con su color cuando el patron interviene en la accion. */
    private static class TarjetaPatron extends Tarjeta {
        final JPanel contenido = new JPanel();
        private final Color color;
        private final JLabel estado = new JLabel("en espera");

        TarjetaPatron(String nombre, String descripcion, Color color) {
            this.color = color;
            setLayout(new BorderLayout(0, 10));
            setAlignmentX(Component.LEFT_ALIGNMENT);
            JPanel cab = new JPanel(new BorderLayout());
            cab.setOpaque(false);
            JPanel izq = new JPanel();
            izq.setOpaque(false);
            izq.setLayout(new BoxLayout(izq, BoxLayout.Y_AXIS));
            JLabel n = new JLabel(nombre);
            n.setFont(new Font(FAMILIA, Font.BOLD, 14));
            n.setForeground(color);
            JLabel d = new JLabel(descripcion);
            d.setFont(F_PEQUENA);
            d.setForeground(TEXTO_SUAVE);
            izq.add(n);
            izq.add(d);
            estado.setFont(new Font(FAMILIA, Font.BOLD, 10));
            estado.setForeground(TEXTO_SUAVE);
            cab.add(izq, BorderLayout.WEST);
            cab.add(estado, BorderLayout.EAST);
            contenido.setOpaque(false);
            contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
            add(cab, BorderLayout.NORTH);
            add(contenido, BorderLayout.CENTER);
        }

        void activar() {
            borde = color;
            grosor = 3;
            estado.setText("ACTIVO");
            estado.setForeground(color);
            repaint();
        }

        void apagar() {
            borde = BORDE;
            grosor = 1;
            estado.setText("en espera");
            estado.setForeground(TEXTO_SUAVE);
            repaint();
        }
    }

    /** Chip de un observador: verde cuando recibio el ultimo aviso. */
    private static class Chip extends JLabel {
        final String nombre;
        private boolean encendido;

        Chip(String nombre) {
            super(nombre);
            this.nombre = nombre;
            setFont(new Font(FAMILIA, Font.BOLD, 12));
            setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
            apagar();
        }

        void encender() {
            encendido = true;
            setText("✓ " + nombre);
            setForeground(Color.WHITE);
            repaint();
        }

        void apagar() {
            encendido = false;
            setText(nombre);
            setForeground(TEXTO_SUAVE);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(encendido ? C_OBSERVER : new Color(0xEEF1F5));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }
    }

    /** Una linea de la traza: patron que intervino y que hizo. */
    private static class Traza {
        final Color color;
        final String patron;
        final String texto;
        final boolean encabezado;

        Traza(Color color, String patron, String texto, boolean encabezado) {
            this.color = color;
            this.patron = patron;
            this.texto = texto;
            this.encabezado = encabezado;
        }
    }

    private static class RenderTraza extends JPanel implements ListCellRenderer<Traza> {
        private final JLabel etiqueta = new JLabel("", SwingConstants.CENTER);
        private final JLabel texto = new JLabel();
        private Color colorEtiqueta = C_SISTEMA;
        private boolean encabezado;

        RenderTraza() {
            setLayout(new BorderLayout(10, 0));
            setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
            etiqueta.setFont(new Font(FAMILIA, Font.BOLD, 10));
            etiqueta.setForeground(Color.WHITE);
            etiqueta.setPreferredSize(new Dimension(78, 20));
            texto.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
            add(etiqueta, BorderLayout.WEST);
            add(texto, BorderLayout.CENTER);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends Traza> lista, Traza t, int i, boolean sel, boolean foco) {
            encabezado = t.encabezado;
            if (t.encabezado) {
                etiqueta.setText("");
                etiqueta.setPreferredSize(new Dimension(0, 22));
                texto.setText("▶ Accion: " + t.patron);
                texto.setFont(new Font(FAMILIA, Font.BOLD, 12));
                texto.setForeground(TEXTO);
                setBackground(new Color(0xF1F3F8));
            } else {
                colorEtiqueta = t.color;
                etiqueta.setText(t.patron);
                etiqueta.setPreferredSize(new Dimension(78, 20));
                texto.setText(t.texto);
                texto.setFont(F_MONO);
                texto.setForeground(TEXTO);
                setBackground(TARJETA);
            }
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (!encabezado) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(colorEtiqueta);
                Insets in = getInsets();
                g2.fillRoundRect(in.left, (getHeight() - 20) / 2, 78, 20, 20, 20);
                g2.dispose();
            }
        }
    }

    /** Pinta la pila del historial con el tope resaltado. */
    private static class RenderPila extends javax.swing.DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> l, Object v, int i, boolean s, boolean f) {
            JLabel c = (JLabel) super.getListCellRendererComponent(l, v, i, false, false);
            c.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 11));
            c.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));
            boolean vacio = "(vacio)".equals(v);
            if (i == 0 && !vacio) {
                c.setText("↑ tope: " + v);
                c.setBackground(new Color(0xE3E3FC));
                c.setForeground(C_COMMAND);
                c.setFont(new Font(Font.MONOSPACED, Font.BOLD, 11));
            } else {
                c.setBackground(new Color(0xF5F5FE));
                c.setForeground(vacio ? TEXTO_SUAVE : TEXTO);
            }
            return c;
        }
    }

    /** Barra de desplazamiento delgada y sin flechas. */
    private static class BarraFina extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            thumbColor = new Color(0xC9CED9);
            trackColor = TARJETA;
        }

        @Override
        protected JButton createDecreaseButton(int o) { return sinBoton(); }

        @Override
        protected JButton createIncreaseButton(int o) { return sinBoton(); }

        private JButton sinBoton() {
            JButton b = new JButton();
            b.setPreferredSize(new Dimension(0, 0));
            return b;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, java.awt.Rectangle r) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(r.x + 3, r.y + 2, r.width - 6, r.height - 4, 8, 8);
            g2.dispose();
        }

        @Override
        public Dimension getPreferredSize(JComponent c) { return new Dimension(10, 10); }
    }

    /** Borde redondeado para los campos de texto. */
    private static class BordeRedondo extends AbstractBorder {
        private final Color color;
        private final int radio;

        BordeRedondo(Color color, int radio) {
            this.color = color;
            this.radio = radio;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radio, radio);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ignorada) {
            // si no se puede, se usa el aspecto por defecto
        }
        SwingUtilities.invokeLater(() -> new CRUDEstudiantesGUI().setVisible(true));
    }
}
