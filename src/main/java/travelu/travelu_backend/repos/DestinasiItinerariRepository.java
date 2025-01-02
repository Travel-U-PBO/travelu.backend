package travelu.travelu_backend.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import travelu.travelu_backend.domain.Cabang;
import travelu.travelu_backend.domain.DestinasiItinerari;


public interface DestinasiItinerariRepository extends JpaRepository<DestinasiItinerari, Long> {

    DestinasiItinerari findFirstByCabangId(Cabang cabang);

}
