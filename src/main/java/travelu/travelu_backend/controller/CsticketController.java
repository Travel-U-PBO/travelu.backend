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
import travelu.travelu_backend.domain.Pelanggan;
import travelu.travelu_backend.domain.Pemesanan;
import travelu.travelu_backend.model.ComplainStatus;
import travelu.travelu_backend.model.CsticketDTO;
import travelu.travelu_backend.repos.PelangganRepository;
import travelu.travelu_backend.repos.PemesananRepository;
import travelu.travelu_backend.service.CsticketService;
import travelu.travelu_backend.util.CustomCollectors;
import travelu.travelu_backend.util.WebUtils;


@Controller
@RequestMapping("/cstickets")
public class CsticketController {

    private final CsticketService csticketService;
    private final PemesananRepository pemesananRepository;
    private final PelangganRepository pelangganRepository;

    public CsticketController(final CsticketService csticketService,
            final PemesananRepository pemesananRepository,
            final PelangganRepository pelangganRepository) {
        this.csticketService = csticketService;
        this.pemesananRepository = pemesananRepository;
        this.pelangganRepository = pelangganRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("statusValues", ComplainStatus.values());
        model.addAttribute("pemesananIdValues", pemesananRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Pemesanan::getId, Pemesanan::getNamaCustomer)));
        model.addAttribute("pelangganIdValues", pelangganRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Pelanggan::getId, Pelanggan::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("cstickets", csticketService.findAll());
        return "csticket/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("csticket") final CsticketDTO csticketDTO) {
        return "csticket/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("csticket") @Valid final CsticketDTO csticketDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "csticket/add";
        }
        csticketService.create(csticketDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("csticket.create.success"));
        return "redirect:/cstickets";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("csticket", csticketService.get(id));
        return "csticket/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("csticket") @Valid final CsticketDTO csticketDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "csticket/edit";
        }
        csticketService.update(id, csticketDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("csticket.update.success"));
        return "redirect:/cstickets";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        csticketService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("csticket.delete.success"));
        return "redirect:/cstickets";
    }

}
