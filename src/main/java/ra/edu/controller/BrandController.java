package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ra.edu.model.dto.BrandDto;
import ra.edu.model.entity.Brand;
import ra.edu.service.BrandService;

@Controller
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("brands", brandService.findAll());
        return "brand/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("brand", new BrandDto());
        return "brand/form";
    }

    @PostMapping("/add")
    public String doAdd(@Valid @ModelAttribute("brand") BrandDto brandDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "brand/form";
        }
        Brand brand = new Brand();
        brand.setName(brandDto.getName());
        brand.setDescription(brandDto.getDescription());
        brandService.save(brand);
        return "redirect:/devices";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Brand brand = brandService.findById(id);
        BrandDto brandDto = new BrandDto();
        brandDto.setId(brand.getId());
        brandDto.setName(brand.getName());
        brandDto.setDescription(brand.getDescription());
        model.addAttribute("brand", brandDto);
        return "brand/form";
    }

    @PostMapping("/edit/{id}")
    public String doEdit(@PathVariable Long id, @Valid @ModelAttribute("brand") BrandDto brandDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "brand/form";
        }
        Brand brand = brandService.findById(id);
        brand.setName(brandDto.getName());
        brand.setDescription(brandDto.getDescription());
        brandService.save(brand);
        return "redirect:/devices";
    }

    @GetMapping("/delete/{id}")
    public String doDelete(@PathVariable Long id) {
        brandService.delete(id);
        return "redirect:/devices";
    }
}

