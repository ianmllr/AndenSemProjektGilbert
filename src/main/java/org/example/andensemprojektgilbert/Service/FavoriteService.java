package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Infrastructure.FavoriteRepo;
import org.example.andensemprojektgilbert.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FavoriteService {
    @Autowired
    private FavoriteRepo favoriteRepo;

    public List<Product> getFavorites(int userid) {
        return favoriteRepo.getFavorites(userid);
    }
    public List<Integer> getFavoriteIds(int userid) {
        List<Product> favorites = getFavorites(userid);
        List<Integer> favoriteIds = new ArrayList<>();
        for (Product product : favorites) {
            favoriteIds.add(product.getId());
        }
        return favoriteIds;
    }


    public boolean addFavorite(int userId, int productId) {
        boolean newFavorite = favoriteRepo.addFavorite(userId, productId);
        if (newFavorite) {
            return true;
        }
        return false;
    }

    public boolean removeFavorite(int userId, int productId) {
        boolean removedAsFavorite = favoriteRepo.removeAsFavorite(userId, productId);
        if (removedAsFavorite) {
            return true;
        }
        return false;
    }
}