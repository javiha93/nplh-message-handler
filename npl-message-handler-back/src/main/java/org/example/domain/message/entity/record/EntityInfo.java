package org.example.domain.message.entity.record;

import lombok.Data;
import lombok.Value;
import org.example.domain.message.Reflection;

@Data
public class EntityInfo extends Reflection {
    String entityName;
    String id;
    String sequence;
    String externalId;
}
