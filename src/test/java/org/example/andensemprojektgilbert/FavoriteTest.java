package org.example.andensemprojektgilbert;

import org.example.andensemprojektgilbert.Infrastructure.FavoriteRepo;
import org.example.andensemprojektgilbert.Model.Product;
import org.example.andensemprojektgilbert.Service.FavoriteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class FavoriteTest {
//    @Autowired
//    private FavoriteService favoriteService;
//
//    @BeforeEach
//    void setUp() {
//        favoriteService.addFavorite(1, 1);
//        favoriteService.addFavorite(1, 2);
//    }
//    @Test
//    void testGetFavoritesSize() {
//        List<Product> favorites = favoriteService.getFavorites(1);
//        assertEquals(2, favorites.size());
//    }
//    @Test
//    void testGetFavoritesIds() {
//        List<Product> favorites = favoriteService.getFavorites(1);
//       Product product = favorites.get(1);
//        assertEquals(2, product.getId());
//    }
//    @Test
//    void testGetFavoritesByIdSize() {
//        List<Integer> favoritesId = favoriteService.getFavoriteIds(1);
//        assertEquals(2, favoritesId.size());
//    }
//    @Test
//    void testGetFavoritesByIdIds() {
//        List<Integer> favoritesId = favoriteService.getFavoriteIds(1);
//        for (Integer favoriteId : favoritesId) {
//            System.out.println(favoriteId);
//        }
//        assertEquals(2, favoritesId.get(1));
//    }
//
//    @AfterEach
//    public void tearDown() {
//        favoriteService.removeFavorite(1, 1);
//        favoriteService.removeFavorite(1, 2);
//    }
}
