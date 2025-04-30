package com.project.anesu.ecommerce.stockmanagementservice.model;

import com.project.anesu.ecommerce.stockmanagementservice.entity.Product;
import java.util.List;

/**
 * Service interface for managing {@link Product} entities in the stock management system.
 * Provides methods for adding, retrieving, updating, and deleting products.
 */
public interface ProductService {

    /**
     * Adds a new {@link Product} to the system.
     *
     * @param product the {@link Product} to be added
     * @return the newly added {@link Product}
     */
    Product addNewProduct(Product product);

    /**
     * Retrieves a {@link Product} by its unique identifier.
     *
     * @param productId the ID of the {@link Product} to retrieve
     * @return the {@link Product} with the specified ID, or {@code null} if not found
     */
    Product getProductById(Long productId);

    /**
     * Retrieves a list of all {@link Product}s in the system.
     *
     * @return a list containing all {@link Product}s
     */
    List<Product> getAllProducts();

    /**
     * Updates an existing {@link Product} in the system.
     *
     * @param productId the ID of the {@link Product} to update
     * @param updatedProduct the updated {@link Product} information
     * @return the updated {@link Product}
     */
    Product updateProduct(Long productId, Product updatedProduct);

    /**
     * Deletes a {@link Product} from the system by its ID.
     *
     * @param productId the ID of the {@link Product} to delete
     */
    void deleteProduct(Long productId);
}
