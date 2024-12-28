package travelu.travelu_backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JadwalDTO {

    private Long id;

    @NotNull
    @Schema(type = "string", example = "18:30")
    private LocalTime waktu;

    @NotNull
    private Integer durasi;

    @NotNull
    private Integer hargaTiket;

    @NotNull
    @JadwalArmadaIdUnique
    private Long armadaId;

    private Long asalCabangId;

    private Long destinasiCabangId;

}
