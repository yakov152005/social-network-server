package org.server.socialnetworkserver.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
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
        return (String) uploadResult.get("secure_url");  // מחזיר את הקישור הישיר לתמונה
    }

    /*
    public static String uploadFileToCloud(MultipartFile multipartFile) throws IOException {
        System.out.println("uploading.....");
        RestTemplate restTemplate = new RestTemplate();
        String urlImgurApi = "https://api.imgur.com/3/image";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Client-ID " + "3184b2a867ea1a4");
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", new ByteArrayResource(multipartFile.getBytes()) {
            @Override
            public String getFilename() {
                return multipartFile.getOriginalFilename();
            }
        });

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(urlImgurApi, HttpMethod.POST, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            return (String) data.get("link");
        }

        throw new IOException("Failed to upload image to Imgur");
    }
     */
}
