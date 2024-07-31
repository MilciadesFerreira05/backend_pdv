package com.pdv.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pdv.models.Category;
import com.pdv.models.Product;
import com.pdv.repositories.ProductRepository;
import com.pdv.services.CategoryService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;

import java.util.List;

/**
 * Controlador para manejar las operaciones relacionadas con productos.
 */
@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "http://localhost:5175")
@Validated
public class ProductController {

    private final ProductRepository productRepository;
    @Autowired
    private CategoryService categoryService;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Obtiene todos los productos.
     *
     * @return Lista de productos.
     */
    @CrossOrigin(origins = "http://localhost:5175")
    @GetMapping(produces = "application/json")
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Crea un nuevo producto.
     *
     * @param product Producto a crear, debe estar validado.
     * @return Respuesta con el producto creado.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Category category = categoryService.findById(product.getCategory().getId());
            product.setCategory(category);
        }
        
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    /**
     * Obtiene un producto por su ID.
     *
     * @param id ID del producto a buscar.
     * @return Respuesta con el producto encontrado o un estado 404 si no se encuentra.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable @Positive @RequestParam int id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id ID del producto a actualizar.
     * @param updatedProduct Producto con los datos actualizados.
     * @return Respuesta con el producto actualizado o un estado 404 si el producto no se encuentra.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable @Positive @RequestParam int id,
                                                  @Valid @RequestBody Product updatedProduct) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setName(updatedProduct.getName());
                    product.setDescription(updatedProduct.getDescription());
                    product.setPrice(updatedProduct.getPrice());
                    product.setStock(updatedProduct.getStock()); // Asegúrate de que el modelo tenga el campo stock.
                    Product savedProduct = productRepository.save(product);
                    return ResponseEntity.ok(savedProduct);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id ID del producto a eliminar.
     * @return Respuesta con el estado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable @Positive @RequestParam int id) {
        return productRepository.findById(id)
                .map(product -> {
                    productRepository.delete(product);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
