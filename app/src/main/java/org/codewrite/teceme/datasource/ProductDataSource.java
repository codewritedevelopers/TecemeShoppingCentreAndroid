package org.codewrite.teceme.datasource;

import androidx.annotation.NonNull;
import androidx.paging.ItemKeyedDataSource;

import org.codewrite.teceme.model.room.ProductEntity;

import java.util.ArrayList;
import java.util.List;

public class ProductDataSource extends ItemKeyedDataSource<Integer, ProductEntity> {

    public void loadInitial(@NonNull LoadInitialParams<Integer> params,
                            @NonNull LoadInitialCallback<ProductEntity> callback) {

        List<ProductEntity> productEntities = new ArrayList<>();

        productEntities.add(new ProductEntity(1,"Milo Tin",null,
                null,null,"","This product a called milo",
                1,null,true,""));

        productEntities.add(new ProductEntity(2,"Millo Tin",null,
                null,null,"","This product a called milo",
                1,null,true,""));
        productEntities.add(new ProductEntity(3,"Millo Tin",null,
                null,null,"","This product a called milo",
                1,null,true,""));

        productEntities.add(new ProductEntity(4,"Millo Tin",null,
                null,null,"","This product a called millo",
                1,null,true,""));
        productEntities.add(new ProductEntity(5,"Millo Tin",null,
                null,null,"","This product a called millo",
                1,null,true,""));
        productEntities.add(new ProductEntity(6,"Millo Tin",null,
                null,null,"","This product a called millo",
                1,null,true,""));

        callback.onResult(productEntities);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params,
                           @NonNull LoadCallback<ProductEntity> callback) {

    }

    @NonNull
    @Override
    public Integer getKey(@NonNull ProductEntity item) {
        return item.getProduct_category_id();
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params,
                          @NonNull LoadCallback<ProductEntity> callback) {
        List<ProductEntity> productEntities = new ArrayList<>();

        productEntities.add(new ProductEntity(1,"Milo Tin",null,
                null,null,"","This product a called milo",
                1,null,true,""));

        productEntities.add(new ProductEntity(2,"Millo Tin",null,
                null,null,"","This product a called milo",
                1,null,true,""));
        productEntities.add(new ProductEntity(3,"Millo Tin",null,
                null,null,"","This product a called milo",
                1,null,true,""));

        productEntities.add(new ProductEntity(4,"Millo Tin",null,
                null,null,"","This product a called millo",
                1,null,true,""));
        productEntities.add(new ProductEntity(5,"Millo Tin",null,
                null,null,"","This product a called millo",
                1,null,true,""));
        productEntities.add(new ProductEntity(6,"Millo Tin",null,
                null,null,"","This product a called millo",
                1,null,true,""));

        callback.onResult(productEntities);
    }
}
