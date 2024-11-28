package geral;

import java.util.ArrayList;
import java.util.List;

public class GerenciadorDeTarefas {
    private List<Tarefa> tarefas;

    public GerenciadorDeTarefas() {
        tarefas = new ArrayList<>();
    }

    public void adicionarTarefa(String titulo, String descricao, String prioridade) {
        tarefas.add(new Tarefa(titulo, descricao, prioridade));
    }

    public void removerTarefa(int index) {
        if (index >= 0 && index < tarefas.size()) {
            tarefas.remove(index);
        }
    }

    public void limparTarefas() {
        tarefas.clear();
    }

    public List<Tarefa> getTarefas() {
        return tarefas;
    }

    public void concluirTarefa(int index) {
        if (index >= 0 && index < tarefas.size()) {
            tarefas.get(index).setConcluida(true);
        }
    }

    public void removerTarefa(String titulo) {
    }
}
