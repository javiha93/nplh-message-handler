package org.example.domain.hl7;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Position {
    private int position;
    private Position subPosition;

    @Override
    public String toString() {
        if (subPosition != null) {
            return position + " - " + subPosition.toString();
        }
        return String.valueOf(position);
    }
}

