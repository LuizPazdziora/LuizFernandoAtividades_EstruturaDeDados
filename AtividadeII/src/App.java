import javax.swing.JOptionPane;

public class App {
    private ListaDupla<Cidade> cidades;

    public App() {
        this.cidades = new ListaDupla<>();
    }

    public void iniciar() {
        String menu = """
                Escolha uma opção:
                1 - Cadastrar cidade
                2 - Cadastrar ligação entre cidades
                3 - Listar todas as cidades e ligações
                4 - Verificar ligação direta e tempo estimado
                5 - Listar ligações dentro de um tempo limite
                6 - Sair
                """;
    
        int opcao = -1; // Inicializa com valor inválido para garantir entrada válida
        do {
            try {
                String entrada = JOptionPane.showInputDialog(menu);
                if (entrada == null) { // Usuário cancelou a entrada
                    JOptionPane.showMessageDialog(null, "Encerrando sistema...");
                    break;
                }
    
                opcao = Integer.parseInt(entrada); // Converte entrada para número
    
                switch (opcao) {
                    case 1 -> cadastrarCidade();
                    case 2 -> cadastrarLigacoes();
                    case 3 -> listarCidades();
                    case 4 -> {
                        String origem = JOptionPane.showInputDialog("Digite o nome da cidade de origem:");
                        String destino = JOptionPane.showInputDialog("Digite o nome da cidade de destino:");
                        verificarLigacao(origem, destino);
                    }
                    case 5 -> {
                        try {
                            double tempoMaximo = Double.parseDouble(JOptionPane.showInputDialog("Digite o tempo máximo para entrega (minutos):"));
                            listarLigacoesPorTempo(tempoMaximo);
                        } catch (NumberFormatException e) {
                            JOptionPane.showMessageDialog(null, "Entrada inválida! Digite um número.");
                        }
                    }
                    case 6 -> JOptionPane.showMessageDialog(null, "Encerrando sistema...");
                    default -> JOptionPane.showMessageDialog(null, "Opção inválida! Digite um número entre 1 e 6.");
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Entrada inválida! Digite um número entre 1 e 6.");
            }
        } while (opcao != 6);
    }

    private void cadastrarCidade() {
        String nome = JOptionPane.showInputDialog("Digite o nome da cidade:");

        if (buscarCidade(nome) != null) {
            JOptionPane.showMessageDialog(null, "A cidade '" + nome + "' já está cadastrada!");
            return;
        }

        Cidade novaCidade = new Cidade(nome);
        cidades.inserir(novaCidade);
        JOptionPane.showMessageDialog(null, "Cidade '" + nome + "' cadastrada com sucesso!");
    }

    private void cadastrarLigacoes() {
        if (cidades.getTamanho() < 2) {
            JOptionPane.showMessageDialog(null, "Cadastre pelo menos duas cidades antes de criar ligações.");
            return;
        }

        String origemNome = JOptionPane.showInputDialog("Nome da cidade de origem:");
        Cidade origem = buscarCidade(origemNome);

        if (origem == null) {
            JOptionPane.showMessageDialog(null, "A cidade '" + origemNome + "' não existe!");
            return;
        }

        String destinoNome = JOptionPane.showInputDialog("Nome da cidade de destino:");
        Cidade destino = buscarCidade(destinoNome);

        if (destino == null) {
            JOptionPane.showMessageDialog(null, "A cidade de destino '" + destinoNome + "' não existe!");
            return;
        }

        try {
            double distancia = Double.parseDouble(JOptionPane.showInputDialog("Distância entre cidades (Km):"));
            double fatorTrafego = Double.parseDouble(JOptionPane.showInputDialog("Fator de tráfego (0 a 2):"));
            int pedagios = Integer.parseInt(JOptionPane.showInputDialog("Número de pedágios:"));

            origem.adicionarLigacao(destino, distancia, fatorTrafego, pedagios);
            JOptionPane.showMessageDialog(null, "Ligação criada entre " + origemNome + " e " + destinoNome);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Erro: Digite apenas números.");
        }
    }

    private void listarCidades() {
        if (cidades.estaVazia()) {
            JOptionPane.showMessageDialog(null, "Nenhuma cidade cadastrada!");
            return;
        }

        StringBuilder sb = new StringBuilder("Lista de cidades e suas ligações:\n");

        No<Cidade> atual = cidades.getInicio();
        while (atual != null) {
            Cidade cidade = atual.getDado();
            sb.append("\n🔹 **Cidade**: ").append(cidade.getNome()).append("\n");

            No<Ligacao> ligacaoAtual = cidade.getLigacoesDiretas().getInicio();
            if (ligacaoAtual == null) {
                sb.append("   ↳ Nenhuma ligação cadastrada.\n");
            } else {
                while (ligacaoAtual != null) {
                    sb.append("   ↳ ").append(ligacaoAtual.getDado()).append("\n");
                    ligacaoAtual = ligacaoAtual.getProximo();
                }
            }
            atual = atual.getProximo();
        }

        JOptionPane.showMessageDialog(null, sb.toString());
    }

    private void verificarLigacao(String origem, String destino) {
        Cidade cidadeOrigem = buscarCidade(origem);

        if (cidadeOrigem == null) {
            JOptionPane.showMessageDialog(null, "Cidade de origem não encontrada!");
            return;
        }

        No<Ligacao> ligacaoAtual = cidadeOrigem.getLigacoesDiretas().getInicio();

        while (ligacaoAtual != null) {
            Ligacao ligacao = ligacaoAtual.getDado();
            if (ligacao.getDestino().getNome().equalsIgnoreCase(destino)) {
                JOptionPane.showMessageDialog(null, "Existe ligação direta!\nTempo estimado: "
                        + ligacao.calcularTempoEntrega() + " minutos.");
                return;
            }
            ligacaoAtual = ligacaoAtual.getProximo();
        }

        JOptionPane.showMessageDialog(null, "Não há ligação direta entre " + origem + " e " + destino + ".");
    }

    private void listarLigacoesPorTempo(double tempoMaximo) {
        if (cidades.estaVazia()) {
            JOptionPane.showMessageDialog(null, "Nenhuma cidade cadastrada!");
            return;
        }
    
        StringBuilder sb = new StringBuilder("Entregas possíveis com tempo ≤ " + tempoMaximo + " minutos:\n");
        No<Cidade> atual = cidades.getInicio();
        boolean encontrouLigacoes = false;
    
        while (atual != null) { // Percorre todas as cidades
            Cidade cidade = atual.getDado();
            No<Ligacao> ligacaoAtual = cidade.getLigacoesDiretas().getInicio();
    
            while (ligacaoAtual != null) { // Percorre todas as ligações da cidade
                Ligacao ligacao = ligacaoAtual.getDado();
                double tempoEntrega = ligacao.calcularTempoEntrega();
    
                if (tempoEntrega <= tempoMaximo) { 
                    sb.append(cidade.getNome()).append(" → ")
                      .append(ligacao.getDestino().getNome())
                      .append(" | Tempo estimado: ").append(String.format("%.2f", tempoEntrega)).append(" min\n");
                    encontrouLigacoes = true;
                }
    
                ligacaoAtual = ligacaoAtual.getProximo();
            }
    
            atual = atual.getProximo();
        }
    
        // Exibir mensagem correta conforme resultados encontrados
        if (encontrouLigacoes) {
            JOptionPane.showMessageDialog(null, sb.toString());
        } else {
            JOptionPane.showMessageDialog(null, "Nenhuma ligação disponível dentro do tempo especificado.");
        }
    }

    private Cidade buscarCidade(String nome) {
        No<Cidade> atual = cidades.getInicio();
        while (atual != null) {
            if (atual.getDado().getNome().equalsIgnoreCase(nome)) {
                return atual.getDado();
            }
            atual = atual.getProximo();
        }
        return null;
    }

    public static void main(String[] args) {
        App sistema = new App();
        sistema.iniciar();
    }
}