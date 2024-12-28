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
import travelu.travelu_backend.model.CabangDTO;
import travelu.travelu_backend.service.CabangService;
import travelu.travelu_backend.util.ReferencedException;
import travelu.travelu_backend.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/cabangs", produces = MediaType.APPLICATION_JSON_VALUE)
public class CabangResource {

    private final CabangService cabangService;

    public CabangResource(final CabangService cabangService) {
        this.cabangService = cabangService;
    }

    @GetMapping
    public ResponseEntity<List<CabangDTO>> getAllCabangs() {
        return ResponseEntity.ok(cabangService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CabangDTO> getCabang(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(cabangService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createCabang(@RequestBody @Valid final CabangDTO cabangDTO) {
        final Long createdId = cabangService.create(cabangDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateCabang(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final CabangDTO cabangDTO) {
        cabangService.update(id, cabangDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteCabang(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = cabangService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        cabangService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
