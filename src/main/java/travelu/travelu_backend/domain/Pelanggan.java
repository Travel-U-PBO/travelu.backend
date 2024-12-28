package travelu.travelu_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Pelanggan extends User {

    @Column(nullable = false, columnDefinition = "longtext")
    private String noTelp;

    @Column
    private String img;

    @OneToMany(mappedBy = "pelangganId")
    private Set<Pemesanan> listPemesanan;

    @OneToMany(mappedBy = "pelangganId")
    private Set<Csticket> listComplain;

}
