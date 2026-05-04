package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ra.edu.model.entity.Device;
import ra.edu.repository.DeviceRepository;
import ra.edu.service.DeviceService;

@Service
@RequiredArgsConstructor
public class DeviceServiceImpl implements DeviceService {
    private final DeviceRepository deviceRepository;

    @Override
    public Page<Device> findAll(Pageable pageable) {
        return deviceRepository.findAll(pageable);
    }

    @Override
    public Page<Device> search(String deviceName, Long brandId, Pageable pageable) {
        return deviceRepository.search(deviceName, brandId, pageable);
    }

    @Override
    public Device findById(Long id) {
        return deviceRepository.findById(id).orElse(null);
    }

    @Override
    public Device save(Device device) {
        return deviceRepository.save(device);
    }

    @Override
    public void delete(Long id) {
        deviceRepository.deleteById(id);
    }
}
