package org.server.socialnetworkserver.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public class UploadFileToCloud {
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
            "cloud_name", "doy0rkaci",
            "api_key", "829823427843214",
            "api_secret", "GCAJeQS_AqChPhWRe3jFOZ4vJqk"
    ));

    public static String uploadFileToCloud(MultipartFile multipartFile) throws IOException {
        Map uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("secure_url");
    }

}
