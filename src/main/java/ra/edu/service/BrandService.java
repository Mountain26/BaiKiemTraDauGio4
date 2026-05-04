package ra.edu.service;

import ra.edu.model.entity.Brand;
import java.util.List;

public interface BrandService {
    List<Brand> findAll();
    Brand findById(Long id);
    Brand save(Brand brand);
    void delete(Long id);
}

