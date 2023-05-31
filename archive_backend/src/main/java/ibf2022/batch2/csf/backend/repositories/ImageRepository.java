package ibf2022.batch2.csf.backend.repositories;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;

@Repository
public class ImageRepository {

	@Autowired
	private AmazonS3 s3;
	
	//TODO: Task 3
	// You are free to change the parameter and the return type
	// Do not change the method's name
	public URL upload( File file) throws IOException {

		Map<String, String> userData = new HashMap<>();
		userData.put("filename", file.getName());
		userData.put("upload-date", (new Date()).toString());
		
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(getFileType(file));
		metadata.setContentLength(file.length());
		metadata.setUserMetadata(userData);

		String key = "images/%s".formatted(UUID.randomUUID().toString().substring(0, 8));
		
		PutObjectRequest putReq = new PutObjectRequest("acmedb", key, file);

		putReq = putReq.withCannedAcl(CannedAccessControlList.PublicRead);
		putReq = putReq.withMetadata(metadata);

		PutObjectResult result = s3.putObject(putReq);
		System.out.printf(">>> result: %s\n", result);
		
		return s3.getUrl("acmedb", key);
	}

	private String getFileType(File file) {
        String fileName = file.getName();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return "image/" + fileName.substring(dotIndex + 1);
        } else {
            return ""; // or handle the case when no file extension is found
        }
    }
}

