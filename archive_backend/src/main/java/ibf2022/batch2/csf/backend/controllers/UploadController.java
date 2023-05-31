package ibf2022.batch2.csf.backend.controllers;

import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.batch2.csf.backend.models.Bundle;
import ibf2022.batch2.csf.backend.models.BundleWithId;
import ibf2022.batch2.csf.backend.services.ArchiveService;
import ibf2022.batch2.csf.backend.services.ImageService;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;

@Controller
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
@CrossOrigin(origins = "*")
public class UploadController {

    @Autowired
    ImageService imageService;
    @Autowired
    ArchiveService archiveService;

    // TODO: Task 2, Task 3, Task 4
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public ResponseEntity<String> postUpload(@RequestPart String title, @RequestPart String name,
            @RequestPart String comment, @RequestPart MultipartFile zipFile) {

        System.out.printf("name: %s , title: %s , comment: %s \n", name, title, comment);
        System.out.printf(">>> filename: %s\n", zipFile.getOriginalFilename());

        try {
            List<URL> urlList = imageService.upload(zipFile);
            String bundleIdInserted = archiveService.recordBundle(title, name, comment, urlList.toString());
            System.out.println(urlList.toString());
            System.out.println("bundleId Inserted to mongo db: " + bundleIdInserted);

            return ResponseEntity.status(201)
                    .body(Json.createObjectBuilder()
                            .add("bundleId", bundleIdInserted)
                            .build().toString());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Json.createObjectBuilder()
                            .add("Error", e.getMessage())
                            .build().toString());
        }
    }

    // TODO: Task 5
    @GetMapping(path = "/bundle/{bundleId}")
    @ResponseBody
    public ResponseEntity<String> retrieveByBundleId(@PathVariable String bundleId) {

        System.out.println(">>>>> retrieved bundleId: " + bundleId);

        Optional<Bundle> opBundle = archiveService.retrieveBundle(bundleId);
        System.out.println(">>>> status: " + opBundle);

        if (opBundle.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Json.createObjectBuilder().add("Error", "bundleId not found!")
                            .build().toString());
        }

        Bundle b = opBundle.get();
        return ResponseEntity.ok().body(Json.createObjectBuilder()
                .add("title", b.title())
                .add("name", b.name())
                .add("date", b.date())
                .add("comments", b.comments())
                .add("urls", b.urls())
                .build().toString());
    }

    // TODO: Task 6
    @GetMapping(path = "/bundles")
    @ResponseBody
    public ResponseEntity<String> getBundles() {

        List<BundleWithId> bundleList = archiveService.getBundles();
        if (bundleList.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Json.createObjectBuilder().add("Error", "Empty Archive")
                            .build().toString());
        }

        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (BundleWithId b : bundleList) {
            arrayBuilder.add(
                Json.createObjectBuilder()
                .add("title", b.title())
                .add("date", b.date())
                .add("bundleId", b.bundleId())
                .build());
        }
    
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(arrayBuilder.build().toString());
    }
}
