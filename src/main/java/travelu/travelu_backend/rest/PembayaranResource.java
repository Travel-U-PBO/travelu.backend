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
import travelu.travelu_backend.model.PembayaranDTO;
import travelu.travelu_backend.service.PembayaranService;
import travelu.travelu_backend.util.ReferencedException;
import travelu.travelu_backend.util.ReferencedWarning;


@RestController
@RequestMapping(value = "/api/pembayarans", produces = MediaType.APPLICATION_JSON_VALUE)
public class PembayaranResource {

    private final PembayaranService pembayaranService;

    public PembayaranResource(final PembayaranService pembayaranService) {
        this.pembayaranService = pembayaranService;
    }

    @GetMapping
    public ResponseEntity<List<PembayaranDTO>> getAllPembayarans() {
        return ResponseEntity.ok(pembayaranService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PembayaranDTO> getPembayaran(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(pembayaranService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createPembayaran(
            @RequestBody @Valid final PembayaranDTO pembayaranDTO) {
        final Long createdId = pembayaranService.create(pembayaranDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updatePembayaran(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final PembayaranDTO pembayaranDTO) {
        pembayaranService.update(id, pembayaranDTO);
        return ResponseEntity.ok(id);   
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deletePembayaran(@PathVariable(name = "id") final Long id) {
        final ReferencedWarning referencedWarning = pembayaranService.getReferencedWarning(id);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        pembayaranService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
