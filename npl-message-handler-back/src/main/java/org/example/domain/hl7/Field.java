package org.example.domain.hl7;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Field {
    private String value;
    private Position position;
}
