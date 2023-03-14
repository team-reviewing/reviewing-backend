package project.reviewing.tag.command.domain;

import org.springframework.data.repository.Repository;

public interface TagRepository extends Repository<Tag, Long> {

    Tag save(Tag entity);
}
