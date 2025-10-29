
package controller;

import lombok.Data;
// TODO: Uncomment when domain classes are available
// import org.example.domain.message.Message;
// import org.example.domain.message.entity.Block;
// import org.example.domain.message.entity.Slide;
// import org.example.domain.message.entity.Specimen;

/**
 * Unified request class that can handle all types of message conversions
 */
@Data
public class UnifiedConvertRequest {
    // TODO: Uncomment when domain classes are available
    // private Message message;
    private String messageType;
    private String hostName;
    private String hostType;
    // private Specimen specimen;
    // private Block block;
    // private Slide slide;
    private String status;

}
