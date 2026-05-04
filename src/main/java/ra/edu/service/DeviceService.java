package ra.edu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ra.edu.model.entity.Device;

public interface DeviceService {
    Page<Device> findAll(Pageable pageable);
    Page<Device> search(String deviceName, Long brandId, Pageable pageable);
    Device findById(Long id);
    Device save(Device device);
    void delete(Long id);
}

