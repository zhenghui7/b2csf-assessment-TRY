package ibf2022.batch2.csf.backend.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ibf2022.batch2.csf.backend.models.Bundle;
import ibf2022.batch2.csf.backend.models.BundleWithId;
import ibf2022.batch2.csf.backend.repositories.ArchiveRepository;

@Service
public class ArchiveService {

    @Autowired
    ArchiveRepository archiveRepository;

    public String recordBundle(String title, String name, String comment, String urlList) {
        // generate date and uuid here
        String genUid = UUID.randomUUID().toString().substring(0, 8);

        return archiveRepository.recordBundle(title, name, comment, urlList, genUid);      
    }

    public Optional<Bundle> retrieveBundle(String bundleId) {
        return archiveRepository.getBundleByBundleId(bundleId);
    }

	public List<BundleWithId> getBundles() {
		return archiveRepository.getBundles();
	}
    
}
