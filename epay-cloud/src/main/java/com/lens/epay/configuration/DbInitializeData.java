package com.lens.epay.configuration;

import com.lens.epay.enums.Role;
import com.lens.epay.model.entity.Firm;
import com.lens.epay.model.entity.User;
import com.lens.epay.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Emir Gökdemir
 * on 17 Nis 2020
 */
@Component
//@Profile({"dev", "test"})
public class DbInitializeData {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FirmRepository firmRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @PostConstruct
    private void postConstruct() {
        List<User> initialUsers = new ArrayList<>();
        if (!userRepository.existsByEmail("lensipt@gmail.com")) {
            User admin = new User();
            admin.setEmail("lensipt@gmail.com");
            admin.setPassword("$2a$10$mLpoOQQ1mf9217XGIBoW4.QOoMPSenH0hm8MU8Hwx2V6ycCA6DJIa");
            admin.setName("admin");
            admin.setSurname("admin");
            admin.setPhoneNumber("05064066032");
            admin.setRole(Role.ADMIN);
            admin.setConfirmed(true);
            initialUsers.add(admin);
        }
        if (!userRepository.existsByEmail("admin@lenstech.vision")) {
            User admin = new User();
            admin.setEmail("admin@lenstech.vision");
            admin.setPassword("$2a$10$mLpoOQQ1mf9217XGIBoW4.QOoMPSenH0hm8MU8Hwx2V6ycCA6DJIa");
            admin.setName("admin");
            admin.setSurname("admin");
            admin.setPhoneNumber("05064066035");
            admin.setRole(Role.ADMIN);
            admin.setConfirmed(true);
            initialUsers.add(admin);
        }
        if (!userRepository.existsByEmail("customer@lenstech.vision")) {
            User customer = new User();
            customer.setEmail("customer@lenstech.vision");
            customer.setPassword("$2a$10$mLpoOQQ1mf9217XGIBoW4.QOoMPSenH0hm8MU8Hwx2V6ycCA6DJIa");
            customer.setName("default");
            customer.setSurname("customer");
            customer.setPhoneNumber("05064066030");
            customer.setRole(Role.CUSTOMER);
            customer.setConfirmed(true);
            initialUsers.add(customer);
        }
        if (!userRepository.existsByEmail("firm@lenstech.vision")) {
            User firmAdmin = new User();
            firmAdmin.setEmail("firm@lenstech.vision");
            firmAdmin.setPassword("$2a$10$mLpoOQQ1mf9217XGIBoW4.QOoMPSenH0hm8MU8Hwx2V6ycCA6DJIa");
            firmAdmin.setName("default");
            firmAdmin.setSurname("firmAdmin");
            firmAdmin.setPhoneNumber("05064066029");
            firmAdmin.setRole(Role.FIRM_ADMIN);
            firmAdmin.setConfirmed(true);
            firmAdmin.setTitle("genel müdür yardımcısı");
            initialUsers.add(firmAdmin);
        }
        if (!initialUsers.isEmpty()) {
            userRepository.saveAll(initialUsers);
        }
        if (!firmRepository.existsByName("SÜTÇE Çiğ İnek Sütü A.Ş.")) {
            Firm defaultFirm = new Firm();
            defaultFirm.setName("SÜTÇE Çiğ İnek Sütü A.Ş.");
            defaultFirm.setCity("Erzurum");
            //TODO: Taha'dan alınıp ayarlanacak.
            defaultFirm.setAddress("Adnan Menderes mah. Şehit Burak Karakuş sok.No:3/B Palandöken/Erzurum");
            defaultFirm.setPhoneNo("+90 545 579 25 25");
            defaultFirm.setEmail("destek@sutcemarket.com");
            firmRepository.save(defaultFirm);
        }
//        if (!branchRepository.existsByName("default branch")) {
//            Branch defaultBranch = new Branch();
//            defaultBranch.setName("default branch");
//            defaultBranch.setCity("İstanbul");
//            defaultBranch.setAddress("Boğaziçi Teknopark");
//            defaultBranch.setFirm(firmRepository.findByName("default firm"));
//            branchRepository.save(defaultBranch);
//        }
//        if (!departmentRepository.existsByName("default department")) {
//            Department defaultDepartment = new Department();
//            defaultDepartment.setName("default department");
//            defaultDepartment.setBranch(branchRepository.findByName("default branch"));
//            defaultDepartment.setDescription("default description");
//            departmentRepository.save(defaultDepartment);
//        }
//        Category defaultCategory = new Category();
//        if (!categoryRepository.existsByName("default category")) {
//            defaultCategory.setName("default category");
//            defaultCategory.setDescription(" default category description");
//            categoryRepository.save(defaultCategory);
//        } else {
//            defaultCategory = categoryRepository.findByName("default category");
//        }
//        Brand defaultBrand = new Brand();
//        if (!brandRepository.existsByName("default brand")) {
//            defaultBrand.setCountry("Turkey");
//            defaultBrand.setName("default brand");
//            brandRepository.save(defaultBrand);
//        }
//        if (!productRepository.existsByName("default product")) {
//            Product defaultProduct = new Product();
//            defaultProduct.setStocked(true);
//            defaultProduct.setBrand(defaultBrand);
//            defaultProduct.setDescription("default description");
//            defaultProduct.setName("default product");
//            defaultProduct.setPrice(100F);
//            defaultProduct.setDiscountedPrice(90F);
//            defaultProduct.setCategory(defaultCategory);
//            productRepository.save(defaultProduct);
//        }
    }
}
