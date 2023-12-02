package impact.moija.config;

import impact.moija.domain.community.Category;
import impact.moija.domain.community.DefaultCategory;
import impact.moija.domain.mentoring.DefaultMentorTag;
import impact.moija.domain.mentoring.MentorTag;
import impact.moija.repository.CategoryRepository;
import impact.moija.repository.mentoring.MentorTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultDataLoader implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final MentorTagRepository mentorTagRepository;

    @Override
    public void run(String... args) {
        loadDefaultCategories();
    }

    private void loadDefaultCategories() {
        for (DefaultCategory defaultCategory : DefaultCategory.values()) {
            if (!categoryRepository.existsByName(defaultCategory.getName())) {
                Category category = Category.builder()
                        .name(defaultCategory.getName())
                        .build();
                categoryRepository.save(category);
            }
        }

        for (DefaultMentorTag defaultTag : DefaultMentorTag.values()) {
            if (!mentorTagRepository.existsByName(defaultTag.getName())) {
                MentorTag tag = MentorTag.builder()
                        .name(defaultTag.getName())
                        .build();
                mentorTagRepository.save(tag);
            }
        }
    }
}
