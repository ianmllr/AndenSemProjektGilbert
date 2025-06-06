package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Infrastructure.IFavoriteRepo;
import org.example.andensemprojektgilbert.Model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FavoriteServiceImp implements IFavoriteService {
    @Autowired
    private IFavoriteRepo favoriteRepo;

    @Override
    public List<Product> getFavorites(int userid) {
        return favoriteRepo.getFavorites(userid);
    }
    @Override
    public List<Integer> getFavoriteIds(int userid) {
        List<Product> favorites = getFavorites(userid);
        List<Integer> favoriteIds = new ArrayList<>();
        for (Product product : favorites) {
            favoriteIds.add(product.getId());
        }
        return favoriteIds;
    }

    @Override
    public boolean addFavorite(int userId, int productId) {
        boolean newFavorite = favoriteRepo.addFavorite(userId, productId);
        if (newFavorite) {
            return true;
        }
        return false;
    }
    @Override
    public boolean removeFavorite(int userId, int productId) {
        boolean removedAsFavorite = favoriteRepo.removeAsFavorite(userId, productId);
        if (removedAsFavorite) {
            return true;
        }
        return false;
    }
}