package project.reviewing.tag.command.domain;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface TagRepository extends Repository<Tag, Long> {

    Tag save(Tag entity);
    List<Tag> findAll();
}
