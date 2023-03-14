package project.reviewing.tag.command.domain;

import org.springframework.data.repository.Repository;

public interface CategoryRepository extends Repository<Category, Long> {

    Category save(Category entity);
}
