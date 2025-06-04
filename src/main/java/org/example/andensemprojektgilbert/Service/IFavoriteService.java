package org.example.andensemprojektgilbert.Service;

import org.example.andensemprojektgilbert.Model.Product;

import java.util.List;

public interface IFavoriteService {
    List<Product> getFavorites(int userId);
    List<Integer> getFavoriteIds(int userId);
    boolean addFavorite(int userId, int productId);
    boolean removeFavorite(int userId, int productId);
}
