package ibf2022.batch2.csf.backend.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.batch2.csf.backend.models.Bundle;
import ibf2022.batch2.csf.backend.models.BundleWithId;
import jakarta.json.Json;

@Repository
public class ArchiveRepository {

	@Autowired
	MongoTemplate mongoTemplate;

	// TODO: Task 4
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	/*
	 * db.archives.insert({
	 * "bundleId": genUid,
	 * "date": new Date().toString(),
	 * "title": title,
	 * "name": name,
	 * "comments": comment,
	 * "urls": urlList
	 * })
	 */
	public String recordBundle(String title, String name, String comment, String urlList, String genUid) {
		mongoTemplate.insert(Document.parse(
				Json.createObjectBuilder()
						.add("bundleId", genUid)
						.add("date", (new Date()).toString())
						.add("title", title)
						.add("name", name)
						.add("comments", comment)
						.add("urls", urlList)
						.build().toString()),
				"archives");

		return genUid;
	}

	// TODO: Task 5
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	// db.collection.findOne({ "bundleId": "bundleId" })
	public Optional<Bundle> getBundleByBundleId(String bundleId) {
		Query query = new Query();
		query.addCriteria(Criteria.where("bundleId").is(bundleId));

		Bundle bundle = mongoTemplate.findOne(query, Bundle.class, "archives");
		return Optional.ofNullable(bundle);
	}

	// TODO: Task 6
	// You are free to change the parameter and the return type
	// Do not change the method's name
	// Write the native mongo query that you will be using in this method
	// db.collection.find().sort({ "date": -1, "title": 1 })
	public List<BundleWithId> getBundles() {
		Query query = new Query();
		query.with(
			Sort.by(Sort.Direction.DESC, "date")
			.and
			(Sort.by(Sort.Direction.ASC, "title")));

	return mongoTemplate.find(query, BundleWithId.class, "archives");
	}
}
