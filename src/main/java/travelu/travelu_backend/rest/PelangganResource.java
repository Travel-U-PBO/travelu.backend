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
import travelu.travelu_backend.model.PelangganDTO;
import travelu.travelu_backend.service.PelangganService;
import travelu.travelu_backend.util.ReferencedException;
import travelu.travelu_backend.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/pelanggans", produces = MediaType.APPLICATION_JSON_VALUE)
public class PelangganResource {

    private final PelangganService pelangganService;

    public PelangganResource(final PelangganService pelangganService) {
        this.pelangganService = pelangganService;
    }

    @GetMapping
    public ResponseEntity<List<PelangganDTO>> getAllPelanggans() {
        return ResponseEntity.ok(pelangganService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PelangganDTO> getPelanggan(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(pelangganService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPelanggan(
            @RequestBody @Valid final PelangganDTO pelangganDTO) {
        final Long createdId = pelangganService.create(pelangganDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePelanggan(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PelangganDTO pelangganDTO) {
        pelangganService.update(id, pelangganDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePelanggan(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = pelangganService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        pelangganService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
