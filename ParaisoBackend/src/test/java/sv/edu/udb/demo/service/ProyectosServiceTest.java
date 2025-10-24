package sv.edu.udb.demo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sv.edu.udb.demo.dto.ProyectosCreateDTO;
import sv.edu.udb.demo.dto.ProyectosUpdateDTO;
import sv.edu.udb.demo.model.Proyectos;
import sv.edu.udb.demo.repository.ProyectosRepository;

import java.util.List;
import java.util.Optional;

// Importaciones estáticas para Mockito y AssertJ
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProyectosServiceTest {

    @Mock
    private ProyectosRepository repo;

    @InjectMocks
    private ProyectosService proyectosService;

    @Test
    void cuandoFindAll_entoncesRetornaLista() {
        // 1. Arrange
        Proyectos p1 = Proyectos.builder().id(1).tit("Proyecto 1").build();
        Proyectos p2 = Proyectos.builder().id(2).tit("Proyecto 2").build();
        when(repo.findAll()).thenReturn(List.of(p1, p2));

        // 2. Act
        List<Proyectos> resultado = proyectosService.findAll();

        // 3. Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.size()).isEqualTo(2);
        verify(repo, times(1)).findAll();
    }

    @Test
    void cuandoFindById_conIdExistente_entoncesRetornaProyecto() {
        // 1. Arrange
        Integer id = 1;
        Proyectos proyectoFalso = Proyectos.builder().id(id).tit("Limpieza de Playa").build();
        when(repo.findById(id)).thenReturn(Optional.of(proyectoFalso));

        // 2. Act
        Proyectos resultado = proyectosService.findById(id);

        // 3. Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getTit()).isEqualTo("Limpieza de Playa");
        verify(repo, times(1)).findById(id);
    }

    @Test
    void cuandoFindById_conIdNoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer id = 99;
        when(repo.findById(id)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> proyectosService.findById(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Proyecto no existe");
        verify(repo, times(1)).findById(id);
    }

    @Test
    void cuandoCreate_entoncesTransformaDTOYGuarda() {
        // 1. Arrange
        ProyectosCreateDTO dto = new ProyectosCreateDTO("Reforestación", "Plantar 100 árboles", "url.jpg");

        // Capturamos el objeto ANTES de que se guarde
        ArgumentCaptor<Proyectos> proyectoCaptor = ArgumentCaptor.forClass(Proyectos.class);

        // Simulamos que 'save' asigna un ID
        Proyectos proyectoGuardado = Proyectos.builder()
                .id(1).tit(dto.tit()).descr(dto.descr()).img(dto.img()).build();
        
        when(repo.save(proyectoCaptor.capture())).thenReturn(proyectoGuardado);

        // 2. Act
        Proyectos resultado = proyectosService.create(dto);

        // 3. Assert
        // A) Verificar el objeto retornado (con ID)
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getTit()).isEqualTo("Reforestación");

        // B) Verificar el objeto capturado (mapeado desde el DTO, sin ID)
        Proyectos proyectoAntesDeGuardar = proyectoCaptor.getValue();
        assertThat(proyectoAntesDeGuardar.getId()).isNull();
        assertThat(proyectoAntesDeGuardar.getDescr()).isEqualTo("Plantar 100 árboles");
    }

    @Test
    void cuandoUpdate_conIdExistenteYDatosParciales_entoncesActualizaParcialmente() {
        // 1. Arrange
        Integer id = 1;
        // DTO solo actualiza el título. 'descr' e 'img' son null.
        ProyectosUpdateDTO dto = new ProyectosUpdateDTO("Título Actualizado", null, null);

        // Estado del proyecto en la BD
        Proyectos proyectoEnDB = Proyectos.builder()
                .id(id)
                .tit("Título Original")
                .descr("Descripción Original")
                .img("original.jpg")
                .build();
        
        when(repo.findById(id)).thenReturn(Optional.of(proyectoEnDB));
        when(repo.save(any(Proyectos.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // 2. Act
        Proyectos resultado = proyectosService.update(id, dto);

        // 3. Assert
        assertThat(resultado).isNotNull();
        
        // Campo que SÍ cambió
        assertThat(resultado.getTit()).isEqualTo("Título Actualizado");
        
        // Campos que NO cambiaron (DTO era null)
        assertThat(resultado.getDescr()).isEqualTo("Descripción Original");
        assertThat(resultado.getImg()).isEqualTo("original.jpg");
        
        verify(repo, times(1)).findById(id);
        verify(repo, times(1)).save(proyectoEnDB);
    }

    @Test
    void cuandoUpdate_conIdNoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer id = 99;
        ProyectosUpdateDTO dto = new ProyectosUpdateDTO("Test", "Test", null);
        
        when(repo.findById(id)).thenReturn(Optional.empty());

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> proyectosService.update(id, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Proyecto no existe");
        
        verify(repo, times(1)).findById(id);
        verify(repo, never()).save(any(Proyectos.class));
    }

    @Test
    void cuandoDelete_conIdExistente_entoncesEliminaExitosamente() {
        // 1. Arrange
        Integer id = 1;
        when(repo.existsById(id)).thenReturn(true);
        doNothing().when(repo).deleteById(id);

        // 2. Act
        proyectosService.delete(id);

        // 3. Assert
        verify(repo, times(1)).existsById(id);
        verify(repo, times(1)).deleteById(id);
    }

    @Test
    void cuandoDelete_conIdNoExistente_entoncesLanzaExcepcion() {
        // 1. Arrange
        Integer id = 99;
        when(repo.existsById(id)).thenReturn(false);

        // 2. Act & 3. Assert
        assertThatThrownBy(() -> proyectosService.delete(id))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Proyecto no existe");

        verify(repo, times(1)).existsById(id);
        verify(repo, never()).deleteById(id);
    }
}