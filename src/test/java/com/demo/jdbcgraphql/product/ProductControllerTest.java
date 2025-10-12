package com.demo.jdbcgraphql.product;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    final DataSource dataSource;
    final GraphQlTester graphQlTester;
    final ProductRepository productRepository;

    ProductControllerTest(DataSource dataSource, GraphQlTester graphQlTester, ProductRepository productRepository) {
        this.dataSource = dataSource;
        this.graphQlTester = graphQlTester;
        this.productRepository = productRepository;
    }

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:18");

    @BeforeEach
    void setup() {
        ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        populator.addScript(new ClassPathResource("schema.sql"));
        populator.execute(dataSource);
    }

    @AfterEach
    void cleanup() {
        productRepository.deleteAll();
    }

    @Test
    void shouldReturnAllProducts() {
        String document = """
                query {
                    getProducts{
                        id,
                        name,
                        category,
                        stock,
                        price
                      }
                }
                """;

        graphQlTester.document(document)
                .execute()
                .path("getProducts")
                .entityList(Product.class)
                .hasSize(30);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Electronics", "Books", "Home & Kitchen", "Sports", "Fashion"})
    void shouldReturnProductsByCategory(String category) {
        String document = String.format("""
                query {
                    getProductsByCategory(category:"%s"){
                        id
                        name
                        category
                        stock
                        price
                    }
                }
                """, category);

        graphQlTester.document(document)
                .execute()
                .path("getProductsByCategory")
                .entityList(Product.class)
                .hasSizeGreaterThan(0);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("stockUpdateScenarios")
    void shouldUpdateStockLevels(String testCase, String mutation, int expectedStock) {
        int productId = 1;
        String productStringBeforeUpdate = productRepository.findProductById(1)
                .orElseThrow(() -> new RuntimeException("Product " + productId + " does not exist"))
                .toString();

        graphQlTester.document(mutation).execute();

        String productStringAfterUpdate = productRepository.findProductById(1)
                .orElseThrow(() -> new RuntimeException("Product " + productId + " does not exist"))
                .toString();

        assertThat(productStringBeforeUpdate).isEqualTo("Product[id=1, name=iPhone 14 Pro, category=Electronics, stock=50, price=999]");
        assertThat(productStringAfterUpdate).isEqualTo("Product[id=1, name=iPhone 14 Pro, category=Electronics, stock=" + expectedStock + ", price=999]");
    }

    @Test
    void shouldAddNewProduct() {
        int totalProductsBeforeUpdate = productRepository.findAll().size();
        int newProductId = 31;

        String document = """
                mutation{
                  addNewProduct(name:"corn chips", category:"Snacks", stock: 12, price:23)
                }
                """;
        graphQlTester.document(document).execute();
        int totalProductsAfterUpdate = productRepository.findAll().size();
        String newProductString = productRepository.findProductById(newProductId)
                .orElseThrow(() -> new RuntimeException("Product " + newProductId + " does not exist"))
                .toString();

        assertThat(totalProductsBeforeUpdate).isEqualTo(30);
        assertThat(totalProductsAfterUpdate).isEqualTo(31);
        assertThat(newProductString).isEqualTo("Product[id=31, name=corn chips, category=Snacks, stock=12, price=23]");
    }

    static Stream<Arguments> stockUpdateScenarios() {
        return Stream.of(
                Arguments.of(
                        "updateStock",
                        """
                                mutation{
                                   updateStock(newStock:453, id:1)
                                 }
                                """,
                        453
                ),
                Arguments.of(
                        "receiveNewShipment",
                        """
                                mutation{
                                   receiveNewShipment(id:1,receivedQuantity:30)
                                 }
                                """,
                        80
                )
        );
    }

}
