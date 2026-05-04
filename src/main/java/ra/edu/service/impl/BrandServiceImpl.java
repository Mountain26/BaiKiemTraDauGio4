package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.model.entity.Brand;
import ra.edu.model.entity.Device;
import ra.edu.repository.BrandRepository;
import ra.edu.repository.DeviceRepository;
import ra.edu.service.BrandService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final BrandRepository brandRepository;
    private final DeviceRepository deviceRepository;

    @Override
    public List<Brand> findAll() {
        return brandRepository.findAll();
    }

    @Override
    public Brand findById(Long id) {
        return brandRepository.findById(id).orElse(null);
    }

    @Override
    public Brand save(Brand brand) {
        return brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Brand brand = brandRepository.findById(id).orElse(null);
        if (brand != null) {
            for (Device device : brand.getDevices()) {
                device.setBrand(null);
                deviceRepository.save(device);
            }
            brandRepository.deleteById(id);
        }
    }
}

