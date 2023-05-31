package ibf2022.batch2.csf.backend.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.batch2.csf.backend.repositories.ImageRepository;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository;

    public List<URL> upload(MultipartFile zipFile) throws FileNotFoundException, IOException {
        
        List<URL> urlList = new ArrayList<>();
        
        // Create a temporary directory to extract the images
        File tempDir = new File(System.getProperty("java.io.tmpdir"), "image-extraction");
        tempDir.mkdirs();

        // Read the zip file as an input stream
        try (ZipInputStream zipInputStream = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;
            byte[] buffer = new byte[1024];

            // Iterate over each entry in the zip file
            while ((entry = zipInputStream.getNextEntry()) != null) {
                if (!entry.isDirectory()) {
                    // Extract the image file
                    String entryName = entry.getName();
                    File outputFile = new File(tempDir, entryName);
                    FileOutputStream fos = new FileOutputStream(outputFile);

                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();

                    // Upload the extracted image to Amazon S3
                    URL url = imageRepository.upload(outputFile);
                    urlList.add(url);
                    System.out.println("Uploaded: " + outputFile.getAbsolutePath());
                }
                zipInputStream.closeEntry();
            }
        }

        // Cleanup the temporary directory
        deleteTempDirectory(tempDir);

        return urlList;
    }

    private void deleteTempDirectory(File tempDir) {
        if (tempDir != null && tempDir.exists()) {
            File[] files = tempDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            tempDir.delete();
        }
    }
}
