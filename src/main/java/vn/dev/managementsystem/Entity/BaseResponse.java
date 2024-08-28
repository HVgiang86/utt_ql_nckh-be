package vn.dev.managementsystem.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Base response class to be used for API responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private int code;
    private String message;
    private T data;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("code", this.code);
        map.put("message", this.message);
        map.put("data", this.data);
        return map;
    }

    public ResponseEntity<Map<String, Object>> toResponse() {
        return ResponseEntity.status(this.code).body(this.toMap());
    }
}