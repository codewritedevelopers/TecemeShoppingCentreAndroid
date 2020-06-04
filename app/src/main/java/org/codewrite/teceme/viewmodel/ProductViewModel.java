package org.codewrite.teceme.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import org.codewrite.teceme.model.rest.ProductJson;
import org.codewrite.teceme.model.room.ProductEntity;
import org.codewrite.teceme.repository.ProductRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private final int loadSize = 5;
    private int page;
    private boolean moreItem;
    private boolean isLoading;
    private  PagedList.BoundaryCallback<ProductEntity> boundaryCallback;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository = new ProductRepository(application);
    }

    public LiveData<PagedList<ProductEntity>> getProducts(final Integer category_id) {
        page = 0;
        moreItem = true;
        isLoading = false;

        boundaryCallback = new PagedList.BoundaryCallback<ProductEntity>() {
            @Override
            public void onZeroItemsLoaded() {
                if (moreItem && !isLoading) {
                    loadProducts(category_id);
                }
                Log.d("ProductViewModel", "onZeroItemsLoaded: ");
            }

            @Override
            public void onItemAtFrontLoaded(@NonNull ProductEntity itemAtFront) {
                Log.d("ProductViewModel", "onItemAtFrontLoaded: ");
            }

            @Override
            public void onItemAtEndLoaded(@NonNull ProductEntity itemAtEnd) {
                if (moreItem && !isLoading) {
                    loadProducts(category_id);
                }
                Log.d("ProductViewModel", "onItemAtEndLoaded: ");
            }
        };

        PagedList.Config pagingConfig = new PagedList.Config.Builder()
                .setPageSize(loadSize)
                .setPrefetchDistance(8)
                .setEnablePlaceholders(true)
                .build();
        if (category_id==null) {
            return new LivePagedListBuilder<>(productRepository.getAllProducts(), pagingConfig)
                    .setBoundaryCallback(boundaryCallback)
                    .build();
        }
        return new LivePagedListBuilder<>(productRepository.getProductsByCategoryId(category_id), pagingConfig)
                .setBoundaryCallback(boundaryCallback)
                .build();
    }

    private void loadProducts(Integer categoryId) {
        isLoading =true;
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
                    productRepository.insert(new ProductRepository.CompleteAllCallback() {
                        @Override
                        public void finish() {
                            isLoading = false;
                        }
                    }, productEntities);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<ProductJson>> call, @NonNull Throwable t) {
                Log.d("ProductDataSource", "onFailure: " + t.getMessage());
            }
        });
    }

    public LiveData<ProductEntity> getProduct(Integer product_id) {
        return productRepository.getProduct(product_id);
    }
}
