package repository;

import com.LearningOutcome;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;


import java.util.List;

@RepositoryRestResource(collectionResourceRel="learningOutcomes", path ="learningOutcomes")
public interface LearningOutcomeRepository extends CrudRepository<LearningOutcome, Long> {

    List<LearningOutcome> findByName(String name);
}