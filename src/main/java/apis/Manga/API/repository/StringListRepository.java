package apis.Manga.API.repository;

import apis.Manga.API.entity.StringList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StringListRepository extends JpaRepository<StringList, Long> {
}
