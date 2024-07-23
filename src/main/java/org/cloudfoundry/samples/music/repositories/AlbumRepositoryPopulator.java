package org.cloudfoundry.samples.music.repositories;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.cloudfoundry.samples.music.domain.Album;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.init.Jackson2ResourceReader;

import java.util.Collection;

/**
 * Populates the Album repository with initial data from a JSON file when the application is ready.
 */
public class AlbumRepositoryPopulator implements ApplicationListener<ApplicationReadyEvent> {
    private final Jackson2ResourceReader resourceReader;
    private final Resource sourceData;

    /**
     * Constructor initializes the Jackson2ResourceReader with a configured ObjectMapper.
     */
    public AlbumRepositoryPopulator() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        resourceReader = new Jackson2ResourceReader(mapper);
        sourceData = new ClassPathResource("albums.json");
    }

    /**
     * Called when the application is ready. It populates the Album repository if it is empty.
     *
     * @param event the event that indicates the application is ready
     */
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        CrudRepository albumRepository =
                BeanFactoryUtils.beanOfTypeIncludingAncestors(event.getApplicationContext(), CrudRepository.class);

        if (albumRepository != null && albumRepository.count() == 0) {
            populate(albumRepository);
        }
    }

    /**
     * Populates the given repository with data from the source JSON file.
     *
     * @param repository the repository to populate
     */

    @SuppressWarnings("unchecked")
    private void populate(CrudRepository repository) {
        Object entity = getEntityFromResource(sourceData);

        if (entity instanceof Collection) {
            for (Album album : (Collection<Album>) entity) {
                if (album != null) {
                    repository.save(album);
                }
            }
        } else {
            repository.save(entity);
        }
    }

    /**
     * Reads the entity from the given resource using the Jackson2ResourceReader.
     *
     * @param resource the resource to read from
     * @return the entity read from the resource
     */
    private Object getEntityFromResource(Resource resource) {
        try {
            return resourceReader.readFrom(resource, this.getClass().getClassLoader());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
