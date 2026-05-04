package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ra.edu.model.dto.DeviceDto;
import ra.edu.model.entity.Device;
import ra.edu.service.BrandService;
import ra.edu.service.DeviceService;

import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final DeviceService deviceService;
    private final BrandService brandService;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping
    public String list(
            @RequestParam(name = "keyword", required = false) String keyword,
            @RequestParam(name = "brandId", required = false) Long brandId,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "5") int size,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Device> devicePage = deviceService.search(keyword, brandId, pageable);
        model.addAttribute("devicePage", devicePage);
        model.addAttribute("brands", brandService.findAll());
        model.addAttribute("keyword", keyword);
        model.addAttribute("brandId", brandId);
        return "device/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("device", new DeviceDto());
        model.addAttribute("brands", brandService.findAll());
        return "device/form";
    }

    @PostMapping("/add")
    public String doAdd(@Valid @ModelAttribute("device") DeviceDto deviceDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            return "device/form";
        }

        Device device = new Device();
        device.setDeviceName(deviceDto.getDeviceName());
        device.setModelCode(deviceDto.getModelCode());
        device.setPrice(deviceDto.getPrice());
        device.setManufactureDate(deviceDto.getManufactureDate());
        device.setBrand(deviceDto.getBrandId() != null ? brandService.findById(deviceDto.getBrandId()) : null);
        device.setIsAvailable(deviceDto.getIsAvailable());

        MultipartFile productImage = deviceDto.getProductImage();
        if (productImage != null && !productImage.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + productImage.getOriginalFilename();
            try {
                java.nio.file.Path uploadDir = java.nio.file.Paths.get(uploadPath);
                java.nio.file.Files.createDirectories(uploadDir);
                java.nio.file.Files.copy(productImage.getInputStream(), uploadDir.resolve(fileName), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                device.setProductImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        deviceService.save(device);
        return "redirect:/devices";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Device device = deviceService.findById(id);
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setId(device.getId());
        deviceDto.setDeviceName(device.getDeviceName());
        deviceDto.setModelCode(device.getModelCode());
        deviceDto.setPrice(device.getPrice());
        deviceDto.setManufactureDate(device.getManufactureDate());
        deviceDto.setBrandId(device.getBrand() != null ? device.getBrand().getId() : null);
        deviceDto.setIsAvailable(device.getIsAvailable());

        model.addAttribute("device", deviceDto);
        model.addAttribute("brands", brandService.findAll());
        return "device/form";
    }

    @PostMapping("/edit/{id}")
    public String doEdit(@PathVariable Long id, @Valid @ModelAttribute("device") DeviceDto deviceDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("brands", brandService.findAll());
            return "device/form";
        }

        Device device = deviceService.findById(id);
        device.setDeviceName(deviceDto.getDeviceName());
        device.setModelCode(deviceDto.getModelCode());
        device.setPrice(deviceDto.getPrice());
        device.setManufactureDate(deviceDto.getManufactureDate());
        device.setBrand(deviceDto.getBrandId() != null ? brandService.findById(deviceDto.getBrandId()) : null);
        device.setIsAvailable(deviceDto.getIsAvailable());

        MultipartFile productImage = deviceDto.getProductImage();
        if (productImage != null && !productImage.isEmpty()) {
            String fileName = UUID.randomUUID() + "_" + productImage.getOriginalFilename();
            try {
                java.nio.file.Path uploadDir = java.nio.file.Paths.get(uploadPath);
                java.nio.file.Files.createDirectories(uploadDir);
                java.nio.file.Files.copy(productImage.getInputStream(), uploadDir.resolve(fileName), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                device.setProductImage(fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        deviceService.save(device);
        return "redirect:/devices";
    }

    @GetMapping("/delete/{id}")
    public String doDelete(@PathVariable Long id) {
        deviceService.delete(id);
        return "redirect:/devices";
    }
}
