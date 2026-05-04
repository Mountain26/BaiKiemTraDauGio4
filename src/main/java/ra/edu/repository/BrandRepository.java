package ra.edu.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ra.edu.model.entity.Brand;

import java.util.List;
import java.util.Optional;

@Repository
public class BrandRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Brand> findAll() {
        return entityManager.createQuery("SELECT b FROM Brand b", Brand.class).getResultList();
    }

    public Optional<Brand> findById(Long id) {
        Brand brand = entityManager.find(Brand.class, id);
        return Optional.ofNullable(brand);
    }

    @Transactional
    public Brand save(Brand brand) {
        if (brand.getId() == null) {
            entityManager.persist(brand);
            return brand;
        } else {
            return entityManager.merge(brand);
        }
    }

    @Transactional
    public void deleteById(Long id) {
        Brand brand = findById(id).orElse(null);
        if (brand != null) {
            brand.getDevices().forEach(device -> device.setBrand(null));
            entityManager.flush();

            entityManager.remove(brand);
        }
    }

    public long count() {
        return entityManager.createQuery("SELECT COUNT(b) FROM Brand b", Long.class).getSingleResult();
    }

    @Transactional
    public void saveAll(List<Brand> brands) {
        for (Brand brand : brands) {
            entityManager.persist(brand);
        }
    }
}


