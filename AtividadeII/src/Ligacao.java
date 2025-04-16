public class Ligacao {
    private Cidade destino;
    private double distancia;
    private double fatorTrafego;
    private int pedagios;

    public Ligacao(Cidade destino, double distancia, double fatorTrafego, int pedagios) {
        this.destino = destino;
        this.distancia = distancia;
        this.fatorTrafego = fatorTrafego;
        this.pedagios = pedagios;
    }

    public Cidade getDestino() {
        return destino;
    }

    public double getDistancia() {
        return distancia;
    }

    public double getFatorTrafego() {
        return fatorTrafego;
    }

    public int getPedagios() {
        return pedagios;
    }

    public double calcularTempoEntrega() {
        return (distancia * fatorTrafego) + (pedagios * 2);
    }

    @Override
    public String toString() {
        return String.format("Destino: %s | Distância: %.2f Km | Tráfego: %.2f | Pedágios: %d | Tempo estimado: %.2f min",
                         destino.getNome(), distancia, fatorTrafego, pedagios, calcularTempoEntrega());
    }
}