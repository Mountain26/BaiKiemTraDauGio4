package ra.edu.model.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeviceDto {
    private Long id;

    @NotEmpty(message = "Device name is required")
    @Size(min = 5, max = 150, message = "Device name must be between 5 and 150 characters")
    private String deviceName;

    @NotEmpty(message = "Model code is required")
    private String modelCode;

    private Double price;

    @NotNull(message = "Manufacture date is required")
    @PastOrPresent(message = "Manufacture date cannot be in the future")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate manufactureDate;

    private MultipartFile productImage;

    private Long brandId;

    private Boolean isAvailable;
}
