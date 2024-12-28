package travelu.travelu_backend.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import travelu.travelu_backend.model.KotaDTO;
import travelu.travelu_backend.service.KotaService;
import travelu.travelu_backend.util.ReferencedException;
import travelu.travelu_backend.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/kotas", produces = MediaType.APPLICATION_JSON_VALUE)
public class KotaResource {

    private final KotaService kotaService;

    public KotaResource(final KotaService kotaService) {
        this.kotaService = kotaService;
    }

    @GetMapping
    public ResponseEntity<List<KotaDTO>> getAllKotas() {
        return ResponseEntity.ok(kotaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<KotaDTO> getKota(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(kotaService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createKota(@RequestBody @Valid final KotaDTO kotaDTO) {
        final Long createdId = kotaService.create(kotaDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateKota(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final KotaDTO kotaDTO) {
        kotaService.update(id, kotaDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteKota(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = kotaService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        kotaService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
