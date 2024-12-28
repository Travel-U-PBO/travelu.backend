package travelu.travelu_backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class Jadwal {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalTime waktu;

    @Column(nullable = false)
    private Integer durasi;

    @Column(nullable = false)
    private Integer hargaTiket;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "armada_id_id", nullable = false, unique = true)
    private Armada armadaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asal_cabang_id_id")
    private Cabang asalCabangId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinasi_cabang_id_id")
    private Cabang destinasiCabangId;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @LastModifiedDate
    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

}
