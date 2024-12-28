package travelu.travelu_backend.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CsticketDTO {

    private Long id;

    @NotNull
    private Integer rating;

    @NotNull
    private String content;

    @NotNull
    private ComplainStatus status;

    @Size(max = 255)
    private String title;

    private String response;

    private Long pemesananId;

    @NotNull
    private Long pelangganId;

}
