package geral;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InterfaceTarefas extends JFrame {
    private GerenciadorDeTarefas gerenciadorDeTarefas;
    private JTable table;
    private DefaultTableModel model;
    private JTextField tfTitulo, tfDescricao;
    private JComboBox<String> comboPrioridade;
    private JCheckBox cbConcluida;

    public InterfaceTarefas() {
        // Instanciando o Gerenciador de Tarefas
        gerenciadorDeTarefas = new GerenciadorDeTarefas();

        // Configurações básicas da janela
        setTitle("Gerenciador de Tarefas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Barra de título personalizada
        JLabel header = new JLabel(" Gerenciador de Tarefas", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 24));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(50, 50, 150));
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(getWidth(), 50));
        add(header, BorderLayout.NORTH);

        // Painel para entrada de dados
        JPanel panelInput = new JPanel();
        panelInput.setLayout(new GridLayout(5, 2, 10, 10));
        panelInput.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelInput.setAlignmentX(CENTER_ALIGNMENT);

        // Definir a posição central para os componentes
        JLabel labelTitulo = new JLabel("Título:");
        tfTitulo = new JTextField();
        tfTitulo.setBorder(new LineBorder(Color.GRAY, 1)); // Adicionando borda
        tfTitulo.setHorizontalAlignment(JTextField.CENTER); // Centraliza o texto
        panelInput.add(labelTitulo);
        panelInput.add(tfTitulo);

        JLabel labelDescricao = new JLabel("Descrição:");
        tfDescricao = new JTextField();
        tfDescricao.setBorder(new LineBorder(Color.GRAY, 1)); // Adicionando borda
        tfDescricao.setHorizontalAlignment(JTextField.CENTER); // Centraliza o texto
        panelInput.add(labelDescricao);
        panelInput.add(tfDescricao);

        JLabel labelPrioridade = new JLabel("Prioridade:");
        comboPrioridade = new JComboBox<>(new String[]{"Baixa", "Média", "Alta"});
        panelInput.add(labelPrioridade);
        panelInput.add(comboPrioridade);


        // Centralizando o painel de entrada
        JPanel panelCentralizado = new JPanel();
        panelCentralizado.setLayout(new BorderLayout());
        panelCentralizado.add(panelInput, BorderLayout.CENTER);
        add(panelCentralizado, BorderLayout.NORTH);

        // Configurando a tabela
        String[] colunas = {"Título", "Descrição", "Prioridade", "Concluída"};
        model = new DefaultTableModel(colunas, 0);
        table = new JTable(model) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 3) {
                    return Boolean.class;  // Coluna de conclusão (checkbox)
                }
                return super.getColumnClass(columnIndex);
            }
        };

        // Definindo o renderizador para a coluna de Prioridade
        table.getColumnModel().getColumn(2).setCellRenderer(new PriorityCellRenderer());
        table.getColumnModel().getColumn(3).setCellEditor(table.getDefaultEditor(Boolean.class)); // Definindo o editor para checkbox
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Painel de botões
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton bAdicionar = new JButton("Adicionar");
        bAdicionar.setIcon(new ImageIcon("icons/add.png"));
        JButton bLimpar = new JButton("Limpar");
        bLimpar.setIcon(new ImageIcon("icons/clear.png"));
        JButton bExcluir = new JButton("Excluir Tarefa");
        bExcluir.setIcon(new ImageIcon("icons/delete.png"));

        panelButtons.add(bAdicionar);
        panelButtons.add(bLimpar);
        panelButtons.add(bExcluir);
        add(panelButtons, BorderLayout.SOUTH);

        // Funcionalidade de Adicionar
        bAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String titulo = tfTitulo.getText();
                String descricao = tfDescricao.getText();
                String prioridade = (String) comboPrioridade.getSelectedItem();


                if (titulo.isEmpty() || descricao.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos!");
                } else {
                    gerenciadorDeTarefas.adicionarTarefa(titulo, descricao, prioridade);
                    model.addRow(new Object[]{titulo, descricao, prioridade});
                    tfTitulo.setText("");
                    tfDescricao.setText("");
                }
            }
        });

        // Funcionalidade de Limpar
        bLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Criar opções personalizadas
                Object[] opcoes = {"Sim", "Não"};
                int confirm = JOptionPane.showOptionDialog(
                        null,
                        "Deseja realmente limpar todas as tarefas?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        opcoes,
                        opcoes[0]
                );

                if (confirm == JOptionPane.YES_OPTION) {
                    gerenciadorDeTarefas.limparTarefas();
                    model.setRowCount(0); // Remove todas as linhas da tabela
                    JOptionPane.showMessageDialog(null, "Todas as tarefas foram removidas!");
                }
            }
        });

        // Funcionalidade de Excluir
        bExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    gerenciadorDeTarefas.removerTarefa(selectedRow);
                    model.removeRow(selectedRow); // Remove a linha da tabela
                    JOptionPane.showMessageDialog(null, "Tarefa excluída com sucesso!");
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma tarefa para excluir.");
                }
            }
        });
    }

    // Classe para renderizar a célula de prioridade com cores
    private class PriorityCellRenderer extends JLabel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value.toString());

            // Definindo as cores baseadas na prioridade
            switch (value.toString()) {
                case "Baixa":
                    setBackground(Color.GREEN);
                    break;
                case "Média":
                    setBackground(Color.YELLOW);
                    break;
                case "Alta":
                    setBackground(Color.RED);
                    break;
                default:
                    setBackground(Color.WHITE);
                    break;
            }

            setOpaque(true);
            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            InterfaceTarefas interfaceTarefas = new InterfaceTarefas();
            interfaceTarefas.setVisible(true);
        });
    }
}
