package org.codewrite.teceme.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.codewrite.teceme.model.rest.ProductJson;
import org.codewrite.teceme.model.room.AccessTokenEntity;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.repository.ProductRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private final int loadSize = 20;
    private int page;
    private int totalCount;
    private boolean moreItem;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
    }

    public LiveData<PagedList<ProductEntity>> getProducts(final Integer category_id) {
        page = 0;
        moreItem = true;
        PagedList.BoundaryCallback<ProductEntity> boundaryCallback;

        boundaryCallback = new PagedList.BoundaryCallback<ProductEntity>() {
            @Override
            public void onZeroItemsLoaded() {
                super.onZeroItemsLoaded();
                loadProducts(category_id);
            }

            @Override
            public void onItemAtFrontLoaded(@NonNull ProductEntity itemAtFront) {
                super.onItemAtFrontLoaded(itemAtFront);
                Log.d("ProductViewModel", "onItemAtFrontLoaded: ");
            }

            @Override
            public void onItemAtEndLoaded(@NonNull ProductEntity itemAtEnd) {
                super.onItemAtEndLoaded(itemAtEnd);
                if (moreItem) {
                    loadProducts(category_id);
                }
                Log.d("ProductViewModel", "onItemAtEndLoaded: ");
            }
        };

        return new LivePagedListBuilder<>(productRepository.getProductsByCategoryId(category_id), loadSize)
                .setBoundaryCallback(boundaryCallback)
                .build();
    }

    private void loadProducts(Integer categoryId) {
        Call<List<ProductJson>> productList = productRepository.getProductList(categoryId, loadSize, page);
        productList.enqueue(new Callback<List<ProductJson>>() {
            @Override
            public void onResponse(@NonNull Call<List<ProductJson>> call,
                                   @NonNull Response<List<ProductJson>> response) {

                if (response.isSuccessful()) {
                    if (response.headers().get("MoreItem") != null) {
                        moreItem = true;
                        page++;
                    }else{
                        moreItem = false;
                    }
                    assert response.body() != null;
                    ProductEntity[] productEntities = new ProductEntity[response.body().size()];
                    int i = 0;
                    for (ProductJson productJson : response.body()) {
                        productEntities[i++] = productJson;
                    }
                    productRepository.insert(productEntities);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductJson>> call, @NonNull Throwable t) {
                Log.d("ProductDataSource", "onFailure: " + t.getMessage());
            }
        });
    }

}
