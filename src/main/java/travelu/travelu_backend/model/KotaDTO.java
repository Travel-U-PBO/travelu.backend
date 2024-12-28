package travelu.travelu_backend.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class KotaDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String name;

}
