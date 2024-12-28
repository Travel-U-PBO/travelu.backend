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
import travelu.travelu_backend.model.JadwalDTO;
import travelu.travelu_backend.service.JadwalService;
import travelu.travelu_backend.util.ReferencedException;
import travelu.travelu_backend.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/jadwals", produces = MediaType.APPLICATION_JSON_VALUE)
public class JadwalResource {

    private final JadwalService jadwalService;

    public JadwalResource(final JadwalService jadwalService) {
        this.jadwalService = jadwalService;
    }

    @GetMapping
    public ResponseEntity<List<JadwalDTO>> getAllJadwals() {
        return ResponseEntity.ok(jadwalService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JadwalDTO> getJadwal(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(jadwalService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createJadwal(@RequestBody @Valid final JadwalDTO jadwalDTO) {
        final Long createdId = jadwalService.create(jadwalDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateJadwal(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final JadwalDTO jadwalDTO) {
        jadwalService.update(id, jadwalDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteJadwal(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = jadwalService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        jadwalService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
