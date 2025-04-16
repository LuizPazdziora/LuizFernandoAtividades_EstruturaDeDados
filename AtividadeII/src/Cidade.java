import javax.swing.JOptionPane;

public class Cidade {
    private String nome;
    private ListaDupla<Ligacao> ligacoesDiretas;

    public Cidade(String nome) {
        this.nome = nome;
        this.ligacoesDiretas = new ListaDupla<>();
    }

    public String getNome() {
        return nome;
    }

    public ListaDupla<Ligacao> getLigacoesDiretas() {
        return ligacoesDiretas;
    }

    public void adicionarLigacao(Cidade destino, double distancia, double fatorTrafego, int pedagios) {
        if (destino == null) {
            JOptionPane.showMessageDialog(null, "Erro: A cidade de destino não pode ser nula!");
            return;
        }

        Ligacao novaLigacao = new Ligacao(destino, distancia, fatorTrafego, pedagios);
        ligacoesDiretas.inserir(novaLigacao);
        JOptionPane.showMessageDialog(null, "Ligação criada entre " + this.nome + " e " + destino.getNome());
    }

    public void listarLigacoes() {
        if (ligacoesDiretas.estaVazia()) {
            JOptionPane.showMessageDialog(null, "A cidade " + nome + " não possui ligações diretas.");
            return;
        }

        StringBuilder sb = new StringBuilder("Ligações diretas da cidade " + nome + ":\n");
        No<Ligacao> aux = ligacoesDiretas.getInicio();

        while (aux != null) {
            sb.append(" → ").append(aux.getDado().getDestino().getNome())
              .append(" | Distância: ").append(aux.getDado().getDistancia())
              .append(" | Tráfego: ").append(aux.getDado().getFatorTrafego())
              .append(" | Pedágios: ").append(aux.getDado().getPedagios())
              .append(" | Tempo estimado: ").append(aux.getDado().calcularTempoEntrega()).append(" min\n");
            aux = aux.getProximo();
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }
}