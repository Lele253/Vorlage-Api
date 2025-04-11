package apis.Manga.API.repository;

import apis.Manga.API.entity.DefaultPage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultPageRepository extends JpaRepository<DefaultPage, Long> {
}
