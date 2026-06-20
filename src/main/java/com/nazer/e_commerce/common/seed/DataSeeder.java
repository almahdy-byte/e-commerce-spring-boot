package com.nazer.e_commerce.common.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import org.bson.types.ObjectId;

import com.nazer.e_commerce.category.repository.CategoryRepository;
import com.nazer.e_commerce.category.schema.Category;
import com.nazer.e_commerce.users.enums.Provider;
import com.nazer.e_commerce.users.enums.UserRoles;
import com.nazer.e_commerce.users.repository.UserRepository;
import com.nazer.e_commerce.users.schema.User;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, CategoryRepository categoryRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) {
            return;
        }

        String hashedPassword = passwordEncoder.encode("123123");

        User admin = User.builder()
                .firstName("Admin")
                .lastName("User")
                .fullName("Admin User")
                .email("admin@example.com")
                .password(hashedPassword)
                .role(UserRoles.ADMIN)
                .provider(Provider.SYSTEM)
                .phoneNumber("01000000000")
                .build();

        User user1 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .fullName("John Doe")
                .email("john@example.com")
                .password(hashedPassword)
                .role(UserRoles.USER)
                .provider(Provider.SYSTEM)
                .phoneNumber("01000000001")
                .build();

        User user2 = User.builder()
                .firstName("Jane")
                .lastName("Smith")
                .fullName("Jane Smith")
                .email("jane@example.com")
                .password(hashedPassword)
                .role(UserRoles.USER)
                .provider(Provider.GOOGLE)
                .phoneNumber("01000000002")
                .build();

        admin = userRepository.save(admin);
        userRepository.save(user1);
        userRepository.save(user2);

        if (categoryRepository.count() > 0) {
            return;
        }

        ObjectId adminId = admin.getId();
        String[][] categories = {
            {"Electronics", "electronics"},
            {"Clothing", "clothing"},
            {"Books", "books"},
            {"Home & Garden", "home-and-garden"},
            {"Sports & Outdoors", "sports-and-outdoors"},
            {"Toys & Games", "toys-and-games"},
            {"Health & Beauty", "health-and-beauty"},
            {"Automotive", "automotive"},
            {"Music", "music"},
            {"Movies & TV", "movies-and-tv"},
            {"Video Games", "video-games"},
            {"Office Supplies", "office-supplies"},
            {"Pet Supplies", "pet-supplies"},
            {"Baby Products", "baby-products"},
            {"Groceries", "groceries"},
            {"Jewelry", "jewelry"},
            {"Shoes", "shoes"},
            {"Furniture", "furniture"},
            {"Tools & Hardware", "tools-and-hardware"},
            {"Arts & Crafts", "arts-and-crafts"}
        };

        for (String[] cat : categories) {
            categoryRepository.save(Category.builder()
                    .categoryName(cat[0])
                    .slug(cat[1])
                    .createdBy(adminId)
                    .build());
        }
    }
}
