package ra.edu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import ra.edu.model.entity.Brand;
import ra.edu.model.entity.Device;
import ra.edu.repository.BrandRepository;
import ra.edu.repository.DeviceRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {
    private final BrandRepository brandRepository;
    private final DeviceRepository deviceRepository;

    @Override
    public void run(String... args) throws Exception {
        if (brandRepository.count() == 0) {
            List<Brand> brands = new ArrayList<>();
            brands.add(new Brand(null, "Apple", "Apple Inc.", null));
            brands.add(new Brand(null, "Samsung", "Samsung Electronics", null));
            brands.add(new Brand(null, "Sony", "Sony Corporation", null));
            brandRepository.saveAll(brands);
        }

        if (deviceRepository.count() == 0) {
            List<Device> devices = new ArrayList<>();
            Brand apple = brandRepository.findAll().get(0);
            devices.add(new Device(null, "iPhone 13", "IP13", 999.0, LocalDate.now(), null, apple, true));
            devices.add(new Device(null, "iPhone 14", "IP14", 1099.0, LocalDate.now(), null, apple, true));
            devices.add(new Device(null, "Macbook Pro", "MBP", 1999.0, LocalDate.now(), null, apple, true));
            devices.add(new Device(null, "iPad Air", "IPA", 799.0, LocalDate.now(), null, apple, true));

            Brand samsung = brandRepository.findAll().get(1);
            devices.add(new Device(null, "Galaxy S22", "SS22", 899.0, LocalDate.now(), null, samsung, true));
            devices.add(new Device(null, "Galaxy Z Fold 4", "SZF4", 1799.0, LocalDate.now(), null, samsung, true));
            devices.add(new Device(null, "Galaxy Tab S8", "STS8", 699.0, LocalDate.now(), null, samsung, true));
            devices.add(new Device(null, "Galaxy Watch 5", "SW5", 279.0, LocalDate.now(), null, samsung, true));

            Brand sony = brandRepository.findAll().get(2);
            devices.add(new Device(null, "Xperia 1 IV", "S1M4", 1599.0, LocalDate.now(), null, sony, true));
            devices.add(new Device(null, "PlayStation 5", "PS5", 499.0, LocalDate.now(), null, sony, true));
            devices.add(new Device(null, "WH-1000XM5", "WH5", 399.0, LocalDate.now(), null, sony, true));
            devices.add(new Device(null, "A7 IV", "A7M4", 2499.0, LocalDate.now(), null, sony, true));

            deviceRepository.saveAll(devices);
        }
    }
}

