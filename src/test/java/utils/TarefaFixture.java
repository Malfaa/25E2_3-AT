package utils;

import org.example.dto.TarefaDTO;
import org.example.model.Tarefa;

public class TarefaFixture {
    public static final int ID_TAREFA = 1;
    public static final String TITULO_TAREFA = "Tarefa 1";
    public static final String TITULO_TAREFA_DTO = "Tarefa 1 DTO";
    public static final String DESCRICAO_TAREFA = "Esta é a descrição da Tarefa 1";
    public static final String DESCRICAO_TAREFA_DTO = "Esta é a descrição da Tarefa 1 DTO";
    public static final boolean CONCLUIDA_TAREFA_ROTULO = true;
    public static final boolean CONCLUIDA_TAREFA_ROTULO_DTO = true;


    public static Tarefa buildTarefa() {
        Tarefa tarefa = new Tarefa();
        tarefa.setId(ID_TAREFA);
        tarefa.setTitulo(TITULO_TAREFA);
        tarefa.setDescricao(DESCRICAO_TAREFA);
        tarefa.setConcluida(CONCLUIDA_TAREFA_ROTULO);
        return tarefa;
    }

    public static TarefaDTO buildTarefaDTO() {
        TarefaDTO dto = new TarefaDTO();
        dto.setTitulo(TITULO_TAREFA_DTO);
        dto.setDescricao(DESCRICAO_TAREFA_DTO);
        dto.setConcluida(CONCLUIDA_TAREFA_ROTULO_DTO);
        return dto;
    }
}
