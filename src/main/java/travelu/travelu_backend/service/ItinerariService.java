package travelu.travelu_backend.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import travelu.travelu_backend.domain.DestinasiItinerari;
import travelu.travelu_backend.domain.Itinerari;
import travelu.travelu_backend.domain.Jadwal;
import travelu.travelu_backend.model.ItinerariDTO;
import travelu.travelu_backend.repos.DestinasiItinerariRepository;
import travelu.travelu_backend.repos.ItinerariRepository;
import travelu.travelu_backend.repos.JadwalRepository;
import travelu.travelu_backend.util.NotFoundException;


@Service
@Transactional
public class ItinerariService {

    private final ItinerariRepository itinerariRepository;
    private final DestinasiItinerariRepository destinasiItinerariRepository;
    private final JadwalRepository jadwalRepository;

    public ItinerariService(final ItinerariRepository itinerariRepository,
            final DestinasiItinerariRepository destinasiItinerariRepository,
            final JadwalRepository jadwalRepository) {
        this.itinerariRepository = itinerariRepository;
        this.destinasiItinerariRepository = destinasiItinerariRepository;
        this.jadwalRepository = jadwalRepository;
    }

    public List<ItinerariDTO> findAll() {
        final List<Itinerari> itineraris = itinerariRepository.findAll(Sort.by("id"));
        return itineraris.stream()
                .map(itinerari -> mapToDTO(itinerari, new ItinerariDTO()))
                .toList();
    }

    public ItinerariDTO get(final Long id) {
        return itinerariRepository.findById(id)
                .map(itinerari -> mapToDTO(itinerari, new ItinerariDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final ItinerariDTO itinerariDTO) {
        final Itinerari itinerari = new Itinerari();
        mapToEntity(itinerariDTO, itinerari);
        return itinerariRepository.save(itinerari).getId();
    }

    public void update(final Long id, final ItinerariDTO itinerariDTO) {
        final Itinerari itinerari = itinerariRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(itinerariDTO, itinerari);
        itinerariRepository.save(itinerari);
    }

    public void delete(final Long id) {
        itinerariRepository.deleteById(id);
    }

    private ItinerariDTO mapToDTO(final Itinerari itinerari, final ItinerariDTO itinerariDTO) {
        itinerariDTO.setId(itinerari.getId());
        itinerariDTO.setTitle(itinerari.getTitle());
        itinerariDTO.setDeskripsi(itinerari.getDeskripsi());
        itinerariDTO.setImg(itinerari.getImg());
        itinerariDTO.setListDestinasi(itinerari.getListDestinasi().stream()
                .map(destinasiItinerari -> destinasiItinerari.getId())
                .toList());
        itinerariDTO.setListJadwal(itinerari.getListJadwal().stream()
                .map(jadwal -> jadwal.getId())
                .toList());
        return itinerariDTO;
    }

    private Itinerari mapToEntity(final ItinerariDTO itinerariDTO, final Itinerari itinerari) {
        itinerari.setTitle(itinerariDTO.getTitle());
        itinerari.setDeskripsi(itinerariDTO.getDeskripsi());
        itinerari.setImg(itinerariDTO.getImg());
        final List<DestinasiItinerari> listDestinasi = destinasiItinerariRepository.findAllById(
                itinerariDTO.getListDestinasi() == null ? Collections.emptyList() : itinerariDTO.getListDestinasi());
        if (listDestinasi.size() != (itinerariDTO.getListDestinasi() == null ? 0 : itinerariDTO.getListDestinasi().size())) {
            throw new NotFoundException("one of listDestinasi not found");
        }
        itinerari.setListDestinasi(new HashSet<>(listDestinasi));
        final List<Jadwal> listJadwal = jadwalRepository.findAllById(
                itinerariDTO.getListJadwal() == null ? Collections.emptyList() : itinerariDTO.getListJadwal());
        if (listJadwal.size() != (itinerariDTO.getListJadwal() == null ? 0 : itinerariDTO.getListJadwal().size())) {
            throw new NotFoundException("one of listJadwal not found");
        }
        itinerari.setListJadwal(new HashSet<>(listJadwal));
        return itinerari;
    }

}
