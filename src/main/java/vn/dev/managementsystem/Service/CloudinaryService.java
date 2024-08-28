package vn.dev.managementsystem.Service;

import java.io.IOException;
import java.util.Map;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

	@Autowired
	private Cloudinary cloudinary;

    public Map upload(MultipartFile file)  {
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        }catch (IOException io){
            throw new RuntimeException("upload fail");
        }
    }

    public Map uploadOffice(MultipartFile file)  {
        try{
            Map data = cloudinary.uploader().upload("sample.docx",
                    ObjectUtils.asMap(
                            "public_id", "sample_doc.docx",
                            "resource_type", "raw",
                            "raw_convert", "aspose"));
            return data;
        }catch (IOException io){
            throw new RuntimeException("upload fail");
        }
    }
	
}
