package bocatto.modelo;

/** Entidad Pedido de la Guia 2, solo con lo que usan los dos patrones. */
public class Pedido {
    private final String id;
    private final int numeroMesa;
    private final double total;
    private String estado = "pendiente";  // todo pedido nace pendiente
    private String metodoPago;

    public Pedido(String id, int numeroMesa, double total) {
        this.id = id;
        this.numeroMesa = numeroMesa;
        this.total = total;
    }

    public String getId() { return id; }
    public int getNumeroMesa() { return numeroMesa; }
    public double getTotal() { return total; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getMetodoPago() { return metodoPago; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }

    @Override
    public String toString() {
        return "Pedido #" + id + " (mesa " + numeroMesa + ", $" + String.format("%.2f", total) + ", " + estado + ")";
    }
}
