package backend.academy.diplom.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Course {
    private Long id;
    private String name;
    private String price;
    private String duration;
    private String startDate;
    private String hours;
    private String format;
    private String whoWhom;
    private String whatMaster;
    private String priceFull;
}
