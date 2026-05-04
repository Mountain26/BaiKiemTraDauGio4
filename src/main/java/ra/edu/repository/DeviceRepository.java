package ra.edu.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.model.entity.Device;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DeviceRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Page<Device> findAll(Pageable pageable) {
        String countQueryStr = "SELECT COUNT(d) FROM Device d";
        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr, Long.class);
        long total = countQuery.getSingleResult();

        String dataQueryStr = "SELECT d FROM Device d";
        TypedQuery<Device> dataQuery = entityManager.createQuery(dataQueryStr, Device.class);
        dataQuery.setFirstResult((int) pageable.getOffset());
        dataQuery.setMaxResults(pageable.getPageSize());

        List<Device> devices = dataQuery.getResultList();
        return new PageImpl<>(devices, pageable, total);
    }

    public Page<Device> search(String deviceName, Long brandId, Pageable pageable) {
        StringBuilder queryBuilder = new StringBuilder("SELECT d FROM Device d WHERE 1=1");
        Map<String, Object> params = new HashMap<>();

        if (deviceName != null && !deviceName.isEmpty()) {
            queryBuilder.append(" AND LOWER(d.deviceName) LIKE LOWER(:deviceName)");
            params.put("deviceName", "%" + deviceName + "%");
        }
        if (brandId != null) {
            queryBuilder.append(" AND d.brand.id = :brandId");
            params.put("brandId", brandId);
        }

        // Count query
        String countQueryStr = queryBuilder.toString().replace("SELECT d FROM Device d", "SELECT COUNT(d) FROM Device d");
        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryStr, Long.class);
        params.forEach(countQuery::setParameter);
        long total = countQuery.getSingleResult();

        // Data query
        TypedQuery<Device> dataQuery = entityManager.createQuery(queryBuilder.toString(), Device.class);
        params.forEach(dataQuery::setParameter);
        dataQuery.setFirstResult((int) pageable.getOffset());
        dataQuery.setMaxResults(pageable.getPageSize());

        List<Device> devices = dataQuery.getResultList();
        return new PageImpl<>(devices, pageable, total);
    }

    public Optional<Device> findById(Long id) {
        Device device = entityManager.find(Device.class, id);
        return Optional.ofNullable(device);
    }

    @Transactional
    public Device save(Device device) {
        if (device.getId() == null) {
            entityManager.persist(device);
            return device;
        } else {
            return entityManager.merge(device);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Device device = findById(id).orElse(null);
        if (device != null) {
            entityManager.remove(device);
        }
    }

    public long count() {
        return entityManager.createQuery("SELECT COUNT(d) FROM Device d", Long.class).getSingleResult();
    }

    @Transactional
    public void saveAll(List<Device> devices) {
        for (Device device : devices) {
            entityManager.persist(device);
        }
    }
}


