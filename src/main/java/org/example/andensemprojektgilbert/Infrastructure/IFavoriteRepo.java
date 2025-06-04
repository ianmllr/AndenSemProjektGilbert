package org.example.andensemprojektgilbert.Infrastructure;

import org.example.andensemprojektgilbert.Model.Product;

import java.util.List;

public interface IFavoriteRepo {
    List<Product> getFavorites(int userId);
    boolean addFavorite(int userId, int productId);
    boolean removeAsFavorite(int userId, int productId);
}
