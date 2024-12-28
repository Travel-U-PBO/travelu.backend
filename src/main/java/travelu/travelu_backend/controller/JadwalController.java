package travelu.travelu_backend.controller;

import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import travelu.travelu_backend.domain.Armada;
import travelu.travelu_backend.domain.Cabang;
import travelu.travelu_backend.model.JadwalDTO;
import travelu.travelu_backend.repos.ArmadaRepository;
import travelu.travelu_backend.repos.CabangRepository;
import travelu.travelu_backend.service.JadwalService;
import travelu.travelu_backend.util.CustomCollectors;
import travelu.travelu_backend.util.ReferencedWarning;
import travelu.travelu_backend.util.WebUtils;


@Controller
@RequestMapping("/jadwals")
public class JadwalController {

    private final JadwalService jadwalService;
    private final ArmadaRepository armadaRepository;
    private final CabangRepository cabangRepository;

    public JadwalController(final JadwalService jadwalService,
            final ArmadaRepository armadaRepository, final CabangRepository cabangRepository) {
        this.jadwalService = jadwalService;
        this.armadaRepository = armadaRepository;
        this.cabangRepository = cabangRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("armadaIdValues", armadaRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Armada::getId, Armada::getPlatNom)));
        model.addAttribute("asalCabangIdValues", cabangRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Cabang::getId, Cabang::getName)));
        model.addAttribute("destinasiCabangIdValues", cabangRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Cabang::getId, Cabang::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("jadwals", jadwalService.findAll());
        return "jadwal/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("jadwal") final JadwalDTO jadwalDTO) {
        return "jadwal/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("jadwal") @Valid final JadwalDTO jadwalDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "jadwal/add";
        }
        jadwalService.create(jadwalDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("jadwal.create.success"));
        return "redirect:/jadwals";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("jadwal", jadwalService.get(id));
        return "jadwal/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("jadwal") @Valid final JadwalDTO jadwalDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "jadwal/edit";
        }
        jadwalService.update(id, jadwalDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("jadwal.update.success"));
        return "redirect:/jadwals";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = jadwalService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            jadwalService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("jadwal.delete.success"));
        }
        return "redirect:/jadwals";
    }

}
