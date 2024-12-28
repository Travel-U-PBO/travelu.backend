package travelu.travelu_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import java.time.OffsetDateTime;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Itinerari {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "longtext")
    private String deskripsi;

    @Column
    private String img;

    @ManyToMany
    @JoinTable(
            name = "ListItinerari",
            joinColumns = @JoinColumn(name = "itinerariId"),
            inverseJoinColumns = @JoinColumn(name = "destinasiItinerariId")
    )
    private Set<DestinasiItinerari> listDestinasi;

    @ManyToMany
    @JoinTable(
            name = "TravelItinerari",
            joinColumns = @JoinColumn(name = "itinerariId"),
            inverseJoinColumns = @JoinColumn(name = "jadwalId")
    )
    private Set<Jadwal> listJadwal;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
