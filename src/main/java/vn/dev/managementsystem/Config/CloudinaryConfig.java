package vn.dev.managementsystem.Config;


import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;

@Configuration
public class CloudinaryConfig {

	@Bean
    public Cloudinary getCloudinary(){
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "dpgtcgutr");
        config.put("api_key", "444554468614136");
        config.put("api_secret", "kcoc0qGGo5plHPcbBRW72A84jLs");
        config.put("secure", true);
        return new Cloudinary(config);
    }
}
